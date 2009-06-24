//
//  ReviewSessionController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "ReviewSessionController.h"

@implementation ReviewSessionController
@synthesize cardFrontViewController;
@synthesize cardBackViewController;

- (void)viewDidLoad {
	NSLog( @"RSC.viewDidLoad" );
	if( self.cardFrontViewController == nil ) {
		self.cardFrontViewController = [[UIViewController alloc]initWithNibName:@"CardFrontView" bundle:nil];
	}
	if( self.cardBackViewController == nil ) {
		self.cardBackViewController = [[UIViewController alloc]initWithNibName:@"CardBackView" bundle:nil];
	}
	
	[self.view insertSubview:cardFrontViewController.view atIndex:0];
	[self.view insertSubview:cardBackViewController.view atIndex:0];
	
	[super viewDidLoad];	

}

- (void)viewWillAppear:(BOOL) animated {
	[[UIApplication sharedApplication] setStatusBarHidden:YES animated:YES];
	
	[self.view bringSubviewToFront:cardFrontViewController.view];
}	

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
}

- (void)dealloc {
	//[cardFrontViewController release];
    [super dealloc];
}

- (IBAction) showAnswerButtonClicked : (id) sender {	
	NSLog( @"showAnswerButton" );
	//[self.navigationController popViewControllerAnimated:YES];
	
	[self.view bringSubviewToFront:cardBackViewController.view];
}

- (IBAction) answerButtonClicked : (id) sender {	
	UISegmentedControl* segControl = sender;
	NSLog( @"Selected: %d", segControl.selectedSegmentIndex );

	[[UIApplication sharedApplication] setStatusBarHidden:NO animated:YES];
	[self.navigationController popViewControllerAnimated:YES];
	
}

@end
