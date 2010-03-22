from django.conf.urls.defaults import *

from django.contrib import admin
admin.autodiscover()

#from user.consumer import MyConsumer

urlpatterns = patterns('',
    (r'^$', 'views.index'),
    (r'^about/$', 'views.about'),
    (r'^help/$', 'views.help'),
    (r'^user/', include('user.urls')),
    (r'^dict/', include('dict.urls')),
    (r'^api/', include('api.urls')),
    (r'^review/', include('review.urls')),
    (r'^bookmarklet/', include('bookmarklet.urls')),

#    (r'^openid/(.*)', MyConsumer()),

    # Uncomment the admin/doc line below and add 'django.contrib.admindocs' 
    # to INSTALLED_APPS to enable admin documentation:
    #(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    (r'^admin/(.*)', admin.site.root),

)

import settings
if settings.DEBUG:
    urlpatterns += patterns('',
        (r'^media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.MEDIA_ROOT}),
  )

