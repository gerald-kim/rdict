//
//  WiktionaryEntry.m
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "WordEntry.h"


@implementation WordEntry
@synthesize lemma;
@synthesize definitionHtml;

- (id) initWithLemma:(NSString *) lemmaArg andDefinitionHtml:(NSString *) definitionHtmlArg {
	self.lemma = lemmaArg;
	self.definitionHtml = [NSMutableString stringWithString: definitionHtmlArg];
	
	return self;
}

- (void) decorateDefinition {
	NSData *htmlData = [[NSData alloc] initWithContentsOfURL: [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"dictionary_view" ofType:@"html"]]];
	NSString *htmlString = [[NSString alloc] initWithData: htmlData encoding: NSUTF8StringEncoding];
	
	[definitionHtml insertString:htmlString atIndex: 0];
//	[definitionHtml appendString:@"</body></html>"];
	
	[htmlString release];
	[htmlData release];
}

- (void)dealloc {
	[lemma release];
	[definitionHtml release];
	[super dealloc];
}

@end
