# -*- coding: utf-8 -*- 

from word import *
import logging
import os
import unittest

class WikiSectionTest( unittest.TestCase ):
    def test_middle_man_creation( self ):
        top = WikiSection( 'top', None )
        child1 = WikiSection( 'child1', None )
        top.add( child1 )
        middle = top.create_middle_section( 'middle' )

        self.assertEquals( 1, len( top.children ) )
        self.assertEquals( 1, len( middle.children ) )
        self.assertEquals( 0, len( child1.children ) )
        self.assertEquals( middle, top.children[0] )
        self.assertEquals( child1, middle.children[0] )
        
        
class WikiSectionFindChildTest( unittest.TestCase ):
    def setUp( self ):
        self.top_section = WikiSection( 'top', '' )
        child1 = WikiSection( 'child1', '' )
        self.top_section.add( child1 )
        self.top_section.add( WikiSection( 'child2', '' ) )
        child3 = WikiSection( 'child3', '' )
        self.top_section.add( child3 )
        
        child1.add( WikiSection( 'child1_1', '' ) )
        
        self.child3_1 = WikiSection( 'child3_1', '' )
        child3.add( self.child3_1 )
        
        child3.add( WikiSection( 'child3_2', '' ) )
        child3.add( WikiSection( 'child3_3', '' ) )
        
    def test_bfs( self ):
        bfs = self.top_section.bfs()
        assert self.top_section == bfs.next()
        assert 'child1' == bfs.next().heading
        assert 'child2' == bfs.next().heading
        assert 'child3' == bfs.next().heading
        assert 'child1_1' == bfs.next().heading
        assert 'child3_1' == bfs.next().heading
        assert 'child3_2' == bfs.next().heading
        assert 'child3_3' == bfs.next().heading
    
    def test_find_top( self ):
        assert self.top_section == self.top_section.find( 'top' )

    def test_find_child3_1( self ):
        assert self.child3_1 == self.top_section.find( 'child3_1' )

    def test_find_in_childrens( self ):
        assert 'child1' == self.top_section.find_in_children( 'child1' ).heading 
        assert None == self.top_section.find_in_children( 'child3_1' ) 
        
def create_word( name ):
    f = open( os.path.join( os.path.dirname( __file__ ), 'pages', name + '.page' ) )
    return Word( name, f.read() )


class WordTest( unittest.TestCase ):    
    def test_parse_word1_help( self ):
        word = create_word( 'help' )
        word.parse()
        
        self.assertEquals( "<English[help]:3:<Pronunciation:0:>,<Etymology 1:1:<Noun:2:<Synonyms:0:>,<Derived terms:0:>>>,<Etymology 2:2:<Verb:3:<Usage notes:0:>,<Synonyms:0:>,<Derived terms:0:>>,<Interjection:1:<Synonyms:0:>>>>"
                     , str( word.doc ) )

        print word.doc
        #english_section = self.maker.reorganize_wiki_document( doc )
        #print english_section
        print word.format()
        
        
class WikiSectionRestructiongTest( unittest.TestCase ):
    #TODO: make assertStructure function to show formatted difference'
    def test_empty_etymology_should_create_new_etymology( self ):
        word = create_word( 'Balto-Slavs' )
        word.parse()
        self.assertEquals( '<English[Balto-Slavs]:1:<Noun:0:>>', str( word.doc ) )
        word.restructure_wiki_document()
        self.assertEquals( '<English[Balto-Slavs]:1:<Etymology:1:<Noun:0:>>>', str( word.doc ) )

#        print self.maker.format( word, rdic_doc )

    def test_one_etymology_should_be_parent_of_other( self ):
        word = create_word( 'name' )
        word.parse()
        self.assertEquals( '<English[name]:5:<Etymology:0:>,<Pronunciation:0:>,<Noun:1:<Derived terms:0:>>,<Verb:1:<Derived terms:0:>>,<See also:0:>>',
                        str( word.doc ) )
        word.restructure_wiki_document()
        self.assertEquals( '<English[name]:1:<Etymology (Pronunciation):3:<Noun:1:<Derived terms:0:>>,<Verb:1:<Derived terms:0:>>,<See also:0:>>>',
                        str( word.doc ) )

    def test_two_pronunciation_should_be_merged( self ):
        word = create_word( 'defect' )
        word.parse()
        self.assertEquals( '<English[defect]:3:<Etymology:0:>,<Noun:2:<Pronunciation:0:>,<Related terms:0:>>,<Verb:2:<Pronunciation:0:>,<Related terms:0:>>>',
                        str( word.doc ) )
        word.restructure_wiki_document()
        self.assertEquals( '<English[defect]:1:<Etymology:2:<Noun (Pronunciation):1:<Related terms:0:>>,<Verb (Pronunciation):1:<Related terms:0:>>>>',
                        str( word.doc ) )
        
