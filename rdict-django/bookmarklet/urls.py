from django.conf.urls.defaults import *

urlpatterns = patterns('rdict-django.bookmarklet.views',
    url(r'^js$', 'js'),
    url(r'^form$', 'form'),
    url(r'^save$', 'save'),
    url(r'^close$', 'close'),
)
