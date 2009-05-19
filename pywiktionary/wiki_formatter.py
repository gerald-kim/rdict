# -*- coding: utf-8 -*-

line_anchors = True
prettyprint = True

# These are the HTML elements that we treat as block elements.
_blocks = set( ['dd', 'div', 'dl', 'dt', 'form', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
               'hr', 'li', 'ol', 'p', 'pre', 'table', 'tbody', 'td', 'tfoot', 'th',
               'thead', 'tr', 'ul', 'blockquote' ] )

# These are the HTML elements which are typically only used with
# an opening tag without a separate closing tag.  We do not
# include 'script' or 'style' because sometimes they do have
# content, and also IE has a parsing bug with those two elements (only)
# when they don't have a closing tag even if valid XHTML.

_self_closing_tags = set( ['area', 'base', 'br', 'col', 'frame', 'hr', 'img', 'input',
                          'isindex', 'link', 'meta', 'param'] )

# We only open those tags and let the browser auto-close them:
_auto_closing_tags = set( ['p'] )

# These are the elements which generally should cause an increase in the
# indention level in the html souce code.
_indenting_tags = set( ['ol', 'ul', 'dl', 'li', 'dt', 'dd', 'tr', 'td'] )

# These are the elements that discard any whitespace they contain as
# immediate child nodes.
_space_eating_tags = set( ['colgroup', 'dl', 'frameset', 'head', 'map' 'menu',
                          'ol', 'optgroup', 'select', 'table', 'tbody', 'tfoot',
                          'thead', 'tr', 'ul'] )

# These are standard HTML attributes which are typically used without any
# value; e.g., as boolean flags indicated by their presence.
_html_attribute_boolflags = set( ['compact', 'disabled', 'ismap', 'nohref',
                                 'noresize', 'noshade', 'nowrap', 'readonly',
                                 'selected', 'wrap'] )

# These are all the standard HTML attributes that are allowed on any element.
_common_attributes = set( ['accesskey', 'class', 'dir', 'disabled', 'id', 'lang',
                          'style', 'tabindex', 'title'] )


def rewrite_attribute_name( name, default_namespace='html' ):
    # Handle any namespaces (just in case someday we support XHTML)
    if ':' in name:
        ns, name = name.split( ':', 1 )
    elif '__' in name:
        ns, name = name.split( '__', 1 )
    elif name == 'xmlns':
        ns = ''
    else:
        ns = default_namespace

    name.replace( '__', '-' )
    if ns == 'html':
        # We have an HTML attribute, fix according to DTD
        if name == 'content_type': # MIME type such as in <a> and <link> elements
            name = 'type'
        elif name == 'content_id': # moin historical convention
            name = 'id'
        elif name in ( 'css_class', 'css' ): # to avoid python word 'class'
            name = 'class'
        elif name.startswith( 'on' ): # event handler hook
            name = name.lower()
    return ns, name


def extend_attribute_dictionary( attributedict, ns, name, value ):
    key = ns, name
    if value is None:
        if attributedict.has_key( key ):
            del attributedict[key]
    else:
        if ns == 'html' and attributedict.has_key( key ):
            if name == 'class':
                # CSS classes are appended by space-separated list
                value = attributedict[key] + ' ' + value
            elif name == 'style':
                # CSS styles are appended by semicolon-separated rules list
                value = attributedict[key] + '; ' + value
            elif name in _html_attribute_boolflags:
                # All attributes must have a value. According to XHTML those
                # traditionally used as flags should have their value set to
                # the same as the attribute name.
                value = name
        attributedict[key] = value

def escape( s, quote=0 ):
    if not isinstance( s, ( str ) ):
        s = unicode( s )
    if not isinstance( s, ( unicode ) ):
        s = unicode( s )

    # Must first replace &
    s = s.replace( "&", "&amp;" )

    # Then other...
    s = s.replace( "<", "&lt;" )
    s = s.replace( ">", "&gt;" )
    if quote:
        s = s.replace( '"', "&quot;" )
    return s

class Formatter:
    """ This defines the output interface used all over the rest of the code.

        Note that no other means should be used to generate _content_ output,
        while navigational elements (HTML page header/footer) and the like
        can be printed directly without violating output abstraction.
    """

    hardspace = '&nbsp;'
    indentspace = ' '

    def __init__( self ):
        self.in_p = 0

        # inline tags stack. When an inline tag is called, it goes into
        # the stack. When a block element starts, all inline tags in
        # the stack are closed.
        self._inlineStack = []
        
        # stack of all tags
        self._tag_stack = []
        self._indent_level = 0


    # Primitive formatter functions #####################################
    def _formatAttributes( self, attr=None, allowed_attrs=None, **kw ):
        """ Return HTML attributes formatted as a single string. (INTERNAL USE BY HTML FORMATTER ONLY!)

        @param attr: dict containing keys and values
        @param allowed_attrs: A list of allowable attribute names
        @param kw: other arbitrary attributes expressed as keyword arguments.
        @rtype: string
        @return: formated attributes or empty string
        """

        # Merge the attr dict and kw dict into a single attributes
        # dictionary (rewriting any attribute names, extracting
        # namespaces, and merging some values like css classes).
        attributes = {} # dict of key=(namespace,name): value=attribute_value
        if attr:
            for a, v in attr.items():
                a_ns, a_name = rewrite_attribute_name( a )
                extend_attribute_dictionary( attributes, a_ns, a_name, v )
        if kw:
            for a, v in kw.items():
                a_ns, a_name = rewrite_attribute_name( a )
                extend_attribute_dictionary( attributes, a_ns, a_name, v )

        # Add title attribute if missing, but it has an alt.
        if attributes.has_key( ( 'html', 'alt' ) ) and not attributes.has_key( ( 'html', 'title' ) ):
            attributes[( 'html', 'title' )] = attributes[( 'html', 'alt' )]

        # Force both lang and xml:lang to be present and identical if
        # either exists.  The lang takes precedence over xml:lang if
        # both exist.
        #if attributes.has_key(('html', 'lang')):
        #    attributes[('xml', 'lang')] = attributes[('html', 'lang')]
        #elif attributes.has_key(('xml', 'lang')):
        #    attributes[('html', 'lang')] = attributes[('xml', 'lang')]

        # Check all the HTML attributes to see if they are known and
        # allowed.  Ignore attributes if in non-HTML namespaces.
        if allowed_attrs:
            for name in [key[1] for key in attributes.keys() if key[0] == 'html']:
                if name in _common_attributes or name in allowed_attrs:
                    pass
                elif name.startswith( 'on' ):
                    pass  # Too many event handlers to enumerate, just let them all pass.
                else:
                    # Unknown or unallowed attribute.
                    err = 'Illegal HTML attribute "%s" passed to formatter' % name
                    raise ValueError( err )

        # Finally, format them all as a single string.
        if attributes:
            # Construct a formatted string containing all attributes
            # with their values escaped.  Any html:* namespace
            # attributes drop the namespace prefix.  We build this by
            # separating the attributes into three categories:
            #
            #  * Those without any namespace (should only be xmlns attributes)
            #  * Those in the HTML namespace (we drop the html: prefix for these)
            #  * Those in any other non-HTML namespace, including xml:

            xmlnslist = ['%s="%s"' % ( k[1], escape( v, 1 ) )
                         for k, v in attributes.items() if not k[0]]
            htmllist = ['%s="%s"' % ( k[1], escape( v, 1 ) )
                        for k, v in attributes.items() if k[0] == 'html']
            otherlist = ['%s:%s="%s"' % ( k[0], k[1], escape( v, 1 ) )
                         for k, v in attributes.items() if k[0] and k[0] != 'html']

            # Join all these lists together in a space-separated string.  Also
            # prefix the whole thing with a space too.
            htmllist.sort()
            otherlist.sort()
            all = [''] + xmlnslist + htmllist + otherlist
            return ' '.join( all )
        return ''

    def _open( self, tag, newline=False, attr=None, allowed_attrs=None, **kw ):
        """ Open a tag with optional attributes (INTERNAL USE BY HTML FORMATTER ONLY!)
        
        @param tag: html tag, string
        @param newline: render tag so following data is on a separate line
        @param attr: dict with tag attributes
        @param allowed_attrs: list of allowed attributes for this element
        @param kw: arbitrary attributes and values
        @rtype: string ?
        @return: open tag with attributes as a string
        """
        # If it is self-closing, then don't expect a closing tag later on.
        is_self_closing = ( tag in _self_closing_tags ) and ' /' or ''

        if tag in _blocks:
            # Block elements
            result = []
            
            # Add language attributes, but let caller overide the default
            attributes = {}
            if attr:
                attributes.update( attr )
            
            # Format
            attributes = self._formatAttributes( attributes, allowed_attrs=allowed_attrs, **kw )
            result.append( '<%s%s%s>' % ( tag, attributes, is_self_closing ) )
            if newline:
                result.append( self._newline() )
            tagstr = u''.join( result )
        else:
            # Inline elements
            # Add to inlineStack
            if not is_self_closing:
                # Only push on stack if we expect a close-tag later
                self._inlineStack.append( tag )
            # Format
            tagstr = u'<%s%s%s>' % ( tag,
                                      self._formatAttributes( attr, allowed_attrs, **kw ),
                                      is_self_closing )
        # XXX SENSE ???
        #if not self.close:
        #    self._tag_stack.append(tag)
        #    if tag in _indenting_tags:
        #        self._indent_level += 1
        return tagstr

    def _close( self, tag, newline=False ):
        """ Close tag (INTERNAL USE BY HTML FORMATTER ONLY!)

        @param tag: html tag, string
        @param newline: render tag so following data is on a separate line
        @rtype: string
        @return: closing tag as a string
        """
        if tag in _self_closing_tags or tag in _auto_closing_tags:
            # This tag was already closed
            tagstr = ''
        elif tag in _blocks:
            # Block elements
            # Close all tags in inline stack
            # Work on a copy, because close(inline) manipulate the stack
            result = []
            stack = self._inlineStack[:]
            stack.reverse()
            for inline in stack:
                result.append( self._close( inline ) )
            # Format with newline
            if newline:
                result.append( self._newline() )
            result.append( '</%s>' % ( tag ) )
            tagstr = ''.join( result )            
        else:
            # Inline elements 
            # Pull from stack, ignore order, that is not our problem.
            # The code that calls us should keep correct calling order.
            if tag in self._inlineStack:
                self._inlineStack.remove( tag )
            tagstr = '</%s>' % tag

        # XXX see other place marked with "SENSE"
        #if tag in _self_closing_tags:
        #    self._tag_stack.pop()
        #    if tag in _indenting_tags:
        #        # decrease indent level
        #        self._indent_level -= 1
        if newline:
            tagstr += self._newline()
        return tagstr

    # Links ##############################################################
    def url( self, on, url=None, css=None, do_escape=0, **kw ):
        """
        Inserts an <a> element.

        Call once with on=1 to start the link, and again with on=0
        to end it (no other arguments are needed when on==0).
        """
        if on:
            attrs = {}

            # Handle the URL mapping
            if url is None and kw.has_key( 'href' ):
                url = kw['href']
                del kw['href']
            if url is not None:
                #url = wikiutil.mapURL(self.request, url)
                # TODO just calling url_quote does not work, as it will also quote "http:" to "http%xx" X)
                if 0: # do_escape: # protocol and server part must not get quoted, path should get quoted
                    url = wikiutil.url_quote( url )
                attrs['href'] = url

            if css:
                attrs['class'] = css
            
            markup = self._open( 'a', attr=attrs, **kw )
        else:
            markup = self._close( 'a' )
        return markup

    def anchordef( self, id ):
        """Inserts an invisible element used as a link target.

        Inserts an empty <span> element with an id attribute, used as an anchor
        for link references.  We use <span></span> rather than <span/>
        for browser portability.
        """
        # Don't add newlines, \n, as it will break pre and
        # line-numbered code sections (from line_achordef() method).
        #return '<a id="%s"></a>' % (id, ) # do not use - this breaks PRE sections for IE
        return '<span class="anchor" id="%s"></span>' % escape( id, 1 )

    def anchorlink( self, on, name='', **kw ):
        """Insert an <a> link pointing to an anchor on the same page.
        """

        attrs = {}
        if name:
            attrs['href'] = '#%s' % name
        if kw.has_key( 'href' ):
            del kw['href']
        if on:
            str = self._open( 'a', attr=attrs, **kw )
        else:
            str = self._close( 'a' )
        return str

    def line_anchorlink( self, on, lineno=0 ):
        if line_anchors:
            return self.anchorlink( on, name="line-%d" % lineno )
        else:
            return ''


    # Text ##############################################################
    
    def _text( self, text ):
        return escape( text )

    # Inline ###########################################################
        
    def strong( self, on, **kw ):
        """Creates an HTML <strong> element.
        """
        tag = 'b'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )

    def emphasis( self, on, **kw ):
        """Creates an HTML <em> element.
        """
        tag = 'i'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )

    def underline( self, on, **kw ):
        """Creates a text span for underlining (css class "u").
        """
        tag = 'span'
        if on:
            return self._open( tag, attr={'class': 'u'}, allowed_attrs=[], **kw )
        return self._close( tag )

    def sup( self, on, **kw ):
        """Creates a <sup> element for superscript text.

        Call once with on=1 to start the region, and a second time
        with on=0 to end it.
        """
        tag = 'sup'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )

    def sub( self, on, **kw ):
        """Creates a <sub> element for subscript text.

        Call once with on=1 to start the region, and a second time
        with on=0 to end it.
        """
        tag = 'sub'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )

    def code( self, on, **kw ):
        """Creates a <tt> element for inline code or monospaced text.

        Call once with on=1 to start the region, and a second time
        with on=0 to end it.

        Any text within this section will have spaces converted to
        non-break spaces.
        """
        tag = 'tt'
        # Maybe we don't need this, because we have tt will be in inlineStack.
        self._in_code = on        
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )
        
    def small( self, on, **kw ):
        """Creates a <small> element for smaller font.

        Call once with on=1 to start the region, and a second time
        with on=0 to end it.
        """
        tag = 'small'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )

    def big( self, on, **kw ):
        """Creates a <big> element for larger font.

        Call once with on=1 to start the region, and a second time
        with on=0 to end it.
        """
        tag = 'big'
        if on:
            return self._open( tag, allowed_attrs=[], **kw )
        return self._close( tag )


    # Paragraphs, Lines, Rules ###########################################
    def _indent_spaces( self ):
        """Returns space(s) for indenting the html source so list nesting is easy to read.

        Note that this mostly works, but because of caching may not always be accurate."""
        if prettyprint:
            return self.indentspace * self._indent_level
        else:
            return ''

    def _newline( self ):
        """Returns the whitespace for starting a new html source line, properly indented."""
        if prettyprint:
            return '\n' + self._indent_spaces()
        else:
            return ''

    def linebreak( self, preformatted=1 ):
        """Creates a line break in the HTML output.
        
        If preformatted is true a <br> element is inserted, otherwise
        the linebreak will only be visible in the HTML source.
        """
        return '<br />\n' + self._indent_spaces()
        
    def paragraph( self, on, **kw ):
        """Creates a paragraph with a <p> element.
        
        Call once with on=1 to start the region, and a second time
        with on=0 to end it.
        """
#        if self._terse:
#            return ''
        self.in_p = on != 0
        tag = 'p'
        if on:
            tagstr = self._open( tag, **kw )
        else:
            tagstr = self._close( tag )
        return tagstr

    def rule( self, size=None, **kw ):
        """Creates a horizontal rule with an <hr> element.

        If size is a number in the range [1..6], the CSS class of the rule
        is set to 'hr1' through 'hr6'.  The intent is that the larger the
        size number the thicker or bolder the rule will be.
        """
        if size and 1 <= size <= 6:
            # Add hr class: hr1 - hr6
            return self._open( 'hr', newline=1, attr={'class': 'hr%d' % size}, **kw )
        return self._open( 'hr', newline=1, **kw )


    def image( self, src=None, **kw ):
        """Creates an inline image with an <img> element.

        The src argument must be the URL to the image file.
        """
        if src:
            kw['src'] = src
        return self._open( 'img', **kw )

    # Lists ##############################################################

    def number_list( self, on, type=None, start=None, **kw ):
        """Creates an HTML ordered list, <ol> element.

        The 'type' if specified can be any legal numbered
        list-style-type, such as 'decimal','lower-roman', etc.

        The 'start' argument if specified gives the numeric value of
        the first list item (default is 1).
        """
        tag = 'ol'
        if on:
            attr = {}
            if type is not None:
                attr['type'] = type
            if start is not None:
                attr['start'] = start
            tagstr = self._open( tag, newline=1, attr=attr, **kw )
        else:
            tagstr = self._close( tag, newline=1 )
        return tagstr
    
    def bullet_list( self, on, **kw ):
        """Creates an HTML ordered list, <ul> element.

        The 'type' if specified can be any legal unnumbered
        list-style-type, such as 'disc','square', etc.
        """
        tag = 'ul'
        if on:
            tagstr = self._open( tag, newline=1, **kw )
        else:
            tagstr = self._close( tag, newline=1 )
        return tagstr

    def listitem( self, on, **kw ):
        """Adds a list item, <li> element, to a previously opened
        bullet or number list.
        """
        tag = 'li'
        if on:
            tagstr = self._open( tag, newline=1, **kw )
        else:
            tagstr = self._close( tag, newline=1 )
        return tagstr

    def definition_list( self, on, **kw ):
        """Creates an HTML definition list, <dl> element.
        """
        tag = 'dl'
        if on:
            tagstr = self._open( tag, newline=1, **kw )
        else:
            tagstr = self._close( tag, newline=1 )
        return tagstr

    def definition_term( self, on, **kw ):
        """Adds a new term to a definition list, HTML element <dt>.
        """
        tag = 'dt'
        if on:
            tagstr = self._open( tag, newline=1, **kw )
        else:
            tagstr = self._close( tag, newline=0 )
        return tagstr

    def definition_desc( self, on, **kw ):
        """Gives the definition to a definition item, HTML element <dd>.
        """
        tag = 'dd'
        if on:
            tagstr = self._open( tag, newline=1, **kw )
        else:
            tagstr = self._close( tag, newline=0 )
        return tagstr

    def text( self, text, **kw ):
        txt = self._text( text )
        if kw:
            return self._open( 'span', **kw ) + txt + self._close( 'span' )
        return txt

    def escapedText( self, text, **kw ):
        txt = escape( text )
        if kw:
            return self._open( 'span', **kw ) + txt + self._close( 'span' )
        return txt

    def rawHTML( self, markup ):
        return markup


    def span( self, on, **kw ):
        tag = 'span'
        if on:
            return self._open( tag, **kw )
        return self._close( tag )
    
    def comment( self, text ):
        return ""
