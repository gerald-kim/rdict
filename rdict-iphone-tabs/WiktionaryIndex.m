//
//  WordIndex.m
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "WiktionaryIndex.h"

@implementation WiktionaryIndex
@synthesize key;
@synthesize lemma;

- (id) initWithUTF8KeyString:(char *) keyArg andUTF8LemmaString:(char*) lemmaArg {
	key = [NSString stringWithUTF8String:keyArg];
	lemma = [NSString stringWithUTF8String:lemmaArg];
	
	return self;
}

- (void) dealloc {
	[self.lemma release];
	[self.key release];
	[super dealloc];
}

@end
