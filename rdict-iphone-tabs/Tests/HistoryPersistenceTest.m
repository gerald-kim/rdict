//
//  HistoryPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "History.h"

@interface HistoryPersistenceTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR
#define SECONDS_IN_ONE_DAY 60*60*24

@implementation HistoryPersistenceTest

-(void) assertHistoryEquals:(History*) expected actual:(History*) actual {
	STAssertEqualStrings( expected.lemma, actual.lemma, nil );
	STAssertEquals( expected.created, actual.created, nil );
}	

-(void) testHistoryCRD {
	History* expected = [[History alloc] initWithLemma:@"question"];
	STAssertFalse( [expected existsInDB], nil );
	[expected save];
	STAssertTrue( [expected existsInDB], nil );
	
	History* actual = (History*) [History findByPK:expected.pk];
	[self assertHistoryEquals:expected actual:actual];
}

-(void) loadData {
	for (int i = 0; i<10; i++) {
		History* history = [[History alloc] initWithLemma:[NSString stringWithFormat:@"lemma %d", i]];
		if ( i < 5 ) {
			history.created = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (SECONDS_IN_ONE_DAY*(10-i))];			
		}

		[history save];
	}		
}

-(void) testFindRecents {
	[self loadData];
	
	NSArray* list = [History findRecents];
	STAssertEquals( [list count], (NSUInteger) 10, nil );
}

-(void) testBuildCountPerDateDictionaryWithEmptyData {
//	[self loadData];
	
	NSArray* list = [History findRecents];	
	NSMutableDictionary* d = [History buildHistorySectionInfo:list];
	
	STAssertEquals( [d count], (NSUInteger) 1, nil );
	STAssertEquals( [d valueForKey:@"sectionCount"], [NSNumber numberWithInt:0], nil );
}

-(void) testBuildCountPerDateDictionary {
	[self loadData];
	
	NSArray* list = [History findRecents];	
	NSMutableDictionary* d = [History buildHistorySectionInfo:list];

	STAssertEquals( [d count], (NSUInteger) 13, nil );
	STAssertEquals( [d valueForKey:@"sectionCount"], [NSNumber numberWithInt:6], nil );
	
	NSString* firstSectionTitle = [d objectForKey:@"0"];
	STAssertEquals( [d valueForKey:firstSectionTitle], [NSNumber numberWithInt:5], nil );
}

@end

#endif