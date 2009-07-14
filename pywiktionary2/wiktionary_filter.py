#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from BeautifulSoup import BeautifulSoup
from cStringIO import StringIO
import re 

class WiktionaryFilter:
    
    def findContent( self, wiktionaryPage ):
        page = BeautifulSoup( wiktionaryPage )
        bodyContent = page.find( 'div', id = 'bodyContent' )
        self.pullUpHeadSpanContent( bodyContent )
        
        buffer = StringIO()
#        buffer.write( '<!DOCTYPE html><html lang="en"><head><meta charset=utf-8></head><body>\n')
        h2s = bodyContent.findAll( 'h2', {'class':'head'} )
        
        englishHead = h2s[0]
        otherLangHead = None
        if len( h2s ) > 1:
            otherLangHead = h2s[1]
            
        g = englishHead.nextSiblingGenerator()
        n = g.next()
        while otherLangHead != n:
            buffer.write( str( n ) )
            buffer.write( '\n' )
            n = g.next()
#        buffer.write( '</body></html>' )
        buffer.flush()
        
        return BeautifulSoup( buffer.getvalue(), fromEncoding = 'utf-8' )

    def pullUpHeadSpanContent( self, content ):
        headSpans = content.findAll( 'span', {'class':'mw-headline'} )
        for headSpan in headSpans:
            parent = headSpan.parent
            parent['class'] = 'head' 
            parent.contents = headSpan.contents
                    
    def executeSoupFilters( self, content ):
        for d in dir( self ):
            if d.startswith( 'soup_filter_' ):
                getattr( self, d )( content )
        
    def soup_filter_removeTitleInA( self, content ):
        for link in content.findAll( 'a' ):
            del( link['title'] )
        
    def soup_filter_removeUnnecessaryElements( self, content ):
        ELEMENTS = { 
            'span' : [ 
                {'class':'editsection'},
            ],
            'div' : [ 
                {'id':'rank'},
                {'class':'infl-table'},
            ],
        } 
        
        for tag in ELEMENTS.keys():
            attrs = ELEMENTS[tag]
            for attr in attrs:
                soups = content.findAll( tag, attr )
                [ s.extract() for s in soups ]
            
    def soup_filter_removeUnnecessaryHeadings( self, content ):
        UNNECESSARY_HEADINGS = [u'Translations']
        
        for unnecessary_heading in UNNECESSARY_HEADINGS:
            headings = content.findAll( attrs = {'class':'head' } )
            for heading in headings:
                #print "HEADING:", heading, heading.contents
                extracts = []
                if unnecessary_heading == heading.contents[0]:
                    #print "unnecessary:", heading
                    extracts.append( heading )
                    g = heading.nextSiblingGenerator()
                    n = g.next()
                    while n != None and n not in headings:
                        #print n
                        extracts.append( n )
                        n = g.next()
                    
                    [ s.extract() for s in extracts ]
                    
    def soup_filter_removeEmptyP( self, content ):
        ps = content.findAll( 'p' )
        
        for p in ps:
            childrens = p.findChildren()
            if len( childrens ) == 1 and u'a' == childrens[0].name:
                p.extract()

    def soup_filter_extractPronounciation( self, content ):
        extiwAs = content.findAll( 'a', {'class':'extiw'} )
        
        for a in extiwAs:
            try:
                if len( a.contents ) == 1 and a.contents[0] in [u'IPA', u'SAMPA']:
                    li = a.parent
                    ul = li.parent
                    ul.contents = [li]
                    break
            except:
                pass
        
    def soup_filter_removeMentionSpans( self, content ):
        spans = content.findAll( 'span', {'class':re.compile('mention-.+')} )
        [ s.replaceWith( s.contents[0] ) for s in spans ]
    
import sys

if __name__ == '__main__':
    #import psyco
    #psyco.full()

    page = sys.stdin.read() 
    filter = WiktionaryFilter()
    content = filter.findContent( page )
    filter.executeSoupFilters( content )
    
    print str( content )
