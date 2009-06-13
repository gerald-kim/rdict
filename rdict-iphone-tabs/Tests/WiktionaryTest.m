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

- (void) testJumpToWord {
	STAssertEqualStrings( [wiktionary jumpToWord:@"a"], @"a", nil );
	STAssertEqualStrings( [wiktionary jumpToWord:@"-"], @"a", nil );
	STAssertEqualStrings( [wiktionary jumpToWord:@"aah"], @"aah", nil );
	STAssertEqualStrings( [wiktionary jumpToWord:@"aaha"], @"aah", nil );
	STAssertEqualStrings( [wiktionary jumpToWord:@"zoo"], @"zoo", nil );
	STAssertEqualStrings( [wiktionary jumpToWord:@"zooh"], @"zoo", nil );
}

- (void) testFillWordList {
	NSUInteger targetIndex = [wiktionary fillWordList:@"zoo"];
	STAssertEquals( targetIndex, (NSUInteger) 99, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"worry", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"zoo", nil );
}

- (void) testJumpToCloseToFirst {
	NSUInteger targetIndex = [wiktionary fillWordList:@"able"];
	STAssertEquals( targetIndex, (NSUInteger) 9, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"a", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"adolescent", nil );
}

- (void) testJumpToMiddle {
	NSUInteger targetIndex = [wiktionary fillWordList:@"adolescent"];
	STAssertEquals( targetIndex, (NSUInteger) 33, nil );
	WiktionaryIndex *wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"activity", nil );
	wiktionaryIndex = [wiktionary.wordList objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"al", nil );
}


@end

#endif