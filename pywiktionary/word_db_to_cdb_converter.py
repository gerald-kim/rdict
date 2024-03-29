#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Aug 5, 2009 by evacuee

import sys
import os
import tc
import cdb
import gzip

reload( sys )
sys.setdefaultencoding( "utf-8" )

class WordDbToCdbConverter:
    def __init__( self, db_dir ):
        self.db_dir = db_dir
        self.cdb_dir = db_dir.replace( '.db', '.cdb' )

    def _get_db_filepath( self, name ):
        return os.path.join( os.path.dirname( __file__ ), self.db_dir, name + '.db' )
    
    def _get_cdb_filepath( self, name ):
        return os.path.join( os.path.dirname( __file__ ), self.cdb_dir, name + '.cdb' )
    
    def open( self ):
        self.word_db = tc.BDB()
        self.word_db.open( self._get_db_filepath( 'word' ), tc.BDBOREADER )

        try:
            os.system( 'rm -rf ' + self.cdb_dir )
            os.system( 'mkdir ' + self.cdb_dir )
        except OSError:
            pass
        
        word_cdb_name = self._get_cdb_filepath( 'word' )
        self.word_cdb = cdb.cdbmake( word_cdb_name, word_cdb_name + ".tmp" )
        self.index_file = open( word_cdb_name.replace( 'word.cdb', 'word.index'), 'w' )
        
    def close( self ):
        self.word_db.close()
        self.word_cdb.finish()
        self.index_file.close()

    def convert( self, db, cdb ):
        c = db.curnew()
        c.first()

        while True:
            try:
                #print c.key(), c.val()
                cdb.add( c.key(), c.val() )
                self.index_file.write( c.key() )
                self.index_file.write( '\n' )
                c.next()
            except KeyError:
                break
        
    
if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s dbdir" % ( sys.argv[0] ) ) 
        sys.exit( 1 )
    
    cdb_converter = WordDbToCdbConverter( sys.argv[1] )
    cdb_converter.open()
    cdb_converter.convert( cdb_converter.word_db, cdb_converter.word_cdb )
    cdb_converter.close()
