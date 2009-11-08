# Django imports
# TODO(guido): Don't import classes/functions directly.
from django import forms
from django.http import Http404
from django.http import HttpResponse, HttpResponseRedirect
from django.http import HttpResponseForbidden, HttpResponseNotFound
from django.shortcuts import render_to_response
from django.template.context import RequestContext
from django.utils import simplejson
from django.utils.safestring import mark_safe

# Local imports


def respond(request, template, params=None):
    """Helper to render a response, passing standard stuff to the response.

    Args:
        request: The request object.
        template: The template name; '.html' is appended automatically.
        params: A dict giving the template parameters; modified in-place.

    Returns:
        Whatever render_to_response(template, params) returns.

    Raises:
        Whatever render_to_response(template, params) raises.
    """
    if params is None:
        params = {}
#    if request.user is not None:
#        account = models.Account.current_user_account

    try:
        return render_to_response(template, params, context_instance=RequestContext(request))
    except MemoryError:
        logging.exception('MemoryError')
        return HttpResponse('MemoryError')
    except AssertionError:
        logging.exception('AssertionError')
        return HttpResponse('AssertionError')


def respond_json(request, json):
    response = HttpResponse()
    response['Content-Type'] = 'text/javascript'
    response.write(json) 
    return response

def respond_text(request, text):
    return render_to_response('text.html', {'text':text}, context_instance=RequestContext(request))

def respond_html(request, html):
    return render_to_response('html.html', {'html':html}, context_instance=RequestContext(request))


def login_required(func):
    """Decorator that redirects to the login page if you're not logged in."""

    def login_wrapper(request, *args, **kwds):
        if request.login_user is None:
            return HttpResponseRedirect(
                    '/openid/?next=%s' % request.get_full_path().encode('utf-8'))
        return func(request, *args, **kwds)

    return login_wrapper


