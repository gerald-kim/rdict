//
//  ReviewSessionController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CardFrontViewController;
@class CardBackViewController;
@class Card;

@interface ReviewSessionController : UIViewController {
	CardFrontViewController* cardFrontViewController;
	CardBackViewController* cardBackViewController;

	NSUInteger cardsRemain;
	
	NSArray* cards;
	NSMutableArray* uncertainCards;
	Card* currentCard;
}

@property (nonatomic, retain) CardFrontViewController* cardFrontViewController;
@property (nonatomic, retain) CardBackViewController* cardBackViewController;
@property (nonatomic, retain) NSArray* cards;

- (IBAction) answerButtonClicked : (id) sender;
- (IBAction) scoreButtonClicked : (id) sender;	


@end
