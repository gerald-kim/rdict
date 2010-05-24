
from south.db import db
from django.db import models
from user.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'User'
        db.create_table('user_user', (
            ('id', orm['user.User:id']),
            ('openid', orm['user.User:openid']),
            ('email', orm['user.User:email']),
            ('created', orm['user.User:created']),
        ))
        db.send_create_signal('user', ['User'])
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'User'
        db.delete_table('user_user')
        
    
    
    models = {
        'user.user': {
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'email': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'openid': ('django.db.models.fields.CharField', [], {'max_length': '150', 'db_index': 'True'})
        }
    }
    
    complete_apps = ['user']
