# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee
from BeautifulSoup import BeautifulSoup

import unittest
import os

from wiktionary_filter import WiktionaryFilter

TEST_WIKTIONARY_PAGE = open( os.path.join( os.path.dirname( __file__ ), 'pages', 'get.html' ) ).read()

class WiktionaryFilterTest( unittest.TestCase ):
    def testFilter( self ):
        filter = WiktionaryFilter()
        content = filter.findContent( TEST_WIKTIONARY_PAGE )

        self.assertTrue( None != content )
        
        filter.executeSoupFilters( content )
        
        f = open( '/tmp/get.html', 'w' )
        f.write( str( content ) )
        f.close()
        os.system( "open /tmp/get.html" ) 
#        filter.executeRegexFilters()
#        out = filter.html()
#        
#        self.assertTrue( None != out )

class SoupFilterTest( unittest.TestCase ):
    def setUp( self ):
        self.filter = WiktionaryFilter()
    
    def testRemoveTitleAttrInA( self ):
        content = BeautifulSoup( '<a href="http://test" title="title">link</a>' )
        self.filter.soup_filter_removeTitleInA( content )
        self.assertEquals( '<a href="http://test">link</a>', str(content) )

#    def testRemoveEditSectionSpan(self ):
#        content = BeautifulSoup( '<span class="editsection">edit</span><div>test</div>' )
#        self.filter.soup_filter_removeEditSectionSpan( content )
#        self.assertEquals( '<div>test</div>', str(content) )

    def testPullUpHeadSpan(self):
        content = BeautifulSoup( '<h3><span class="editsection">[<a href="/w/index.php?title=get&amp;action=edit&amp;section=3" title="Edit section: Etymology 1">edit</a>]</span> <span class="mw-headline">Etymology 1</span></h3>' )
        self.filter.pullUpHeadSpanContent( content )
        self.assertEquals( '<h3 class="head">Etymology 1</h3>', str(content) )

        
    def testRemoveUnnecessaryElements( self ):    
        content = BeautifulSoup( '''<div id="rank" title="Word frequency based on Project Gutenberg corpus"><a href="/wiki/away">away</a>&nbsp;«&nbsp;<a href="/wiki/against">against</a>&nbsp;«&nbsp;<a href="/wiki/though">though</a>&nbsp;«&nbsp;<a href="/wiki/Wiktionary:Frequency_lists">#149:&nbsp;get</a>&nbsp;»&nbsp;<a href="/wiki/eyes">eyes</a>&nbsp;»&nbsp;<a href="/wiki/hand">hand</a>&nbsp;»&nbsp;<a href="/wiki/young">young</a></div>                    
<div class="infl-table"><table border="0" width="100%"></div>
<hr>
''')
        self.filter.soup_filter_removeUnnecessaryElements( content )
        self.assertEquals( '', str(content).strip() )
    

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
