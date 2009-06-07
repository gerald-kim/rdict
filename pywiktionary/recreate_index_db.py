#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import tc
reload( sys )
sys.setdefaultencoding( "utf-8" )


class IndexCreator:
    
    def __init__(self, db_dir):
        self.db_dir = db_dir
        
    def _open_db( self, path ):
        db = tc.BDB()
        db.open( path, tc.BDBOWRITER | tc.BDBOCREAT )
        return db
        
    def _get_db_filepath( self, dir, name ):
        return os.path.join( os.path.dirname( __file__ ), dir, name + '.db' )
    
    def create_index( self ):
        try:
            os.remove( self._get_db_filepath( self.db_dir, 'index' ) )
        except OSError:
            pass
        
        db_word = tc.BDB()
        db_word.open( self._get_db_filepath( self.db_dir, 'word' ), tc.BDBOREADER )
        
        db_index = self._open_db( self._get_db_filepath( self.db_dir, 'index' ) )

        c = db_word.curnew()
        c.first()
        while 1:
            try:
                c.key()
            except KeyError:
                break
            db_index.putdup( c.key().lower(), c.key() )
            c.next()
        db_index.close()
        db_word.close()

if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s dbdir" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

    indexCreator = IndexCreator( sys.argv[1] )
    indexCreator.create_index()