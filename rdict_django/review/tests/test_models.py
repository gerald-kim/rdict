#!/usr/bin/env python
# encoding: utf-8

from transactionaltest import TransactionalTestCase
from review.models import *
from user.models import *
from datetime import date, timedelta

class CardTest( TransactionalTestCase ):
    def setUpInTransaction( self ):
        self.user = User()
        self.user.save()
        
    def testnew_card( self ):
        c = Card.new_card( 'q', 'answer', self.user )
        c.save()
        
        self.assertEquals( 'q', c.question )
        self.assertEquals( 'answer', c.answer )
        self.assertEquals( self.user, c.user )
        self.assertEquals( 0, c.reps_since_lapse )
        self.assertEquals( 2.5, c.easiness )
        
        self.assertEquals( date.today() + timedelta( days=1 ), c.scheduled )
        
    def testfind_scheduled( self ):
        c = Card.new_card( 'q', 'answer', self.user )
        c.save()
        
        list = Card.find_scheduled( self.user )
        self.assertEquals( 0, len( list ) )
        
    def testStudyMakesOneRep( self ):
        c = Card.new_card( 'q', 'answer', self.user )
        c.save()
        
        s = RepSession()
        s.user = self.user
        s.save()

        c.study( 5, s )
        
        r = Rep.objects.all()[0]
        self.assertTrue( None != r.created )
        
        print c.interval
        print c.easiness
        c.study( 3, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
        c.study( 5, s )
        print c.interval
        print c.easiness
