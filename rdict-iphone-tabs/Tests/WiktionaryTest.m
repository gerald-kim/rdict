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

- (void) testInitialForwardListingStartsFromFirst {
	NSArray *list = [wiktionary listForward:nil];
	STAssertEquals( [list count], (NSUInteger) 10, nil );
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEqualStrings( idx.key, @"a", nil );
	idx = [list objectAtIndex:9];
	STAssertEqualStrings( idx.key, @"able", nil );
}

- (void) testInitialForwardListingShouldKeepLocation {
	[wiktionary listForward:nil];
	NSArray *list = [wiktionary listForward:nil];
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEquals( [list count], (NSUInteger) 10, nil );
	STAssertEqualStrings( idx.key, @"aboard", nil );
}

- (void) testForwardListingShouldStopAtLast {
	NSArray *list = [wiktionary listForward:@"zoe"];
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEquals( [list count], (NSUInteger) 4, nil );
	STAssertEqualStrings( idx.key, @"zoe", nil );
}

- (void) testBackwardListing {
	NSArray *list = [wiktionary listBackward:@"zoo"];
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEquals( [list count], (NSUInteger) 10, nil );
	STAssertEqualStrings( idx.key, @"zoo", nil );
}

- (void) testBackwardShouldStopAtFirst {
	NSArray *list = [wiktionary listBackward:@"abbey"];
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEquals( [list count], (NSUInteger) 5, nil );
	STAssertEqualStrings( idx.key, @"abbey", nil );
}

@end

#endif