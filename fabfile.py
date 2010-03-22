
from fabric.api import *
from fabric.contrib.files import exists

def production():
    env.hosts = ['www.ampliostudios.com']
    env.user = 'amplio'

def deploy():
    require( 'hosts', provided_by=[production] )
    with cd( 'Sites/ampliostudios/web' ): 
        run( 'git pull' )                 
        if exists( 'fcgi.pid' ):
            run( "kill `cat ~/Sites/ampliostudios/web/fcgi.pid`" )
            run( 'rm fcgi.pid' )
        run( './fcgi.sh' )

    # run

