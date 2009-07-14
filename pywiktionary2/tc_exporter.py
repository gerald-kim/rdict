#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from models import *

import tc
import os

class TokyoCabinetExporter:
    def __init__( self, session, dir_name ):
        self.session = session
        self.db_dir = dir_name + '.db'

        os.mkdir( self.db_dir )

    def open_tc( self ):
        self.index_db = tc.BDB()
        self.index_db.open( os.path.join( self.db_dir, 'index.db' ), tc.BDBOWRITER | tc.BDBOCREAT )
        self.word_db = tc.HDB()
        self.word_db.open( os.path.join( self.db_dir, 'word.db' ), tc.HDBOWRITER | tc.HDBOCREAT )

    def close_tc( self ):
        self.index_db.close()
        self.word_db.close()

    def get_words( self ):
        q = self.session.query( Word ).filter_by( downloaded=True, filtered=True )
        return q.all()

    def export_word( self, word ):
        index_key = word.lemma.lower()

        if self.index_db.has_key( index_key  ):
            values = self.index_db.getlist( index_key )
            self.index_db.outlist( index_key )
            self.index_db.put( index_key, word.lemma )
            self.index_db.putlist( index_key, values )
        else:
            self.index_db.put( index_key, word.lemma )

        self.word_db.put( word.lemma, word.definition )


if __name__ == '__main__':
    pass


