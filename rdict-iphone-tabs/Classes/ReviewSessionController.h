//
//  ReviewSessionController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CardViewController;
@class ReviewUnfinishedViewController;
@class ReviewFinishedViewController;
@class Card;

@interface ReviewSessionController : UIViewController {
	
	CardViewController* cardFrontViewController;
	CardViewController* cardBackViewController;
	ReviewUnfinishedViewController* reviewUnfinishedViewController;
	ReviewFinishedViewController* reviewFinishedViewController;
	
	NSString* helpMessage;
	
	NSUInteger cardsRemain;
	
	NSArray* reviewCards;
	NSArray* scheduledCards;
	NSMutableArray* uncertainCards;
	Card* currentCard;
	
	BOOL useScheduledCards;
}

@property BOOL useScheduledCards;

- (IBAction) helpButtonClicked : (id) sender;
- (IBAction) answerButtonClicked : (id) sender;
- (IBAction) scoreButtonClicked : (id) sender;	
- (IBAction) reviewAgainButtonClicked : (id) sender;
- (IBAction) reviewCompleteButtonClicked : (id) sender;
- (IBAction) skipExtraPracticeButtonClicked : (id) sender;

- (void) showCardFrontView;
- (void) showCardBackView;
- (void) showReviewUnfinishedView;
- (void) showReviewFinishedView;
- (void) initCards:(NSArray*) theCards;
- (void) updateAndSwitchViewTo : (CardViewController*) newCardController;
//- (NSString*) getStatusMesgAndSetStatusArrow;

@end
