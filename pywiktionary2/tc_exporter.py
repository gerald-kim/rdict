#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from models import *

import tc
import os

class TokyoCabinetExporter:
    def __init__( self, session, db_name ):
        self.session = session
        self.db_dir = db_name

        os.mkdir( self.db_dir )

    def open_tc( self ):
        self.index_db = tc.BDB()
        self.index_db.open( os.path.join( self.db_dir, 'index.db' ), tc.BDBOWRITER | tc.BDBOCREAT )
        self.word_db = tc.BDB()
        self.word_db.open( os.path.join( self.db_dir, 'word.db' ), tc.BDBOWRITER | tc.BDBOCREAT )
#        self.word_db = tc.HDB()
#        self.word_db.open( os.path.join( self.db_dir, 'word.db' ), tc.HDBOWRITER | tc.HDBOCREAT )

    def close_tc( self ):
        self.index_db.close()
        self.word_db.close()
        os.system( 'tcbmgr optimize -tb ' + os.path.join( self.db_dir, 'index.db' ) )
        os.system( 'tcbmgr optimize -tb ' + os.path.join( self.db_dir, 'word.db' ) )

    def get_words( self ):
        q = self.session.query( Word ).filter_by( downloaded = True, filtered = True )
        return q.all()

    def export_word( self, word ):
        index_key = word.lemma.lower().encode( 'utf-8' )
        
        lemma = word.lemma.encode( 'utf-8' )
        definition = word.definition.encode( 'utf-8' )
        
        if self.index_db.has_key( index_key ):
            values = self.index_db.getlist( index_key )
            self.index_db.outlist( index_key )
            self.index_db.put( index_key, lemma )
            self.index_db.putlist( index_key, values )
        else:
            self.index_db.put( index_key, lemma )

        self.word_db.put( lemma, definition )


if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s wiktionarydb" % ( sys.argv[0] ) ) 
        sys.exit( 1 )
    WordCount = 0
    
    session = create_session()
    exporter = TokyoCabinetExporter( session, sys.argv[1] )
    exporter.open_tc()
    
    for word in exporter.get_words():
        exporter.export_word( word )
        WordCount += 1
        if WordCount % 100 == 0:
            print "exported: ", WordCount        
        
    exporter.close_tc()

