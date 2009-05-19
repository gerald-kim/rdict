# -*- coding: utf-8 -*- 

from wiki_formatter import *

import unittest

class FormatterTest( unittest.TestCase ):
    def setUp( self ):
        self.f = Formatter()
    
    def testParagraph( self ):
        self.assertEquals( '<p>', self.f.paragraph( 1 ) )
        self.assertEquals( '', self.f.paragraph( 0 ) )
        
    def testText( self ):
        self.assertEquals( 'text', self.f.text( 'text' ) )

    def testEmphasis( self ):
        self.assertEquals( '<i>', self.f.emphasis( 1 ) )
        self.assertEquals( '</i>', self.f.emphasis( 0 ) )

    def testStrong( self ):
        self.assertEquals( '<b>', self.f.strong( 1 ) )
        self.assertEquals( '</b>', self.f.strong( 0 ) )

    def testUnderline( self ):
        self.assertEquals( '<span class="u">', self.f.underline( 1 ) )
        self.assertEquals( '</span>', self.f.underline( 0 ) )
        
    def testLineBreak( self ):
        self.assertEquals( '<br />\n', self.f.linebreak() )

