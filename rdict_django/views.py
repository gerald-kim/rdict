# -*- coding: utf-8 -*-

# Django imports
# TODO(guido): Don't import classes/functions directly.
from django import forms
from django.http import Http404
from django.http import HttpResponse, HttpResponseRedirect
from django.http import HttpResponseForbidden, HttpResponseNotFound

# Local imports
from view_utils import respond, login_required

import logging

def index( request ):
    return respond( request, 'index.html' )

def about( request ):
    return respond( request, 'about.html' )

def help( request ):
    return respond( request, 'help.html' )
