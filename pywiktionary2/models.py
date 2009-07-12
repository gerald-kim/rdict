#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 12, 2009 by evacuee

from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Table, Column, Integer, String, MetaData, ForeignKey, BOOLEAN
from sqlalchemy.orm import sessionmaker
from hashlib import sha1
import os
import urllib2
import bz2

WORD_DB = 'word.db'
FILE_DIR = 'word.db.files'

def setup_test_env():
    global WORD_DB, FILE_DIR
    WORD_DB = 'tests.db'
    FILE_DIR = 'tests.db.files' 

def create_session():
    engine = create_engine( 'sqlite:///' + WORD_DB, echo = False, encoding = 'utf-=8', convert_unicode = True, assert_unicode = True )
    if not u'words' in engine.table_names():
        metadata = MetaData()
        words_table = Table( 'words', metadata,
            Column( 'lemma', String, primary_key = True ),
            Column( 'revision', Integer ),
            Column( 'downloaded', BOOLEAN ),
            Column( 'filtered', BOOLEAN )
        )
        metadata.create_all( engine )
        
    Session = sessionmaker( bind = engine )
    session = Session()
    return session

Base = declarative_base()

class Word( Base ):
    __tablename__ = 'words'
    
    lemma = Column( String, primary_key = True )
    revision = Column( Integer )
    downloaded = Column( BOOLEAN )
    filtered = Column( BOOLEAN )
    
    def __init__( self, lemma, revision ):
        self.lemma = lemma
        self.revision = revision
    
    def __repr__( self ):
        return "<Word('%s','%d')>" % ( self.lemma, self.revision )

    def get_file_name( self ):
        return self.lemma.replace( ' ', '_' )
    
    def get_file_dir( self ):
        hash = sha1( self.lemma ).hexdigest()
        return os.path.join( os.path.dirname( __file__ ), FILE_DIR, hash[0:2], hash[2:4] )
                             
    def get_page_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".html.bz2" )
        
    def get_definition_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".def.bz2" )
        
    def download_page( self ):
        url = "http://en.wiktionary.com/wiki/" + urllib2.quote( self.get_file_name() )
        print 'wget -qc -O - %s | bzip2 -c9 > %s' % ( url, self.get_page_path() )
        os.system( 'mkdir -p %s' % self.get_file_dir() )
        os.system( 'wget -qc -O - %s | bzip2 -c9 > %s' % ( url, self.get_page_path() ) )
        
        #os.system( ) 
        self.downloaded = True
        
    def get_page( self ):
        if not self.downloaded:
            return None
        
        f = bz2.BZ2File( self.get_page_path() )
        page = unicode( f.read(), 'utf-8' )
        f.close()
        
        return page
    
    page = property( get_page )
        
   
