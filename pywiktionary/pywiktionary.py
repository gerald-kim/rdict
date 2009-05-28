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
    
class DictionaryCursor:
    def __init__( self, dictionary ):
        self.forward_index_cursor = dictionary.index_db.curnew()
        self.forward_index_cursor.first()
        self.backword_index_cursor = dictionary.index_db.curnew()
        self.backword_index_cursor.first()
        self.tmp_index_cursor = dictionary.index_db.curnew()
    
    def __del__( self ):
        del self.forward_index_cursor
        del self.backword_index_cursor
        del self.tmp_index_cursor
        
class Dictionary:
    def __init__( self, database_dir ):
        self.database_dir = database_dir
    
    def open( self ):
        self.index_db = self._open_db( 'index' ) 
        self.word_db = self._open_db( 'word' ) 
        
    def close( self ):
        
        self.index_db.close()
        self.word_db.close()
        
    def _open_db( self, name ):
        db = tc.BDB()
#        print os.path.join( self.database_dir, name )
        db.open( os.path.join( self.database_dir, name + '.db' ), tc.BDBOREADER | tc.BDBONOLCK )
        return db
    
    def create_cursor( self ):
        return DictionaryCursor( self )
    
    def close_cursor( self, cursor ):
        del cursor

    def list_forward( self, cursor, word, limit=10 ):
        word_lower = word.lower()
        if word_lower:
            try:
                cursor.tmp_index_cursor.jump( word_lower )
                cursor.forward_index_cursor.jump( word_lower )
            except KeyError:
                return []

        list = []
        for i in range( limit ):
            try:
                word_index = WordIndex( cursor.forward_index_cursor.key(), cursor.forward_index_cursor.val() )
                list.append( word_index )
                cursor.forward_index_cursor.next()
            except KeyError: 
                break
        return list
        
    def get_definition( self, word ):
        try:
            return self.word_db[word]
        except KeyError:
            return None
