#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import tc
import cdb

reload( sys )
sys.setdefaultencoding( "utf-8" )

class SmallCdbMaker:
    def __init__(self, db_dir):
        self.db_dir = db_dir
        self.small_db_dir = 'enwiktionary-small_cdb'

    def create_smalldb_for_cdb( self ):
        try:
            os.system( 'rm -rf ' + self.small_db_dir )
            os.system( 'mkdir ' + self.small_db_dir )
        except OSError:
            pass
        
        db_word = tc.BDB()
        db_word.open( self._get_db_filepath( self.db_dir, 'word' ), tc.BDBOREADER )
        
        word_map = self.get_popular_word_map()
        
        c = db_word.curnew()
        c.first()
                
        i = 0
        small_filename = ('word%d' % i)
        db_small = self._open_db( self._get_db_filepath( self.small_db_dir, small_filename ) )

        while 1:
            try:
                c.key()
            except KeyError:
                break
            if word_map.has_key( c.key() ):
                db_small.add( c.key(), c.val() )
            
                if db_small.numentries >= 400:
                    db_small.finish()
                    i = i + 1
                    small_filename = ('word%d' % i)
                    db_small = self._open_db(self._get_db_filepath( self.small_db_dir, small_filename ) )
                    
            c.next()
            
        db_small.finish()
        del(db_small)
        
        db_word.close()
        
    def _get_db_filepath( self, dir, name ):
        return os.path.join( os.path.dirname( __file__ ), dir, name + '.db' )
    
    def _open_db( self, path ):
        db = cdb.cdbmake(path, path + ".tmp");
        return db
        
    def get_popular_word_map( self ):
        file = open( 'popular_10000.txt' )
        word_list = file.readlines()
        word_map = {}
        for word in word_list:
            word_map[word.strip()] = 1
        return word_map

if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s dbdir" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

    
    small_db_maker = SmallCdbMaker( sys.argv[1] )
    small_db_maker.create_smalldb_for_cdb()