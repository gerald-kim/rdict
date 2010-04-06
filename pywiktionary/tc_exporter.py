#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 7, 2009 by evacuee

from models import *

import tc
import os

reload( sys )
sys.setdefaultencoding( "utf-8" )

class TokyoCabinetExporter:
    def __init__( self, db_name ):
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
        os.system( 'tcbmgr optimize -td ' + os.path.join( self.db_dir, 'index.db' ) )
        os.system( 'tcbmgr optimize -td ' + os.path.join( self.db_dir, 'word.db' ) )

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
        print( "Usage: %s wiktionarydb [wordset]" % ( sys.argv[0] ) ) 
        sys.exit( 1 )
    WordCount = 0
    
    word_set = None
    if len( sys.argv ) == 3:
        word_set = {}
        for line in open( sys.argv[2] ).readlines():
            word = unicode( line.strip() )
            word_set[ word ] = 1
            if word.find( '-' ) > 0:
                word_set[ word.replace( '-', ' ' ) ] = 1
    
    word_manager = WordManager()
    word_manager.connect()
    exporter = TokyoCabinetExporter( sys.argv[1] )
    exporter.open_tc()
    
    for tuple in word_manager.get_tuples_with_lemma_for_exporting():
        lemma = tuple[0];
        if word_set and not word_set.has_key( lemma ):
            continue
                
        word = word_manager.get( lemma )
        exporter.export_word( word )
        WordCount += 1
        if WordCount % 100 == 0:
            print "exported: ", WordCount        
        
    exporter.close_tc()
    word_manager.close()
