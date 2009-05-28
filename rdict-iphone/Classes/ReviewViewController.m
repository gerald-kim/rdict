//
//  ReviewViewController.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "ReviewViewController.h"
#import "ReviewHomeViewController.h"
#import "CardFrontController.h"

@implementation ReviewViewController
@synthesize reviewHomeViewController;
@synthesize cardFrontController;
@synthesize mesg;
@synthesize startReviewButton;

- (void) viewDidLoad {
	self.title = @"Review";
	
	ReviewHomeViewController *controller = [[ReviewHomeViewController alloc] initWithNibName:@"ReviewHomeView" bundle:nil];
	
	self.reviewHomeViewController = controller;
	
	[controller release];
	
	[self.view insertSubview: self.reviewHomeViewController.view atIndex: 0];
}

-(void) viewWillAppear:(BOOL) animated{
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	[delegate.navController setNavigationBarHidden:NO animated:YES];
	
	if([delegate.cards count] > 0){
		self.mesg.text = [NSString stringWithFormat:@"You have %d word(s) to study today.  It will take about y minutes.", [delegate.cards count]];
		[self.startReviewButton setHidden:NO];
	}
	else {		
		[self.startReviewButton setHidden:YES];
		self.mesg.text = @"Good job!\nYou're done studying for today.";
	}
}

- (IBAction)startReviewButtonPressed:(id)sender{
	self.title = @"Button Pressed";
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	[delegate.navController setNavigationBarHidden:YES animated:YES];
	
	CardFrontController *controller = [[CardFrontController alloc] initWithNibName:@"CardFront" bundle:nil];
	 
	self.cardFrontController = controller;

	[delegate.navController pushViewController:self.cardFrontController animated:YES];

	[controller release];	
} 


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
}


@end
