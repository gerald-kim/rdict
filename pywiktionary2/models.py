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
import time
import traceback
from wiktionary_filter import *

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

        #Index( 'idx_downloaded', word_table.downloaded )


    Session = sessionmaker( bind = engine, autocommit = True  )
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
        self.downloaded = False
        self.filtered = False

    def __repr__( self ):
        return u"<Word('%s','%d')>" % ( self.lemma, self.revision )

    def get_file_name( self ):
        return urllib2.quote( self.lemma.encode( 'utf-8' ) )

    def get_file_dir( self ):
        hash = sha1( self.get_file_name() ).hexdigest()
        return os.path.join( os.path.dirname( __file__ ), FILE_DIR, hash[0:2], hash[2:4] )

    def get_page_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".html.bz2" )

    def get_definition_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".def.bz2" )

    def download_page( self ):
        if None != self.page:
            self.downloaded = True
            return

        try:
            time.sleep( 1 )
            url = "http://en.wiktionary.com/wiki/" + self.get_file_name()
            #print 'wget -qc -O - %s | bzip2 -c9 > %s' % ( url, self.get_page_path() )
            os.system( 'mkdir -p "%s"' % self.get_file_dir() )
            os.system( 'wget -qc -O - "%s" | bzip2 -c9 > "%s"' % ( url, self.get_page_path() ) )

            #os.system( )
            self.downloaded = True
        except KeyboardInterrupt:
            raise
        except:
            self.downloaded = None
#            self.session.flush()
            print "Unexpected error on word(%s):" % ( self.lemma.encode('utf-8'))
            #traceback.print_exception( *sys.exc_info() )
            raise

    def filter_page( self ):
        try:
            filter = WiktionaryFilter()
            contentSoup = filter.findContentSoup( self.page )
            content = filter.executeFilters( contentSoup )

            f = bz2.BZ2File( self.get_definition_path(), 'w' )
            f.write( content )
            f.close()

            self.filtered = True
        except KeyboardInterrupt:
            raise
        except:
            self.filtered = None
            print u"filtering error: %s" % ( self.lemma )
            traceback.print_exception( *sys.exc_info() )

    def read_bzip_file( self, path ):
        content = None
        try:
            f = bz2.BZ2File( path )
            content = unicode( f.read(), 'utf-8' )
            f.close()
        except IOError:
            content = None
        except UnicodeDecodeError:
            content = None

        return content

    def get_page( self ):
        return self.read_bzip_file( self.get_page_path() )

    def get_definition( self ):
        return self.read_bzip_file( self.get_definition_path() )

    page = property( get_page )
    definition = property( get_definition )