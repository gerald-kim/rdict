from pyapns import configure, provision, notify

token = '561889ee286a61c2c3fcf2989aa0929b8f743adc595577d30339a33cdfd5c5da'
configure({'HOST': 'http://localhost:7077/'})
provision('5525D75T6C.com.ampliostudios.awtest', open('apns-dev.pem').read(), 'sandbox')
notify('5525D75T6C.com.ampliostudios.awtest', token, {'aps':{'badge': 9}})
