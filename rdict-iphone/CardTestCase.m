//
//  CardTest.m
//  RDict
//
//  Created by Stephen Bodnar on 17/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "Card.h"

@interface CardTestCase : SenTestCase {}
@end

@implementation CardTestCase

-(void) testCard{
	Card *c = [[Card alloc] initWithQuestion:@"How big is it?" Answer:@"Very big."];
	
	STAssertEqualObjects(@"How big is it?", c.question, @"Something went wrong: %s");
	STAssertEqualObjects(@"Very big.", c.answer, nil);
	STAssertEquals(0, c.reps_since_lapse, nil);
	STAssertEquals((float) 2.5, c.easiness, nil);
	
	//self.assertEquals( date.today() + timedelta( days=1 ), c.scheduled )
	//STAssertEquals(today + 1, c.scheduled, nil);
	
	[c release];
}

@end
