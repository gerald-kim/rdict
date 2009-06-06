//
//  DictionaryViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "DictionaryViewController.h"
#import "SwitchViewController.h"


@implementation DictionaryViewController
@synthesize switchViewController;
@synthesize titleButton;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	super.navigationItem.title = @"Test";
//	[super.navigationItem.leftBarButtonItem.customView setHidden:TRUE];
//	[super.navigationItem.rightBarButtonItem.customView setHidden:TRUE];
	/*
	UIBarButtonItem *fooButton = [[UIBarButtonItem alloc]
								  initWithTitle:@"Foo" 
								  style:UIBarButtonItemStyleBordered
								  target:self 
								  action:@selector(prevButtonPressed:)];
	self.navigationItem.leftBarButtonItem = fooButton;
	[fooButton release];

	*/
	[titleButton setTitle:@"Test" forState:UIControlStateNormal];
	[titleButton setTitle:@"Test" forState:UIControlStateHighlighted];
	
    [super viewDidLoad];
}


- (IBAction) prevButtonPressed:(id) sender {
	NSLog(@"prevButtonPressed in DictionaryView");
}

- (IBAction) nextButtonPressed:(id) sender {
	NSLog(@"nextButtonPressed in DictionaryView");
}

- (IBAction) titleButtonPressed:(id) sender {
	NSLog(@"titleButtonPressed in DictionaryView");
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[switchViewController release];
    [super dealloc];
}


@end
