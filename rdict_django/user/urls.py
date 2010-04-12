from django.conf.urls.defaults import *

urlpatterns = patterns(
    'user.views',
    url( r'^rpx', 'rpx' ),
    url( r'^signout', 'signout' ),
    )
