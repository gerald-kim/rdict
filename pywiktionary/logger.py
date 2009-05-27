import logging, os

loggers = {}

def get_logger( name='' ):
    logger = None
    if loggers.has_key( name ):
        logger = loggers[name]
    else:
        formatter = logging.Formatter( "%(asctime)s - %(name)s - %(levelname)s - %(message)s" )
        simpleFormatter = logging.Formatter( "%(message)s" )
    
        logger = logging.getLogger( name )
        logger.setLevel( logging.DEBUG )
    
        ch = logging.StreamHandler()
        ch.setLevel( logging.DEBUG )
        ch.setFormatter( formatter )
        #logger.addHandler( ch )
        
        work_dir = os.path.join( os.path.dirname( __file__), 'work' )
        if not os.path.exists( work_dir ):
            os.makedirs( work_dir )

        if name:
            fh = logging.FileHandler( os.path.join( work_dir, name + '.log' ) )
            fh.setLevel( logging.INFO )
            fh.setFormatter( simpleFormatter )
            logger.addHandler( fh )
        
        loggers[name] = logger
    return logger

LOGGER = get_logger( 'root' )
WORD_LOGGER = get_logger( 'word' )
STRUCTURE_LOGGER = get_logger( 'structure' )
MACRO_LOGGER = get_logger( 'macro' )
INTERWIKI_LOGGER = get_logger( 'interwiki' )
