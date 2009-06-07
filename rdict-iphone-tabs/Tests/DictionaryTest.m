//
//  DictionaryTest.m
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "DictionaryEntry.h"
#import "Dictionary.h"

@interface DictionaryTest : SenTestCase {}
@end

@implementation DictionaryTest

- (void) testSearch {
	
	Dictionary *dic = [[Dictionary alloc] initWithDicPath:@"/Users/sbodnar/programming/projects/old/iphone-rdict/RDict/en-brief.db"];
	
	DictionaryEntry *dicEntry = [dic searchByWord: @"fish"];
	
	fprintf(stderr, "%s\n", dicEntry.word);
	
	STAssertEqualObjects(@"fish", dicEntry.word, nil);
	
	[dicEntry release];
	[dic release];
}

@end
