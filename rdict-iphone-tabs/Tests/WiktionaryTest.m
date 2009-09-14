//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
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
	WordEntry *entry = [wiktionary wordEntryByLemma:@"you"];
	STAssertNotNULL( entry, nil );
	STAssertEqualStrings( @"you", entry.lemma, nil );
	STAssertTrue( [entry.definitionHtml length] > 100, nil ); 
	
}


- (void) testGetEmptyWiktionaryEntry {
	WordEntry *entry = [wiktionary wordEntryByLemma:@"youuuu"];
	STAssertNULL( entry, nil );
}


- (void) testJumpToWord {
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"a"].key, @"a", nil );	
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"-"].key, @"-'d", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"aah"].key, @"aah", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"aaha"].key, @"aah", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"zoo"].key, @"zoo", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"zooh"].key, @"zoo", nil );
}

- (void) testFillWordList {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"zoo"];
	NSLog( @"target Index %d", targetIndex );
	STAssertEquals( targetIndex, (NSUInteger) 82, nil );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"z", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"zzz", nil );
}

- (void) testJumpToCloseToFirst {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"able"];
	
	NSLog( @"target Index %d", targetIndex );
	STAssertEquals( targetIndex, (NSUInteger) 33, nil );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"abbreviation", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"absolut", nil );
}

- (void) testJumpToMiddle {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"adolescent"];
	STAssertEquals( targetIndex, (NSUInteger) 33, @"a" );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"administered", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"advertiser", nil );
}

@end

#endif