//
//  CardPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <TargetConditionals.h>
#import "GTMSenTestCase.h"
#import "Card.h"
#import <unistd.h>
#import <stdlib.h>
#import "SQLiteInstanceManager.h"

@interface CardPersistenceTest : SenTestCase {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardPersistenceTest

-(void) setUp {
	[[SQLiteInstanceManager sharedManager] setDatabaseFilepath:@"test.db"];	
}

-(void) tearDown {
	[[SQLiteInstanceManager sharedManager] deleteDatabase];
}

-(void) assertCardEquals:(Card*) expected actual:(Card*) actual {
	STAssertEqualStrings( expected.question, actual.question, nil );
	STAssertEqualStrings( expected.answer, actual.answer, nil );
	STAssertEquals( expected.repsSinceLapse, actual.repsSinceLapse, nil );
	STAssertEquals( expected.interval, actual.interval, nil );
	STAssertEquals( expected.easiness, actual.easiness, nil );
	STAssertEquals( expected.grade, actual.grade, nil );
	STAssertEquals( expected.finalGrade, actual.finalGrade, nil );
	STAssertEquals( expected.scheduled, actual.scheduled, nil );
	STAssertEquals( expected.created, actual.created, nil );
	STAssertEquals( expected.studied, actual.studied, nil );	
}	
	
-(void) testCardCRD {
	Card* expected = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	STAssertFalse( [expected existsInDB], nil );
	[expected save];
	STAssertTrue( [expected existsInDB], nil );

	Card* actual = (Card*) [Card findByPK:expected.pk];
	[self assertCardEquals:expected actual:actual];
	
	expected.question = @"new question";
	expected.question = @"new answre";
	expected.repsSinceLapse = 33;
	expected.easiness = 2.55;
	expected.interval = 3;
	expected.grade = 4;
	expected.finalGrade = 5;
	expected.scheduled = [[NSDate alloc] init];
	expected.studied = [[NSDate alloc] init];
	[expected save];
	
	actual = (Card*) [Card findByPK:expected.pk];
	[self assertCardEquals:expected actual:actual];
}

-(void) testFindScheduledCards {
	Card* expected1 = [[Card alloc] initWithQuestion:@"question 1" andAnswer:@"answer"];
	//TODO Check timeinterval calc
	expected1.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (SECONDS_IN_ONE_DAY)];
	NSLog( @"expected1.scheduled : %@", expected1.scheduled );
	[expected1 save];
	Card* expected2 = [[Card alloc] initWithQuestion:@"question 2" andAnswer:@"answer"];
	expected2.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) - (SECONDS_IN_ONE_DAY*2)];
	NSLog( @"expected2.scheduled : %@", expected2.scheduled );	
	[expected2 save];
	Card* expected3 = [[Card alloc] initWithQuestion:@"question 3" andAnswer:@"answer"];
	expected3.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) + (SECONDS_IN_ONE_DAY)];
	[expected3 save];
	
	NSArray* scheduled = (NSArray*) [Card findByScheduled];
	STAssertEquals( (NSUInteger) 2, [scheduled count], nil );
	STAssertEqualStrings( @"question 1", ((Card*) [scheduled objectAtIndex:(NSUInteger)0]).question, nil );
	STAssertEqualStrings( @"question 2", ((Card*) [scheduled objectAtIndex:(NSUInteger)1]).question, nil );
	
	int scheduledCount = [Card countByScheduled];
	STAssertEquals( 2, scheduledCount, nil );
	
	NSArray* schedule = [Card reviewSchedulesWithLimit:2];
	STAssertEquals( (NSUInteger) 1, [schedule count], nil );
	NSArray* row = [schedule objectAtIndex:0];
	STAssertEqualStrings( @"1", [row objectAtIndex:1], nil );
//	NSLog( @"%@ : %@", [row objectAtIndex:0], [row objectAtIndex:1] );	
}

-(void) testFindSearchedTodayCards {
	Card* expected1 = [[Card alloc] initWithQuestion:@"question 1" andAnswer:@"answer"];
	[expected1 save];

	//TODO fix this
	STAssertEquals( 0, [Card countByCriteria:[Card searchedTodayCriteria]], nil);
	
}

@end

#endif