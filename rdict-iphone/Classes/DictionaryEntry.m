//
//  DictionaryEntry.m
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "DictionaryEntry.h"


@implementation DictionaryEntry
@synthesize word;
@synthesize entry;

- (id) initWithWord: (NSString *) word_arg andEntry:(NSString *) entry_arg {
	
	self.word = [[NSString alloc] initWithString: word_arg];
	self.entry = [[NSString alloc] initWithString: entry_arg];
	
	
	return self;
}

- (void)dealloc {
	[self.word release];
	[self.entry release];
	[super dealloc];
}

@end
