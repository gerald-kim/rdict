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

reload( sys )
sys.setdefaultencoding( "utf-8" )

if __name__ == '__main__':
    word_manager = WordManager()
    word_manager.connect()

    if len( sys.argv ) == 2:
        word = word_manager.get( unicode( sys.argv[1], 'utf-8' ) )
        if word.filter_page( word_manager ):
            word_manager.mark_filtered( word.lemma )

        f = open( '/tmp/word.html', 'w' )
        f.write( word.definition.encode( 'utf-8' ) )
        f.close()
        os.system( 'open /tmp/word.html' )

    word_manager.commit()
    word_manager.close()