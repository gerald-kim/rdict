# Django settings for rdict project.

import os
import logging

DEBUG = True
TEMPLATE_DEBUG = DEBUG

FORCE_SCRIPT_NAME = ''

ADMINS = ( 
    ( 'Jaewoo Kim', 'jaewoo@ampliostudios.com' ),
    ( 'Steve Bodnar', 'steve@ampliostudios.com' ),
 )

MANAGERS = ADMINS

DATABASE_ENGINE = 'sqlite3'    # 'postgresql_psycopg2', 'postgresql', 'mysql', 'sqlite3' or 'oracle'.
DATABASE_NAME = 'rdict.db'     # Or path to database file if using sqlite3.
DATABASE_USER = ''             # Not used with sqlite3.
DATABASE_PASSWORD = ''         # Not used with sqlite3.
DATABASE_HOST = ''             # Set to empty string for localhost. Not used with sqlite3.
DATABASE_PORT = ''             # Set to empty string for default. Not used with sqlite3.

DICT_DB = os.path.join( os.path.dirname( __file__ ), '..', 'pywiktionary', 'enwiktionary.db' ).replace( '\\', '/' )

# Local time zone for this installation. Choices can be found here:
# http://en.wikipedia.org/wiki/List_of_tz_zones_by_name
# although not all choices may be available on all operating systems.
# If running in a Windows environment this must be set to the same as your
# system time zone.
TIME_ZONE = 'Asia/Seoul'

# Language code for this installation. All choices can be found here:
# http://www.i18nguy.com/unicode/language-identifiers.html
LANGUAGE_CODE = 'en-us'

SITE_ID = 1

# If you set this to False, Django will make some optimizations so as not
# to load the internationalization machinery.
USE_I18N = False

# Absolute path to the directory that holds media.
MEDIA_ROOT = ''
MEDIA_ROOT = os.path.join( os.path.dirname( __file__ ), 'media' ).replace( '\\', '/' )

# URL that handles the media served from MEDIA_ROOT. Make sure to use a
# trailing slash if there is a path component (optional in other cases).
MEDIA_URL = '/media/'

# URL prefix for admin media -- CSS, JavaScript and images. Make sure to use a
# trailing slash.
ADMIN_MEDIA_PREFIX = '/admin_media/'

# Make this unique, and don't share it with anybody.
SECRET_KEY = 'e&%y%j_w0w%l*tpjvsm@kdwqphe!8s$4tw^39tl6_^^pnbw6f!'

# List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = ( 
    'django.template.loaders.filesystem.load_template_source',
    'django.template.loaders.app_directories.load_template_source',
#     'django.template.loaders.eggs.load_template_source',
 )

TEMPLATE_CONTEXT_PROCESSORS = ( 
    'django.core.context_processors.auth',
    'django.core.context_processors.debug',
    'django.core.context_processors.i18n',
    'django.core.context_processors.media',
    'django.core.context_processors.request',
 )
                    

MIDDLEWARE_CLASSES = ( 
    'django.middleware.gzip.GZipMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.middleware.doc.XViewMiddleware',
#    'django.middleware.transaction.TransactionMiddleware',
    'user.middleware.UserMiddleware',
    'dict.middleware.PywiktionaryMiddleware',
    'review.middleware.CheckScheduledCardMiddleware',
    #'firepython.middleware.FirePythonDjango',
 )

ROOT_URLCONF = 'rdict_django.urls'

TEMPLATE_DIRS = ( 
    # Put strings here, like "/home/html/django_templates" or "C:/www/django/templates".
    # Always use forward slashes, even on Windows.
    # Don't forget to use absolute paths, not relative paths.
   os.path.join( os.path.dirname( __file__ ), 'templates' ).replace( '\\', '/' ),
 )

INSTALLED_APPS = ( 
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.sites',
    'django.contrib.admin',
    'south',
    'django_nose',
#    'django_evolution',
#    'test_utils',
    'user',
    'dict',
    'review',
    'bookmarklet',
 )

TEST_RUNNER = 'django_nose.NoseTestSuiteRunner'

#local setting
try:    
    from settings_local import *
except ImportError, e:
    pass

if DEBUG:
    logging.basicConfig( level=logging.DEBUG )
else:
    logging.basicConfig( level=logging.INFO )
