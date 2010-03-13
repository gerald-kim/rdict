#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 12, 2009 by evacuee

import sqlite3
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

UPDATED, DOWNLOADED, FILTERED, DELETED = range(4)

class WordManager:
    def connect( self ):
        self.conn = sqlite3.connect( WORD_DB )
        self.conn.isolation_level = None

        self.c = self.conn.cursor()
        self.c.execute( 'PRAGMA synchronous = 2' )
        self.c.execute( "select count(*) from sqlite_master where type = 'table' and tbl_name = 'words'" )
        if self.c.next()[0] == 0:
            self.c.execute( "CREATE TABLE words ( lemma VARCHAR NOT NULL, revision INTEGER, status INTEGER, updated BOOLEAN, PRIMARY KEY (lemma))" )

    def begin( self ):
        self.c.execute( 'begin' )


    def commit( self ):
        self.conn.commit()

    def rollback( self ):
        self.conn.rollback()

    def close( self ):
        self.c.close()
        self.conn.close()

    def save( self, word ): 
        word.updated = True
        
        self.c.execute( 'select revision from words where lemma = ?', ( word.lemma, ) )
        r = self.c.fetchone()

        if not r:
            self.c.execute( 'insert into words values ( ?, ?, 0, 1 ) ', ( word.lemma, word.revision, ) )
        else:
            revision = int(r[0])

            if revision < word.revision:
                self.c.execute( 'update words set revision = ?, status = 0, updated = 1 where lemma = ?', ( word.revision, word.lemma, ) )
            else:
                self.c.execute( 'update words set updated = 1 where lemma = ?', ( word.lemma, ) )

    def get( self, lemma ):
        self.c.execute( 'select * from words where lemma = ?', ( lemma, ) )
        r = self.c.fetchone()
        if r:
            return Word( r[0], r[1], r[2], 1 == r[3] )
        else:
            return None

    def unmark_updated( self ):
        self.c.execute( 'update words set updated = 0' )

    def delete_unupdated( self ):
        self.c.execute( 'update words set status = 4 where updated = 0' )

    def mark_deleted( self, lemma ):
        self.c.execute( 'update words set status = 4 where lemma = ?', ( lemma, ) )

    def mark_downloaded( self, lemma ):
        self.c.execute( 'update words set status = 1 where lemma = ?', ( lemma, ) )

    def mark_filtered( self, lemma ):
        self.c.execute( 'update words set status = 2 where lemma = ?', ( lemma, ) )

    def get_tuples_with_lemma_for_download( self ):
        self.c.execute( 'select lemma from words where status = 0' );
        return self.c.fetchall()

    def get_tuples_with_lemma_for_filter( self ):
        self.c.execute( 'select lemma from words where status = 1' );
        return self.c.fetchall()

    def get_tuples_with_lemma_for_exporting( self ):
        self.c.execute( 'select lemma from words where filtered = 1 and downloaded = 1' );
        return self.c.fetchall()

    def find_existing_words( self, lemmas ):
        existing_words = {}
        criteria = u'lemma in ( %s )' % ( ( u'?,'*len( lemmas ) )[:-1] )
        self.c.execute( u'select lemma from words where ' + criteria, lemmas )
        for row in self.c.fetchall():
            existing_words[ row[0] ] = 1
        return existing_words


class Word:
    DELETED_PAGE = 'Wiktionary does not yet have an entry for'

    def __init__( self, lemma, revision=0, status=0, updated=False ):
        self.lemma = lemma
        self.revision = revision
        self.status = status
        self.updated = updated
        self.cached_page = None

    def __repr__( self ):
        return u"<Word('%s','%d', '%d', '%d')>" % ( self.lemma, self.revision, self.status, self.updated )

    def get_file_name( self ):
        return urllib2.quote( self.lemma.replace( u'/', '_SLASH_' ).encode( 'utf-8' ) )

    def get_file_dir( self ):
        hash = sha1( self.get_file_name() ).hexdigest()
        return os.path.join( os.path.dirname( __file__ ), FILE_DIR, hash[0:2], hash[2:4] )

    def get_page_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".html.bz2" )

    def get_definition_path( self ):
        return os.path.join( self.get_file_dir(), self.get_file_name() + ".def.bz2" )

    def download_page( self ):
        try:
            time.sleep( 0.1 )
            url = "http://en.wiktionary.com/wiki/" + self.get_file_name()
            #print 'wget -qc -O - %s | bzip2 -c9 > %s' % ( url, self.get_page_path() )
            os.system( 'mkdir -p "%s"' % self.get_file_dir() )
            os.system( 'curl -sL "%s" | bzip2 -c9 > "%s"' % (url, self.get_page_path()) )

            return True
        except KeyboardInterrupt:
            raise
        except:
            print "Unexpected error on word(%s):" % ( self.lemma.encode( 'utf-8' ) )
            traceback.print_exception( *sys.exc_info() )
            return False

    def deleted( self ):
        return self.page.find( self.DELETED_PAGE ) > 0
        
    def filter_page( self, word_manager ):
#        if None != self.definition:
#            return True

        try:
            filter = WiktionaryFilter( word_manager )
            contentSoup = filter.findContentSoup( self.page )
            content = filter.executeFilters( contentSoup )

            f = bz2.BZ2File( self.get_definition_path(), 'w' )
            f.write( content )
            f.close()

            return True
        except KeyboardInterrupt:
            raise
        except:
            print u"filtering error: %s" % ( self.lemma )
            traceback.print_exception( *sys.exc_info() )
            return False

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
        if None == self.cached_page:
            self.cached_page = self.read_bzip_file( self.get_page_path() )
        return self.cached_page
        

    def get_definition( self ):
        return self.read_bzip_file( self.get_definition_path() )

    page = property( get_page )
    definition = property( get_definition )
