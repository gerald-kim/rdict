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
#import "ReviewUnfinishedViewController.h"
#import "ReviewFinishedViewController.h"
#import "Card.h"

@interface ReviewSessionController()

- (void) showCardFrontView;
- (void) showCardBackView;
- (void) showReviewUnfinishedView;
- (void) showReviewFinishedView;
- (void) initCards:(NSArray*) theCards;

@end


@implementation ReviewSessionController

@synthesize reviewCards;

- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	[super viewDidLoad];
	if( cardFrontViewController == nil ) {
		cardFrontViewController = [[CardFrontViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
		[self.view insertSubview:cardFrontViewController.view atIndex:0];
	}
	if( cardBackViewController == nil ) {
		cardBackViewController = [[CardBackViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
		[self.view insertSubview:cardBackViewController.view atIndex:0];
	}	
	if( reviewUnfinishedViewController == nil ) {
		reviewUnfinishedViewController = [[ReviewUnfinishedViewController alloc]initWithNibName:@"ReviewUnfinishedView" bundle:nil];
		[self.view insertSubview:reviewUnfinishedViewController.view atIndex:0];
	}	
	if( reviewFinishedViewController == nil ) {
		reviewFinishedViewController = [[ReviewFinishedViewController alloc]initWithNibName:@"ReviewFinishedView" bundle:nil];
		[self.view insertSubview:reviewFinishedViewController.view atIndex:0];
	}	
	
}

- (void) initCards:(NSArray*) theCards {
	cards = theCards;
	cardsRemain = [cards count];
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];	
	
	NSLog( @"-[RSC viewWillAppear] cards retainCount: %d", [reviewCards retainCount] );
	
	
	[self initCards:reviewCards];
	uncertainCards = [[NSMutableArray alloc] init];
	
	[self showCardFrontView];
}	

- (void)viewDidDisappear:(BOOL) animated {
	NSLog( @"-[RSC viewDidDisappear] cards retainCount: %d", [reviewCards retainCount] );
	[reviewCards release]; reviewCards = nil;
	[uncertainCards release]; 
}

- (void)viewDidUnload {
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

- (void) showReviewUnfinishedView {
	[self.view bringSubviewToFront:reviewUnfinishedViewController.view];
}

- (void) showReviewFinishedView {
	[self.view bringSubviewToFront:reviewFinishedViewController.view];	
}


- (IBAction) answerButtonClicked : (id) sender {	
	NSLog( @"RSC.showAnswerButton" );
	//[self.navigationController popViewControllerAnimated:YES];
	[self showCardBackView];
}


- (IBAction) scoreButtonClicked : (id) sender {
	UIButton *button = (UIButton*) sender;
	NSUInteger score = [button.currentTitle intValue];
	
	cardsRemain--;		

	if( cards == reviewCards ) {
		if( score <= 3 ) {
			[uncertainCards addObject:currentCard];
		}
		[currentCard study:score];
		if ( 0 == cardsRemain &&  0 != [uncertainCards count]  ) {
			[self showReviewUnfinishedView];
			return;
		}
	}
	
	if ( 0 == cardsRemain ) {
		[self showReviewFinishedView];
	} else {
		[self showCardFrontView];
	}
}

- (IBAction) reviewAgainButtonClicked : (id) sender {
	[self initCards:uncertainCards];
	[self showCardFrontView];
}

- (IBAction) reviewCompleteButtonClicked : (id) sender {
	[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
	[self.navigationController popViewControllerAnimated:YES];		
	
}

@end