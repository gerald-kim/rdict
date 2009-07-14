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
    q = s.query(Word).filter_by( downloaded = True ).filter_by( filtered = False )
    WordCount = 0
    
    while q.count() > 0:
        for word in q.slice( 0, 100 ).all():
            word.filter_page()

            WordCount += 1
            if WordCount % 100 == 0:
                print "Downloaded: ", WordCount