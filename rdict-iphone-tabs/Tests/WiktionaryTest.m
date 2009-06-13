//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <TargetConditionals.h>
#import "GTMSenTestCase.h"

#import "Wiktionary.h"

@interface WiktionaryTest : SenTestCase {
	Wiktionary *wiktionary;
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation WiktionaryTest

- (void) setUp {
	wiktionary = [[Wiktionary alloc] init];
}

- (void) tearDown {
	[wiktionary release];
}

- (void) testInit {
	STAssertNotNULL( wiktionary.indexDb, nil ); 
	STAssertNotNULL( wiktionary.wordDb, nil ); 
	char* value = tcbdbget2(wiktionary.indexDb, "you");
	STAssertEqualStrings( @"you", [NSString stringWithUTF8String:value], nil );
	
	free( value );
	
}

- (void) testGetWiktionaryEntry {
	WiktionaryEntry *entry = [wiktionary getWiktionaryEntry:@"you"];
	STAssertNotNULL( entry, nil );
	STAssertEqualStrings( @"you", entry.lemma, nil );
	STAssertTrue( [entry.definitionHtml length] > 100, nil ); 
	
}

- (void) testGetEmptyWiktionaryEntry {
	WiktionaryEntry *entry = [wiktionary getWiktionaryEntry:@"youuuu"];
	STAssertNULL( entry, nil );
}

- (void) testJumpToFirstWord {
	NSInteger targetIndex = [wiktionary jumpToWord:@"a"];
	STAssertEquals( targetIndex, (NSInteger) 0, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"a", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"adolescent", nil );
}

- (void) testJumpToLastWord {
	NSInteger targetIndex = [wiktionary jumpToWord:@"zoo"];
	STAssertEquals( targetIndex, (NSInteger) 99, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"worry", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"zoo", nil );
}

- (void) testJumpToCloseToFirst {
	NSInteger targetIndex = [wiktionary jumpToWord:@"able"];
	STAssertEquals( targetIndex, (NSInteger) 9, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"a", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"adolescent", nil );
}

- (void) testJumpToMiddle {
	NSInteger targetIndex = [wiktionary jumpToWord:@"adolescent"];
	STAssertEquals( targetIndex, (NSInteger) 33, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"activity", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"al", nil );
}

@end

#endif