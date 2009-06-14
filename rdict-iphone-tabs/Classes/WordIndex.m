//
//  WordIndex.m
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "WordIndex.h"

@implementation WordIndex
@synthesize key;
@synthesize lemma;

- (id) initWithKeyString:(char *) keyArg andLemmaString:(char*) lemmaArg {
	key = [[NSString alloc] initWithUTF8String:keyArg];
	lemma = [[NSString alloc] initWithUTF8String:lemmaArg];
	
	return self;
}

- (void) dealloc {
	[self.lemma release];
	[self.key release];
	[super dealloc];
}

@end
