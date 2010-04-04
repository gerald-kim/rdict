//
//  CardPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "Card.h"
#import <unistd.h>
#import <stdlib.h>

@interface CardSelectTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardSelectTest

-(void) populate {
	for (int i = 0; i < 5; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"past" andAnswer:@"answer"];
		expected.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (i * SECONDS_IN_ONE_DAY)];
		[expected save];		
	}
	for (int i = 1; i < 6; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"future" andAnswer:@"answer"];
		expected.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) + (i * SECONDS_IN_ONE_DAY)];
		[expected save];		
	}
}


-(void) testFindScheduledCards {
	STAssertEquals( 10, [Card count], @"Card count shouldn't be 0" );
	
	NSArray* scheduled = (NSArray*) [Card findByScheduled];
	STAssertEquals( [scheduled count], (NSUInteger) 05, nil );
	STAssertNotEqualStrings( @"future", ((Card*) [scheduled objectAtIndex:(NSUInteger)0]).question, nil );
	STAssertNotEqualStrings( @"future", ((Card*) [scheduled objectAtIndex:(NSUInteger)1]).question, nil );
	
	int scheduledCount = [Card countByScheduled];
	STAssertEquals( 5, scheduledCount, nil );
	
	NSArray* schedule = [Card reviewSchedulesWithLimit:2];
	STAssertEquals( (NSUInteger) 2, [schedule count], nil );
	NSArray* row = [schedule objectAtIndex:0];
	STAssertEqualStrings( @"1", [row objectAtIndex:1], nil );
	//	NSLog( @"%@ : %@", [row objectAtIndex:0], [row objectAtIndex:1] );	
	
//	NSLog( @"##################### Key-valuecoding test" );	
//	NSLog( @"avg: %@", [scheduled valueForKeyPath:@"@max.scheduled"] );
}

-(void) testFindCreatedTodayCards {
	Card* card = [[Card allObjects] objectAtIndex:0];
	[card study:3];
	STAssertEquals( 9, [Card countByCriteria:[Card todayCardCriteria]], nil);
}

@end

#endif