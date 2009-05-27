#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
reload( sys )
sys.setdefaultencoding( "utf-8" )

from makedb import WiktionaryDbMaker

if __name__ == '__main__':
    if len( sys.argv ) < 2:
        print( "Usage: %s xmlfile" % ( sys.argv[0] ) ) 
        sys.exit( 1 )

    maker = WiktionaryDbMaker( sys.argv[1] )
    maker.open()
    maker.reopen_index()
    maker.create_index()
    maker.close()
