//
//  HomeViewController.m
//  View Switcher
//
//  Created by Stephen Bodnar on 14/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "HomeViewController.h"
#import "Card.h"

@implementation HomeViewController
@synthesize dicViewController;
@synthesize reviewViewController;
@synthesize controllers;
@synthesize goToReviewButton;

- (void) viewWillAppear: (BOOL) animated {
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	delegate.cards = [[NSMutableArray alloc] initWithArray:[Card allObjects]];
	
	[self.goToReviewButton setTitle:[NSString stringWithFormat:@"Review (%d)", [delegate.cards count]] forState: UIControlStateNormal];	
}

- (void)viewDidLoad {
	self.title = @"Home";
	
	NSMutableArray *array = [[NSMutableArray alloc] init];
	self.controllers = array;
	[array release];	
}

- (IBAction)dicButtonPressed:(id)sender {
	if(self.dicViewController.view.superview == nil){
		DictionaryViewController *dicController = [[DictionaryViewController alloc] initWithNibName:@"DictionaryView" bundle:nil];
		self.dicViewController = dicController;
		[dicController release];
	}
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	[delegate.navController pushViewController:self.dicViewController animated:YES];
}


- (IBAction)reviewButtonPressed:(id)sender {
	if (self.reviewViewController == nil) {
		ReviewViewController *reviewController = [[ReviewViewController alloc] initWithNibName:@"ReviewView" bundle:nil];
		self.reviewViewController = reviewController;
		
		RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
		delegate.reviewViewController = reviewController;
		
		[reviewController release];
	}
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	[delegate.navController pushViewController:self.reviewViewController animated:YES];
}

- (void)dealloc {
	[controllers release];
    [super dealloc];
}


@end
