# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from BeautifulSoup import BeautifulSoup

class WiktionaryFilter:
    
    def findContent( self, wiktionaryPage ):
        page = BeautifulSoup( wiktionaryPage )
        return page.find( 'div', id='bodyContent' )
    
    def executeSoupFilters( self, content ):
        for d in dir( self ):
            if d.startswith( 'soup_filter_' ):
                getattr( self, d )( content)
        
    def soup_filter_removeTitleInA( self, content ):
        for link in content.findAll( 'a' ):
            del( link['title'] )
        
    def soup_filter_removeEditSectionSpan(self, content ):
        editSectionSpan = content.findAll( 'span', {'class':'editsection'} )
        [ ess.extract() for ess in editSectionSpan ]
