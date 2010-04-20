//
//  Wiktionary.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "Wiktionary.h"

@implementation Wiktionary

@synthesize indexDb;
@synthesize wordDb;
@synthesize wordIndexes;

#define LIST_SIZE 100

- (void) logtcbdberror: (TCBDB*) db {
	int errorCode = tcbdbecode( db );
	NSString *errorMessage = [NSString stringWithUTF8String:tcbdberrmsg( errorCode )];
	DebugLog( @"error: %@",  errorMessage );
	[errorMessage release];	
}


- (id) init {
	indexDb = tcbdbnew();	
	if( !tcbdbopen(indexDb, [[[NSBundle mainBundle] pathForResource:@"index" ofType:@"db"] UTF8String], BDBOREADER ) ) {
		[self logtcbdberror:indexDb];		
	}

	forwardCursor = tcbdbcurnew( indexDb );
	backwardCursor = tcbdbcurnew( indexDb );
	wordCursor = tcbdbcurnew( indexDb );
	
	wordIndexes = [[NSMutableArray alloc] initWithCapacity:(NSUInteger) LIST_SIZE];
	
	return self;
}

- (void) openWordDb {
	if ( NULL == wordDb ) {
		wordDb = tcbdbnew();
		if( !tcbdbopen(wordDb, [[[NSBundle mainBundle] pathForResource:@"word" ofType:@"db"] UTF8String], BDBOREADER ) ) {
			[self logtcbdberror:wordDb];		
		}		
	}
}

- (void) dealloc {
	if ( NULL != indexDb ) {
		tcbdbclose( indexDb );
		tcbdbdel( indexDb );
	}
	if ( NULL != wordDb ) {
		tcbdbclose( wordDb );
		tcbdbdel( wordDb );		
	}
	
	tcbdbcurdel( forwardCursor );
	tcbdbcurdel( backwardCursor );
	tcbdbcurdel( wordCursor );
	//	[self closeDb: wordDb];
	[super dealloc];
}


- (WordEntry*) wordEntryByLemma: (NSString*) aLemma {
	char *value = tcbdbget2( self.wordDb, aLemma.UTF8String );
	
	if( !value ){
//		[self logtcbdberror:wordDb];
		return nil;
	}
	
	WordEntry *entry = [[WordEntry alloc] initWithLemma:aLemma andDefinitionHtml:[NSString stringWithUTF8String:value]];
	free( value );
	return entry;
}

- (WordIndex*) findIndexByQuery:(NSString*) aQuery {
	bool jumpSuccess = tcbdbcurjump2( wordCursor, [aQuery UTF8String] );
	NSMutableString *indexKey = [[[NSMutableString alloc] init] autorelease];
	
	if ( jumpSuccess ) {
		char* key = tcbdbcurkey2( wordCursor );
		[indexKey setString:[NSString stringWithUTF8String:key]];
		
		DebugLog( @"jump success : %@, %@", aQuery, indexKey );
		free( key );
		if ( ![indexKey hasPrefix:aQuery] ) {
			if ( tcbdbcurprev( wordCursor ) ) {
				char* key = tcbdbcurkey2( wordCursor );
				[indexKey setString:[NSString stringWithUTF8String:key]];
				free( key );
			}
			DebugLog( @"noPrefix %@, %@", aQuery, indexKey ); 	
		}
	} 
	if ( !jumpSuccess ) {
		DebugLog( @"JumpFail %@", aQuery ); 	
		tcbdbcurlast( wordCursor );
		char* key = tcbdbcurkey2( wordCursor );
		[indexKey setString:[NSString stringWithUTF8String:key]];
		free( key );
	}
	
	tcbdbcurjump2( wordCursor, [indexKey UTF8String] );
	char* val = tcbdbcurval2( wordCursor );
	WordIndex* index = [[WordIndex alloc] initWithKeyString:[indexKey UTF8String] andLemmaString:val];
	free( val );
	
	return index;
}

- (WordIndex*) wordIndexFromCursor:(BDBCUR*) cursor {
	char* key = tcbdbcurkey2( cursor );
	WordIndex *index = nil;
	if ( key ) {		
		char* val = tcbdbcurval2( cursor );
		index = [[WordIndex alloc] initWithKeyString:key andLemmaString:val];
		free(val);
	}
	free(key);
	return index;
}

- (NSUInteger) fillIndexesByKey:(NSString*) aWord {
	WordIndex* wordIndex = [[self findIndexByQuery:aWord] autorelease];
	NSString* indexKey = [NSString stringWithString:wordIndex.key];
	
	[wordIndexes removeAllObjects];
	tcbdbcurjump2( forwardCursor, [indexKey UTF8String] );
	tcbdbcurjump2( backwardCursor, [indexKey UTF8String] );	
	tcbdbcurprev( backwardCursor );
	
	int i = 0;
	while( [wordIndexes count] < LIST_SIZE ) {
		bool forward = ( i% 3 != 2);
		WordIndex *index = [self wordIndexFromCursor: forward ? forwardCursor : backwardCursor ];
		if ( index != nil ) {
			if( forward ) {
				tcbdbcurnext( forwardCursor );
				[wordIndexes addObject:index];
			} else {
				tcbdbcurprev( backwardCursor );
				[wordIndexes insertObject:index atIndex:(NSUInteger)0];
			}
		}
		i++;
		[index release];
	}
	
	for ( int i = 0; i < LIST_SIZE; i++ ) {
		WordIndex* index = [wordIndexes objectAtIndex:(NSUInteger)i];
		if ( [indexKey isEqualToString:index.key] ) {
			return (NSUInteger) i;
		}
	}	
	
	return (NSUInteger) 0;
}



/*
- (NSMutableArray*) listForward:(NSString*) lemma withLimit:(NSUInteger) limit {
	NSMutableArray *array = [[NSMutableArray alloc] initWithCapacity:limit];
	
	if ( lemma ) {
		tcbdbcurjump2( forwardCursor, [lemma UTF8String]);
	}
	
	do {
		char* key = tcbdbcurkey2( forwardCursor );
		if ( key ) {		
			char* val = tcbdbcurval2( forwardCursor );
			
			WiktionaryIndex *index = [[WiktionaryIndex alloc] initWithUTF8KeyString:key andUTF8LemmaString:val];
			[array addObject:index];
			[index release];
			free(key);
			free(val);
		} else {
			break;
		}
	} while ( tcbdbcurnext( forwardCursor ) &&  --limit > 0 );	

	return array;
}
*/


@end
