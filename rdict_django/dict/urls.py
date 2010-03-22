from django.conf.urls.defaults import *

urlpatterns = patterns( 'dict.views',
    url( r'^__suggest$', 'suggest' ),
    url( r'^(?P<q>.*)$', 'search' ),
                        
 )
