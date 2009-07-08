# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from BeautifulSoup import BeautifulSoup
from cStringIO import StringIO

class WiktionaryFilter:
    
    def findContent( self, wiktionaryPage ):
        page = BeautifulSoup( wiktionaryPage )
        bodyContent = page.find( 'div', id = 'bodyContent' )
        self.pullUpHeadSpanContent( bodyContent )
        
        buffer = StringIO()
#        buffer.write( '<!DOCTYPE html><html lang="en"><head><meta charset=utf-8></head><body>\n')
        h2s = bodyContent.findAll( 'h2', {'class':'head'}  )
        
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
        
        return BeautifulSoup( buffer.getvalue(), fromEncoding='utf-8' )

    def pullUpHeadSpanContent( self, content ):
        headSpans = content.findAll('span', {'class':'mw-headline'} )
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
            
