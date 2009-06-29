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
#import "Card.h"

@interface ReviewSessionController()

- (void)initCards:(NSArray*) theCards;
- (void)showCardFrontView;
- (void)showCardBackView;

@end


@implementation ReviewSessionController

@synthesize scheduledCards;

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
	
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"RSC.viewWillAppear" );
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];	
	
	
	[self initCards:scheduledCards];
	uncertainCards = [[NSMutableArray alloc] init];
	
	[self showCardFrontView];
}	

- (void)viewDidDisappear:(BOOL) animated {
	NSLog( @"RSC.viewDidDisappear" );
	NSLog( @"RSC.viewDidDisappear scheduledCards.retainCount : %d", [scheduledCards retainCount] );

//	[scheduledCards release];
	scheduledCards = nil;
//	[uncertainCards release];
//	NSLog( @"RSC.viewDidDisappear scheduledCards.retainCount : %d", [scheduledCards retainCount] );
	
}

- (void)viewDidUnload {
	NSLog( @"RSC.viewDidUnload" );
	[cardFrontViewController release];
	cardFrontViewController = nil;
	[cardBackViewController release];
	cardFrontViewController = nil;
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void)initCards:(NSArray *)theCards {
	NSLog( @"RSC.initCards 0x%x", theCards );
	
	cards = theCards;
	cardsRemain = [cards count];
}

- (void)showCardFrontView {
	currentCard = [cards objectAtIndex:[cards count] - cardsRemain];	
	NSLog( @"RSC.showCardFrontView card(0x%x, %d) ", currentCard, [currentCard retainCount] );

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
	NSUInteger score = [button.currentTitle intValue];
	NSLog( @"RSC.scoreButtonClicked score: %d", score );
	NSLog( @"RSC.scoreButtonClicked card(0x%x, %d) ", currentCard, [currentCard retainCount] );
	
	cardsRemain--;		
	
	if( YES ) {
		if( score <= 3 ) {
			[uncertainCards addObject:currentCard];
		}
//		[currentCard studyWithScore:[button.currentTitle intValue]];	
		
		if( 0 == cardsRemain && [uncertainCards count] > 0 ) {
			[self initCards:uncertainCards];
		}
	} 
	
	if ( 0 == cardsRemain ) {
		[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
		[self.navigationController popViewControllerAnimated:YES];		
	} else {
		[self showCardFrontView];
	}	 	
}

@end