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
	NSDictionary* otherDictionaries = [NSDictionary dictionaryWithObjectsAndKeys:
									   @"http://m.engdic.daum.net/dicen/mobile_search.do?endic_kind=all&m=all&q=", @"Daum Dictionary",
									   @"http://www.google.com/dictionary?langpair=en%7Cen&q=", @"Google Dictionary",
									   @"http://dictionary.reference.com/browse/", @"Dictionary.com",
									   @"http://www.urbandictionary.com/iphone/search?term=", @"Urban Dictionary",
									   nil];
									   
	
	NSData *htmlData = [[NSData alloc] initWithContentsOfURL: [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"dictionary_view" ofType:@"html"]]];
	NSString *htmlString = [[NSString alloc] initWithData: htmlData encoding: NSUTF8StringEncoding];
	
	[definitionHtml insertString:htmlString atIndex: 0];
	[definitionHtml appendString:@"<h2>Other dictionaries definition.</h2>"];
	[definitionHtml appendString:@"<ul>"];
	for (id key in otherDictionaries) {
		[definitionHtml appendString:[NSString stringWithFormat:
									  @"<li><a href='%@%@'>%@</a></li>", [otherDictionaries objectForKey:key], lemma, key]];
	}
	
	[lemma  stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

	[definitionHtml appendString:@"</ul>"];
	[definitionHtml appendString:@"</body></html>"];
	
	[htmlString release];
	[htmlData release];
}

- (void)dealloc {
	[lemma release];
	[definitionHtml release];
	[super dealloc];
}

@end
