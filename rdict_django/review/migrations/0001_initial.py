# -*- coding: utf-8 -*-

from south.db import db
from django.db import models
from review.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'RepSession'
        db.create_table('review_repsession', (
            ('id', orm['review.RepSession:id']),
            ('user', orm['review.RepSession:user']),
            ('created', orm['review.RepSession:created']),
            ('modified', orm['review.RepSession:modified']),
        ))
        db.send_create_signal('review', ['RepSession'])
        
        # Adding model 'Rep'
        db.create_table('review_rep', (
            ('id', orm['review.Rep:id']),
            ('rep_session', orm['review.Rep:rep_session']),
            ('card', orm['review.Rep:card']),
            ('grade', orm['review.Rep:grade']),
            ('final_grade', orm['review.Rep:final_grade']),
            ('created', orm['review.Rep:created']),
            ('modified', orm['review.Rep:modified']),
        ))
        db.send_create_signal('review', ['Rep'])
        
        # Adding model 'Card'
        db.create_table('review_card', (
            ('id', orm['review.Card:id']),
            ('user', orm['review.Card:user']),
            ('question', orm['review.Card:question']),
            ('answer', orm['review.Card:answer']),
            ('reps_since_lapse', orm['review.Card:reps_since_lapse']),
            ('easiness', orm['review.Card:easiness']),
            ('interval', orm['review.Card:interval']),
            ('scheduled', orm['review.Card:scheduled']),
            ('created', orm['review.Card:created']),
            ('modified', orm['review.Card:modified']),
            ('studied', orm['review.Card:studied']),
            ('effective_ended', orm['review.Card:effective_ended']),
        ))
        db.send_create_signal('review', ['Card'])
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'RepSession'
        db.delete_table('review_repsession')
        
        # Deleting model 'Rep'
        db.delete_table('review_rep')
        
        # Deleting model 'Card'
        db.delete_table('review_card')
        
    
    
    models = {
        'review.card': {
            'answer': ('django.db.models.fields.TextField', [], {}),
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'easiness': ('django.db.models.fields.FloatField', [], {}),
            'effective_ended': ('django.db.models.fields.DateTimeField', [], {'null': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'interval': ('django.db.models.fields.IntegerField', [], {}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'question': ('django.db.models.fields.CharField', [], {'max_length': '200'}),
            'reps_since_lapse': ('django.db.models.fields.IntegerField', [], {}),
            'scheduled': ('django.db.models.fields.DateField', [], {}),
            'studied': ('django.db.models.fields.DateTimeField', [], {'null': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['user.User']"})
        },
        'review.rep': {
            'card': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['review.Card']"}),
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'final_grade': ('django.db.models.fields.IntegerField', [], {}),
            'grade': ('django.db.models.fields.IntegerField', [], {}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'rep_session': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['review.RepSession']"})
        },
        'review.repsession': {
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['user.User']"})
        },
        'user.user': {
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'email': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'openid': ('django.db.models.fields.CharField', [], {'max_length': '150', 'db_index': 'True'})
        }
    }
    
    complete_apps = ['review']
