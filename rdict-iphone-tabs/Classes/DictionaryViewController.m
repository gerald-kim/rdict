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
@synthesize navigationItem;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	navigationItem.title = @"Ttt";
    [super viewDidLoad];
}


- (IBAction) testButtonPressed:(id) sender {
	NSLog(@"testButtonPressed in DictionaryView");
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
