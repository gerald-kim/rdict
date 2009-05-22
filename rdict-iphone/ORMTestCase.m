//
//  ORMTestCase.m
//  RDict
//
//  Created by Stephen Bodnar on 21/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SQLiteInstanceManager.h"
#import "GTMSenTestCase.h"
#import "Card.h"

@interface ORMTestCase : SenTestCase {
	SQLiteInstanceManager* _manager;
}
@end

@implementation ORMTestCase

-(id) init
{
	_manager = [SQLiteInstanceManager sharedManager];
	[_manager deleteDatabase];
	return self;
}

- (void) testSaveAndLoadACard {
	
	Card *c = [[Card alloc] initWithQuestion:@"How big is it?" Answer:@"Very big."];
	[c save];
	
	NSArray *cards = [Card allObjects];
	
	STAssertEquals((NSUInteger) 1, [cards count], nil);
	
	Card *card = [cards objectAtIndex:0];
	
	STAssertEqualObjects(@"How big is it?", card.question, nil);
	
	[c deleteObject];
	
	[c release];
}

@end
