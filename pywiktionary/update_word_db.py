#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
import os
import codecs
import xml.sax
import sys

reload( sys )
sys.setdefaultencoding( "utf-8" )


from models import *

WordCount = 0

class WikiHandler( xml.sax.ContentHandler ):
    def __init__( self ):
        self.element = None
        self.page = None
        self.text = ""
        self.revision = 0
        self.long = {}
        
    def startElement( self, name, attrs ):
        self.element = name
        
    def endElement( self, name ):
        if self.element == "text":
            if self.page:
                if self.page in self.long:
                    #print self.page, len( self.text )
                    #print
                    pass
                self.doPage( self.page, self.text, self.revision )
                self.page = None
            self.text = ""
        self.element = None

    def characters( self, content ):
        if self.element == "title":
            if self.checkPage( content ):
                self.page = content
        elif self.element == "id":
            self.revision = int( content )
        elif self.element == "text":
            if self.page:
                self.text += content
                if len( self.text ) > 100000 and self.page not in self.long:
                    self.long[self.page] = 1
                    
    def checkPage( self, page ):
        return True
    
    def doPage( self, page, text, revision ):
        pass

class WordHandler( WikiHandler ):
    def __init__( self, word_manager ):
        self.word_manager = word_manager
        WikiHandler.__init__( self )
    
    def doPage( self, page, text, revision ):
        if page.startswith( u'Wiktionary:' ) or page.startswith( u'Help:' ):
            return
         
        m = re.match( r"#redirect\s*\[\[(.*?)\]\]", text, re.IGNORECASE )
        if m:
#            print "Put to redir"
#            self.db_redir.put( page, m.group( 1 ) )
            return

        m = re.search( r"^==English==", text, re.IGNORECASE | re.MULTILINE )
        if not m:
            return
        
        #self.db_tmp.put( page, text )
        self.word_manager.save( Word( page, revision ) )
        
        global WordCount
        WordCount += 1
        if WordCount % 100 == 0:
            print "\033[A", WordCount


class WordDbMaker:
    def __init__( self, xml_file ):
        self.xml_file = xml_file

    def process_redir_and_filter_english( self ):
        f = os.popen( "bunzip2 -c %s" % self.xml_file, "r" )
        
        word_manager = WordManager()
        word_manager.connect()
        word_manager.unmark_updated()
        
        xml.sax.parse( f, WordHandler( word_manager ) )
        f.close()
        
        word_manager.delete_unupdated()
        word_manager.close()
        
    
if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s xmlfile" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

          
    maker = WordDbMaker( sys.argv[1] )
    maker.process_redir_and_filter_english()
