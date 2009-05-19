# -*- coding: utf-8 -*- 

from makedb import *
import logging
import os
import unittest


def test_init_parse_db_dir_name():
    maker = WiktionaryDbMaker( 'enwiktionary-test-pages-articles.xml.bz2' )
    assert 'wiktionary_test.db' == maker.db_dir


class WiktionaryDbMakerTestBase( unittest.TestCase ):    
    def setUp( self ):
        self.maker = WiktionaryDbMaker( 'enwiktionary-test-pages-articles.xml.bz2' )
        self.maker.open()

    def tearDown( self ):
        self.maker.close()
        os.system( 'rm -rf  wiktionary_test.db' )
    
class WiktionaryDbMakerTest( WiktionaryDbMakerTestBase ):
    def test_check_database_file_opened( self ):
        self.assertEquals( 4, len( os.listdir( self.maker.db_dir ) ) )
        
    def test_reopen_should_do_nothing( self ):
        try:
            self.maker.open()
            self.assertEquals( 4, len( os.listdir( self.maker.db_dir ) ) )
        except OSError:
            self.fail( 'should ignore "File exists" error' )
        
    def check_db_entires( self ):
        self.assertEquals( 'name', self.maker.db_redir.get( 'Name' ) )
        self.assertEquals( 1, len( self.maker.db_redir.keys() ) )
        self.assertEquals( 5, len( self.maker.db_tmp.keys() ) )
        
    def test_process_should_fill_redir_and_tmp_db( self ):
        self.maker.process_redir_and_filter_english()
        self.check_db_entires()
        
    def test_redir_and_tmp_db_should_be_cached( self ):
        self.maker.close()
        self.maker.open()
        self.maker.process_redir_and_filter_english()
        self.check_db_entires()

