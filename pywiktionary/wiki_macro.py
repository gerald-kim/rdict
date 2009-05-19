# -*- coding: utf-8 -*- 

from logger import * 
import re
IGNORED_MACROS = set( ['rank', 'mid2', 'bottom2', 'rel-mid3',
            'rel-bottom', 'mid2', 'mid3', 'mid4', 'rel-mid', 'rel-mid4' ] ) 
ITALIC_PARENTHESIS_MACROS = set( [''] )

def is_ignored_macro_line( line ):
    line = line.strip()
    macro_re = re.compile( '{{([^|]+).*}}', re.DOTALL )
    m = macro_re.match( line )
    if m and  m.group( 1 ) in IGNORED_MACROS:
        return True
    return False


class Macro:
    def __init__( self, word, macro, formatter ):
        self.word = word
        self.macro_args = macro.split( '|' )
        self.macro_name = self.macro_args[0]
        self.macro_args.remove( self.macro_name )
        self.formatter = formatter
        
    def execute( self ):
        if self.macro_name in IGNORED_MACROS:
            return ''

        try:
            builtins = self.__class__
            execute = getattr( builtins, '_macro_' + self.macro_name )
            return execute( self, self.macro_args )
        except AttributeError:
            MACRO_LOGGER.warn( self.word + '\t' + self.macro_name )
            text = self.macro_name
            if len( self.macro_args ) > 0:
                text += ' ' + ' '.join( self.macro_args )
            return self._encloseWithParenthesis( text ) 
        
    def _encloseWithParenthesis( self, text ):
        return ( '(' + self.formatter.emphasis( 1 ) + text + self.formatter.emphasis( 0 ) + ')' )
        
    def _macro_test( self, args ):
        return self.formatter.rawHTML( args[0] )

    def _macro_italbrac( self, args ):
        return  self._encloseWithParenthesis( u' '.join( args ) )
