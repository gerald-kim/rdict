//
//  CardTest.m
//  RDict
//
//  Created by Stephen Bodnar on 17/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "Card.h"
#include <math.h>

@interface CardTestCase : SenTestCase {}
@end

@implementation CardTestCase

-(void) testInitWithQuestionAndAnswer{
	Card *c = [[Card alloc] initWithQuestion:@"How big is it?" Answer:@"Very big."];
	
	STAssertEqualObjects(@"How big is it?", c.question, @"Something went wrong: %s");
	STAssertEqualObjects(@"Very big.", c.answer, nil);
	STAssertEquals((float) 2.5, c.easiness, nil);
	STAssertEquals(0, c.repsSinceLapse, nil);
	STAssertEquals(-1, c.interval, nil);
	
	NSDate *expectedScheduled = [NSDate date];
	
	STAssertTrue( 1 > [expectedScheduled timeIntervalSinceDate: c.scheduled], nil);
	STAssertTrue( 0 <= [expectedScheduled timeIntervalSinceDate: c.scheduled], nil);
	
	//self.assertEquals( date.today() + timedelta( days=1 ), c.scheduled )
	//STAssertEquals(today + 1, c.scheduled, nil);
	
	[c release];
}

-(void) testInitWithCard{
	Card *c = [[Card alloc] initWithQuestion:@"How big is it?" Answer:@"Very big."];
	
	Card *c1 = [[Card alloc] initWithCard:c];
	
	STAssertTrue( c != c1, nil);
	STAssertEqualObjects(c.question, c1.question, @"Something went wrong: %s");
	STAssertEqualObjects(c.answer, c1.answer, nil);
	
	[c release];
}

-(void) testRounding {

	float twoPointTwo = 2.2;
	float twoPointSeven = 2.7;
	
	STAssertEquals(2, (int)twoPointTwo, nil);
	STAssertEquals(2, (int)twoPointSeven, nil);
	
	STAssertEquals(3, (int)ceil(twoPointSeven), nil);
}

-(void) testCalcInterval {
	Card *c = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	STAssertEquals(0, c.repsSinceLapse, nil);
	STAssertEquals(-1, c.interval, nil);
	
	[c calcInterval];
	
	STAssertEquals(1, c.interval, nil);
	
	c.repsSinceLapse += 1;
	[c calcInterval];
	
	STAssertEquals(6, c.interval, nil);
	
	c.repsSinceLapse += 1;
	c.easiness = 2.2;
	
	int prevInterval = c.interval;
	
	[c calcInterval];
	
	STAssertEquals( (int) ceil(prevInterval * c.easiness), c.interval, nil);
	
	[c release];
}

-(void) testCalcEasinessByGrade {
	
	Card *c = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	STAssertEquals((float)2.5, c.easiness, nil);
	
	// EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02))
	
	int grade = 4;
	float prevEasiness = 2.5;
	float expectedEasiness = prevEasiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
	
	STAssertTrue( 1.3 < expectedEasiness, nil);
	
	[c adjustEasinessByGrade: grade];
	
	STAssertEquals(expectedEasiness, c.easiness, nil);
	
	// If EF is less than 1.3 then let EF be 1.3.
	
	grade = 5;
	prevEasiness = 0.1;
	expectedEasiness = prevEasiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
	
	STAssertTrue( 1.3 > expectedEasiness, nil);
	
	c.easiness = 0.1;
	[c adjustEasinessByGrade: grade];
	
	STAssertEquals((float) 1.3, c.easiness, nil);
	
	[c release];
}

-(void) testCalcEasinessByGradeLessThanThreeIgnoresEFAndResetsInterval {
	
	// If grade is less than 3, don't change EF and reset reps and interval
	
	Card *c = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	int grade = 2;
	float prevEasiness = c.easiness;
	
	[c adjustEasinessByGrade: grade];
	
	STAssertEquals(prevEasiness, c.easiness, nil);
	STAssertEquals(1, c.interval, nil);
	
	[c release];
}

-(void) testNSDates {
	
	NSDate *today = [[NSDate alloc] init];
	NSDate *anotherToday = [[NSDate alloc] init];
	
	STAssertTrue( 1 > [anotherToday timeIntervalSinceDate: today], nil);
	STAssertTrue( 0 <= [anotherToday timeIntervalSinceDate: today], nil);
	
	NSDate *tomorrow = [[NSDate alloc] initWithTimeInterval: 60*60*24 sinceDate: today];
	
	NSTimeInterval expectedIntervalInSeconds = 60*60*24;
	NSTimeInterval interval = [tomorrow timeIntervalSinceDate: today];
	
	STAssertEquals(expectedIntervalInSeconds, interval, nil);
	
	[today release];
	[anotherToday release];
	[tomorrow release];
}

-(void) testSchedule {
	Card *c = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	NSDate *prevScheduled = c.scheduled;
	
	[c schedule];
	
	NSTimeInterval expectedSeconds = 60 * 60 * 24; // 1 day
	
	STAssertEquals( expectedSeconds, [c.scheduled timeIntervalSinceDate: prevScheduled], nil);
	[c release];
}

- (void) testLoadScheduledCards {	
	Card *cardForToday = [[Card alloc] initWithQuestion:@"today" Answer: @"the answer"];
	Card *cardFor19700101 = [[Card alloc] initWithQuestion:@"1970 baby yeah!" Answer: @"the answer"];
	cardFor19700101.scheduled = [NSDate dateWithTimeIntervalSince1970:0];
	Card *cardFor19700102 = [[Card alloc] initWithQuestion:@"1970 second day" Answer: @"the answer"];
	cardFor19700102.scheduled = [NSDate dateWithTimeIntervalSince1970:60*60*24];
	
	[cardForToday save];
	[cardFor19700101 save];
	[cardFor19700102 save];
	
	NSMutableArray *cards = [Card loadCardsByScheduledDate: cardFor19700101.scheduled];
	
	STAssertEquals((NSUInteger)1, [cards count], nil);
	
	Card *card = [cards lastObject];
	
	STAssertEquals(@"1970 baby yeah!", card.question, nil);
	
	cards = [Card loadCardsByScheduledDate: cardFor19700102.scheduled];
	
	STAssertEquals((NSUInteger)1, [cards count], nil);
	
	card = [cards lastObject];
	
	STAssertEquals(@"1970 second day", card.question, nil);
	
	[cardForToday deleteObject];
	[cardFor19700101 deleteObject];
	[cardFor19700102 deleteObject];
	
	[cardForToday release];
	[cardFor19700101 release];
	[cardFor19700102 release];
}

- (void) testLoadScheduledCardsButNoneScheduled {	
	Card *cardForToday = [[Card alloc] initWithQuestion:@"today" Answer: @"the answer"];

	NSDate *dateAt1970 = [NSDate dateWithTimeIntervalSince1970:0];
	
	[cardForToday save];
	
	NSMutableArray *cardsScheduled = [Card loadCardsByScheduledDate: dateAt1970];
	
	STAssertEquals((NSUInteger)0, [cardsScheduled count], nil);
		
	[cardForToday deleteObject];
	
	[cardForToday release];
	//[dateAt1970 release];
	[cardsScheduled release];
}

@end
