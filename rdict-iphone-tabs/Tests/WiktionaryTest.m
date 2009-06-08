//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "Wiktionary.h"

//index.db contents
//1 a
//2 aah
//3 abandon
//4 abandoned
//5 abbey
//6 abducted
//7 abigail
//8 abilities
//9 ability
//10 able
//11 aboard
//12 abort
//13 abortion
//14 about
//15 above
//16 absence
//17 absent
//18 absolute

@interface WiktionaryTest : SenTestCase {
	Wiktionary* wiktionary;
	
}

@end

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

- (void) testInitialForwardListingShouldStopAtLast {
	NSArray *list = [wiktionary listForward:@"zoe"];
	WiktionaryIndex *idx = [list objectAtIndex:0];
	STAssertEquals( [list count], (NSUInteger) 4, nil );
	STAssertEqualStrings( idx.key, @"zoe", nil );
}

@end
