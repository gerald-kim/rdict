//
//  ReviewSessionController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "ReviewSessionController.h"
#import "CardFrontViewController.h"
#import "CardBackViewController.h"
#import "Card.h"

@interface ReviewSessionController()

- (void)showCardFrontView;

@end


@implementation ReviewSessionController
@synthesize cardFrontViewController;
@synthesize cardBackViewController;
@synthesize cards;

- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	[super viewDidLoad];
	if( self.cardFrontViewController == nil ) {
		self.cardFrontViewController = [[CardFrontViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
		[self.view insertSubview:cardFrontViewController.view atIndex:0];
	}
	if( self.cardBackViewController == nil ) {
		self.cardBackViewController = [[CardBackViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
		[self.view insertSubview:cardBackViewController.view atIndex:0];
	}	
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];	
	
	cardsRemain = [cards count];
	[self showCardFrontView];
}	

- (void)viewDidUnload {
	[cardFrontViewController release];
	cardFrontViewController = nil;
	[cardBackViewController release];
	cardBackViewController = nil;
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void)showCardFrontView {
	Card* currentCard = [cards objectAtIndex:[cards count] - cardsRemain];
	
	cardFrontViewController.statusLabel.text = [NSString stringWithFormat:@"%d cards remain.", cardsRemain];
	cardFrontViewController.questionLabel.text = currentCard.question;
	[self.view bringSubviewToFront:cardFrontViewController.view];	
}

- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.showAnswerButton" );
	//[self.navigationController popViewControllerAnimated:YES];
	
	[self.view bringSubviewToFront:cardBackViewController.view];
	cardBackViewController.segmentControl.selectedSegmentIndex = -1;
}


- (IBAction) scoreButtonClicked : (id) sender {	
	NSLog( @"RSC.scoreButtonClicked" );
	if ( -1 == cardBackViewController.segmentControl.selectedSegmentIndex ) {
		return;
	}
	
	cardsRemain--;
	
	if ( 0 == cardsRemain ) {
		[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
		[self.navigationController popViewControllerAnimated:YES];		
	} else {
		[self showCardFrontView];
	}
}

@end