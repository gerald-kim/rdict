//
//  CardReviewTest.m
//  RDict
//
//  Created by Jaewoo Kim on 7/2/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <TargetConditionals.h>
#import "GTMSenTestCase.h"
#import "Card.h"

@interface CardReviewTest : SenTestCase {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardReviewTest

-(void) testCardInitialStatus {
	Card* card = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	STAssertNotNil( card, nil );
	
	STAssertEquals( card.easiness, 2.5, nil );
	STAssertEquals( card.repsSinceLapse, 0, nil );
	STAssertNotNil( card.created, nil );
	STAssertNotNil( card.scheduled, nil );
	STAssertNil( card.studied, nil );
}

@end

#endif
