# -*- coding: utf-8 -*- 

import unittest
import tc
import os   
from pywiktionary import * 

TEST_DB = 'pywiktionary-test.db'
class WordIndexTest( unittest.TestCase ):
    def test_eq_and_hash( self ):
        i1 = WordIndex( 'a', 'a' )
        i2 = WordIndex( 'a', 'a' )
        i3 = WordIndex( 'a', 'b' )
        assert i1 == i2
        assert i1.__hash__() == i2.__hash__()
        assert i1 != i3
        assert i1.__hash__() != i3.__hash__()

DUMMY_STR = 'ABCDefghijklmnopqrstuvWXYZ'
class DictionaryDatabaseTest( unittest.TestCase ):

    def setUp( self ):
        os.makedirs( TEST_DB )
        
        word_db = tc.BDB()
        word_db.open( os.path.join( TEST_DB, 'word.db' ), tc.BDBOCREAT | tc.BDBOWRITER )
        index_db = tc.BDB()
        index_db.open( os.path.join( TEST_DB, 'index.db' ), tc.BDBOCREAT | tc.BDBOWRITER )
        for i in range( len( DUMMY_STR ) ):
            word_db.put( DUMMY_STR[i], str( i ) + DUMMY_STR )
            index_db.put( DUMMY_STR[i].lower(), DUMMY_STR[i] )
        word_db.close()
        index_db.close()
        self.dict = Dictionary( TEST_DB )
        self.dict.open() 
        self.cursor = self.dict.create_cursor()
    
    def tearDown( self ):
        try:
            self.dict.close_cursor( self.cursor )
            self.dict.close()
        except:
            pass
        os.system( 'rm -rf ' + TEST_DB )

    def test_init( self ):
        assert self.dict.index_db
        assert self.dict.word_db
    
    def test_initial_forward_listing_start_from_first( self ):
        list = self.dict.list_forward( self.cursor, None )
        self.assertEquals( 10, len( list ) )
        
        first_index = list[0]
        self.assertEquals( 'a', first_index.word_lower )
        self.assertEquals( 'A', first_index.word )
    
    def test_forward_listing_should_keep_location( self ):
        list = self.dict.list_forward( self.cursor, 'a', 1 )
        self.assertEquals( 1, len( list ) )
        
        list = self.dict.list_forward( self.cursor, None, 1 )
        self.assertEquals( 1, len( list ) )
        self.assertEquals( 'b', list[0].word_lower )

    def test_forward_listing_should_stop_at_lasst( self ):
        list = self.dict.list_forward( self.cursor, 'a', 30 )
        self.assertEquals( 26, len( list ) )

        list = self.dict.list_forward( self.cursor, None, 1 )
        self.assertEquals( 0, len( list ) )
        
    def test_list_backword( self ):
        pass
    
    def test_get_existing( self ):
        definition = self.dict.get_definition( 'A' )
        self.assertEquals( '0' + DUMMY_STR, definition )

    def test_get_nonexisting( self ):
        definition = self.dict.get_definition( 'a' )
        self.assertEquals( None, definition )
