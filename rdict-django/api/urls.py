from django.conf.urls.defaults import *

urlpatterns = patterns( 'rdict-django.api.views',
    url( r'^search$', 'search' ),
                        
 )
