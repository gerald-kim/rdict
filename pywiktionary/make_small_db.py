#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import tc
reload( sys )
sys.setdefaultencoding( "utf-8" )


class SmallDbMaker:
    def __init__(self, db_dir):
        self.db_dir = db_dir
        self.small_db_dir = 'enwiktionary-small.db'
        
    def _open_db( self, path ):
        db = tc.BDB()
        db.open( path, tc.BDBOWRITER | tc.BDBOCREAT )
        return db
        
    def _get_db_filepath( self, dir, name ):
        return os.path.join( os.path.dirname( __file__ ), dir, name + '.db' )

    def get_popular_word_map( self ):
        file = open( 'popular_10000.txt' )
        word_list = file.readlines()
        word_map = {}
        for word in word_list:
            word_map[word.strip()] = 1
        return word_map
    
    def create_smalldb( self ):
        try:
            os.system( 'rm -rf ' + self.small_db_dir )
            os.system( 'mkdir ' + self.small_db_dir )
        except OSError:
            pass
        
        db_word = tc.BDB()
        db_word.open( self._get_db_filepath( self.db_dir, 'word' ), tc.BDBOREADER )
        db_small = self._open_db( self._get_db_filepath( self.small_db_dir, 'word' ) )

        word_map = self.get_popular_word_map()
        
        c = db_word.curnew()
        c.first()
        while 1:
            try:
                c.key()
            except KeyError:
                break
            if word_map.has_key( c.key() ):
                db_small.put( c.key(), c.val() )
            c.next()
        db_small.close()
        db_word.close()

if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s dbdir" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

    
    small_db_maker = SmallDbMaker( sys.argv[1] )
    small_db_maker.create_smalldb()