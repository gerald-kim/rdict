#!/usr/bin/env python
#
# -*- coding: utf-8 -*-
# Copyright (c) 2009, Amplio Studios
# All rights reserved.
# Created at Jul 13, 2009 by evacuee

from models import *
import time 

if __name__ == '__main__':
    s = create_session()
    q = s.query(Word).filter_by( downloaded = False )
    WordCount = 0
    
    while q.count() > 0:
        for word in q.slice( 0, 10 ).all():
            time.sleep( 1 )

            word.download_page()

            WordCount += 1
            if WordCount % 100 == 0:
                print "\033[A", WordCount
                
        
    
