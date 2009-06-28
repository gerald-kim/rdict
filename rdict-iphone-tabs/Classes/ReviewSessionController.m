//
//  ReviewSessionController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "ReviewSessionController.h"
#import "CardFrontViewController.h"
#import "CardBackViewController.h"
#import "Card.h"

@interface ReviewSessionController()

- (void)showCardFrontView;
- (void)showCardBackView;

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
	[cards release];
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

- (void)showCardFrontView {
//	if( cardsRemain > 0 ) {
		currentCard = [cards objectAtIndex:[cards count] - cardsRemain];		
//	} else if ( [uncertainCards count] > 0 ) {
//		currentCard = [uncertainCards objectAtIndex:0];
//	}
	
	cardFrontViewController.statusLabel.text = [NSString stringWithFormat:@"%d cards remain.", cardsRemain];
	cardFrontViewController.questionLabel.text = currentCard.question;
	
	[self.view bringSubviewToFront:cardFrontViewController.view];	
}

- (void)showCardBackView {	
	cardBackViewController.statusLabel.text = [NSString stringWithFormat:@"%d cards remain.", cardsRemain];
	cardBackViewController.questionLabel.text = currentCard.question;
	cardBackViewController.answerTextView.text = currentCard.answer;
	
	[self.view bringSubviewToFront:cardBackViewController.view];

}

- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.showAnswerButton" );
	//[self.navigationController popViewControllerAnimated:YES];
	[self showCardBackView];
}


- (IBAction) scoreButtonClicked : (id) sender {
	UIButton *button = (UIButton*) sender;
	NSLog( @"RSC.scoreButtonClicked %@", button.currentTitle );

	NSUInteger score = [button.currentTitle intValue];
	if( score < 3 ) {
		[uncertainCards addObject:currentCard];
	}
//	[currentCard studyWithScore:[button.currentTitle intValue]];	
	
//	if( cardsRemain > 0 ) {
		cardsRemain--;		
//	} else if ( [uncertainCards count] > 0 ) {
//		[uncertainCards removeObject:currentCard];
//	}
	
	if ( 0 == cardsRemain ) {
		[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
		[self.navigationController popViewControllerAnimated:YES];		
	} else {
		[self showCardFrontView];
	}
}

@end