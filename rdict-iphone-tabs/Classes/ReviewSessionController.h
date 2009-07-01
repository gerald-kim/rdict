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
@class ReviewUnfinishedViewController;
@class ReviewFinishedViewController;
@class Card;

@interface ReviewSessionController : UIViewController {
	CardFrontViewController* cardFrontViewController;
	CardBackViewController* cardBackViewController;
	ReviewUnfinishedViewController* reviewUnfinishedViewController;
	ReviewFinishedViewController* reviewFinishedViewController;

	NSUInteger cardsRemain;
	
	NSArray* cards;
	NSArray* reviewCards;
	NSMutableArray* uncertainCards;
	Card* currentCard;
}

@property (nonatomic, retain) NSArray* reviewCards;

- (IBAction) answerButtonClicked : (id) sender;
- (IBAction) scoreButtonClicked : (id) sender;	
- (IBAction) reviewAgainButtonClicked : (id) sender;
- (IBAction) reviewCompleteButtonClicked : (id) sender;

@end
