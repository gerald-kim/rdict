# -*- coding: utf-8 -*- 

from wiki_macro import *
from wiki_formatter import *

import unittest

class MacroTest( unittest.TestCase ):
    def setUp( self ):
        self.f = Formatter() 
         
class MacroExecutionTest( MacroTest ):
    def testMacroParsingInInit( self ):
        m = Macro( 'word', 'rank|123|333', self.f )
        self.assertEquals( 'rank', m.macro_name )
        self.assertEquals( ['123', '333' ], m.macro_args )
    
    def testUnknownMacro( self ):
        m = Macro( 'word', 'unknown|333|xxx', self.f )
        self.assertEquals( '(<i>unknown 333 xxx</i>)', m.execute() )

    def testTestMacro( self ):
        m = Macro( 'word', 'test|args', self.f )
        self.assertEquals( 'args', m.execute() )

class IgnoredMacroTest( MacroTest ):
    def testIgnoredMacroReturnsEmptyString( self ):
        m = Macro( 'word', 'rank|123|333', self.f )
        self.assertEquals( '', m.execute() )
    
    
