//
//  ReviewFinishedViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/28/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "ReviewFinishedViewController.h"


@implementation ReviewFinishedViewController

@synthesize scheduledCards;
@synthesize totalCardLabel;

- (void) viewWillAppear:(BOOL)animated {
	NSLog(@"RFVC.willShowViewController");
	[super viewWillAppear:animated];
	
	totalCardLabel.text = [NSString stringWithFormat:@"You've studied %d cards", [scheduledCards count]];
	//TODO show statistics.
}

- (void) viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[scheduledCards release];
}

- (void) didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void) viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end
