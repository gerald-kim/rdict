#!/usr/bin/env python
# encoding: utf-8

from django.http import HttpRequest

from transactionaltest import TransactionalTestCase
from user.models import *
from user.middleware import UserMiddleware

class OpenId:
    def __init__(self):
        self.openid = ''
        self.sreg = {}
        
class UserMiddlewareTest(TransactionalTestCase):
    def testlogin_userNotExist(self):
        request = HttpRequest()
        request.session = {}
        
        middleware = UserMiddleware()
        middleware.process_request(request)
        
        self.assertEqual(None, request.login_user)
        
    #XXX need a mock test
    def testFirstLogin(self):
        pass