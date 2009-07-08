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

    def testRemoveEditSectionSpan(self ):
        content = BeautifulSoup( '<span class="editsection">edit</span><div>test</div>' )
        self.filter.soup_filter_removeEditSectionSpan( content )
        self.assertEquals( '<div>test</div>', str(content) )

        
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
