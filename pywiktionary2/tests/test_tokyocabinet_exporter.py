# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

import unittest
import os

from models import *
from tc_exporter import *

#from dict.models import Word

class MockWord( Word ):
    def __init__( self, lemma ):
        Word.__init__( self, lemma, 0 )

    def get_definition( self ):
        return self.lemma * 10

    definition = property( get_definition )

class TokyoCabinetExporterTest( unittest.TestCase ):
    def setUp( self ):
        self.session = create_session()
        self.session.begin()

        self.exporter = TokyoCabinetExporter( self.session, 'enwiktionary-test' )
        self.exporter.open_tc()


    def tearDown( self ):
        self.session.rollback()
        self.exporter.close_tc()
        os.system( 'rm -rf enwiktionary-test' )


    def test_get_words( self ):
        for lemma in [ u'c', u'c++', u'java', u'python', u'Python' ]:
            word = Word( lemma, 0 )
            word.downloaded = True
            word.filtered = True
            self.session.add( word )
        self.session.add( Word( u'word', 0 ) )


        words = self.exporter.get_words()

        self.assertEquals( 5, len( words ) )
        self.assertEquals( u'c', words[0].lemma )

    def test_export_word( self ):
        self.exporter.export_word( MockWord( 'b' ) )
        self.exporter.export_word( MockWord( 'C' ) )
        self.exporter.export_word( MockWord( 'c' ) )

        self.assertEquals( 3, len( self.exporter.index_db.values() ) )
        self.assertEquals( [u'c', u'C'], self.exporter.index_db.getlist('c') )

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
