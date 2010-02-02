//
//  ReviewSessionController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "ReviewSessionController.h"
#import "CardViewController.h"
#import "ReviewUnfinishedViewController.h"
#import "ReviewFinishedViewController.h"
#import "Card.h"

@implementation ReviewSessionController

@synthesize scheduledCards;

- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	
	[super viewDidLoad];
	
	cardFrontViewController = [[CardViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
	cardBackViewController = [[CardViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
	
	[self.view insertSubview:cardFrontViewController.view atIndex:0];
	[self.view insertSubview:cardBackViewController.view atIndex:0];
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[super viewWillAppear:animated];
		
	uncertainCards = [[NSMutableArray alloc] init];
	
	[self initCards:scheduledCards];
	[self showCardFrontView];
}

- (void)viewDidUnload {
	[cardFrontViewController release];
	[cardBackViewController release];
	
	[scheduledCards release]; scheduledCards = nil;
	[uncertainCards release];
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void) initCards:(NSArray*) theCards {
	cardsForReview = theCards;
	cardsRemain = [cardsForReview count];
}

#pragma mark -
#pragma mark Answer, Score

- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.answerButtonClicked" );
	[self showCardBackView];
}

- (IBAction) scoreButtonClicked : (id) sender {
	NSLog( @"RSC.scoreButtonClicked" );
	//TODO refactor scoreButtonClicked function. it's too complex
	UIButton *scoreButton = (UIButton*) sender;

	NSUInteger score = scoreButton.tag;
	
	if( cardsForReview == scheduledCards ) {
		if( score <= 3 ) {
			[uncertainCards addObject:currentCard];
		}
		[currentCard study:score];
	}

	if ( 0 == cardsRemain) {
		if ( cardsForReview == scheduledCards && 0 != [uncertainCards count] ) {
			[self showReviewUnfinishedView];
		} else {
			[self showReviewFinishedView];
			
		}  
	} else {
		[self showCardFrontView];
	}
}

- (IBAction) helpButtonClicked : (id) sender {	
	UIAlertView* alert;
	
	alert = [[[UIAlertView alloc] initWithTitle:@"Help" message:@"Help message" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease];
	
	[alert show];
}

- (void)showCardFrontView {
	NSLog( @"RSC.viewWillAppear2" );
	currentCard = [cardsForReview objectAtIndex:[cardsForReview count] - cardsRemain];		
	NSLog( @"RSC.viewWillAppear3" );
	cardsRemain--;
	
	[self updateAndSwitchViewTo: cardFrontViewController];
}

- (void)showCardBackView {	
//	helpMessage = backHelpMessage;
	[self updateAndSwitchViewTo: cardBackViewController];
}

- (void) updateAndSwitchViewTo: (CardViewController*) cardViewController {
//	self.statusLabel.text = [self getStatusMesgAndSetStatusArrow];

	cardViewController.questionLabel.text = currentCard.question;
	cardViewController.answerTextView.text = currentCard.answer;
	
	[self.view bringSubviewToFront:cardViewController.view];
}

/*
- (NSString*) getStatusMesgAndSetStatusArrow {
	if(cardsRemain > 1) {
		self.statusArrow.hidden = NO;
		return [NSString stringWithFormat:statusLabelNMoreCards, cardsRemain];
	}
	else if (cardsRemain == 1) {
		self.statusArrow.hidden = NO;
		return [NSString stringWithFormat:statusLabelOneMoreCard, cardsRemain];
	}
	else {
		self.statusArrow.hidden = YES;
		return [NSString stringWithFormat:statusLabelLastCard, cardsRemain];
	}
}
*/

#pragma mark -
#pragma mark ReviewFinish
- (void) showReviewUnfinishedView {
	reviewUnfinishedViewController = [[ReviewUnfinishedViewController alloc]initWithNibName:@"ReviewUnfinishedView" bundle:nil];
	reviewUnfinishedViewController.scheduledCards = scheduledCards;
	reviewUnfinishedViewController.uncertainCards = uncertainCards;
	
	[self.view insertSubview:reviewUnfinishedViewController.view atIndex:0];
	[self.view bringSubviewToFront:reviewUnfinishedViewController.view];
}

- (IBAction) reviewAgainButtonClicked : (id) sender {
	//TODO shuffle uncertainCards
	[reviewUnfinishedViewController.view removeFromSuperview];
	[reviewUnfinishedViewController release];
		
	[self initCards:uncertainCards];
	[self showCardFrontView];
}

- (void) showReviewFinishedView {
	reviewFinishedViewController = [[ReviewFinishedViewController alloc]initWithNibName:@"ReviewFinishedView" bundle:nil];
	reviewFinishedViewController.scheduledCards = scheduledCards;
	
	[self.view insertSubview:reviewFinishedViewController.view atIndex:0];
	[self.view bringSubviewToFront:reviewFinishedViewController.view];
	[reviewFinishedViewController viewWillAppear:TRUE];
	
}

- (IBAction) reviewCompleteButtonClicked : (id) sender {
	[reviewFinishedViewController.view removeFromSuperview];
	[reviewFinishedViewController release];
	
	[self.navigationController popViewControllerAnimated:YES];		
}

- (IBAction) skipExtraPracticeButtonClicked : (id) sender {
	[reviewUnfinishedViewController.view removeFromSuperview];
	[reviewUnfinishedViewController release];
	
	[self.navigationController popViewControllerAnimated:YES];			
}

@end
