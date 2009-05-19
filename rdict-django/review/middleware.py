
from django.db import models

from user.models import *
from review.models import *

class CheckScheduledCardMiddleware( object ):
    """
    """
    def process_request( self, request ):
        scheduled_card_count = 0
        if request.login_user:
            scheduled_card_count = Card.find_scheduled( request.login_user ).count()
        request.scheduled_card_count = scheduled_card_count