//
//  CardReviewTest.m
//  RDict
//
//  Created by Jaewoo Kim on 7/2/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <TargetConditionals.h>
#import "SQLiteInstanceManager.h"
#import "GTMSenTestCase.h"
#import "Card.h"
#import "StudyLog.h"

@interface CardStudyTest : SenTestCase {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardStudyTest

-(void) setUp {
	[[SQLiteInstanceManager sharedManager] deleteDatabase];
}

-(void) testSaveNewCard {
	STAssertEquals( 0, [Card count], nil );
	[Card saveCardWithQuestion:@"question" andAnswer:@"answer"];

	STAssertEquals( 1, [Card count], nil );
}

-(void) testSaveExistingCardAgain {
	STAssertEquals( 0, [Card count], nil );
	[Card saveCardWithQuestion:@"question" andAnswer:@"answer"];
	STAssertEquals( 1, [Card count], nil );

	[Card saveCardWithQuestion:@"question" andAnswer:@"new answer"];
	STAssertEquals( 1, [Card count], nil );

}

-(void) testCardInitialStatus {
	Card* card = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	STAssertNotNil( card, nil );
	
	STAssertEquals( card.easiness, 2.5, nil );
	STAssertEquals( card.repsSinceLapse, 0, nil );
	STAssertNotNil( card.created, nil );
	STAssertNotNil( card.scheduled, nil );
	STAssertNil( card.studied, nil );
}

-(void) testForgot {
	Card* card = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	double oldEasiness = card.easiness;
	[card study:0];
	STAssertEquals( oldEasiness, card.easiness, nil );
	STAssertNotNil( card.studied, nil );	
}

-(void) testMinimalEasinessShouldGreaterThen1_3 {
	Card* card = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	card.easiness = 1.31;
	[card study:3];
	STAssertEquals( 1.3, card.easiness, nil );
}

-(void) testStudyLogShouldSaved {
	Card* card = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	[card study:3];
	
	STAssertEquals( [StudyLog count], 1, nil );
	StudyLog* firstLog = [StudyLog lastStudyLogOfCard:card];
	STAssertEquals( firstLog.studyIndex, (NSUInteger) 0, nil );
//	NSInteger pk = [firstLog pk];
//	[firstLog release];
//	[firstLog dealloc];
	
	[card study:4];
	StudyLog* secondLog = [StudyLog lastStudyLogOfCard:card];
	STAssertEquals( firstLog.studyIndex, (NSUInteger) 1, nil );
	STAssertEquals( secondLog.studyIndex, (NSUInteger) 0, nil );
}

@end

#endif
