#!/usr/bin/env python
# encoding: utf-8

from rdict_django.transactionaltest import TransactionalTestCase
from rdict_django.dict.models import *


class WordTest(TransactionalTestCase):
    def testCaseSenstiveLemma(self):
        w1 = WordTest.createWord(0);
        w1.save()
        
        w2 = WordTest.createWord(0);
        w2.lemma = w2.lemma.upper()
        w2.save()
        
        a2 = Word.objects.get(lemma = w2.lemma)
        self.assertEquals( w2, a2 )
        self.assertNotEquals( w1, a2 )
    
    @staticmethod
    def createWord(idx):
        w = Word()
        w.lemma = 'lemma' + str(idx)
        w.definition = 'definition' + str(idx)
        return w
