//
//  DictionaryViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "DictionaryViewController.h"


@implementation DictionaryViewController
@synthesize backButton;
//@synthesize navigationItem;
@synthesize lemma;

- (void)viewWillAppear:(BOOL)animated {
	self.title = lemma;
	self.navigationController.navigationBarHidden = NO;
	[super viewWillAppear:animated];
//	self.navigationItem.rightBarButtonItem = 
//	[titleButton setTitle:lemma forState:UIControlStateNormal];
//	[titleButton setTitle:lemma forState:UIControlStateSelected];
}

- (void)viewWillDisappear:(BOOL)animated {
	self.navigationController.navigationBarHidden = YES;
}



- (IBAction) titleButtonPressed:(id)sender {
	[self.navigationController popViewControllerAnimated:YES];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[lemma release];
    [super dealloc];
}


@end
