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
	tcbdbcurfirst( forwardCursor );
	
	return self;
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

- (NSMutableArray*) listForward:(NSString*) lemma  {
	return [self listForward:lemma withLimit:10];
}

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

- (void) dealloc {
	tcbdbclose( indexDb );
	tcbdbdel( indexDb );
	tcbdbclose( wordDb );
	tcbdbdel( wordDb );
	
	tcbdbcurdel( forwardCursor );
	
	//	[self closeDb: wordDb];
	[super dealloc];
}

@end
