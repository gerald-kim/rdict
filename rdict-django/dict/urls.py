from django.conf.urls.defaults import *

urlpatterns = patterns( 'rdict-django.dict.views',
    url( r'^__suggest$', 'suggest' ),
    url( r'^(?P<q>.*)$', 'search' ),
                        
 )
