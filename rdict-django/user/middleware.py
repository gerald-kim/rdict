
from django.db import models
from models import User

import logging

class UserMiddleware(object):
    """
    """
    def process_request(self, request):
        openids = request.session.get('openids', [])
        if openids:
            openid = openids[-1] # Last authenticated OpenID
            try:
                u = User.objects.get(openid=openid.openid)
            except User.DoesNotExist:
                u = User()
                u.email = openid.sreg['email']
                u.openid = openid.openid
                
                u.save() 
            request.login_user = u
        else:
            request.login_user = None