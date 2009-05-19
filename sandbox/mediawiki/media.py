# -*- coding: iso-8859-1 -*-

# media.py --- Plugin parser for MoinMoin parsing MediaWiki documents

# $Id: media.py,v 1.16 2006/04/20 19:35:38 stefan Exp $

# Copyright 2005 Stefan Merten <smerten@oekonux.de>

# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

###############################################################################
###############################################################################

import re

from MoinMoin.parser import wiki

###############################################################################
###############################################################################

# Because the syntaxes are rather similar most stuff is inherited from the
# standard wiki parser. Some functions are copied and modified.
class Parser(wiki.Parser):

    # the big, fat, ugly one ;)
    formatting_rules = ur"""(?:(?P<emph_ibb>'''''(?=[^']+'''))
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
(?P<macro>\[\[(%%(macronames)s)\(.*?\)\]\]))
(?P<media_anchor>\[\[#\w+\]\])
(?P<ml>^(\*+#+[*#]*|#+\*+[*#]*))
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
(?P<ent>[<>&])"""  % {
        'url': wiki.Parser.url_pattern,
        'url_rule': wiki.Parser.url_rule,
        }
#(?P<processor>(\{\{\{(#!.*|\s*$)))

    # Map languages to page name and label of discussion pages
    lang2nm_spc = { 'en': ( u'Talk', u'Discussion', u'User', u'Image',
                            u'Media', ),
                    'de': ( u'Diskussion', u'Diskussion', u'Benutzer', u'Bild',
                            u'Media', ),
                    # Fallback for unknown languages
                    '': ( u'Talk', u'Discussion', u'User', u'Image',
                          u'Media', ),
                    }

    ###########################################################################

    def _b_repl(self, match):
        """Handle <b>."""
        # This is not really correct because it mixes with ''' notation
        return self._emph_repl("'''")

    ###########################################################################

    def _i_repl(self, match):
        """Handle <i>."""
        # This is not really correct because it mixes with '' notation
        return self._emph_repl("''")

    ###########################################################################

    def _media_bracket_repl(self, word):
        """Handle double bracket links."""
        content = word[2:-2]
        if content:
            words = content.split("|", 1)
            wikiname = words[0]
            if len(words) > 1:
                text = words[1]
            else:
                text = None

            # Remap link as in MediaWiki
            wikiname = re.sub("\s+", "_", wikiname.strip())
            wikiname = wikiname[0].upper() + wikiname[1:]

            # Handle special namespaces
            if re.search("(?i)_" + self.talk_name + ":", wikiname):
                wikiname = re.sub("(?i)_" + self.talk_name + ":", ":",
                                  wikiname) + self.talk_page
            elif re.search("(?i)^" + self.talk_name + ":", wikiname):
                wikiname = re.sub("(?i)^" + self.talk_name + ":", "",
                                  wikiname) + self.talk_page

            wikiname = re.sub("(?i)^" + self.user_name + ":", "", wikiname)

            att_re = "(?i)^(" + self.media_name + "|" + self.image_name + "):"
            if re.search(att_re, wikiname):
                words[0] = re.sub(att_re, "attachment:", wikiname)
                return self.attachment(words)

            # Map remaining namespaces to top level pages
            wikiname = re.sub("^(\w+):", r"\1/", wikiname)

            return self._word_repl(wikiname, text)
        else:
            return word

    ###########################################################################

    def _media_heading_repl(self, word):
        """Handle headings."""
        # The easiest thing to do is to convert the markup
        asMoin = word.strip()
        asMoin = re.sub("^=(=+)", r"\1 ", asMoin)
        asMoin = re.sub("(=+)=$", r" \1", asMoin)
        return self._heading_repl(asMoin)

    ###########################################################################

    def _list(self, result, style=None):
        """Handle all lists."""
        self._close_item(result)
        self.in_li = 1
        css_class = ''
        if self.line_was_empty and not self.first_list_item:
            css_class = 'gap'
        result.append(self.formatter.listitem(1, css_class=css_class,
                                              style=style))
        return ''.join(result)

    ###########################################################################

    def _li_repl(self, match):
        """Handle bullet lists."""
        result = [ self._indent_to(len(match), "ul", None, None), ]
        return self._list(result)

    ###########################################################################

    def _ol_repl(self, match):
        """Handle numbered lists."""
        result = [ self._indent_to(len(match), "ol", "1", None), ]
        return self._list(result)

    ###########################################################################

    def _ml_repl(self, match):
        """Handle mixed lists."""
        if match[-1] == "*":
            return self._li_repl(match)
        else:
            return self._ol_repl(match)

    ###########################################################################

    def _ind_repl(self, match):
        """Handle indented blocks."""
        result = [ self._indent_to(len(match), "ul", None, None), ]
        return self._list(result, style="list-style-type:none")

    ###########################################################################

    def _dl_repl(self, match):
        """Handle definition lists."""
        prefix = re.search("^;+", match).group(0)
        term = match[len(prefix):-1].strip()
        result = [ self._indent_to(len(prefix), "dl", None, None), ]
        self._close_item(result)
        self.in_dd = 1
        result.extend([
                self.formatter.definition_term(1),
                self.formatter.text(term),
                self.formatter.definition_term(0),
                self.formatter.definition_desc(1),
                ])
        return ''.join(result)

    ###########################################################################

    def _sup_repl(self, word):
        """Handle superscript."""
        return self.formatter.sup(1) + \
            self.formatter.text(word[5:-6]) + \
            self.formatter.sup(0)

    ###########################################################################

    def _sub_repl(self, word):
        """Handle subscript."""
        return self.formatter.sub(1) + \
            self.formatter.text(word[5:-6]) + \
            self.formatter.sub(0)

    ###########################################################################

    def _small_repl(self, word):
        """Handle small."""
        self.is_small = not self.is_small
        return self.formatter.small(self.is_small)

    ###########################################################################

    def _tt_repl(self, word):
        """Handle inline code."""
        return self.formatter.code(1) + \
            self.formatter.text(word[4:-5]) + \
            self.formatter.code(0)

    ###########################################################################

    def _pre_repl(self, word):
        """Handle code displays."""
        if word[1] != '/' and not self.in_pre:
            self.in_pre = 3
            return self._closeP() + self.formatter.preformatted(self.in_pre)
        elif word[1] == '/' and self.in_pre:
            self.in_pre = 0
            self.inhibit_p = 0
            return self.formatter.preformatted(self.in_pre)
        return word

    ###########################################################################

    def _media_entity_repl(self, word):
        """Handle inlined entity."""
        return self.formatter.rawHTML(word)

    ###########################################################################

    def _br_repl(self, word):
        """Handle inlined entity."""
        return self.formatter.linebreak(0)

    ###########################################################################

    def _media_anchor_repl(self, word):
        """Handle inlined entity."""
        return self.formatter.anchordef(word[3:-2])

    ###########################################################################

    # This is copied from the super class and modified
    def format(self, formatter):
        """ For each line, scan through looking for magic
            strings, outputting verbatim any intervening text.
        """
        self.formatter = formatter
        self.hilite_re = self.formatter.page.hilite_re
        ( self.talk_name, self.talk_label,
          self.user_name, self.image_name,
          self.media_name, ) = self.lang2nm_spc.get(getattr(self.cfg,
                                                            'default_lang',
                                                            ""),
                                                    self.lang2nm_spc[''])
        self.talk_page = "/" + self.talk_name

        # prepare regex patterns
        rules = self.formatting_rules.replace('\n', '|')
        if getattr(self.cfg, 'allow_numeric_entities', False):
            rules = ur'(?P<ent_numeric>&#\d{1,5};)|' + rules

        self.request.clock.start('compile_huge_and_ugly')        
        scan_re = re.compile(rules, re.UNICODE)
        eol_re = re.compile(r'\r?\n', re.UNICODE)
        indent_re = re.compile("^[*#;:]", re.UNICODE)
        self.request.clock.stop('compile_huge_and_ugly')        

        # get text and replace TABs
        rawtext = self.raw.expandtabs()

        # go through the lines
        self.lineno = 0
        self.lines = eol_re.split(rawtext)
        self.line_is_empty = 0

        # write out discussion link at the very top unless this is discussion
        # already
        page_name = self.request.page.page_name
        if not page_name.endswith(self.talk_page):
            # not a discussion page already
            self.request.write(self._word_repl(self.talk_page, self.talk_label))

        # Main loop
        for line in self.lines:
            self.lineno = self.lineno + 1
            self.table_rowstart = 1
            self.line_was_empty = self.line_is_empty
            self.line_is_empty = 0
            self.first_list_item = 0
            self.inhibit_p = 0

            if self.in_pre:
                # TODO: move this into function
                # still looking for processing instructions
                # TODO: use strings for pre state, not numbers
                if self.in_pre == 1:
                    self.processor = None
                    self.processor_is_parser = 0
                    processor_name = ''
                    if (line.strip()[:2] == "#!"):
                        processor_name = line.strip()[2:].split()[0]
                        self.processor = wikiutil.importPlugin(
                            self.request.cfg, "processor", processor_name, "process")
                                                               
                        # now look for a parser with that name
                        if self.processor is None:
                            self.processor = wikiutil.importPlugin(
                                self.request.cfg, "parser", processor_name, "Parser") 
                            if self.processor:
                                self.processor_is_parser = 1
                    if self.processor:
                        self.in_pre = 2
                        self.colorize_lines = [line]
                        self.processor_name = processor_name
                        continue
                    else:
                        self.request.write(self._closeP() +
                                           self.formatter.preformatted(1))
                        self.in_pre = 3
                if self.in_pre == 2:
                    # processing mode
                    endpos = line.find("}}}")
                    if endpos == -1:
                        self.colorize_lines.append(line)
                        continue
                    if line[:endpos]:
                        self.colorize_lines.append(line[:endpos])
                    
                    # Close p before calling processor
                    # TODO: do we really need this?
                    self.request.write(self._closeP())
                    res = self.formatter.processor(self.processor_name,
                                                   self.colorize_lines, 
                                                   self.processor_is_parser)
                    self.request.write(res)
                    del self.colorize_lines
                    self.in_pre = 0
                    self.processor = None

                    # send rest of line through regex machinery
                    line = line[endpos+3:]                    
            else:
                # we don't have \n as whitespace any more
                # This is the space between lines we join to one paragraph
                line = line + ' '
                
                # Paragraph break on empty lines
                if not line.strip():
                    if self.in_table:
                        self.request.write(self.formatter.table(0))
                        self.in_table = 0
                    # CHANGE: removed check for not self.list_types
                    # p should close on every empty line
                    if (self.formatter.in_p):
                        self.request.write(self.formatter.paragraph(0))
                    self.line_is_empty = 1
                    continue

                # Reset indent level if needed
                if not indent_re.match(line):
                    self.request.write(self._indent_to(0, "ul", None, None))

            # Scan line, format and write
            formatted_line = self.scan(scan_re, line)
            self.request.write(formatted_line)

            if self.in_pre:
                self.request.write(self.formatter.linebreak())

        # Close code displays, paragraphs, tables and open lists
        self.request.write(self._undent())
        if self.in_pre: self.request.write(self.formatter.preformatted(0))
        if self.formatter.in_p: self.request.write(self.formatter.paragraph(0))
        if self.in_table: self.request.write(self.formatter.table(0))

###############################################################################
###############################################################################

podDocumentation = """

=head1 NAME

media.py - A MediaWiki parser plugin for MoinMoin

=head1 SYNOPSIS

  #format media

  ==MediaWiki level 1 header==
  * Bullet without indentation
  ** On level two - as MediaWiki likes

=head1 DESCRIPTION

B<media.py> is a parser plugin for MoinMoin. It can parse a subset of MediaWiki
syntax and use it for MoinMoin.

=head2 Supported features

Currently a substantial subset of MediaWiki is supported. These constructs are
recognized:

  MediaWiki syntax	Meaning				Notes

  ''x''			Italics
  '''x'''		Bold
  '''''x'''''		VeryStrong
  <sup>x</sup>		Superscript text
  <sub>x</sub>		Subscript text
  <small>x</small>	Small text
  <u>x</u>		Underline
  <tt>x</tt>		Preformatted embedded text
  <i>x</i>		Italics
  <b>x</b>		Bold
  &x;			HTML entities
  <br>			Breaking paragraphs

  ----			Divider

  http://x		External Link
  [http://x]		External Link			[5]
  [http://x y]		External Link with text

  [[x]]			Link				[1]
  [[x|y]]		Link with text			[1]
  [[#Anchor]]		Setting an anchor in a page

  ==x==			Header level 1
  ===x===		Header level 2
  ...
  * x			Bullet list level 1
  ** x			Bullet list level 2
  ...
  # x			Numbered list level 1
  ## x			Numbered list level 2
  ...
  *#*			Mixed lists
  : x			Indented paragraph level 1
  :: x			Indented paragraph level 2
  ...
  ;x:y			Definition level 1
  ;;x:y			Definition level 2		[2]
  ...
  <pre>x</pre>		Preformatted uninterpreted text
  <nowiki>x</nowiki>	Uninterpreted text		[3]

  <!-- x -->		Comments			[4]

=over 4

=item [1]

These restrictions currently apply:

=over 4

=item *

Stuff in parentheses is not hidden from display.

=item *

Namespaces are not hidden. Instead they are transformed to top level MoinMoin
pages (the most natural mapping of namespaces).

=item *

Interwiki and language linking MediaWiki is not really supported well.

=item *

Special namespace C<Special:> is not supported. Such links map to a macro in
MoinMoin.

=item *

Dates. Done by a macro in MoinMoin.

=back

These adaptions are made:

=over 4

=item *

The C<User:> namespace is dropped silently to map the MediaWiki logic into
MoinMoin logic.

=item *

Special namespaces C<Image:> and C<Media:> used to embed pictures or to attach
arbitrary data are mapped to the C<attachment:> for MoinMoin so they are
effectively mapped to the attachment feature of MoinMoin.

=item *

The C<Talk:> namespace is mapped to a sub page C</Talk>.

=item *

Namespace operation is currently supported for English and German.

=back

=item [2]

This is probably an extension to MediaWiki original syntax.

=item [3]

<nowiki> is handled the same as <pre>. I.e. the whitespace structure is kept.

=item [4]

Comments are only recognized if they appear on a single physical line.

=item [5]

The missing link text is not replaced by an automatically generated number.

=back

Each non-discussion page gets an automatic link to its discussion page.

=head2 Unsupported features

These things are not yet supported:

  MediaWiki syntax	Meaning

  <center>x</center>	Centered text
  ISBN x		Link to a book by its ISBN
  RFC x			Link to an RFC
  <strike>x</strike>	Stroke text
  <math>x</math>	TeX markup
   x			Preformatted text line

Moreover these features are not yet supported:

=over 4

=item * Tables

=item * HTML

=item * MediaWiki templates

=back

If you are really missing a feature please check

  http://en.wiki.oekonux.org/Oekonux/Project/Wiki/MediaWikiFAQ/FeatureRequests

Feel free to add your feature request there.

=head2 Unsupportable features

The following things are beyond a parser because they are replaced while
processing an edit of before displaying a page actually. While editing the
MoinMoin counterparts must be used:

  MediaWiki syntax	MoinMoin syntax		Meaning

  ~~~			@USERNAME@		Insert user name
  ~~~~			@SIG@			Insert user name and current time
  #REDIRECT [[x]]	#REDIRECT x		Page redirection

=head2 Additional features

The following things are inherited from MoinMoin and can be used:

  MoinMoin syntax	Meaning				Notes

  [[macro(arguments)]]	Macros present in MoinMoin	[1]
  someone@example.com	Mail addresses

=over 4

=item [1]

You must use parentheses even if there are no arguments. Otherwise macro syntax
would conflict with page names.

=back

=head1 INSTALLATION

See

	http://moinmoin.wikiwikiweb.de/ParserMarket#head-17c33967bbb4345a453627b944bad1f1bc4b2791

=head1 AUTHOR

Stefan Merten <smerten@oekonux.de>

=head1 LICENSE

This program is licensed under the terms of the GPL. See

	http://www.gnu.org/licenses/gpl.txt

=head1 AVAILABILTY

See

	http://www.merten-home.de/FreeSoftware/media4moin/

"""
