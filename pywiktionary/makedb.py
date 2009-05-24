#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
import os
import codecs
import tc
import xml.sax
import sys

reload( sys )
sys.setdefaultencoding( "utf-8" )

from word import * 

GFDL = "Wiktionary:Text of the GNU Free Documentation License"
WordCount = 0


class WikiHandler( xml.sax.ContentHandler ):
    def __init__( self ):
        self.element = None
        self.page = None
        self.text = ""
        self.long = {}
    def startElement( self, name, attrs ):
        self.element = name
        
    def endElement( self, name ):
        if self.element == "text":
            if self.page:
                if self.page in self.long:
                    print self.page, len( self.text )
                    print
                self.doPage( self.page, self.text )
                self.page = None
            self.text = ""
        self.element = None

    def characters( self, content ):
        if self.element == "title":
            if self.checkPage( content ):
                self.page = content
        elif self.element == "text":
            if self.page:
                self.text += content
                if len( self.text ) > 100000 and self.page not in self.long:
                    self.long[self.page] = 1
    def checkPage( self, page ):
        return False
    def doPage( self, page, text ):
        pass

class WordHandler( WikiHandler ):
    def __init__( self, db_redir, db_tmp ):
        self.db_redir = db_redir
        self.db_tmp = db_tmp
        WikiHandler.__init__( self )
        
    def checkPage( self, page ):
        return ':' not in page or page == GFDL

    def doPage( self, page, text ):
        if page == GFDL:
            global GFDLtext
            GFDLtext = text
            return
        page = page.encode( 'utf-8' )
        text = text.encode( 'utf-8' )
        
        m = re.match( r"#redirect\s*\[\[(.*?)\]\]", text, re.IGNORECASE )
        if m:
            self.db_redir.put( page, m.group( 1 ) )
            return

        m = re.search( r"^==English==", text, re.IGNORECASE | re.MULTILINE )
        if not m:
            return
        
        self.db_tmp.put( page, text )
        
        global WordCount
        WordCount += 1
        if WordCount % 100 == 0:
            print "\033[A", WordCount


class WiktionaryDbMaker:
    XML_RE = re.compile( r'enwiktionary-(\w+)' )
    COMPLETE_KEY = '###COMPLETED###';
    def __init__( self, xml_file ):
        self.xml_file = xml_file
        self.db_dir = 'wiktionary_' + self.XML_RE.match( self.xml_file ).group( 1 ) + '.db'
        
    def _get_db_filepath( self, name ):
        return os.path.join( self.db_dir, name + '.tcb' )
        
    def _open_tcb( self, name ):
        db = tc.BDB()
        db.open( self._get_db_filepath( name ), tc.BDBOWRITER | tc.BDBOCREAT )
        return db
        
    def open( self ):
        if not os.path.exists( self.db_dir ):
            os.makedirs( self.db_dir )

        self.db_redir = self._open_tcb( 'redir' )
        self.db_tmp = self._open_tcb( 'tmp' )
        self.db_heading = self._open_tcb( 'heading' )
        self.db_word = self._open_tcb( 'word' )
        #self.db_index = self._open_tcb( 'index' )
        
    def close( self ):
        self.db_redir.close();
        self.db_tmp.close();
        self.db_heading.close();
        self.db_word.close();
        #self.db_index.close();
        
    def reopen_index( self ):
        try:
            os.remove( self._get_db_filepath( 'index' ) )
        except OSError:
            pass
        self.db_index = self._open_tcb( 'index' )
    
    def create_index( self ):
        c = self.db_word.curnew()
        c.first()
        while 1:
            try:
                c.key()
            except KeyError:
                break
            self.db_index.putdup( c.key().lower(), c.key() )
            c.next()
        self.db_index.close()
        
    def process_redir_and_filter_english( self ):
        if self.db_tmp.has_key( self.COMPLETE_KEY ):
            print "Skipping redir processing, english filter"
            return 
        
        f = os.popen( "bunzip2 -c %s" % self.xml_file, "r" )
        xml.sax.parse( f, WordHandler( self.db_redir, self.db_tmp ) )
        f.close()
        
        for ( key, value ) in self.db_redir.items():
            if not self.db_tmp.has_key( value ):
                self.db_redir.out( key )
                
        self.db_tmp.put( self.COMPLETE_KEY, '1' )
        
    def collect_heading( self ):
        #TODO: check heading not in word.PARTS  
        for key in self.db_tmp.iterkeys():
            try:
                word = Word( key, self.db_tmp.get( key ) )
                word.parse()
                english_section = word.get_english_section()
                
                print key
                for section in english_section.bfs():
                    if section and section.heading:
                        self.db_heading.putcat( section.heading, word.lemma + "\n" )
            except Exception, e:
                pass
            
    def print_structure( self ):
        for key in self.db_tmp.iterkeys():
            try:
                word = Word( key, self.db_tmp[key] )
                word.parse()
                word.restructure_wiki_document()
                print "%s\t%s" % ( key.ljust( 20 ), str( word.doc ) )
            except Exception, e:
                LOGGER.error( e )
                WORD_LOGGER.error( key )
    
    def format_word( self, key ):
        word = Word( key, self.db_tmp[key] )
        word.parse()
        word.restructure_wiki_document()
        self.db_word.put( key, word.format() )

    def format_all( self ):
        word_count = 0
        
        for key in self.db_tmp.iterkeys():
            try:
                self.format_word( key )

                word_count += 1
                if word_count % 100 == 0:
                    print "\033[A", word_count

            except Exception, e:
                LOGGER.error( e )
                WORD_LOGGER.error( key )
    
    
if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s xmlfile" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

          
    maker = WiktionaryDbMaker( sys.argv[1] )
    maker.open()
    maker.process_redir_and_filter_english()
    #maker.collect_heading()
    if len( sys.argv ) == 3:
        maker.format_word( sys.argv[2] )
    else:
        maker.format_all()
        
    maker.close()
