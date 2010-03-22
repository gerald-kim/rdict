# -*- coding: utf-8 -*- 

# Local imports
from review.models import *
from view_utils import *


def js(request):
    return respond(request, "bookmarklet/bookmarklet.js")

def close(request):
    return respond(request, "bookmarklet/close.html")

@login_required
def form(request):
    c = {'q':request.GET['q'], 'a':request.GET['a']}
    return respond(request, "bookmarklet/form.html", c)

@login_required
def save(request):
    card = Card.new_card(request.GET['q'], request.GET['a'], request.login_user)
    card.save()
    
    return respond_json(request, "")
