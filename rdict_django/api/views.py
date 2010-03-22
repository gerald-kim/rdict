# -*- coding: utf-8 -*- 

from datetime import datetime 
import re

# Local imports
from dict.models import *
from review.models import *
from view_utils import *
from pywiktionary import *

import settings
import logging


def search( request ):
    q = request.GET['q']
    definition = request.pywiktionary.get_definition( q.encode( 'utf-8' ) )
    #definition = None
    if not definition:
        raise Http404
    return respond_html( request, definition )
