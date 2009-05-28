from django.conf.urls.defaults import *

urlpatterns = patterns( 'rdict-django.dict.views',
    url( r'^search$', 'search' ),
    url( r'^suggest$', 'suggest' ),
                        
 )
