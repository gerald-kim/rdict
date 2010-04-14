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
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"-"].key, @"-age", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"aah"].key, @"aaa", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"aaha"].key, @"aaa", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"zoo"].key, @"zoo", nil );
	STAssertEqualStrings( [wiktionary findIndexByQuery:@"zooh"].key, @"zoo", nil );
}

- (void) testFillWordList {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"zoo"];
	DebugLog( @"target Index %d", targetIndex );
	STAssertEquals( targetIndex, (NSUInteger) 90, nil );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"yesterday", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"zydeco", nil );
}

- (void) testJumpToCloseToFirst {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"-an"];
	
	DebugLog( @"target Index %d", targetIndex );
	STAssertEquals( targetIndex, (NSUInteger) 6, nil );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"'em", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"abandon", nil );
}

- (void) testJumpToMiddle {
	NSUInteger targetIndex = [wiktionary fillIndexesByKey:@"adolescent"];
	STAssertEquals( targetIndex, (NSUInteger) 33, @"a" );
	WordIndex *wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)0];
	STAssertEqualStrings( wiktionaryIndex.key, @"adjoin", nil );
	wiktionaryIndex = [wiktionary.wordIndexes objectAtIndex:(NSUInteger)99];
	STAssertEqualStrings( wiktionaryIndex.key, @"aerogramme", nil );
}

@end

#endif