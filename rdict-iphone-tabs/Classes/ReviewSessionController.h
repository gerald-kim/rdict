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
	IBOutlet UILabel* statusLabel;
	IBOutlet UIImageView* statusArrow;
	IBOutlet UIView* flashcardViewPlaceholder;
	IBOutlet UIButton* showAnswerButton;
	IBOutlet UIView* answerButtonGroup;
	
	CardViewController* cardFrontViewController;
	CardViewController* cardBackViewController;
	ReviewUnfinishedViewController* reviewUnfinishedViewController;
	ReviewFinishedViewController* reviewFinishedViewController;
	
	NSUInteger cardsRemain;
	
	NSArray* reviewCards;
	NSArray* scheduledCards;
	NSMutableArray* uncertainCards;
	Card* currentCard;
}

@property (nonatomic, retain) IBOutlet UILabel* statusLabel;
@property (nonatomic, retain) IBOutlet UIImageView* statusArrow;
@property (nonatomic, retain) IBOutlet UIView* flashcardViewPlaceholder;
@property (nonatomic, retain) IBOutlet UIButton* showAnswerButton;
@property (nonatomic, retain) IBOutlet UIView* answerButtonGroup;

- (IBAction) showHelpMesg : (id) sender;
- (IBAction) answerButtonClicked : (id) sender;
- (IBAction) scoreButtonClicked : (id) sender;	
- (IBAction) reviewAgainButtonClicked : (id) sender;
- (IBAction) reviewCompleteButtonClicked : (id) sender;

- (void) showCardFrontView;
- (void) showCardBackView;
- (void) showReviewUnfinishedView;
- (void) showReviewFinishedView;
- (void) initCards:(NSArray*) theCards;
- (void) updateAndSwitchFrom : (CardViewController*) oldCardController To : (CardViewController*) newCardController;
- (NSString*) getStatusMesgAndSetStatusArrow;
- (void) prepareAnimation;
- (BOOL) isOnFirstCard;

@end
