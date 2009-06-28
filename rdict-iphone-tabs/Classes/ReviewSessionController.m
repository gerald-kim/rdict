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

@implementation ReviewSessionController
@synthesize cardFrontViewController;
@synthesize cardBackViewController;
@synthesize cards;
@synthesize uncertainCards;

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
	[self.view bringSubviewToFront:cardFrontViewController.view];
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
	
	[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
	[self.navigationController popViewControllerAnimated:YES];
}

@end