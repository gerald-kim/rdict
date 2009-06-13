//
//  Wiktionary.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "Wiktionary.h"

@implementation Wiktionary

@synthesize indexDb;
@synthesize wordDb;
@synthesize wordList;

const NSUInteger LIST_SIZE = 100;

- (void) logtcbdberror: (TCBDB*) db {
	int errorCode = tcbdbecode( db );
	NSString *errorMessage = [NSString stringWithUTF8String:tcbdberrmsg( errorCode )];
	NSLog( @"error: %@",  errorMessage );
	[errorMessage release];	
}

- (id) init {
	indexDb = tcbdbnew();
	wordDb = tcbdbnew();
	
	if( !tcbdbopen(indexDb, [[[NSBundle mainBundle] pathForResource:@"index" ofType:@"db"] UTF8String], BDBOREADER ) ) {
		[self logtcbdberror:indexDb];		
	}
	if( !tcbdbopen(wordDb, [[[NSBundle mainBundle] pathForResource:@"word" ofType:@"db"] UTF8String], BDBOREADER ) ) {
		[self logtcbdberror:wordDb];		
	}
	
	forwardCursor = tcbdbcurnew( indexDb );
	backwardCursor = tcbdbcurnew( indexDb );
	wordCursor = tcbdbcurnew( indexDb );
	
	wordList = [[NSMutableArray alloc] initWithCapacity:(NSUInteger) LIST_SIZE];
	
	return self;
}

- (void) dealloc {
	tcbdbclose( indexDb );
	tcbdbdel( indexDb );
	tcbdbclose( wordDb );
	tcbdbdel( wordDb );
	
	tcbdbcurdel( forwardCursor );
	tcbdbcurdel( backwardCursor );
	tcbdbcurdel( wordCursor );
	//	[self closeDb: wordDb];
	[super dealloc];
}


- (WiktionaryEntry*) getWiktionaryEntry: (NSString*) lemma {
	char *value = tcbdbget2( self.wordDb, lemma.UTF8String );
	
	if( !value ){
//		[self logtcbdberror:wordDb];
		return NULL;
	}
	
	WiktionaryEntry *entry = [[WiktionaryEntry alloc] initWithLemma:lemma andDefinitionHtml:[NSString stringWithUTF8String:value]];
	
	return entry;
}

- (WiktionaryIndex*) getWiktionaryIndexFromCursor:(BDBCUR*) cursor {
	char* key = tcbdbcurkey2( cursor );
	WiktionaryIndex *index = nil;
	if ( key ) {		
		char* val = tcbdbcurval2( cursor );
		index = [[WiktionaryIndex alloc] initWithUTF8KeyString:key andUTF8LemmaString:val];
		free(val);
	}
	free(key);
	return index;
}

- (NSString*) jumpToWord:(NSString*) word {
	bool jumpSuccess = tcbdbcurjump2( wordCursor, [word UTF8String] );
	NSString *jumpWord = NULL;
	if ( jumpSuccess ) {
		char* key = tcbdbcurkey2( wordCursor );
		jumpWord = [NSString stringWithUTF8String:key];
		NSLog( @"jump success : %@, %@", word, jumpWord );
		free( key );
		if ( ![jumpWord hasPrefix:word] ) {
			if ( tcbdbcurprev( wordCursor ) ) {
				char* key = tcbdbcurkey2( wordCursor );
				jumpWord = [NSString stringWithUTF8String:key];
				free( key );
			}
			NSLog( @"noPrefix %@, %@", word, jumpWord ); 	

		}
	} 
	if ( !jumpSuccess ) {
		NSLog( @"JumpFail %@", word ); 	
		tcbdbcurlast( wordCursor );
		char* key = tcbdbcurkey2( wordCursor );
		jumpWord = [NSString stringWithUTF8String:key];
		free( key );
	}
	return jumpWord;
}

- (NSUInteger) fillWordList:(NSString*) word {
	NSString* jumpWord = [self jumpToWord:word];
	
	[wordList removeAllObjects];
	tcbdbcurjump2( forwardCursor, [jumpWord UTF8String] );
	tcbdbcurjump2( backwardCursor, [jumpWord UTF8String] );	
	tcbdbcurprev( backwardCursor );
	
	int i = 0;
	while( [wordList count] < LIST_SIZE ) {
		bool forward = ( i% 3 != 2);
		WiktionaryIndex *index = [self getWiktionaryIndexFromCursor: forward ? forwardCursor : backwardCursor ];
		if ( index != nil ) {
			if( forward ) {
				tcbdbcurnext( forwardCursor );
				[wordList addObject:index];
			} else {
				tcbdbcurprev( backwardCursor );
				[wordList insertObject:index atIndex:(NSUInteger)0];
			}
		}
		i++;
		[index release];
	}
	
	for ( int i = 0; i < LIST_SIZE; i++ ) {
		WiktionaryIndex* index = [wordList objectAtIndex:(NSUInteger)i];
		if ( [jumpWord isEqualToString:index.key] ) {
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
