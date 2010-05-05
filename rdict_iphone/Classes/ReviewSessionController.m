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
#import "RDictAppDelegate.h"
#import <QuartzCore/QuartzCore.h>


@implementation ReviewSessionController

static NSString* backHelpMessage = @"5 - correct answer; very easy\n"
"4 - correct answer after brief pause\n"
"3 - correct answer, needed time to recall\n"
"2 - incorrect answer, easily recognized correct one\n"
"1 - incorrect answer; difficult to remember correct one\n"
"0 - completely forget";

@synthesize scheduledCards;

#define LABEL_COUNT_DOWN 1

- (void) initCountLabel {
  UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
	btn.frame = CGRectMake(0, 0, 70, 30);
	
	countLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 70, 30)];
	countLabel.tag = LABEL_COUNT_DOWN;
	countLabel.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.0];
	countLabel.font = [UIFont boldSystemFontOfSize:12];
	countLabel.adjustsFontSizeToFitWidth = NO;
	countLabel.textAlignment = UITextAlignmentRight;
	countLabel.textColor = [UIColor whiteColor];
	countLabel.lineBreakMode = UILineBreakModeWordWrap;
	countLabel.numberOfLines = 2;
	countLabel.text = @"last card";
	countLabel.highlightedTextColor = [UIColor whiteColor];
	[btn addSubview:countLabel];
//	[countLabel release];

	UIBarButtonItem *modalBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btn];
	self.navigationItem.rightBarButtonItem = modalBarButtonItem;
}

- (void)viewDidLoad {
	DebugLog( @"RSC.viewDidLoad" );
	
	[super viewDidLoad];
	self.title = @"Review Exercise-100/100";
	
	
	cardFrontViewController = [[CardViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
	cardBackViewController = [[CardViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
	reviewUnfinishedViewController = [[ReviewUnfinishedViewController alloc]initWithNibName:@"ReviewUnfinishedView" bundle:nil];
	reviewFinishedViewController = [[ReviewFinishedViewController alloc]initWithNibName:@"ReviewFinishedView" bundle:nil];
	
	UIImage *backgroundImage = [UIImage imageNamed:@"contents_bg.png"];
	UIColor *backgroundColor = [[UIColor alloc] initWithPatternImage:backgroundImage];
	cardFrontViewController.view.backgroundColor = backgroundColor;
	cardBackViewController.view.backgroundColor = backgroundColor;
	reviewUnfinishedViewController.view.backgroundColor = backgroundColor;
	reviewFinishedViewController.view.backgroundColor = backgroundColor;
	[backgroundColor release];
	
	cardBackViewController.answerTextView.layer.cornerRadius = 10.0;
	cardBackViewController.answerTextView.layer.borderColor = [[UIColor grayColor] CGColor];
	cardBackViewController.answerTextView.layer.borderWidth = 1;
	reviewUnfinishedViewController.tableView.layer.cornerRadius = 10.0;
	reviewUnfinishedViewController.tableView.layer.borderColor = [[UIColor grayColor] CGColor];
	reviewUnfinishedViewController.tableView.layer.borderWidth = 1;
	
	[self.view insertSubview:cardFrontViewController.view atIndex:0];
	[self.view insertSubview:cardBackViewController.view atIndex:0];

	
	[self initCountLabel];

}

- (void)viewWillAppear:(BOOL) animated {
	DebugLog( @"RSC.viewWillAppear" );
	[super viewWillAppear:animated];
	[(RDictAppDelegate*) [[UIApplication sharedApplication] delegate] updateReviewTabAndBadge];
		
	uncertainCards = [[NSMutableArray alloc] init];
	
	[self initCards:scheduledCards];
	[self showCardFrontView];
}

- (void)viewDidDisappear:(BOOL)animated {
	[scheduledCards release]; scheduledCards = nil;
	[uncertainCards release];
	
}

- (void)viewDidUnload {
	[cardFrontViewController release];
	[cardBackViewController release];	
	[reviewUnfinishedViewController release];
	[reviewFinishedViewController release];	
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
	DebugLog( @"RSC.answerButtonClicked" );
	[self showCardBackView];
}

- (IBAction) scoreButtonClicked : (id) sender {
	DebugLog( @"RSC.scoreButtonClicked" );
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
//	UIAlertView* alert;
	
//	alert = [[[UIAlertView alloc] initWithTitle:@"Help" message:backHelpMessage delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease];
	//	[alert show];

	UIAlertView *testAlert = [[UIAlertView alloc] initWithTitle:@"Help" message:backHelpMessage delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[testAlert show];
	
	((UILabel*)[[testAlert subviews] objectAtIndex:1]).textAlignment = UITextAlignmentLeft;
	
	
}

- (void)showCardFrontView {
	DebugLog( @"RSC.viewWillAppear2" );
	
	self.title = @"Question";

	currentCard = [cardsForReview objectAtIndex:[cardsForReview count] - cardsRemain];		
	cardsRemain--;

	if (cardsRemain == 0) {
		countLabel.text = @"Last card!";
	} else if (cardsRemain == 1) {
		countLabel.text = [NSString stringWithFormat:@"1 card\nremain"];		
	} else {
		countLabel.text = [NSString stringWithFormat:@"%d cards\nremain", cardsRemain];		
	}
	
	[self updateAndSwitchViewTo: cardFrontViewController];
}

- (void)showCardBackView {	
//	helpMessage = backHelpMessage;

	self.title = @"Answer";

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
//	reviewUnfinishedViewController.scheduledCards = scheduledCards;
	reviewUnfinishedViewController.uncertainCards = uncertainCards;
	
	self.title = @"Review Again?";
	countLabel.text = @"";
	
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
	reviewFinishedViewController.scheduledCards = scheduledCards;

	self.title = @"Review Finished";
	countLabel.text = @"";
	
	[self.view insertSubview:reviewFinishedViewController.view atIndex:0];
	[self.view bringSubviewToFront:reviewFinishedViewController.view];
	[reviewFinishedViewController viewWillAppear:TRUE];
	
}

- (IBAction) reviewCompleteButtonClicked : (id) sender {
	[reviewFinishedViewController.view removeFromSuperview];
	
	[self.navigationController popViewControllerAnimated:YES];		
}

- (IBAction) skipExtraPracticeButtonClicked : (id) sender {
	[reviewUnfinishedViewController.view removeFromSuperview];
	
	[self.navigationController popViewControllerAnimated:YES];			
}

@end