from django.db import models

from dict.models import * 
#from review.models import *

class User(models.Model):
    openid = models.CharField(max_length=150, db_index=True)
#    name = models.CharField(max_length=100)
    email = models.CharField(max_length=100)
#    dictionaries = models.ManyToManyField(Dictionary, through="DictionaryUsing")
    created         = models.DateTimeField(auto_now_add=True)
    
    def __unicode__(self):
        return self.openid

#class DictionaryUsing(models.Model):
#    user = models.ForeignKey(User)
#    dictionary = models.ForeignKey(Dictionary)
#    order = models.IntegerField()
    
