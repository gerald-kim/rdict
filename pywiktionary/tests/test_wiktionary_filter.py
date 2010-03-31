# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from BeautifulSoup import BeautifulSoup

import unittest
import os

from wiktionary_filter import *

TEST_WIKTIONARY_PAGE = open( os.path.join( os.path.dirname( __file__ ), 'pages', 'get.html' ) ).read()

mock_word_manager = MockWordManager()
mock_word_manager.set_existing_words( [u'get on', u'gets', u'receive' ] )

class WiktionaryFilterTest( unittest.TestCase ):
    def longtest_filter( self ):
        filter = WiktionaryFilter( mock_word_manager )
        contentSoup = filter.findContentSoup( TEST_WIKTIONARY_PAGE )

        self.assertTrue( None != contentSoup )

        content = filter.executeFilters( contentSoup )

        f = open( '/tmp/get.html', 'w' )
        f.write( content )
        f.close()
        os.system( "open /tmp/get.html" )

class SoupFilterTest( unittest.TestCase ):
    def setUp( self ):
        self.filter = WiktionaryFilter( mock_word_manager )

    def test_remove_title_attr_in_a( self ):
        content = BeautifulSoup( '<a href="http://test" title="title">link</a>' )
        self.filter.soup_filter_removeTitleInA( content )
        self.assertEquals( '<a href="http://test">link</a>', str( content ) )

#    def testRemoveEditSectionSpan(self ):
#        content = BeautifulSoup( '<span class="editsection">edit</span><div>test</div>' )
#        self.filter.soup_filter_removeEditSectionSpan( content )
#        self.assertEquals( '<div>test</div>', str(content) )

    def test_pull_up_head_span( self ):
        content = BeautifulSoup( '<h3><span class="editsection">[<a href="/w/index.php?title=get&amp;action=edit&amp;section=3" title="Edit section: Etymology 1">edit</a>]</span> <span class="mw-headline">Etymology 1</span></h3>' )
        self.filter.pullUpHeadSpanContent( content )
        self.assertEquals( '<h3 class="head">Etymology 1</h3>', str( content ) )


    def test_remove_unnecessary_elements( self ):
        content = BeautifulSoup( '''<div id="rank" title="Word frequency based on Project Gutenberg corpus"><a href="/wiki/away">away</a>&nbsp;«&nbsp;<a href="/wiki/against">against</a>&nbsp;«&nbsp;<a href="/wiki/though">though</a>&nbsp;«&nbsp;<a href="/wiki/Wiktionary:Frequency_lists">#149:&nbsp;get</a>&nbsp;»&nbsp;<a href="/wiki/eyes">eyes</a>&nbsp;»&nbsp;<a href="/wiki/hand">hand</a>&nbsp;»&nbsp;<a href="/wiki/young">young</a></div>
<div class="infl-table"><table border="0" width="100%"></div>
<a class="image"><img></a>
<table class="gallery"></table><div class="printfooter">Retrived from~~~</div><hr/>''' )
        self.filter.soup_filter_removeUnnecessaryElements( content )
        self.assertEquals( '', str( content ).strip() )

    def test_remove_heading( self ):
        content = BeautifulSoup( '''<h3 class="head">head1</h3>
<h4 class="head">Translations</h4>
<div>bbb</div>
<div>ccc</div>
<h3 class="head">Verb</h3>
''' )
        self.filter.soup_filter_removeUnnecessaryHeadings( content )
        self.assertEquals( '<h3 class="head">head1</h3>\n<h3 class="head">Verb</h3>', str( content ).strip() )

    def test_remove_empty_p( self ):
        content = BeautifulSoup( '''<p><a name="Pronunciation" id="Pronunciation"></a></p><h3 class="head">Pronunciation</h3>''' )

        self.filter.soup_filter_removeEmptyP( content )
        self.assertEquals( '<h3 class="head">Pronunciation</h3>', str( content ) )

    def test_extract_pronounciation( self ):
        content = BeautifulSoup( u'''<ul><li><a href="http://en.wikipedia.org/wiki/IPA_chart_for_English" class="extiw">IPA</a>: <span class="IPA">/gɛt/</span>, <span class="IPA">/gɪt/</span>, <a href="http://en.wikipedia.org/wiki/SAMPA_chart_for_English" class="extiw">SAMPA</a>: <tt class="SAMPA">/gEt/</tt>, <tt class="SAMPA">/gIt/</tt></li>
<li><span class="unicode audiolink">&#160;<a href="http://upload.wikimedia.org/wikipedia/commons/2/2f/En-us-get.ogg" class="internal">Audio (US)</a></span><sup><a href="http://en.wikipedia.org/wiki/Wikipedia:Media_help_(Ogg)" class="extiw">help</a>, <a href="/wiki/File:en-us-get.ogg">file</a></sup></li>
<li>Rhymes: <a href="/wiki/Rhymes:English:-%C9%9Bt"><span class="IPA">-ɛt</span></a></li>
</ul>''' )
        self.filter.soup_filter_extractPronounciation( content )
        self.assertEquals( u'<ul><li><a href="http://en.wikipedia.org/wiki/IPA_chart_for_English" class="extiw">IPA</a>: <span class="IPA">/gɛt/</span>, <span class="IPA">/gɪt/</span>, <a href="http://en.wikipedia.org/wiki/SAMPA_chart_for_English" class="extiw">SAMPA</a>: <tt class="SAMPA">/gEt/</tt>, <tt class="SAMPA">/gIt/</tt></li></ul>', unicode( content ) )

    def test_remove_mention_spans( self ):
        content = BeautifulSoup( u'''<span class="mention-Latn"><a href="/wiki/geta#Old_Norse">geta</a></span>. Akin to <a href="http://en.wikipedia.org/wiki/Old_English_language" class="extiw">Old English</a> <span class="mention-Latn"><a href="/wiki/%C4%A1ietan#Old_English" class="mw-redirect">gietan</a></span>''' )
        self.filter.soup_filter_remove_mention_spans( content )
        self.assertEquals( u'<a href="/wiki/geta#Old_Norse">geta</a>. Akin to <a href="http://en.wikipedia.org/wiki/Old_English_language" class="extiw">Old English</a> <a href="/wiki/%C4%A1ietan#Old_English" class="mw-redirect">gietan</a>', unicode( content ) )

    def test_remove_new_page_links( self ):
        content = BeautifulSoup( u'''<a href="link" class="new">Link</a> <a href="link2" class="new">Link2</a>''' )
        self.filter.soup_filter_remove_new_page_links( content )
        self.assertEquals( u'Link Link2', str( content ) )

    def test_remove_appendix_links( self ):
        content = BeautifulSoup( u'''<a href="/wiki/Appendix:Glossary#I">idiomatic</a>''' )
        self.filter.soup_filter_remove_appendix_links( content )
        self.assertEquals( u'idiomatic', str( content ) )

    def test_word_links( self ):
        content = BeautifulSoup( u'''<a href="/wiki/get_on">get on</a>, <a href="/wiki/get_on">getting</a>''' )
        self.filter.soup_filter_word_links( content )
        self.assertEquals( u'<a href="get%20on" onclick="return s(this);">get on</a>, getting', str( content ) )

    def test_fold_etymology( self ):
        content = BeautifulSoup( u'''<h3 class="head">Etymology</h3> <p><span class="etyl"></span></p><p>fff</p><h3 class="head">Other Head</h3>''' )
        self.filter.soup_filter_zz_fold_etymology( content )
        self.assertEquals( u'''<h3 class="head">Etymology <a href="javascript:f('etymology_1',this)">[show]</a></h3><div id="etymology_1" style="display:none"> <p><span class="etyl"></span></p><p>fff</p></div><h3 class="head">Other Head</h3>''', str(content) );
        
    def test_fold_multiple_etymology( self ):
        content = BeautifulSoup( u'''<h3 class="head">Etymology 1</h3> <p>abc</p><h3 class="head">Etymology 2</h3> <p>def</p>''' )
        self.filter.soup_filter_zz_fold_etymology( content )
        self.assertEquals( u'''<h3 class="head">Etymology 1 <a href="javascript:f('etymology_1',this)">[show]</a></h3><div id="etymology_1" style="display:none"> <p>abc</p></div><h3 class="head">Etymology 2 <a href="javascript:f('etymology_2',this)">[show]</a></h3><div id="etymology_2" style="display:none"> <p>def</p></div>''', str(content) );
        
                                 
    def test_add_remember_buttons( self ):
        content = BeautifulSoup( u'''<ol>
<li>definition1
<dl>
<dd><i>Example1</i></dd>
</dl>
</li>
<li>(<i>transitive</i>) definition2</a>.
</li>
</ol>''' )
        self.filter.soup_filter_add_remember_buttons( content )
        self.assertEquals( u'<ol>\n<li><a href="#233c9f30" onclick="r(this)" class="r"></a>definition1\n<dl>\n<dd><i>Example1</i></dd>\n</dl>\n</li>\n<li><a href="#ea47d020" onclick="r(this)" class="r"></a>(<i>transitive</i>) definition2.\n</li>\n</ol>', str( content ) )

if __name__ == "__main__":
    unittest.main()
