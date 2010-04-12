# -*- coding: utf-8 -*-

from view_utils import *
import urllib2, urllib
import json

def rpx(request):
    token = request.POST['token']

    api_params = {
        'token': token,
        'apiKey': '7151c99b457a7989b2261ff55081e970511c1169',
        'format': 'json',
        }
    
    # make the api call
    http_response = urllib2.urlopen('https://rpxnow.com/api/v2/auth_info',
                                    urllib.urlencode(api_params))

    # read the json response
    auth_info_json = http_response.read()

    # Step 3) process the json response
    auth_info = json.loads(auth_info_json)

    # Step 4) use the response to sign the user in
    if auth_info['stat'] == 'ok':
        profile = auth_info['profile']
        
        # 'identifier' will always be in the payload
        # this is the unique idenfifier that you use to sign the user
        # in to your site
        identifier = profile['identifier']

        # these fields MAY be in the profile, but are not guaranteed. it
        # depends on the provider and their implementation.
        name = profile.get('displayName')
        email = profile.get('email')
        profile_pic_url = profile.get('photo')
        
        # actually sign the user in.  this implementation depends highly on your
        # platform, and is up to you.
        #sign_in_user(identifier, name, email, profile_pic_url)
        print identifier, name, email
        request.session['rpxuser'] = (identifier, name, email)
        return HttpResponseRedirect('/')
    else:
        print 'An error occured: ' + auth_info['err']['msg']
        return HttpResponseServerError()

def signout(request):
    del request.session['rpxuser']
    return HttpResponseRedirect('/')
