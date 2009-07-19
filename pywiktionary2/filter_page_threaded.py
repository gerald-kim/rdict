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
import multiprocessing
from Queue import Empty

THREAD_COUNT = 4
STOP = '##STOP##'

def do_queue(filter_queue):
    word_manager = WordManager()
    word_manager.connect()
        
    lemma_tuples = word_manager.get_tuples_with_lemma_for_filter()
    print "LENGTH: ", len(lemma_tuples)
    
    for tuple in lemma_tuples:
        lemma = tuple[0]
        print "put to filter_queue ", lemma.encode( 'utf-8' )
        sys.stdout.flush() 
        filter_queue.put( lemma )

    word_manager.close()
    [filter_queue.put(STOP) for i in range(THREAD_COUNT)]                    
    
    
def do_filter(filter_queue, result_queue):
    word_manager = WordManager()
    word_manager.connect()
    
    lemma = None
    while lemma != STOP:
        try:
            lemma = filter_queue.get(timeout=0.5)
            
            print "do filtering:", lemma
            sys.stdout.flush()
            
            word = Word( lemma )
            result = word.filter_page()
            if result:
                word_manager.mark_filtered( lemma )
                word_manager.commit()
                
            print "done filtering:", lemma, ",", result
            sys.stdout.flush()

            result_queue.put( lemma )
        except Empty:
            print 'TIMEOUT'

    word_manager.close()
            
    result_queue.put( STOP )

if __name__ == '__main__':
    filter_queue = multiprocessing.Queue(10)
    result_queue = multiprocessing.Queue(10)

    process = multiprocessing.Process(target=do_queue, args=(filter_queue, ) )
    process.start()
    
    processes = [multiprocessing.Process(target=do_filter, args=(filter_queue, result_queue, )) for i in range(THREAD_COUNT)]
    for p in processes: 
        p.start()

    o = None
    stop_count = 0
    while stop_count != THREAD_COUNT:
        try:
            o = result_queue.get(timeout=10)
            print "done", o
            sys.stdout.flush()
            if STOP == o:
                stop_count = stop_count + 1
        except Empty:
            print 'TIMEOUT'

    time.sleep( 5 )
    print
    