//
//  CardEditViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 11/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "CardEditViewController.h"
#import "RDictAppDelegate.h"


@implementation CardEditViewController

@synthesize card;
@synthesize cardAnswerView;

- (void)viewDidLoad {
    [super viewDidLoad];
	
	UIBarButtonItem *doneButton = [[[UIBarButtonItem alloc] 
								   initWithBarButtonSystemItem:UIBarButtonSystemItemDone
								   target:self action:@selector(doneButtonClicked:)] autorelease];
	self.navigationItem.rightBarButtonItem = doneButton;
}

- (void) viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	[(RDictAppDelegate*) [[UIApplication sharedApplication] delegate] updateReviewTab];

	self.title = card.question;
	self.cardAnswerView.text = card.answer;
}

- (void) viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	
	if (card) {
		[card release];		
	}
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}

- (IBAction) doneButtonClicked : (id) sender {
	card.answer = cardAnswerView.text;
	[card save];
	[self.navigationController popViewControllerAnimated:TRUE];
}

- (IBAction) deleteButtonClicked : (id) sender {
	[card deleteObject];
	card = nil;
	[self.navigationController popViewControllerAnimated:TRUE];
}

- (void)dealloc {
    [super dealloc];
}


@end
