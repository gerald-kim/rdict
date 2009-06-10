//
//  WiktionaryEntry.m
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "WiktionaryEntry.h"


@implementation WiktionaryEntry
@synthesize lemma;
@synthesize definitionHtml;

- (id) initWithLemma:(NSString *) lemmaArg andDefinitionHtml:(NSString *) definitionHtmlArg {
	lemma = lemmaArg;
	definitionHtml = [NSMutableString stringWithString: definitionHtmlArg];
	return self;
}

- (void)dealloc {
	[self.lemma release];
	[self.definitionHtml release];
	[super dealloc];
}

@end
