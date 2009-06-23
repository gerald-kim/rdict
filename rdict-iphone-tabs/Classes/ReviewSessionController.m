//
//  ReviewSessionController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "ReviewSessionController.h"
#import "CardFrontViewController.h"

@implementation ReviewSessionController
@synthesize cardFrontViewController;

- (void)viewDidLoad {
//    [super viewDidLoad];	
	[self.view insertSubview:cardFrontViewController.view atIndex:0];	
}

- (void)didReceiveMemoryWarning {
   [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}

- (void)dealloc {
	//[cardFrontViewController release];
    [super dealloc];
}


@end
