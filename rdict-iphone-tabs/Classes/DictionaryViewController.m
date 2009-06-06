//
//  DictionaryViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "DictionaryViewController.h"


@implementation DictionaryViewController
@synthesize titleButton;

- (void)viewWillAppear:(BOOL)animated {
//	self.navigationController.navigationBarHidden = NO;
	[titleButton setTitle:@"Title" forState:UIControlStateNormal];
	[titleButton setTitle:@"Title" forState:UIControlStateSelected];
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
    [super dealloc];
}


@end
