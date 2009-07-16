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
    s = create_session()
    if len( sys.argv ) == 2:
        word = s.query(Word).filter_by( lemma = unicode( sys.argv[1], 'utf-8' ) ).one()
        word.filter_page()
        
        f = open( '/tmp/word.html', 'w' )
        f.write( word.definition.encode( 'utf-8' ) )
        f.close()
        os.system( 'open /tmp/word.html' )
    else:
        q = s.query(Word).filter_by( downloaded = True ).filter_by( filtered = False )
        WordCount = 0
        
        while q.count() > 0:
            for word in q.slice( 0, 100 ).all():
                word.filter_page()
                s.flush()
    
                WordCount += 1
                if WordCount % 100 == 0:
                    print "Downloaded: ", WordCount