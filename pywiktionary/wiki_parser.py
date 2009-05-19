# -*- coding: utf-8 -*-

from wiki_formatter import *
from wiki_macro import *
from logger import *

import re, types
from cStringIO import StringIO

class Parser:
    punct_pattern = re.escape( u'''"\'}]|:,.)?!''' )

    url_schemas = ['http', 'https' ]

    url_pattern = u'|'.join( url_schemas )
    url_rule = ur'%(url_guard)s(%(url)s)\:([^\s\<%(punct)s]|([%(punct)s][^\s\<%(punct)s]))+' % {
        'url_guard': u'(^|(?<!\w))',
        'url': url_pattern,
        'punct': punct_pattern,
    }

    formatting_rules = ur"""(?:(?P<emph_ibb>'''''(?=[^']+''')))
(?P<emph_ibi>'''''(?=[^']+''))
(?P<emph_ib_or_bi>'{5}(?=[^']))
(?P<emph>'{2,3})
(?P<b></?b>)
(?P<i></?i>)
(?P<u></?u>)
(?P<sup><sup>.*?</sup>)
(?P<sub><sub>.*?</sub>)
(?P<tt><tt>.*?</tt>)
(?P<pre></?(pre|nowiki)>)
(?P<small></?small>)
(?P<br><br\s*/?>)
(?P<comment><!--.*?-->)
(?P<rule>-{4,})
(?P<macro>\{\{.*\}\})
(?P<media_anchor>\[\[#\w+\]\])
(?P<dml>^(\*+#+[*#]*:+|#+\*+[*#]*:+))
(?P<ml>^(\*+#+[*#]*|#+\*+[*#]*))
(?P<dol>^#+:+)
(?P<ol>^#+)
(?P<dl>^;+[^:]+:)
(?P<li>^\*+)
(?P<ind>^:+)
(?P<media_heading>^\s*(?P<hmarker>=+).*(?P=hmarker)\s*$)
(?P<media_bracket>\[\[.*?\]\])
(?P<url_bracket>\[((%(url)s)\:|#|\:)[^\s\]]+(\s[^\]]+)?\])
(?P<url>%(url_rule)s)
(?P<email>[-\w._+]+\@[\w-]+(\.[\w-]+)+)
(?P<media_entity>&\w+;)
(?P<ent>[<>&])""" % {
        'url': url_pattern,
        'url_rule': url_rule,
        }
    no_new_p_before = ( "heading rule table tableZ tr td "
                       "ul ol dl dt dd li li_none indent "
                       "macro processor pre "
                       "dml ml dol " )
    no_new_p_before = no_new_p_before.split()
    no_new_p_before = dict( zip( no_new_p_before, [1] * len( no_new_p_before ) ) )



    def __init__( self, word, raw ):
        self.word = word
        self.raw = raw
        self.macro = None

        self.is_em = 0
        self.is_b = 0
        self.is_u = 0
        self.is_strike = 0
        self.lineno = 0
        self.in_list = 0 # between <ul/ol/dl> and </ul/ol/dl>
        self.in_li = 0 # between <li> and </li>
        self.in_dd = 0 # between <dd> and </dd>
        self.is_small = False
        self.inhibit_p = 0 # if set, do not auto-create a <p>aragraph

        # holds the nesting level (in chars) of open lists
        self.list_indents = []
        self.list_types = []
        
        self.formatting_rules = self.formatting_rules

    def _close_item( self, result ):
        #result.append("<!-- close item begin -->\n")
        if self.in_li:
            self.in_li = 0
            if self.formatter.in_p:
                result.append( self.formatter.paragraph( 0 ) )
            result.append( self.formatter.listitem( 0 ) )
        if self.in_dd:
            self.in_dd = 0
            if self.formatter.in_p:
                result.append( self.formatter.paragraph( 0 ) )
            result.append( self.formatter.definition_desc( 0 ) )
        #result.append("<!-- close item end -->\n")


    def _indent_level( self ):
        """Return current char-wise indent level."""
        return len( self.list_indents ) and self.list_indents[-1]

    def _indent_to( self, new_level, list_type, numtype, numstart ):
        """Close and open lists."""
        open = []   # don't make one out of these two statements!
        close = []

        while self._indent_level() > new_level:
            self._close_item( close )
            if self.list_types[-1] == 'ol':
                tag = self.formatter.number_list( 0 )
            elif self.list_types[-1] == 'dl':
                tag = self.formatter.definition_list( 0 )
            else:
                tag = self.formatter.bullet_list( 0 )
            close.append( tag )

            del self.list_indents[-1]
            del self.list_types[-1]
            
            if self.list_types: # we are still in a list
                if self.list_types[-1] == 'dl':
                    self.in_dd = 1
                else:
                    self.in_li = 1
                
        # Open new list, if necessary
        if self._indent_level() < new_level:
            self.list_indents.append( new_level )
            self.list_types.append( list_type )

            if self.formatter.in_p:
                close.append( self.formatter.paragraph( 0 ) )
            
            if list_type == 'ol':
                tag = self.formatter.number_list( 1, numtype, numstart )
            elif list_type == 'dl':
                tag = self.formatter.definition_list( 1 )
            else:
                tag = self.formatter.bullet_list( 1 )
            open.append( tag )
            
            self.first_list_item = 1
            self.in_li = 0
            self.in_dd = 0
                    
        self.in_list = self.list_types != []
        return ''.join( close ) + ''.join( open )


    def _undent( self ):
        """Close all open lists."""
        result = []
        #result.append("<!-- _undent start -->\n")
        self._close_item( result )
        for type in self.list_types[::-1]:
            if type == 'ol':
                result.append( self.formatter.number_list( 0 ) )
            elif type == 'dl':
                result.append( self.formatter.definition_list( 0 ) )
            else:
                result.append( self.formatter.bullet_list( 0 ) )
        #result.append("<!-- _undent end -->\n")
        self.list_indents = []
        self.list_types = []
        return ''.join( result )

    def replace( self, match ):
        """ Replace match using type name """
        result = []
        for type, hit in match.groupdict().items():
            if hit is not None and type != "hmarker":
                # Open p for certain types
                if not ( self.inhibit_p or self.formatter.in_p
                         or ( type in self.no_new_p_before ) ):
                    result.append( self.formatter.paragraph( 1 ) )
                
                # Get replace method and replece hit
                #print 'type %s : %s' % ( type, hit )
                replace = getattr( self, '_' + type + '_repl' )
                result.append( replace( hit ) )
                return ''.join( result )
        else:
            # We should never get here
            import pprint
            raise Exception( "Can't handle match " + `match`
                + "\n" + pprint.pformat( match.groupdict() )
                + "\n" + pprint.pformat( match.groups() ) )

        return ""

    def scan( self, scan_re, line ):
        """ Scans one line
        
        Append text before match, invoke replace() with match, and add text after match.
        """
        result = []
        lastpos = 0

        ###result.append(u'<span class="info">[scan: <tt>"%s"</tt>]</span>' % line)
        for match in scan_re.finditer( line ):
            # Add text before the match
            if lastpos < match.start():
                ###result.append(u'<span class="info">[add text before match: <tt>"%s"</tt>]</span>' % line[lastpos:match.start()])
                if not ( self.inhibit_p or self.formatter.in_p ):
                    result.append( self.formatter.paragraph( 1 ) )
                result.append( self.formatter.text( line[lastpos:match.start()] ) )
            
            # Replace match with markup
            if not ( self.inhibit_p or self.formatter.in_p or
                     self.in_list ):
                result.append( self.formatter.paragraph( 1 ) )
            result.append( self.replace( match ) )
            lastpos = match.end()
        
        ###result.append('<span class="info">[no match, add rest: <tt>"%s"<tt>]</span>' % line[lastpos:])
        
        # Add paragraph with the remainder of the line
        if not ( self.in_li or self.in_dd or self.inhibit_p or
                self.formatter.in_p ) and lastpos < len( line ):
            result.append( self.formatter.paragraph( 1 ) )
        result.append( self.formatter.text( line[lastpos:] ) )
        return u''.join( result )
    
    # This is copied from the super class and modified
    def format( self, formatter ):
        """ For each line, scan through looking for magic
            strings, outputting verbatim any intervening text.
        """
        self.formatter = formatter
        buffer = StringIO()
        rules = self.formatting_rules.replace( '\n', '|' )

        scan_re = re.compile( rules, re.UNICODE )
        eol_re = re.compile( r'\r?\n', re.UNICODE )
        indent_re = re.compile( "^[*#;:]", re.UNICODE )
        comment_re = re.compile( "<!--.*-->", re.UNICODE | re.MULTILINE | re.DOTALL )
        template_re = re.compile( "{{[^}]*}}", re.UNICODE | re.MULTILINE | re.DOTALL )

        # get text and replace TABs
        rawtext = self.raw.expandtabs()
        rawtext = comment_re.sub( '', rawtext )

        # go through the lines
        self.lineno = 0
        self.lines = eol_re.split( rawtext )
        self.line_is_empty = 0

        # Main loop
        for line in self.lines:
            if is_ignored_macro_line( line ):
                continue
            self.lineno = self.lineno + 1
            self.line_was_empty = self.line_is_empty
            self.line_is_empty = 0
            self.first_list_item = 0
            self.inhibit_p = 1

            # we don't have \n as whitespace any more
            # This is the space between lines we join to one paragraph
            line = line + ' '
            
            # Paragraph break on empty lines
            if not line.strip():
                # CHANGE: removed check for not self.list_types
                # p should close on every empty line
                if ( self.formatter.in_p ):
                    buffer.write( self.formatter.paragraph( 0 ) )
                self.line_is_empty = 1
                continue

            # Reset indent level if needed
            if not indent_re.match( line ):
                buffer.write( self._indent_to( 0, "ul", None, None ) )

            # Scan line, format and write
            formatted_line = self.scan( scan_re, line )
            buffer.write( formatted_line )


        # Close code displays, paragraphs, tables and open lists
        buffer.write( self._undent() )
        if self.formatter.in_p: buffer.write( self.formatter.paragraph( 0 ) )
        return buffer.getvalue()

    def _emph_ibb_repl( self, word ):
        """Handle mixed emphasis, i.e. ''''' followed by '''."""
        self.is_b = not self.is_b
        self.is_em = not self.is_em
        if self.is_em and self.is_b:
            self.is_b = 2
        return self.formatter.emphasis( self.is_em ) + self.formatter.strong( self.is_b )

    def _emph_ibi_repl( self, word ):
        """Handle mixed emphasis, i.e. ''''' followed by ''."""
        self.is_b = not self.is_b
        self.is_em = not self.is_em
        if self.is_em and self.is_b:
            self.is_em = 2
        return self.formatter.strong( self.is_b ) + self.formatter.emphasis( self.is_em )


    def _emph_ib_or_bi_repl( self, word ):
        """Handle mixed emphasis, exactly five '''''."""
        ##print "*", self.is_b, self.is_em, "*"
        b_before_em = self.is_b > self.is_em > 0
        self.is_b = not self.is_b
        self.is_em = not self.is_em
        if b_before_em:
            return self.formatter.strong( self.is_b ) + self.formatter.emphasis( self.is_em )
        else:
            return self.formatter.emphasis( self.is_em ) + self.formatter.strong( self.is_b )

    def _emph_repl( self, word ):
        """Handle emphasis, i.e. '' and '''."""
        ##print "#", self.is_b, self.is_em, "#"
        if len( word ) == 3:
            self.is_b = not self.is_b
            if self.is_em and self.is_b:
                self.is_b = 2
            return self.formatter.strong( self.is_b )
        else:
            self.is_em = not self.is_em
            if self.is_em and self.is_b:
                self.is_em = 2
            return self.formatter.emphasis( self.is_em )

    def _b_repl( self, match ):
        """Handle <b>."""
        # This is not really correct because it mixes with ''' notation
        return self._emph_repl( "'''" )

    def _i_repl( self, match ):
        """Handle <i>."""
        # This is not really correct because it mixes with '' notation
        return self._emph_repl( "''" )

    def _u_repl( self, word ):
        """Handle underline."""
        self.is_u = not self.is_u
        return self.formatter.underline( self.is_u )

    def _sup_repl( self, word ):
        """Handle superscript."""
        return self.formatter.sup( 1 ) + \
            self.formatter.text( word[5:-6] ) + \
            self.formatter.sup( 0 )

    def _sub_repl( self, word ):
        """Handle subscript."""
        return self.formatter.sub( 1 ) + \
            self.formatter.text( word[5:-6] ) + \
            self.formatter.sub( 0 )

    def _tt_repl( self, word ):
        """Handle inline code."""
        return self.formatter.code( 1 ) + \
            self.formatter.text( word[4:-5] ) + \
            self.formatter.code( 0 )

    def _small_repl( self, word ):
        """Handle small."""
        self.is_small = not self.is_small
        return self.formatter.small( self.is_small )

    def _br_repl( self, word ):
        """Handle inlined entity."""
        return self.formatter.linebreak( 0 )

    def _comment_repl( self, word ):
        # if we are in a paragraph, we must close it so that normal text following
        # in the line below the comment will reopen a new paragraph.
        if self.formatter.in_p:
            self.formatter.paragraph( 0 )
        self.line_is_empty = 1 # markup following comment lines treats them as if they were empty
        return self.formatter.comment( word )

    def _closeP( self ):
        if self.formatter.in_p:
            return self.formatter.paragraph( 0 )
        return ''
    
    def _rule_repl( self, word ):
        """Handle sequences of dashes."""
        result = self._undent() + self._closeP()
        if len( word ) <= 4:
            result = result + self.formatter.rule()
        else:
            # Create variable rule size 1 - 6. Actual size defined in css.
            size = min( len( word ), 10 ) - 4
            result = result + self.formatter.rule( size )
        return result


    def _ent_repl( self, word ):
        """Handle SGML entities."""
        return self.formatter.text( word )
        #return {'&': '&amp;',
        #        '<': '&lt;',
        #        '>': '&gt;'}[word]

    ###########################################################################
    #LISTING
    def _li_none_repl( self, match ):
        """Handle type=none (" .") lists."""
        result = []
        self._close_item( result )
        self.in_li = 1
        css_class = None
        if self.line_was_empty and not self.first_list_item:
            css_class = 'gap'
        result.append( self.formatter.listitem( 1, css_class=css_class, style="list-style-type:none" ) )
        return ''.join( result )


    def _list( self, result, style=None ):
        """Handle all lists."""
        self._close_item( result )
        self.in_li = 1
        css_class = ''
        if self.line_was_empty and not self.first_list_item:
            css_class = 'gap'
        result.append( self.formatter.listitem( 1, css_class=css_class,
                                              style=style ) )
        return ''.join( result )

    def _li_repl( self, match ):
        """Handle bullet lists."""
        result = [ self._indent_to( len( match ), "ul", None, None ), ]
        return self._list( result )

    def _ol_repl( self, match ):
        """Handle numbered lists."""
        result = [ self._indent_to( len( match ), "ol", None, None ), ]
        return self._list( result )

    def _ml_repl( self, match ):
        """Handle mixed lists."""
        if match[-1] == "*":
            return self._li_repl( match )
        else:
            return self._ol_repl( match )

    def _dol_repl( self, match ):
        return self._dml_repl( match )
    
    def _dml_repl( self, match ):
        """Handle mixed lists."""
        result = [ self.formatter._newline(), self._indent_to( len( match ), "dl", None, None ), ]
        self._close_item( result )
        self.in_dd = 1
        result.extend( [
                self.formatter.definition_desc( 1 ),
                ] )
        return ''.join( result )
        
    def _dl_repl( self, match ):
        """Handle definition lists."""
        prefix = re.search( "^;+", match ).group( 0 )
        term = match[len( prefix ):-1].strip()
        result = [ self._indent_to( len( prefix ), "dl", None, None ), ]
        self._close_item( result )
        self.in_dd = 1
        result.extend( [
                self.formatter.definition_term( 1 ),
                self.formatter.text( term ),
                self.formatter.definition_term( 0 ),
                self.formatter.definition_desc( 1 ),
                ] )
        return ''.join( result )

    def _ind_repl( self, match ):
        """Handle indented blocks."""
        result = [ self._indent_to( len( match ), "ul", None, None ), ]
        return self._list( result, style="list-style-type:none" )

    ###########################################################################
    #LINK
    def _url_bracket_repl( self, word ):
        """Handle bracketed URLs."""

        # Local extended link?
        if word[1] == ':':
            words = word[2:-1].split( ':', 1 )
            if len( words ) == 1:
                words = words * 2
            words[0] = 'wiki:Self:%s' % words[0]
            return self.interwiki( words, pretty_url=1 )
            #return self._word_repl(words[0], words[1])

        # Traditional split on space
        words = word[1:-1].split( None, 1 )
        if len( words ) == 1:
            words = words * 2

        if words[0][0] == '#':
            # anchor link
            return ( self.formatter.url( 1, words[0] ) + 
                    self.formatter.text( words[1] ) + 
                    self.formatter.url( 0 ) )

        scheme = words[0].split( ":", 1 )[0]
        if scheme == "wiki":
            return self.interwiki( words, pretty_url=1 )

        return ( self.formatter.url( 1, words[0], css=scheme, do_escape=0 ) + 
                self.formatter.text( words[1] ) + 
                self.formatter.url( 0 ) )

    def _url_repl( self, word ):
        """Handle literal URLs including inline images."""
        scheme = word.split( ":", 1 )[0]

        if scheme == "wiki":
            return self.interwiki( [word] )

        return ( self.formatter.url( 1, word, css=scheme ) + 
                self.formatter.text( word ) + 
                self.formatter.url( 0 ) )

    def _email_repl( self, word ):
        """Handle email addresses (without a leading mailto:)."""
        return ( self.formatter.url( 1, "mailto:" + word, css='mailto' ) + 
                self.formatter.text( word ) + 
                self.formatter.url( 0 ) )


    ###########################################################################
    #MEDIA WIKI
    def _media_entity_repl( self, word ):
        """Handle inlined entity."""
        return self.formatter.rawHTML( word )


    def _media_heading_repl( self, word ):
        """Handle headings."""
        # The easiest thing to do is to convert the markup
        asMoin = word.strip()
        asMoin = re.sub( "^=(=+)", r"\1 ", asMoin )
        asMoin = re.sub( "(=+)=$", r" \1", asMoin )
        return self._heading_repl( asMoin )

    interwikimap = {
        'wikipedia': 'http://en.wikipedia.org/wiki/',
        'w': 'http://en.wikipedia.org/wiki/',
        'wiktionary': 'http://en.wiktionary.org/wiki/',
        'wikt': 'http://en.wiktionary.org/wiki/',
        'wikinews': 'http://en.wikinews.org/wiki/',
        'n': 'http://en.wikinews.org/wiki/',
        'wikibooks': 'http://en.wikibooks.org/wiki/',
        'b': 'http://en.wikibooks.org/wiki/',
        'wikiquote': 'http://en.wikiquote.org/wiki/',
        'q': 'http://en.wikiquote.org/wiki/',
        '': '',
    } #http://meta.wikimedia.org/wiki/Help:Interwiki_linking

    def _split_links( self, text ):
        links = text.split( ':' )
        interwiki = None
        if 2 == len( links ):
            interwiki = links[0]
            links[0] = links[1]
        links = links[0].split( '|' )
        
        link = links[0]
        text = link
        if 2 == len( links ):
            text = links[1]
        return ( interwiki, link, text )
    
    def _media_bracket_repl( self, word ):
        """Handle double bracket links."""
        ( interwiki, link, text ) = self._split_links( word[2:-2] )
        
        if interwiki:
            if self.interwikimap.has_key( interwiki ):
                prefix = self.interwikimap[interwiki]
            else:
                prefix = 'unknown://'
                INTERWIKI_LOGGER.info( self.word + "\t" + interwiki )
                
            return ( self.formatter.url( 1, prefix + link, 'wikipedia' ) + 
                     self.formatter.text( text ) + 
                     self.formatter.url( 0 ) )
        else:
            return ( self.formatter.url( 1, link, 'rdict' ) + 
                     self.formatter.text( text ) + 
                     self.formatter.url( 0 ) )

    def _media_anchor_repl( self, word ):
        ( interwiki, link, text ) = self._split_links( word[2:-2] )
        
        return ( self.formatter.url( 1, link, 'anchor' ) + 
                 self.formatter.text( text ) + 
                 self.formatter.url( 0 ) )
        
                     

    def _macro_repl( self, word ):
        """Handle macros ({{macroname}})."""
        macro_text = word[2:-2]
        m = Macro( self.word, macro_text, self.formatter )
        return m.execute()
