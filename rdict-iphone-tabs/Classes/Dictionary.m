//
//  Dictionary.m
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Dictionary.h"
#import "DictionaryEntry.h"

@implementation Dictionary
@synthesize bdb;

- (id) initWithDicPath: (NSString *) path {
	int ecode;
	self.bdb = tcbdbnew();
	
	if(!tcbdbopen(bdb, path.UTF8String, BDBOREADER)){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "open error: %s\n", tcbdberrmsg(ecode));
	}
	
	return self;
}

- (DictionaryEntry *) searchByWord: (NSString *) word {
	int ecode;
	char *value;
	const char *text = word.UTF8String;
	
	value = tcbdbget2(self.bdb, text);
	
	if(! value){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "get error: %s\n", tcbdberrmsg(ecode));
	}
	
	NSString *nsString = [[NSString alloc] initWithUTF8String:value];
	DictionaryEntry *dicEntry = [[DictionaryEntry alloc] initWithWord: word andEntry: nsString];
	[dicEntry retain];
	[nsString release];
	return dicEntry;
}

- (void) dealloc {
	int ecode;
	
	if(!tcbdbclose(bdb)){
		ecode = tcbdbecode(self.bdb);
		fprintf(stderr, "close error: %s\n", tcbdberrmsg(ecode));
	}
	
	tcbdbdel(self.bdb);
	
	[super dealloc];
}

@end
