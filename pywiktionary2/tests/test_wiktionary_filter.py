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
    
    def longTestFilter( self ):
        '''
        testFilter (WiktionaryFilterTest)
        
        to run this test you should run nosetests with -i option
            $ nosetests -i longTest
        '''
        filter = WiktionaryFilter()
        content = filter.findContent( TEST_WIKTIONARY_PAGE )

        self.assertTrue( None != content )
        
        filter.executeSoupFilters( content )
        
        f = open( '/tmp/get.html', 'w' )
        f.write( str( content ) )
        f.close()
        os.system( "open /tmp/get.html" ) 

class SoupFilterTest( unittest.TestCase ):
    def setUp( self ):
        self.filter = WiktionaryFilter()
    
    def testRemoveTitleAttrInA( self ):
        content = BeautifulSoup( '<a href="http://test" title="title">link</a>' )
        self.filter.soup_filter_removeTitleInA( content )
        self.assertEquals( '<a href="http://test">link</a>', str( content ) )

#    def testRemoveEditSectionSpan(self ):
#        content = BeautifulSoup( '<span class="editsection">edit</span><div>test</div>' )
#        self.filter.soup_filter_removeEditSectionSpan( content )
#        self.assertEquals( '<div>test</div>', str(content) )

    def testPullUpHeadSpan( self ):
        content = BeautifulSoup( '<h3><span class="editsection">[<a href="/w/index.php?title=get&amp;action=edit&amp;section=3" title="Edit section: Etymology 1">edit</a>]</span> <span class="mw-headline">Etymology 1</span></h3>' )
        self.filter.pullUpHeadSpanContent( content )
        self.assertEquals( '<h3 class="head">Etymology 1</h3>', str( content ) )

        
    def testRemoveUnnecessaryElements( self ):    
        content = BeautifulSoup( '''<div id="rank" title="Word frequency based on Project Gutenberg corpus"><a href="/wiki/away">away</a>&nbsp;«&nbsp;<a href="/wiki/against">against</a>&nbsp;«&nbsp;<a href="/wiki/though">though</a>&nbsp;«&nbsp;<a href="/wiki/Wiktionary:Frequency_lists">#149:&nbsp;get</a>&nbsp;»&nbsp;<a href="/wiki/eyes">eyes</a>&nbsp;»&nbsp;<a href="/wiki/hand">hand</a>&nbsp;»&nbsp;<a href="/wiki/young">young</a></div>                    
<div class="infl-table"><table border="0" width="100%"></div>
''' )
        self.filter.soup_filter_removeUnnecessaryElements( content )
        self.assertEquals( '', str( content ).strip() )
    
    def testRemoveHeading( self ):
        content = BeautifulSoup( '''<h3 class="head">head1</h3>
<h4 class="head">Translations</h4>
<div>bbb</div>
<div>ccc</div>
<h3 class="head">Verb</h3>
''' )
        self.filter.soup_filter_removeUnnecessaryHeadings( content )
        self.assertEquals( '<h3 class="head">head1</h3>\n<h3 class="head">Verb</h3>', str( content ).strip() )

    def testRemoveEmptyP( self ):
        content = BeautifulSoup( '''<p><a name="Pronunciation" id="Pronunciation"></a></p><h3 class="head">Pronunciation</h3>''' )
        
        self.filter.soup_filter_removeEmptyP( content )
        self.assertEquals( '<h3 class="head">Pronunciation</h3>', str( content ) )

    def testExtractPronounciation( self ):
        content = BeautifulSoup( u'''<ul><li><a href="http://en.wikipedia.org/wiki/IPA_chart_for_English" class="extiw">IPA</a>: <span class="IPA">/gɛt/</span>, <span class="IPA">/gɪt/</span>, <a href="http://en.wikipedia.org/wiki/SAMPA_chart_for_English" class="extiw">SAMPA</a>: <tt class="SAMPA">/gEt/</tt>, <tt class="SAMPA">/gIt/</tt></li>
<li><span class="unicode audiolink">&#160;<a href="http://upload.wikimedia.org/wikipedia/commons/2/2f/En-us-get.ogg" class="internal">Audio (US)</a></span><sup><a href="http://en.wikipedia.org/wiki/Wikipedia:Media_help_(Ogg)" class="extiw">help</a>, <a href="/wiki/File:en-us-get.ogg">file</a></sup></li>
<li>Rhymes: <a href="/wiki/Rhymes:English:-%C9%9Bt"><span class="IPA">-ɛt</span></a></li>
</ul>''' )
        self.filter.soup_filter_extractPronounciation( content )
        self.assertEquals( u'<ul><li><a href="http://en.wikipedia.org/wiki/IPA_chart_for_English" class="extiw">IPA</a>: <span class="IPA">/gɛt/</span>, <span class="IPA">/gɪt/</span>, <a href="http://en.wikipedia.org/wiki/SAMPA_chart_for_English" class="extiw">SAMPA</a>: <tt class="SAMPA">/gEt/</tt>, <tt class="SAMPA">/gIt/</tt></li></ul>', unicode(content) )
        
    def testRemoveMentionSpans( self ):
        content = BeautifulSoup( u'''<span class="mention-Latn"><a href="/wiki/geta#Old_Norse">geta</a></span>. Akin to <a href="http://en.wikipedia.org/wiki/Old_English_language" class="extiw">Old English</a> <span class="mention-Latn"><a href="/wiki/%C4%A1ietan#Old_English" class="mw-redirect">gietan</a></span>''' )
        self.filter.soup_filter_removeMentionSpans( content )
        self.assertEquals( u'<a href="/wiki/geta#Old_Norse">geta</a>. Akin to <a href="http://en.wikipedia.org/wiki/Old_English_language" class="extiw">Old English</a> <a href="/wiki/%C4%A1ietan#Old_English" class="mw-redirect">gietan</a>', unicode( content ) )
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
