# -*- coding: utf-8 -*-

from logger import *
from wiki_parser import * 

import re
import os
import codecs
import xml.sax
import operator

PARTS = {
    'English': None,
    'Etymology': None,
    'Etymology 1': None,
    'Etymology 2': None,
    'Etymology 3': None,
    'Etymology 4': None,
    'Etymology 5': None,
    'Etymology 6': None,
    'Etymology 7': None,
    'Pronunciation': None,
    'Pronunciation 1': None,
    'Pronunciation 2': None,
}

DEFINITION_PARTS = {
    'Adjective' : 'adj.',
    'Adverb' : 'adv.',
    'Article' : None, #the 
    'Cardinal number' : None, #the 
    'Circumfix' : None, #a- -ing
    'Contraction' : None, #let's
    'Determiner' : None, #some
    'Idiom' : None, #what for, read my lips
    '{{idiom}}' : 'Idiom', #go jump in the lake
    'Infix' : None, #bloody
    'Initialism' : None, #VRML
    '{{initialism|English}}' : 'Initialism', #GOQ
    '{{initialism|english}}' : 'Initialism', #CIC
    '{{initialism}}' : 'Initialism', #rpm
    'Interfix' : None, #-i-
    'Interjection' : None, #yum
    'Intransitive verb' : 'iv.', #spend a penny
    'Letter' : None, #K
    'Noun' : 'n.', #K
    'Number' : None, #seven
    'Ordinal number' : 'Number', #fiveth
    'Phrase' : None, #your welcome, who knows
    'Prefix' : None, #sex-
    'Preposition' : None, #with
    'Pronoun' : None, #you
    'Proper noun' : None, #eMac
    'Proverb' : None, #the pen is mightier than the sword
    'Suffix' : None, #-some
    'Symbol' : None, #C19
    'Transitive verb' : None, #bump into
    'Verb' : None, #get
    'eating' : None, #eating
}

EXTRA_DEFINITION_PARTS = {
    'Abbreviation' : None, #sword
    '{{abbreviation}}' : 'Abbreviation', #tel
    '{{abbreviation|English}}' : 'Abbreviation', #al
    'Abbreviation' : None, #sword
    'Acronym' : None, #j/k
    '{{acronym}}' : 'Acronym', #cola
    'Alternative spelling' : 'Alternative spellings', #Taipei (not current version)
    'Alternative spellings' : None, #yes man, yes-man
    'Antonyms' : None, #zoom in
    'Compounds' : None, #random
    'Conjunction' : None, #win 
    'Conjugation' : 'Conjunction', #win over 
    'Coordinate terms' : None, #west 
    'Derived terms' : None, #zombie
    'Hypernyms' : None, #son -> child
    'Hyponyms' : None, #uncle
    'Meronyms' : None, #door -> handle, latch
    'Particle' : None, #like
    'Related Terms' : 'Related terms', #desocialization
    'Related terms' : None, #
    'See Also' : 'See also', #wrong
    'See also' : None, #wrong
    'Synonyms' : None, #zone
    'Troponyms' : None, #sleep
    'Usage notes' : None, #wrong
}

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

ERROR_PARTS = {
    'Alternative / informal spellings' : None, #e.g. (not current version)
    'Gregg' : None, #Gregg (not current version)
}

class WikiSection:
    def __init__( self, heading, body ):
        self.heading = heading
        self.body = body
        #self.lines = re.split("\n+", body.strip())
        #if len(self.lines) == 1 and len(self.lines[0]) == 0:
        #    self.lines = []
        self.children = []
        self.parent = None
        self.pronunciation = None
        
    def __str__( self ):
        heading = self.heading
        if self.pronunciation:
            heading += ' (Pronunciation)'
        return "<%s:%i:%s>" % ( heading, len( self.children ), ','.join( [str( x ) for x in self.children] ) )

    def add( self, section ):
        section.parent = self
        self.children.append( section )

    def remove( self, section ):
        self.children.remove( section )
        if section:
            section.parent = None
        
    def bfs( self ):
        yield self
        queue = list( self.children )
        while len( queue ) > 0:
            first = queue[0]
            queue = queue[1:]
            queue.extend( first.children )
            yield first

    def _find( self, heading, iter ):
        for section in iter:
            if section and section.heading == heading:
                return section
        return None
        
    def find( self, heading ):
        return self._find( heading, self.bfs() )
    
    def find_in_children( self, heading ):
        return self._find( heading, self.children )
    
    def create_middle_section( self, heading ):
        section = WikiSection( heading, '' )
        section.children = self.children
        self.children = []
        self.add( section )
        return section
    
    def etymology_count(self):
        return reduce( operator.add, map( lambda x:[0,1][x.heading.startswith('Etymology')] , self.children ) )
        
class Word:
    def __init__( self, lemma, raw_text ):
        self.lemma = lemma
        self.raw_text = raw_text
        self.doc = None

    def parse( self ):
        text = self.raw_text
        
        headings = list( re.finditer( "^(=+)\s*(.*?)\s*=+\n", text, re.MULTILINE ) )
        doc = WikiSection( "WORD[" + self.lemma + "]", "" )
        stack = [doc]
        for i, m in enumerate( headings ):
            depth = len( m.group( 1 ) )
            if depth < len( stack ):
                stack = stack[:depth]
            else:
                while depth > len( stack ):
                    s = WikiSection( None, "" )
                    stack[-1].add( s )
                    stack.append( s )
            heading = m.group( 2 ) 
            if heading in IGNORE_PARTS or heading in ERROR_PARTS:
                continue
            if i + 1 < len( headings ):
                s = WikiSection( heading, text[m.end( 0 ):headings[i + 1].start( 0 )].strip() )
            else:
                s = WikiSection( heading, text[m.end( 0 ):].strip() )
            assert len( stack ) == depth
            stack[-1].add( s )
            stack.append( s )
        self.doc = doc.find( 'English' )
        self.doc.heading = 'English[' + self.lemma + ']' 

    def restructure_wiki_document( self ):
        """
        restructur sections and collects pronounciation parts to word 
        """
        english_section = self.doc
        etymology = english_section.find( 'Etymology' )

        english_pronunciation = english_section.find_in_children( 'Pronunciation' ) 
        if english_pronunciation:
            english_section.remove( english_pronunciation )
            
        if not etymology and not english_section.find( 'Etymology 1' ):
            english_section.create_middle_section( 'Etymology' )
        elif etymology and 0 == len( etymology.children ):
            pronunciation_sections = []
            
            bfs = english_section.bfs()
            for section in bfs:
                if section.heading == 'Pronunciation':
                    section.parent.pronunciation = section
                    section.parent.remove( section )
            
            i = iter( english_section.children )
            for rest in i:
                if etymology == rest:
                    continue
                etymology.add( rest )
            for c in etymology.children:
                english_section.remove( c )
        elif english_section.find( 'Etymology 1' ) and english_section.find( 'Etymology 2' ):
            return
            
        else:
            STRUCTURE_LOGGER.error( self.lemma )
    
        if english_pronunciation:
            for section in english_section.children:
                if section.heading.startswith( 'Etymology' ) and None == section.pronunciation:
                    section.pronunciation = english_pronunciation
                    

    def format( self ):
        def etymology_index_gen():                
            etymology_index = 1
            while True:
                yield etymology_index
                etymology_index += 1
                
        idx_gen = etymology_index_gen()
        
        def f( depth, section, idx_gen ):
            if depth == 0:
                s = ""
            elif section.heading.startswith( 'Etymology' ):
                if self.doc.etymology_count() == 1:
                    s = "<h1>%s</h1>\n" % (self.lemma)
                else:
                    s = "<h1>%s <sup>%d</sup></h1>\n" % (self.lemma, idx_gen.next() )
            else:
                s = "<h%d>%s</h%d>\n" % ( depth, section.heading, depth )
                p = Parser( self.lemma, section.body )
                formatter = Formatter()
                s += p.format( formatter ) + '\n'
            for child in section.children:
                s += f( depth + 1, child, idx_gen )
            return s
            
        s = f( 0, self.doc, idx_gen )
        
#        s += "Ref: http://en.wiktionary.org/wiki/%s\n" % word
        return s
