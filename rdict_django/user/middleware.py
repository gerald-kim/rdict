
from django.db import models
from models import User

import logging

class UserMiddleware(object):
    """
    """
    def process_request(self, request):
        rpxuser = request.session.get('rpxuser', None)
        if rpxuser:
            try:
                u = User.objects.get(openid=rpxuser[0])
            except User.DoesNotExist:
                u = User()
                u.openid = rpxuser[0]
                u.name = rpxuser[1]
                u.email = rpxuser[2]
                
                u.save() 
            request.login_user = u
        else:
            request.login_user = None
