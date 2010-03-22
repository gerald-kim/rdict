# -*- coding: utf-8 -*-

from datetime import datetime, date, timedelta
from django.db import models
from django.db import  connection

from user.models import User

class Card( models.Model ):
    user = models.ForeignKey( User )
    question = models.CharField( max_length=200 )
    answer = models.TextField()

    reps_since_lapse = models.IntegerField()
    easiness = models.FloatField()
    

    interval = models.IntegerField()
    scheduled = models.DateField() #gmt, user's timezone 기준으로 계산 필요
    
    created = models.DateTimeField( auto_now_add=True )
    modified = models.DateTimeField( auto_now=True )
    studied = models.DateTimeField( null=True )
    effective_ended = models.DateTimeField( null=True ) 

    def __unicode__( self ):
        return "'%s' card owned by %s" % ( self.question, self.user_id )

    @classmethod
    def find_scheduled( cls, user ):
        return Card.objects.filter( user=user, scheduled__lte=date.today() )

    @classmethod
    def find_duplicated_questions( cls, user ):
        dups = []
        try:
            cursor = connection.cursor()
            cursor.execute( "select question from review_card where user_id = %d group by question having( count(question)>1 ) "
                             % ( user.id ) )
            dups = map( lambda d: d[0], cursor.fetchall() )
        finally:
            cursor.close()
        return dups
         
    @classmethod
    def get_schedules( cls, user, days ):
        day_after_14days = ( datetime.now() + timedelta( days=days ) ).date()
        try:
            cursor = connection.cursor()
            cursor.execute( "select scheduled, count(*) from review_card where user_id = %d and scheduled < '%s' group by scheduled"
                             % ( user.id, str( day_after_14days ) ) )
            schedules = cursor.fetchall()
        finally:
            cursor.close()
        return schedules
    
    @classmethod
    def new_card( cls, question, answer, user ):
        card = Card()
        card.question = question
        card.answer = answer
        card.user = user
        card.reps_since_lapse = 0
        card.easiness = 2.5
        card.interval = 1
        card.scheduled = date.today() + timedelta( days=card.interval )
        
        return card
        
    def calc_interval( self ):
        if self.reps_since_lapse == 0:
            self.interval = 1
        elif self.reps_since_lapse == 1:
            self.interval = 6
        else:
            self.interval = self.interval * self.easiness
        self.scheduled = date.today() + timedelta( days=self.interval )
        
    def study( self, grade, rep_session ):
        rep = Rep()
        rep.card = self
        rep.rep_session = rep_session
        rep.grade = grade
        rep.final_grade = grade
        rep.save()
        
        if grade < 3:
            self.reps_since_lapse = 0
            self.calc_interval()
        else:
            self.reps_since_lapse += 1
            self.easiness = max( self.easiness + ( 0.1 - ( 5 - grade ) * ( 0.08 + ( 5 - grade ) * 0.02 ) ) , 1.3 )
            self.calc_interval() 

        self.studied = datetime.now()
        self.save()
        
    def forget( self ):
        #Should be trackable
        self.reps_since_lapse = 0
        self.calc_interval()


class RepSession( models.Model ):
    user = models.ForeignKey( User )
    created = models.DateTimeField( auto_now_add=True )
    modified = models.DateTimeField( auto_now=True )
    
class Rep( models.Model ):
    '''Repetition'''
    rep_session = models.ForeignKey( RepSession )
    card = models.ForeignKey( Card )
    grade = models.IntegerField()
    final_grade = models.IntegerField()
    #thinking_time   = models.IntegerField();
    
    created = models.DateTimeField( auto_now_add=True )
    modified = models.DateTimeField( auto_now=True )

    @classmethod
    def find_poor_grade( cls, rep_session ):
        return Rep.objects.filter( rep_session=rep_session, final_grade__lt=4 ).order_by( 'modified' )
    
    def repeat( self, grade ):
        self.final_grade = grade
        self.save()
    