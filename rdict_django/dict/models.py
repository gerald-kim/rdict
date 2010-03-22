from django.db import models

class Word( models.Model ):
    lemma = models.CharField( max_length=150, db_index=True )
    definition = models.TextField()
    #test = models.TextField()
    
    def __unicode__( self ):
        return self.lemma

#class Dictionary(models.Model):    
#    name = models.CharField(max_length=50)
#    description = models.TextField()
