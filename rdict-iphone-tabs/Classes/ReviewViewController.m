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
#import "Card.h"

@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize cardInfomationLabel;

- (void)viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	NSLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	self.navigationController.navigationBarHidden = NO;	
	
	int cardCount = [Card count];
	NSLog( @"Card counts: %d", cardCount );
	cardInfomationLabel.text = [[NSString alloc] initWithFormat:@"There %d cards to review today.", cardCount];
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
		reviewSessionController.hidesBottomBarWhenPushed = YES;
		//self.reviewSessionController.wantsFullScreenLayout = YES;
	}
	
	reviewSessionController.cards = [Card allObjects];
	[self.navigationController pushViewController:reviewSessionController animated:YES];
//	reviewSessionController.cards = ;
}

@end
