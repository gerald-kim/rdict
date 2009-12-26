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

@interface ReviewSessionController()

- (void) showCardFrontView;
- (void) showCardBackView;
- (void) showReviewUnfinishedView;
- (void) showReviewFinishedView;
- (void) initCards:(NSArray*) theCards;
- (void) updateAndSwitchToCardView : (CardViewController*) cardViewController;

@end

@implementation ReviewSessionController


- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	
	[super viewDidLoad];
	cardFrontViewController = [[CardViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
	[self.view insertSubview:cardFrontViewController.view atIndex:0];
	
	cardBackViewController = [[CardViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
	[self.view insertSubview:cardBackViewController.view atIndex:0];
	
	scheduledCards = [Card findByScheduled];
	[scheduledCards retain];
	uncertainCards = [[NSMutableArray alloc] init];
	[self initCards:scheduledCards];
	
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[super viewWillAppear:animated];
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];	
	
	[self showCardFrontView];
}	

- (void)viewDidDisappear:(BOOL) animated {
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload {
	[scheduledCards release]; scheduledCards = nil;
	[uncertainCards release]; 
	[cardFrontViewController release];
	[cardBackViewController release];
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void) initCards:(NSArray*) theCards {
	reviewCards = theCards;
	cardsRemain = [reviewCards count];
}

#pragma mark -
#pragma mark Answer, Score

- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.showAnswerButton" );
	//[self.navigationController popViewControllerAnimated:YES];
	[self showCardBackView];
}


- (IBAction) scoreButtonClicked : (id) sender {
	//TODO refactor scoreButtonClicked function. it's too complex
	UIButton *scoreButton = (UIButton*) sender;
	NSUInteger score = scoreButton.tag;
	
	if( reviewCards == scheduledCards ) {
		if( score <= 3 ) {
			[uncertainCards addObject:currentCard];
		}
		[currentCard study:score];
	}
	
	if ( 0 == cardsRemain ) {
		if ( reviewCards == scheduledCards && 0 != [uncertainCards count] ) {
			[self showReviewUnfinishedView];
		} else {
			[self showReviewFinishedView];
			
		}  
	} else {
		[self showCardFrontView];
	}
}

- (IBAction) frontHelpButtonClicked : (id) sender {	
	NSLog( @"RSC.frontHelpButton" );
	
	UIAlertView *alert = [[[UIAlertView alloc] initWithTitle:@"Help" message:@"Can you remeber this word?\nThink about the defintion.\n\nWhen you remember, or if you decide you can't remember, push the 'Show Answer' button." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease];
    [alert show];
}
		
- (IBAction) backHelpButtonClicked : (id) sender {	
	NSLog( @"RSC.backHelpButton" );
			
	UIAlertView *alert = [[[UIAlertView alloc] initWithTitle:@"Help" message:@"How easy was it to remember the word?\n\nTell Vocabulator by pressing one of the buttons." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease];
	[alert show];	
	}
			

- (void)showCardFrontView {
	currentCard = [reviewCards objectAtIndex:[reviewCards count] - cardsRemain];		
	cardsRemain--;
	
	[self updateAndSwitchToCardView:cardFrontViewController];
}

- (void)showCardBackView {	
	[self updateAndSwitchToCardView:cardBackViewController];
}

- (void) updateAndSwitchToCardView : (CardViewController*) cardViewController {
	//TODO change text when there's no remaining cards
	cardViewController.statusLabel.text = [NSString stringWithFormat:@"%d cards remain.", cardsRemain];
	cardViewController.questionLabel.text = currentCard.question;
	cardViewController.answerTextView.text = currentCard.answer;
	
	[self.view bringSubviewToFront:cardViewController.view];	
}

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
	[reviewFinishedViewController viewDidDisappear:TRUE];
	[reviewFinishedViewController.view removeFromSuperview];
	[reviewFinishedViewController release];
	
	[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
	[self.navigationController popViewControllerAnimated:YES];		
}

@end