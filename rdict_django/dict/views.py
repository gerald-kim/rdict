# -*- coding: utf-8 -*- 

from datetime import datetime 
import re

# Local imports
from dict.models import *
from review.models import *
from view_utils import *
from pywiktionary import *

import logging

def search( request, q ):
    #q = request.GET['q']
    #logging.debug(q.encode('utf-8'))
    word = Word()
    word.lemma = q
    word.definition = request.pywiktionary.get_definition( q.encode( 'utf-8' ) )

    return respond( request, 'index.html', {'word':word, 'q':q, 'userSubmittedSearch':True} )

def suggest( request ):
    q = request.GET['q']
    
    try:
        cursor = request.pywiktionary.create_cursor()
        word_indexes = request.pywiktionary.list_forward( cursor, q, 10 )
    except Exception, e:
        request.pywiktionary.close_cursor( cursor )
        word_indexes = []

    return respond( request, 'suggest.html', {'word_indexes' : word_indexes} )
