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
	
	[c calcEasinessByGrade: grade];
	
	STAssertEquals(expectedEasiness, c.easiness, nil);
	
	// If EF is less than 1.3 then let EF be 1.3.
	
	grade = 5;
	prevEasiness = 0.1;
	expectedEasiness = prevEasiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
	
	STAssertTrue( 1.3 > expectedEasiness, nil);
	
	c.easiness = 0.1;
	[c calcEasinessByGrade: grade];
	
	STAssertEquals((float) 1.3, c.easiness, nil);
	
	[c release];
}

-(void) testCalcEasinessByGradeLessThanThreeIgnoresEFAndResetsInterval {
	
	Card *c = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	int grade = 3;
	float prevEasiness = 2.5;
	float expectedEasiness = prevEasiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
	
	STAssertTrue( 1.3 < expectedEasiness, nil);
	
	[c calcEasinessByGrade: grade];
	
	STAssertEquals(expectedEasiness, c.easiness, nil);
	
	// If grade is less than 3, don't change EF and reset reps and interval
	
	grade = 2;
	prevEasiness = c.easiness;
	
	[c calcEasinessByGrade: grade];
	
	STAssertEquals(prevEasiness, c.easiness, nil);
	
	[c release];
}

@end
