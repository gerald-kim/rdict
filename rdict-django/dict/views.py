# -*- coding: utf-8 -*- 

from datetime import datetime 
import re

# Local imports
from dict.models import *
from review.models import *
from view_utils import *

import settings

import tc
dict = tc.BDB()
dict.open( settings.DICT_DB, tc.BDBFOPEN | tc.BDBONOLCK )


import logging

def search( request ):
    q = request.GET['q']
    #logging.debug(q.encode('utf-8'))
    try:
        d = dict.get( q.encode( 'utf-8' ) )
        word = Word()
        word.lemma = q
        word.definition = d
    except Exception, e:
        logging.exception("search fail")
        return respond(request, 'index.html', {'word':None, 'q':q, 'userSubmittedSearch':True})

    return respond(request, 'index.html', {'word':word, 'q':q, 'userSubmittedSearch':True})
