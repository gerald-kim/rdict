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

#import <QuartzCore/QuartzCore.h>

static NSString* frontHelpMessage = @"Can you remeber this word?\nThink about the definition.\n\nWhen you remember, or if you decide you can't remember, push the 'Show Answer' button.";
static NSString* backHelpMessage = @"How easy was it to remember the word?\n\nTell Vocabulator by pressing one of the buttons.";

static NSString* statusLabelNMoreCards = @"%d more cards";
static NSString* statusLabelOneMoreCard = @"1 more card";
static NSString* statusLabelLastCard = @"Last card!";

@implementation ReviewSessionController

@synthesize helpMessage;
@synthesize statusLabel;
@synthesize statusArrow;
@synthesize flashcardViewPlaceholder;
@synthesize showAnswerButton;
@synthesize answerButtonGroup;
@synthesize useScheduledCards;

- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	
	[super viewDidLoad];
	
	cardFrontViewController = [[CardViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
	cardBackViewController = [[CardViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[super viewWillAppear:animated];
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];
	
	[self.flashcardViewPlaceholder addSubview:cardFrontViewController.view];
	[self.flashcardViewPlaceholder addSubview:cardBackViewController.view];
	
	if(self.useScheduledCards) {
		scheduledCards = [Card findByScheduled];
		NSLog(@"num scheduled cards to study: %d ", [scheduledCards count]);
	}
	else {
		scheduledCards = [Card findByToday];
		NSLog(@"num today cards to study: %d ", [scheduledCards count]);
	}
	
	[scheduledCards retain];
	
	uncertainCards = [[NSMutableArray alloc] init];
	
	[self initCards:scheduledCards];
	[self showCardFrontView];
}

- (void)viewWillDisappear:(BOOL) animated {
	[cardFrontViewController.view removeFromSuperview];
	[cardBackViewController.view removeFromSuperview];
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
	reviewCards = theCards;
	cardsRemain = [reviewCards count];
}

#pragma mark -
#pragma mark Answer, Score

- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.answerButtonClicked" );
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

	if ( 0 == cardsRemain) {
		if ( reviewCards == scheduledCards && 0 != [uncertainCards count] ) {
			[self showReviewUnfinishedView];
		} else {
			[self showReviewFinishedView];
			
		}  
	} else {
		[self showCardFrontView];
	}
}

- (IBAction) showHelpMesg : (id) sender {	
	UIAlertView* alert;
	
	alert = [[[UIAlertView alloc] initWithTitle:@"Help" message:self.helpMessage delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease];
	
	[alert show];
}

- (void)showCardFrontView {
	NSLog( @"RSC.viewWillAppear2" );
	currentCard = [reviewCards objectAtIndex:[reviewCards count] - cardsRemain];		
	NSLog( @"RSC.viewWillAppear3" );
	cardsRemain--;
	
	helpMessage = frontHelpMessage;
	self.showAnswerButton.hidden = NO;
	self.answerButtonGroup.hidden = YES;
	[self updateAndSwitchViewTo: cardFrontViewController];
}

- (void)showCardBackView {	
	helpMessage = backHelpMessage;
	self.showAnswerButton.hidden = YES;
	self.answerButtonGroup.hidden = NO;
	[self updateAndSwitchViewTo: cardBackViewController];
}

- (void) updateAndSwitchViewTo: (CardViewController*) newCardController {
	self.statusLabel.text = [self getStatusMesgAndSetStatusArrow];

	newCardController.questionLabel.text = currentCard.question;
	newCardController.answerTextView.text = currentCard.answer;
	
	if (! [self isOnFirstCard]) {
		if(cardFrontViewController == newCardController)
			[self prepareSlideAnimation];
		else
			[self prepareFlipAnimation];
	}
	
	[self.flashcardViewPlaceholder bringSubviewToFront:newCardController.view];
}

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

- (BOOL) isOnFirstCard {
	return [reviewCards count] == cardsRemain;
}

- (void) prepareSlideAnimation {
	CATransition *transition = [CATransition animation];
	transition.duration = 0.5;
	transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
	transition.type = kCATransitionPush;
	transition.subtype = kCATransitionFromRight;
	transition.delegate = self;
	
	[self.flashcardViewPlaceholder.layer addAnimation:transition forKey:nil];
}

- (void) prepareFlipAnimation {
	[UIView beginAnimations:@"View Flip" context:nil];
	[UIView setAnimationDuration:0.5];
	[UIView setAnimationCurve: UIViewAnimationCurveEaseInOut];
	[UIView setAnimationTransition: UIViewAnimationTransitionFlipFromRight forView: self.flashcardViewPlaceholder cache: YES];
	[UIView commitAnimations];	
}

#pragma mark -
#pragma mark ReviewFinish
- (void) showReviewUnfinishedView {
	reviewUnfinishedViewController = [[ReviewUnfinishedViewController alloc]initWithNibName:@"ReviewUnfinishedView" bundle:nil];
	reviewUnfinishedViewController.scheduledCards = scheduledCards;
	reviewUnfinishedViewController.uncertainCards = uncertainCards;
	
	[cardFrontViewController.view removeFromSuperview];
	[cardBackViewController.view removeFromSuperview];
	
	[self.view insertSubview:reviewUnfinishedViewController.view atIndex:0];
	[self.view bringSubviewToFront:reviewUnfinishedViewController.view];
}

- (IBAction) reviewAgainButtonClicked : (id) sender {
	//TODO shuffle uncertainCards
	[reviewUnfinishedViewController.view removeFromSuperview];
	[reviewUnfinishedViewController release];
	
	[self.flashcardViewPlaceholder addSubview:cardFrontViewController.view];
	[self.flashcardViewPlaceholder addSubview:cardBackViewController.view];	
	
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