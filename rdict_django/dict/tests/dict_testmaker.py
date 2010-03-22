from django.test import TestCase
from django.test import Client
from django import template
from django.db.models import get_model

c = Client()

class Testmaker(TestCase):
    #fixtures = ["dict"]


    def test_dictsearch_124367375554(self): 
        r = c.get('/dict/held')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(unicode(r.context[-1]["q"]), u"held")
        self.assertEqual(unicode(r.context[-1]["userSubmittedSearch"]), u"True")
        self.assertEqual(unicode(r.context[-1]["word"]), u"held")

    def test_dictsuggest_124367378631(self): 
        r = c.get('/dict/__suggest', {'q': 'he'})
        self.assertEqual(r.status_code, 200)
        self.assertEquals( 10, len( r.context["word_indexes"] ) )
