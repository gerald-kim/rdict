# -*- coding: utf-8 -*- 


from django.core import serializers
from django.utils import simplejson
from django.core.paginator import Paginator, InvalidPage, EmptyPage

# Local imports
from review.models import *
from view_utils import *
import string
import logging

@login_required
def word_save( request ):
    q = request.GET.get( 'q' )
    a = request.GET.get( 'a' )
    
    try:
        card = Card.objects.get( user = request.login_user, question = q )
        card.forget()
        card.answer += '\n\n' + a
    except Card.DoesNotExist:
        card = Card.new_card( q, a, request.login_user )
    card.save()
    
    return respond_json( request, simplejson.dumps( {'result':True, 'card_id': card.id } ) )

@login_required
def index( request ):
    card_count = Card.objects.filter( user=request.login_user ).count()
    session_count = RepSession.objects.filter( user=request.login_user ).count()
    schedules = Card.get_schedules( request.login_user, 14 )
    
    context = {"card_count": card_count, "session_count" : session_count,
               "schedules" : schedules }
    return respond( request, 'review/index.html', context )

@login_required
def new( request ):
    scheduled_cards = Card.find_scheduled( request.login_user )
    if len( scheduled_cards ) == 0:
        #TODO provide some meaningful message
        return HttpResponseRedirect( "/" )
    
    rep_session = RepSession()
    rep_session.user = request.login_user
    rep_session.save()
    
    return HttpResponseRedirect( "/review/" + str( rep_session.id ) + "/" )
    
@login_required    
def review( request, session_id ):
    rep_session = RepSession.objects.get( id=session_id )
    
    scheduled_cards = Card.find_scheduled( request.login_user )
    card = scheduled_cards[0]
    
    return respond( request, 'review/review.html', {'rep_session':rep_session, 'card':card, 'idx':0} )

@login_required    
def answer( request, session_id ):
    card_id = int( request.GET['card_id'] )
    rep_id = int( request.GET['rep_id'] )
    grade = int( request.GET['grade'] )
    
    rep_session = RepSession.objects.get( id=session_id )
    
    if card_id != 0:
        card = Card.objects.get( id=card_id )
        card.study( grade, rep_session )
    if rep_id != 0:
        rep = Rep.objects.get( id=rep_id )
        rep.repeat( grade )
    
    reminding_cards = Card.find_scheduled( request.login_user ).count()
    reminding_reps = Rep.find_poor_grade( rep_session ).count()
    
    return respond_json( request,
        simplejson.dumps( { "result": True, "reminding_cards": reminding_cards, "reminding_reps": reminding_reps } ) )

            
@login_required
def next_card( request, session_id ):
    rep_session = RepSession.objects.get( id=session_id )
    
    next_card = Card.find_scheduled( request.login_user )[0]

    return respond_json( request, simplejson.dumps( {
            "card_id": next_card.id,
            "rep_id": 0,
            "question": next_card.question,
            "answer": next_card.answer,
        } ) )

@login_required
def next_rep( request, session_id ):
    rep_session = RepSession.objects.get( id=session_id )
    
    next_rep = Rep.find_poor_grade( rep_session )[0]

    return respond_json( request, simplejson.dumps( {
            "card_id": 0,
            "rep_id": next_rep.id,
            "question": next_rep.card.question,
            "answer": next_rep.card.answer,
        } ) )


@login_required
def card_list( request ):
    SORT = {'c':'-created', 's':'-studied'}
    sort = request.GET.get( 'sort', 's' )
    if sort not in SORT.keys():
        sort = 'c'
    
    all_cards = Card.objects.filter( user=request.login_user ).order_by( SORT[sort] )
    paginator = Paginator( all_cards, 10 ) 
    
    try:
        page = int( request.GET.get( 'page', '1' ) )
    except ValueError:
        page = 1
    
    try:
        card_page = paginator.page( page )
    except ( EmptyPage, InvalidPage ):
        card_page = paginator.page( paginator.num_pages )
        
    duplicated_questions = Card.find_duplicated_questions( request.login_user )

    return respond( request, 'review/card_list.html', {"card_page": card_page,
        'sort':sort, 'has_duplication': len( duplicated_questions ) > 0 } )


@login_required
def card_save( request ):
    card_id = request.POST.get( 'card_id' )[12:]
    answer = request.POST.get( 'update_value' )
    original_answer = request.POST.get( 'original_html' )
    
    card = Card.objects.get( id=card_id )
    if card and request.login_user == card.user:
        card.answer = answer
        card.save()
    else:
        answer = original_answer

    return respond_text( request, answer )

@login_required
def card_delete( request ):
    card_id = request.GET.get( 'card_id' )
    card = Card.objects.get( id=card_id )
    
    if card and request.login_user == card.user:
        card.delete()
    
    return respond_json( request, simplejson.dumps( {'result':True} ) )