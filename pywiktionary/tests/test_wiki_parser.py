# -*- coding: utf-8 -*- 

from wiki_parser import *

import unittest

class ParserTest( unittest.TestCase ):
    def setUp( self ):
        self.f = Formatter()

class SimpleMarkupParsingTest( ParserTest ):
    def testStrong( self ):
        p = Parser( 'word', "'''strong'''" )
        self.assertEquals( "<b>strong</b> ", p.format( self.f ) )

    def testEm( self ):
        p = Parser( 'word', "''em''" )
        self.assertEquals( "<i>em</i> ", p.format( self.f ) )

    def testEmStrong( self ):
        p = Parser( 'word', "'''''emstrong'''''" )
        self.assertEquals( "<i><b>emstrong</b></i> ", p.format( self.f ) )
        
        
        
    def testListing( self ):
        p = Parser( 'word', """# item1
#* def
#*: description
#* def2
#*: description2
# item2
#: ''example2''
#:: ''example2-1''
""" ) 
        
        expected = """<ol>
<li class="">
 item1 <ul>
<li class="">
 def 
<dl>
<dd>
 description </dd>
</dl>

</li>
<li class="">
 def2 
<dl>
<dd>
 description2 </dd>
</dl>

</li>

</ul>

</li>
<li class="">
 item2 
<dl>
<dd>
 <i>example2</i> 
<dl>
<dd>
 <i>example2-1</i> </dd>
</dl>

</dl>

</ol>
"""
        print p.format( self.f )
        self.assertEquals( expected, p.format( self.f ) )


class LinkParsingTest( ParserTest ):
    def testMediaAnchorShouldRaiseException( self ):
        p = Parser( 'word', "[[#anchor|Anchor]]" )
        self.assertEquals( '<a class="rdict" href="#anchor">Anchor</a> ', p.format( self.f ) )

    def testParseLocalLink( self ):
        p = Parser( 'page', 'link to [[word]].' )
        self.assertEquals( 'link to <a class="rdict" href="word">word</a>. ', p.format( self.f ) )

    def testParseLocalLinkWithText( self ):
        p = Parser( 'page', 'link to [[word|Word]].' )
        self.assertEquals( 'link to <a class="rdict" href="word">Word</a>. ', p.format( self.f ) )

    def testParseInterWiki( self ):
        p = Parser( 'page', 'link to [[w:word]]' )
        self.assertEquals( 'link to <a class="wikipedia" href="http://en.wikipedia.org/wiki/word">word</a> ', p.format( self.f ) )
        p = Parser( 'page', 'link to [[ww:word]]' )
        self.assertEquals( 'link to <a class="wikipedia" href="unknown://word">word</a> ', p.format( self.f ) )
        
    def testParseInterWikiWithText( self ):
        p = Parser( 'page', 'link to [[w:word|Word]]' )
        self.assertEquals( 'link to <a class="wikipedia" href="http://en.wikipedia.org/wiki/word">Word</a> ', p.format( self.f ) )
        
    def testParseUrl( self ):
        p = Parser( 'page', 'link to [http://www.google.com]' )
        self.assertEquals( 'link to <a class="http" href="http://www.google.com">http://www.google.com</a> ', p.format( self.f ) )
        
class MacroParsingTest( ParserTest ):
    def testParseIgnoredMacro( self ):
        p = Parser( 'page', 'macro {{macro}}' )
        self.assertEquals( 'macro (<i>macro</i>) ', p.format( self.f ) )
