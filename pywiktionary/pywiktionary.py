# -*- coding: utf-8 -*- 

import tc
import os   

class WordIndex:
    def __init__( self, word_lower, word ):
        self.word_lower = word_lower
        self.word = word

    def __eq__( self, obj ):
        return obj.__class__ == self.__class__ and obj.word_lower == self.word_lower and obj.word == self.word 
        
    def __hash__( self ):
        return ( self.word_lower + ':' + self.word ).__hash__()
    
class Dictionary:
    def __init__( self, database_dir ):
        self.database_dir = database_dir
    
    def open( self ):
        self.index_db = self._open_db( 'index' ) 
        self.word_db = self._open_db( 'word' ) 
        self.forward_index_cursor = self.index_db.curnew()
        self.forward_index_cursor.first()
        self.backword_index_cursor = self.index_db.curnew()
        self.backword_index_cursor.first()
        self.tmp_index_cursor = self.index_db.curnew()
        
    def close( self ):
        self.index_db.close()
        self.word_db.close()
        
    def _open_db( self, name ):
        db = tc.BDB()
#        print os.path.join( self.database_dir, name )
        db.open( os.path.join( self.database_dir, name + '.db' ), tc.BDBOREADER | tc.BDBONOLCK )
        return db

    def list_forward( self, word_lower, limit=10 ):
        if word_lower:
            try:
                self.tmp_index_cursor.jump( word_lower )
                self.forward_index_cursor.jump( word_lower )
            except KeyError:
                return []

        list = []
        for i in range( limit ):
            try:
                word_index = WordIndex( self.forward_index_cursor.key(), self.forward_index_cursor.val() )
                list.append( word_index )
                self.forward_index_cursor.next()
            except KeyError: 
                break
        return list
        
    def get_definition( self, word ):
        try:
            return self.word_db[word]
        except KeyError:
            return None