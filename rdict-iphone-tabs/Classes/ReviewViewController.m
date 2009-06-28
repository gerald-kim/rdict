//
//  ReviewViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "RDictAppDelegate.h"
#import "ReviewViewController.h"
#import "ReviewSessionController.h"


@implementation ReviewViewController
@synthesize reviewSessionController;

- (void)viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	NSLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	self.navigationController.navigationBarHidden = NO;	
}

- (void) viewDidAppear:(BOOL)animated {

}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}

- (void)dealloc {
	if( nil != self.reviewSessionController ) {
		[reviewSessionController release];
	}
    [super dealloc];
}

- (IBAction) studyButtonClicked:(id) sender {
	if( nil == self.reviewSessionController ) {
		self.reviewSessionController = [[ReviewSessionController alloc]initWithNibName:@"ReviewSessionView" bundle:nil];
		self.reviewSessionController.hidesBottomBarWhenPushed = YES;
		//self.reviewSessionController.wantsFullScreenLayout = YES;
	}
	
	[self.navigationController pushViewController:reviewSessionController animated:YES];
//	reviewSessionController.cards = ;
}

@end
