//
//  CardBackViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "CardBackViewController.h"

@implementation CardBackViewController

@synthesize segmentControl;

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"CBC.viewWillAppear" );
	segmentControl.selectedSegmentIndex = -1;
}

- (void)viewDidAppear:(BOOL) animated {
	NSLog( @"CBC.viewDidAppear" );
}

@end
