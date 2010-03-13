# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 12, 2009 by evacuee

import unittest

from models import *

setup_test_env()

class word_managerConnectionTest( unittest.TestCase ):
    def test_get_connection( self ):
        word_manager = WordManager()
        word_manager.connect()
        word_manager.begin()

        self.assertTrue( None != word_manager.conn )

        word_manager.c.execute( "insert into words values ( 'ZZZZ', 0, 0, 1)" )

        word_manager.rollback()
        word_manager.close()

class word_managerTest( unittest.TestCase ):
    def setUp( self ):
        self.word_manager = WordManager()
        self.word_manager.connect()
        self.word_manager.begin()

    def tearDown( self ):
        self.word_manager.rollback()

    def test_update_flag( self ):
        word = Word( u'lemma', 0 )
        self.word_manager.save( word )

        actual = self.word_manager.get( u'lemma' )
        self.assertTrue( actual.updated )

        self.word_manager.unmark_updated()
        
        actual = self.word_manager.get( u'lemma' )
        self.assertFalse( actual.updated )
        
    def test_word_CRU( self ):
        expected = Word( u'lemma', 0 )
        self.word_manager.save( expected )

        actual = self.word_manager.get( u'lemma' )
        self.assertEquals( expected.lemma, actual.lemma )
        self.assertEquals( expected.revision, actual.revision )
        self.assertEquals( UPDATED, actual.status )
        self.assertTrue( actual.updated )

        self.word_manager.mark_downloaded( u'lemma' )
        actual = self.word_manager.get( u'lemma' )
        self.assertEquals( DOWNLOADED, actual.status )

        self.word_manager.mark_filtered( u'lemma' )
        actual = self.word_manager.get( u'lemma' )
        self.assertEquals( FILTERED, actual.status )

        expected = Word( u'lemma', 333 )
        self.word_manager.save( expected )

        actual = self.word_manager.get( u'lemma' )
        self.assertEquals( expected.lemma, actual.lemma )
        self.assertEquals( expected.revision, actual.revision )
        self.assertEquals( UPDATED, actual.status )

    def test_find_existing_words( self ):
        self.word_manager.save( Word( u'lemma1' ) )
        self.word_manager.save( Word( u'lemma2' ) )
        self.word_manager.save( Word( u'lemma3' ) )
        self.word_manager.save( Word( u'lemma4' ) )

        existing_words = self.word_manager.find_existing_words( [u'lemma1', u'lemma3', u'lemma5'] )
        self.assertEquals( 2, len( existing_words ) )

class WordTest( unittest.TestCase ):
    def test_get_unicode_filename( self ):
        word = Word( u'café' )
        self.assertEquals( 'caf%C3%A9', word.get_file_name() )

    def test_get_slashed_filename( self ):
        word = Word( u'24/7' )
        self.assertEquals( '24_SLASH_7', word.get_file_name() )

    def test_get_file_prefix( self ):
        word = Word( u'word' )

        file_dir = word.get_file_dir();
        #print file_dir
        self.assertTrue( file_dir.endswith( 'tests.db.files/3c/bc' ) )

    def longtest_page_retrival( self ):
        word = Word( u'piñata' )

        self.assertTrue( word.download_page() )

        page = word.page
        #print page.encode( 'utf-8' )
        self.assertTrue( page.startswith( u'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN' ) )
        self.assertTrue( page.endswith( u'</body></html>\n' ) )

    def longtest_page_filter( self ):
        word_manager = WordManager()
        word_manager.connect()

        word = Word( u'get', 123 )

        self.assertTrue( word.download_page() )
        self.assertTrue( word.filter_page( word_manager ) )

        definition = word.definition
        #print definition.encode( 'utf-8' )
        self.assertTrue( len( definition ) > 10 * 1024 )
        word_manager.close()

    def longtest_deleted( self ):
        word_manager = WordManager()
        word_manager.connect()

        word = Word( u'tion', 123 )

        self.assertTrue( word.download_page() )
        self.assertTrue( word.deleted() )
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
