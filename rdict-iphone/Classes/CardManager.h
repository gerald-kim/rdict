//
//  CardManager.h
//  RDict
//
//  Created by Stephen Bodnar on 21/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

@interface CardManager : NSObject {
	NSMutableArray *cards;
}

@property (nonatomic, retain) NSMutableArray *cards;

- (void) addCard:(Card *) card;
- (NSArray *) loadCards;
- (void) removeCard:(int) index;

@end
