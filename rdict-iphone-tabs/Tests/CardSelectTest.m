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
	Card* expected1 = [[Card alloc] initWithQuestion:@"question 1" andAnswer:@"answer"];
	//TODO Check timeinterval calc
	expected1.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (SECONDS_IN_ONE_DAY)];
	NSLog( @"expected1.scheduled : %@", expected1.scheduled );
	[expected1 save];
	Card* expected2 = [[Card alloc] initWithQuestion:@"question 2" andAnswer:@"answer"];
	expected2.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (SECONDS_IN_ONE_DAY*2)];
	NSLog( @"expected2.scheduled : %@", expected2.scheduled );	
	[expected2 save];
	Card* expected3 = [[Card alloc] initWithQuestion:@"tomorrow" andAnswer:@"answer"];
	expected3.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) + (SECONDS_IN_ONE_DAY)];
	[expected3 save];
}


-(void) testFindScheduledCards {
	STAssertEquals( 3, [Card count], @"Card count should be 0" );
	
	
	NSArray* scheduled = (NSArray*) [Card findByScheduled];
	STAssertEquals( (NSUInteger) 2, [scheduled count], nil );
	STAssertNotEqualStrings( @"tomorrow", ((Card*) [scheduled objectAtIndex:(NSUInteger)0]).question, nil );
	STAssertNotEqualStrings( @"tomorrow", ((Card*) [scheduled objectAtIndex:(NSUInteger)1]).question, nil );
	
	int scheduledCount = [Card countByScheduled];
	STAssertEquals( 2, scheduledCount, nil );
	
	NSArray* schedule = [Card reviewSchedulesWithLimit:2];
	STAssertEquals( (NSUInteger) 1, [schedule count], nil );
	NSArray* row = [schedule objectAtIndex:0];
	STAssertEqualStrings( @"1", [row objectAtIndex:1], nil );
	//	NSLog( @"%@ : %@", [row objectAtIndex:0], [row objectAtIndex:1] );	
	
//	NSLog( @"##################### Key-valuecoding test" );	
//	NSLog( @"avg: %@", [scheduled valueForKeyPath:@"@max.scheduled"] );
}

-(void) testFindCreatedTodayCards {
	Card* card = [[Card allObjects] objectAtIndex:0];
	[card study:3];
	STAssertEquals( 2, [Card countByCriteria:[Card searchedTodayCriteria]], nil);
}

@end

#endif