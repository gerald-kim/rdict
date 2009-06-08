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
	
	return self;
}

- (WiktionaryEntry*) getWiktionaryEntry: (NSString*) lemma {
	char *value = tcbdbget2( self.wordDb, lemma.UTF8String );
	
	if( !value ){
		[self logtcbdberror:wordDb];
	}
	
	WiktionaryEntry *entry = [[WiktionaryEntry alloc] initWithLemma:lemma andDefinitionHtml:[NSString stringWithUTF8String:value]];
	
	return entry;
}

- (void) dealloc {
	if( !tcbdbclose( indexDb ) ) {
		[self logtcbdberror:indexDb];
	}
	tcbdbdel( indexDb );
	if( !tcbdbclose( wordDb ) ) {
		[self logtcbdberror:wordDb];
	}
	tcbdbdel( wordDb );
	
	//	[self closeDb: wordDb];
	[super dealloc];
}

@end
