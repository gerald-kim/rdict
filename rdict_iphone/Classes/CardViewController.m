//
//  CardBackViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "rdict.h"
#import "CardViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation CardViewController

@synthesize remainsLabel;
@synthesize questionLabel;
@synthesize answerTextView;

- (void)viewDidLoad {
	[super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)dealloc {
    [super dealloc];
}

- (void)viewWillAppear:(BOOL) animated {
	DebugLog( @"CBC.viewWillAppear" );
}

- (void)viewDidAppear:(BOOL) animated {
	DebugLog( @"CBC.viewDidAppear" );}

@end
