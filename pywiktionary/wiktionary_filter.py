#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from BeautifulSoup import BeautifulSoup, Tag
from cStringIO import StringIO
import urllib2
import hashlib
import re

IGNORE_PARTS = {
    'Abbreviations' : None, #fifteenth
    'Affix' : None, #-nil-
    'Alternative forms' : None, #toffee
    'Anagrams' : None, #who
    'Declension' : None, #who
    'Descendants' : None, #mug, IQ
    'Dictionary notes' : None, #resume
    'Examples' : None, #exception that proves the rule
    'External links' : None, #vivid
    'Homographs' : None, #h
    'Homonyms' : None, #bosh
    'Homophones' : None, #root -> route(same pronouncation)
    'Hyphenation' : None, #favour
    'Inflection' : None, #tridens
    'Noun form' : 'n.', #pâtés de foie gras -> only 1
    'Older form' : 'n.', #Orchomenos -> 1
    'Participle' : None, #haring -> 1
    'Quotations' : None, #woman
    'References' : None, #wife
    'Romaji' : None, #gaido
    'Scientific names' : None, #tiger
    'Shorthand' : None, #able
    'Sources' : None, #able
    'Trivia' : None, #starting
    'Translations' : None, #name
}

class MockWordManager:
    def __init__(self):
        self.existing_words = {}

    def set_existing_words( self, lemmas ):
        self.existing_words = {}
        for lemma in lemmas:
            self.existing_words[lemma] = 1

    def find_existing_words( self, lemmas ):
        words = {}
        for lemma in lemmas:
            if self.existing_words.has_key( lemma ):
                words[lemma] = 1

        return words

class WiktionaryFilter:
    def __init__( self, word_manager=MockWordManager() ):
        self.word_manager = word_manager

    def findContentSoup( self, wiktionaryPage ):
        page = BeautifulSoup( wiktionaryPage )
        bodyContent = page.find( 'div', id='bodyContent' )
        self.pullUpHeadSpanContent( bodyContent )

        buffer = StringIO()
#        buffer.write( '<!DOCTYPE html><html lang="en"><head><meta charset=utf-8></head><body>\n')
        h2s = bodyContent.findAll( 'h2', {'class':'head'} )

        englishHead = h2s[0]
        otherLangHead = None
        if len( h2s ) > 1:
            otherLangHead = h2s[1]

        g = englishHead.nextSiblingGenerator()
        n = g.next()
        while otherLangHead != n:
            buffer.write( str( n ) )
            buffer.write( '\n' )
            n = g.next()
#        buffer.write( '</body></html>' )
        buffer.flush()

        return BeautifulSoup( buffer.getvalue(), fromEncoding='utf-8' )

    def pullUpHeadSpanContent( self, content ):
        headSpans = content.findAll( 'span', {'class':'mw-headline'} )
        for headSpan in headSpans:
            parent = headSpan.parent
            parent['class'] = 'head'
            parent.contents = headSpan.contents

    def executeFilters( self, content ):
        for d in dir( self ):
            if d.startswith( 'soup_filter_' ):
 #               print "exec : ", d
                getattr( self, d )( content )

        contentStr = str( content )
        for d in dir( self ):
            if d.startswith( 'regex_filter_' ):
                contentStr = getattr( self, d )( contentStr )
        return contentStr

    def soup_filter_removeTitleInA( self, content ):
        for link in content.findAll( 'a' ):
            del( link['title'] )

    def soup_filter_removeUnnecessaryElements( self, content ):
        ELEMENTS = ['hr']
        ELEMENTS_WITH_ATTRIBUTE = {
            'span' : [
                {'class':'editsection'},
                {'class':'interProject'},
                ],
            'div' : [
                {'id':'rank'},
                {'class':re.compile( 'noprint' )},
                {'class':'infl-table'},
                {'class':'floatright'},
                {'class':'catlinks'},
                {'class':re.compile( '.+tright' )},
                {'class':'printfooter'}
                ],
            'a' : [
                {'class':'image'}
                ],
            'table' : [
                {'class':'gallery'},
                {'class':'gallery2'}
                ]
            }

        for tag in ELEMENTS:
            soups = content.findAll( tag )
            [s.extract() for s in soups]

        for tag in ELEMENTS_WITH_ATTRIBUTE.keys():
            attrs = ELEMENTS_WITH_ATTRIBUTE[tag]
            for attr in attrs:
                soups = content.findAll( tag, attr )
                [ s.extract() for s in soups ]

    def soup_filter_removeUnnecessaryHeadings( self, content ):
        #UNNECESSARY_HEADINGS = [u'Translations']

        headings = content.findAll( attrs={'class':'head' } )
        for heading in headings:
            #print "HEADING:", heading, heading.contents
            extracts = []
            if IGNORE_PARTS.has_key( heading.renderContents().strip() ):
                #print "unnecessary:", heading
                extracts.append( heading )
                g = heading.nextSiblingGenerator()
                n = g.next()
                while n != None and n not in headings:
                    #print n
                    extracts.append( n )
                    n = g.next()

                [ s.extract() for s in extracts ]

    def soup_filter_removeEmptyP( self, content ):
        ps = content.findAll( 'p' )

        for p in ps:
            childrens = p.findChildren()
            if len( childrens ) == 1 and u'a' == childrens[0].name:
                p.extract()

    def soup_filter_extractPronounciation( self, content ):
        # extiwAs = content.findAll( 'a', {'class':'extiw'} )

        # for a in extiwAs:
        #     try:
        #         if len( a.contents ) == 1 and a.contents[0] in [u'IPA', u'SAMPA']:
        #             li = a.parent
        #             ul = li.parent
        #             ul.contents = [li]
        #             break
        #     except:
        #         pass

        audioLinkTables = content.findAll( 'table', {'class':'audiotable'} )
        for span in audioLinkTables:
            span.parent.extract()

        audioLinkSpans = content.findAll( 'span', {'class':'unicode audiolink'} )
        for span in audioLinkSpans:
            span.parent.extract()

    def soup_filter_remove_mention_spans( self, content ):
        spans = content.findAll( 'span', {'class':re.compile( 'mention-.+' )} )
        [ s.replaceWith( s.contents[0] ) for s in spans ]

    def soup_filter_remove_new_page_links( self, content ):
        links = content.findAll( 'a', {'class':'new'} )
        [a.replaceWith( a.renderContents() ) for a in links]

    def soup_filter_remove_appendix_links( self, content ):
        links = content.findAll( 'a', {'href':re.compile( '^/wiki/Appendix:.+' )} )
        [a.replaceWith( a.renderContents() ) for a in links]

    def soup_filter_word_links( self, content ):
        links = content.findAll( 'a', {'href':re.compile( '^/wiki/+' )} )
        words = [unicode( a.renderContents(), 'utf-8') for a in links]
        existing_words = self.word_manager.find_existing_words( words )
        for a in links:
            if not existing_words.has_key( a.renderContents() ):
                a.replaceWith( a.renderContents() )
            else:
                a['href'] = urllib2.quote( a.renderContents() )
                a['onclick'] = u"return s(this);"

    def soup_filter_fold_etymology( self, content ):
        heads = content.findAll( 'h2', {'class':'head'} ) + content.findAll( 'h3', {'class':'head'} ) + content.findAll( 'h4', {'class':'head'} )
        etymologys = []
        for h in heads:
            if h.contents[0].lower().startswith('etymology'):
#                print "found", h.content[0]
                etymologys.append( h )
#                print 'Etymology found: ', h

        etymology_index = 1
        for e in etymologys:
            div = Tag( content, 'div' )
            div['id'] = u'etymology_'+str(etymology_index)
            div['style'] = u'display:none'
            linkSoup = BeautifulSoup( u''' <a href="#" onclick="f('%s',this)">[show]</a>''' % div['id'] )
            e.append( linkSoup )

            paragraphs = []
            
            n = e.nextSibling
            first = 1
            while n and (n.__class__.__name__ == 'NavigableString' or  (n.__dict__.has_key('name') and n.name == 'p') ):
                paragraphs.append( n )
                n = n.nextSibling
                
            [div.append(p) for p in paragraphs]
            
            eIndex = e.parent.contents.index( e )
            e.parent.insert( eIndex + 1, div )
 
            etymology_index = etymology_index + 1
                    
                
    def soup_filter_add_remember_buttons(self, content ):
        lis = content.findAll(lambda tag: tag.name == u'li' and tag.parent.name == u'ol' )
        for li in lis:
            hexdigest = hashlib.sha1( str( li ) ).hexdigest()
            li.insert( 0, u'<a href="#%s" onclick="r(this)" class="r"></a>' % (hexdigest[:8]) )

    def regex_filter_shorten_qualifier1( self, content ):
        r = re.compile( '<span class="ib-brac"><span class="qualifier-brac">\(</span></span><span class="ib-content"><span class="qualifier-content">(.+)</span></span><span class="ib-brac"><span class="qualifier-brac">\)</span></span>', re.UNICODE | re.MULTILINE )
        return r.sub( r'(<i>\1</i>)', content )

    def regex_filter_shorten_qualifier2( self, content ):
        r = re.compile( '<span class="ib-brac">\(</span><span class="ib-content">(.+)</span><span class="ib-brac">\)</span>', re.UNICODE | re.MULTILINE )
        return r.sub( r'(<i>\1</i>)', content )


import sys

if __name__ == '__main__':
    #import psyco
    #psyco.full()

    page = sys.stdin.read()
    filter = WiktionaryFilter()
    content = filter.findContent( page )
    content = filter.executeFilters( content )

    print str( content )
