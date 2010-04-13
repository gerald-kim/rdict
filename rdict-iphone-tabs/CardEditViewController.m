//
//  CardEditViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 11/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "CardEditViewController.h"
#import "RDictAppDelegate.h"
#import <QuartzCore/QuartzCore.h>


@implementation CardEditViewController

@synthesize card;
@synthesize cardAnswerView;
@synthesize deleteButton;

- (void)viewDidLoad {
    [super viewDidLoad];
	
	UIImage *backgroundImage = [UIImage imageNamed:@"contents_bg.png"];
	UIColor *backgroundColor = [[UIColor alloc] initWithPatternImage:backgroundImage];
	self.view.backgroundColor = backgroundColor;
	[backgroundColor release];
	
	cardAnswerView.layer.cornerRadius = 10.0;
	cardAnswerView.layer.borderColor = [[UIColor grayColor] CGColor];
	cardAnswerView.layer.borderWidth = 1;
	
	//deleteButton.backgroundColor = BGCOLOR;
	
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
	UIAlertView *alert = [[UIAlertView alloc] init];
	[alert setTitle:@"Delete Card"];
	[alert setMessage:[NSString stringWithFormat:@"Do you want to delete this card?"]];
	[alert setDelegate:self];
	[alert addButtonWithTitle:@"Cancel"];
	[alert addButtonWithTitle:@"Yes"];
	[alert show];
	[alert release];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	NSLog(@"DVC.clickedButtonAtIndex");
	if (buttonIndex == 1) {
		[card deleteObject];
		card = nil;
		[self.navigationController popViewControllerAnimated:TRUE];
	}
}

- (void)dealloc {
    [super dealloc];
}


@end
