//
//  CardManager.m
//  RDict
//
//  Created by Stephen Bodnar on 21/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Card.h"
#import "CardManager.h"

@implementation CardManager
@synthesize cards;

- (id) init {
	self.cards = [[NSMutableArray alloc] init];
	return self;
}

- (void) addCard:(Card *) card {
	Card *newCard = [[Card alloc] initWithCard: card];
	
	[self.cards addObject: newCard];
	
	[newCard retain];
}

- (void) removeCard: (int) index {
	[self.cards removeObjectAtIndex:index];
}

- (NSArray *) loadCards {
	NSArray *allCards = [[NSArray alloc] initWithArray: self.cards];
	[allCards retain];
	return allCards;
}


- (void)dealloc {
    [self.cards release];
	[super dealloc];
}

@end
