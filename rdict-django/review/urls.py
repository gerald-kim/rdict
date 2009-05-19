from django.conf.urls.defaults import *

urlpatterns = patterns( 'rdict-django.review.views',
    url( r'^(?P<session_id>\d+)/$', 'review' ),
    url( r'^(?P<session_id>\d+)/answer$', 'answer' ),
    url( r'^(?P<session_id>\d+)/next_card$', 'next_card' ),
    url( r'^(?P<session_id>\d+)/next_rep$', 'next_rep' ),
    url( r'^new$', 'new' ),
    url( r'^cards/$', 'card_list' ),
    url( r'^cards/save$', 'card_save' ),
    url( r'^cards/delete$', 'card_delete' ),
    url( r'^$', 'index' ),
    url( r'^word_save$', 'word_save' ),

 )
