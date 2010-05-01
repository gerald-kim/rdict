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
									   @"http://m.engdic.daum.net/dicen/mobile_search.do?endic_kind=all&m=all&q=", @"Daum Dictionary(English-Korean)",
									   @"http://i.word.com/idictionary/", @"Merriam-Webster.com",
									   @"http://mobile-dictionary.reverso.net/english-french/", @"Collins English French",
									   @"http://mobile-dictionary.reverso.net/english-italian/", @"Collins Italian Dictionary",	
									   @"http://mobile-dictionary.reverso.net/english-spanish/", @"Collins Spanish Dictionary",	
									   @"http://mobile-dictionary.reverso.net/english-german/", @"Collins German Dictionary",	
									   nil];
									   
	
	NSData *htmlData = [[NSData alloc] initWithContentsOfURL: [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"dictionary_view" ofType:@"html"]]];
	NSString *htmlString = [[NSString alloc] initWithData: htmlData encoding: NSUTF8StringEncoding];
	
	[definitionHtml insertString:htmlString atIndex: 0];
	[definitionHtml appendString:@"<h2>Another dictionaries' definition:</h2>"];
	[definitionHtml appendString:@"<ul>"];
	NSArray *sortedArray = [[otherDictionaries allKeys] sortedArrayUsingSelector:@selector(caseInsensitiveCompare:)];

	for (id key in sortedArray) {
		[definitionHtml appendString:[NSString stringWithFormat:
									  @"<li><a href='%@%@'>%@</a></li>", [otherDictionaries objectForKey:key], lemma, key]];
	}
	
	[lemma  stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

	[definitionHtml appendString:@"</ul>"];
	
	[definitionHtml appendString:@"<p style='color:gray;font-size:9pt'>This definition is retrived from : "];
	[definitionHtml appendString:[NSString stringWithFormat:@"<a href='http://en.wiktionary.com/wiki/%@'>Wiktionary - %@</a><br>", [lemma stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding], lemma]];
	[definitionHtml appendString:@"Text is available under the <a href='http://creativecommons.org/licenses/by-sa/3.0/'>CC-BY-SA License</a>.</p>"];
	
//	[definitionHtml appendString:@"<script type=\"text/javascript\" src=\"jquery-1.3.2.min.js\"></script>"];
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
