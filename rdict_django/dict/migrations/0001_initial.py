
from south.db import db
from django.db import models
from dict.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'Word'
        db.create_table('dict_word', (
            ('id', orm['dict.Word:id']),
            ('lemma', orm['dict.Word:lemma']),
            ('definition', orm['dict.Word:definition']),
        ))
        db.send_create_signal('dict', ['Word'])
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'Word'
        db.delete_table('dict_word')
        
    
    
    models = {
        'dict.word': {
            'definition': ('django.db.models.fields.TextField', [], {}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lemma': ('django.db.models.fields.CharField', [], {'max_length': '150', 'db_index': 'True'})
        }
    }
    
    complete_apps = ['dict']
