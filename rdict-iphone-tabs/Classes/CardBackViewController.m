//
//  CardBackViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "CardBackViewController.h"

@implementation CardBackViewController

@synthesize statusLabel;
@synthesize questionLabel;
@synthesize answerTextView;

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void)viewWillAppear:(BOOL) animated {
	NSLog( @"CBC.viewWillAppear" );
}

- (void)viewDidAppear:(BOOL) animated {
	NSLog( @"CBC.viewDidAppear" );
}

@end
