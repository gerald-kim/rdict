//
//  CardManagerTest.m
//  RDict
//
//  Created by Stephen Bodnar on 21/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "Card.h"
#import "CardManager.h"

@interface CardManagerTest : SenTestCase {}
@end

@implementation CardManagerTest

-(void) testAddCard{
	
	CardManager *cMgr = [[CardManager alloc] init];
	
	STAssertEquals((NSUInteger) 0, [cMgr.cards count], nil);
		
	Card *card = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	[cMgr addCard:card];
	
	STAssertEquals((NSUInteger) 1, [cMgr.cards count], nil);
	
	[card release];
	[cMgr release];
}

-(void) testAddAndLoad{
	
	CardManager *cMgr = [[CardManager alloc] init];
	
	NSArray *cards = [cMgr loadCards];
	
	STAssertEquals((NSUInteger) 0, [cards count], nil);
	
	[cards release];
	
	Card *card = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	[cMgr addCard: card];
	
	cards = [cMgr loadCards];
	
	STAssertEquals((NSUInteger) 1, cards.count, nil);
	STAssertEqualObjects(@"How big?", ((Card *)[cards objectAtIndex:0]).question, nil);
	
	[card release];
	[cards release];
	[cMgr release];
}

-(void) testRemoveCard{
	
	CardManager *cMgr = [[CardManager alloc] init];
	
	STAssertEquals((NSUInteger) 0, [cMgr.cards count], nil);
	
	Card *card = [[Card alloc] initWithQuestion:@"How big?" Answer:@"Big."];
	
	[cMgr addCard:card];
	
	STAssertEquals((NSUInteger) 1, [cMgr.cards count], nil);
	
	[cMgr removeCard:0];
	
	STAssertEquals((NSUInteger) 0, [cMgr.cards count], nil);
	
	[card release];
	[cMgr release];
}
	
@end
