
from django_openid.consumer import SessionConsumer

import logging

class MyConsumer(SessionConsumer):
    redirect_after_login = '/'
    sreg = ['email'] # 'timezone' 

    base_template = 'base.html'
    extension_args = {
        'ext1.mode':'fetch_request',
        'ext1.required':'email',
        'ext1.type.email':'http://schema.openid.net/contact/email',
    }
    extension_namespaces = {
        'sreg': 'http://openid.net/sreg/1.0',
        'ext1': 'http://openid.net/srv/ax/1.0',
    }
    
    def set_user_session(self, request, response, user_session):
        """Drity hack to using google account"""
        if request.GET.has_key('openid.ext1.value.email'):
            openids = request.session.get('openids', [])
            openids[-1].sreg['email'] = request.GET['openid.ext1.value.email']
            logging.debug('email: ' + request.GET['openid.ext1.value.email'])

        SessionConsumer.set_user_session(self, request, response, user_session)
