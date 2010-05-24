
from south.db import db
from django.db import models
from user.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding field 'User.name'
        db.add_column('user_user', 'name', orm['user.user:name'])
        
    
    
    def backwards(self, orm):
        
        # Deleting field 'User.name'
        db.delete_column('user_user', 'name')
        
    
    
    models = {
        'user.user': {
            'created': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'email': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'default': "''", 'max_length': '100'}),
            'openid': ('django.db.models.fields.CharField', [], {'max_length': '150', 'db_index': 'True'})
        }
    }
    
    complete_apps = ['user']
