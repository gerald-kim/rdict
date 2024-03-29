#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import tc
import sqlite3

reload( sys )
sys.setdefaultencoding( "utf-8" )

class SmallCdbMaker:
    def __init__(self, db_dir):
        self.db_dir = db_dir
        self.small_db_dir = db_dir.replace('/','') + '_sqlite'

    def create_smalldb_for_cdb( self ):
        try:
            os.system( 'rm -rf ' + self.small_db_dir )
            os.system( 'mkdir ' + self.small_db_dir )
        except OSError:
            pass
        
        db_word = tc.BDB()
        db_word.open( self._get_db_filepath( self.db_dir, 'word' ), tc.BDBOREADER )
        
        c = db_word.curnew()
        c.first()
        
        insert_count = 0
        i = 0
        small_filename = ('word')
        
        conn = sqlite3.connect(self._get_db_filepath( self.small_db_dir, small_filename ))
        small_db_c = conn.cursor()
        small_db_c.execute('''create table word_db (_id integer primary key, word text, def text)''')
        
        small_db_c.execute('''CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US')''');
        small_db_c.execute('''INSERT INTO "android_metadata" VALUES ('en_US')''');
        
        while insert_count < 1000:
            try:
                c.key()
            except KeyError:
                break
            small_db_c.execute(u'insert into word_db values (null,?,?)', [unicode(c.key()), unicode(c.val())])
            insert_count = insert_count + 1
                    
            c.next()
        
        small_db_c.execute('''create index word_db_index on word_db(word);''');
        
        conn.commit()
        small_db_c.close()
        
        db_word.close()
        
    def _get_db_filepath( self, dir, name ):
        return os.path.join( os.path.dirname( __file__ ), dir, name + '.db' )
    
if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s dbdir" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

    
    small_db_maker = SmallCdbMaker( sys.argv[1] )
    small_db_maker.create_smalldb_for_cdb()