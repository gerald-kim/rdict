//
//  DictionaryTest.m
//  RDict
//
//  Created by Stephen Bodnar on 27/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "DictionaryEntry.h"

@interface DictionaryEntryTest : SenTestCase {}
@end


@implementation DictionaryEntryTest

- (void) testInit {
	
	//Dictionary *dic = [[Dictionary alloc] init];
	

	DictionaryEntry *dicEntry = [[DictionaryEntry alloc] initWithWord: @"fish" andEntry: @"The entry appears here in HTML."];
	
	STAssertEqualObjects(@"fish", dicEntry.word, nil);
	STAssertEqualObjects(@"The entry appears here in HTML.", dicEntry.entry, nil);
	
	[dicEntry release];
}

@end
