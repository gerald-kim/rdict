//
//  ScheduledOrTodayCardTest
//  RDict
//
//  Created by Jaewoo Kim on 2/2/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "Card.h"
#import <unistd.h>
#import <stdlib.h>

@interface ScheduledOrTodayCardTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation ScheduledOrTodayCardTest

-(void) testWhenNoCardsForReview {
	NSString *reviewMessage = [Card messageForReview];
	STAssertEqualStrings( @"None Available", reviewMessage, nil );
}

-(void) testMessageWhenNoScheduledAndToday {
	for (int i = 0; i < REVIEW_LIMIT; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"future" andAnswer:@"answer"];
		[expected save];		
	}	
	
	STAssertEqualStrings( @"Early Practice", [Card messageForReview], nil );
	STAssertEquals( (NSUInteger) REVIEW_LIMIT, [[Card cardsForReview] count], nil );
}

-(void) testMessageWhenScheduled {
	for (int i = 0; i < REVIEW_LIMIT; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"future" andAnswer:@"answer"];
		expected.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (i * SECONDS_IN_ONE_DAY)];
		[expected save];		
	}	
	
	STAssertEqualStrings( @"Scheduled", [Card messageForReview], nil );
	STAssertEquals( (NSUInteger) REVIEW_LIMIT, [[Card cardsForReview] count], nil );
}

-(void) testCountsInLimit {
	for (int i = 0; i < REVIEW_LIMIT; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"future" andAnswer:@"answer"];
		expected.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (i * SECONDS_IN_ONE_DAY)];
		[expected save];		
	}	
	
	NSString* expected = [NSString stringWithFormat:@"%d card(s)", REVIEW_LIMIT];
	STAssertEqualStrings( expected, [Card countMessageForReview], nil );
}

-(void) testCountsOutOfLimit {
	for (int i = 0; i < REVIEW_LIMIT + 1 ; i++) {
		Card* expected = [[Card alloc] initWithQuestion:@"future" andAnswer:@"answer"];
		expected.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (i * SECONDS_IN_ONE_DAY)];
		[expected save];		
	}	
	
	NSString* expected = [NSString stringWithFormat:@"%d of %d cards", REVIEW_LIMIT, REVIEW_LIMIT+1];
	STAssertEqualStrings( expected, [Card countMessageForReview], nil );
}




@end

#endif