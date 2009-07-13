# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 12, 2009 by evacuee

import unittest

from models import *

setup_test_env()

class SessionTest( unittest.TestCase ):
    def test_session_creation( self ):
        session = create_session()
        self.assertTrue( None != session )

class WordModelTest( unittest.TestCase ):
    def setUp( self ):
        self.session = create_session()
        self.session.begin()
        
    def tearDown( self ):
        self.session.flush()
        self.session.rollback()
        
    def test_word_CRUD( self ):
        word = Word( u'word', 123 )
        self.session.add( word )
        
    def test_get_file_prefix(self):
        word = Word( u'word', 123 )
        
        file_dir = word.get_file_dir();
        print file_dir
        self.assertTrue( file_dir.endswith( 'tests.db.files/3c/bc' ) )
        
    def longtest_page_retrival(self):
        word = Word( u'get', 123 )
        
        self.assertFalse( word.downloaded )
        word.download_page()
        self.assertTrue( word.downloaded )
        
        page = word.page
        print page.encode( 'utf-8' )
        self.assertTrue( page.startswith( u'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN' ) )
        self.assertTrue( page.endswith( u'</body></html>\n' ) )
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
