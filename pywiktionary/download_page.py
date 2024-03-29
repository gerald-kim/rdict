#!/usr/bin/env python
#
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 13, 2009 by evacuee

from models import *
import time 
import sys
import traceback

if __name__ == '__main__':
    word_manager = WordManager()
    word_manager.connect()
    lemma_tuples_iter = iter( word_manager.get_tuples_with_lemma_for_download() )
    WordCount = 0
    
    tuple = lemma_tuples_iter.next()
    
    while tuple:
        lemma = tuple[0]
        word = Word( lemma )
        try:
            if word.download_page():
                if word.deleted():
                    word_manager.mark_deleted( lemma )
                else:
                    word_manager.mark_downloaded( lemma )
        except:
            print "Unexpected error on word[%s] %s" % ( lemma.encode( 'utf-8' ), word.get_page_path() )
            traceback.print_exception( *sys.exc_info() )

        WordCount += 1
        if WordCount % 100 == 0:
            print "Downloaded: ", WordCount
        tuple = lemma_tuples_iter.next()
        
    word_manager.close()
