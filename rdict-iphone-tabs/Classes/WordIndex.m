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

- (id) initWithKeyString:(const char *) keyArg andLemmaString:(const char*) lemmaArg {
	key = [[NSString alloc] initWithUTF8String:keyArg];
	lemma = [[NSString alloc] initWithUTF8String:lemmaArg];

	return self;
}

- (void) dealloc {
	[lemma release];
	[key release];
	[super dealloc];
}

@end
