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
@synthesize scheduleText;

- (void)viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	NSLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	self.navigationController.navigationBarHidden = NO;	
	
	
	int cardCount = [Card countByScheduled];
	NSArray* schedule = [Card reviewSchedulesWithLimit:7];
	NSMutableString* text = [NSMutableString string];
	for( NSArray* row in schedule ) {
		[text appendFormat:@"%@ : %@\n", [row objectAtIndex:0], [row objectAtIndex:1]];
	}
	scheduleText.text = text;
	
	NSLog( @"Card counts: %d", cardCount );
	cardInfomationLabel.text = [NSString stringWithFormat:@"There %d cards to review today.", cardCount];
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
	[reviewSessionController release];
    [super dealloc];
}

- (IBAction) studyButtonClicked:(id) sender {
	if( nil == reviewSessionController ) {
		reviewSessionController = [[ReviewSessionController alloc]initWithNibName:@"ReviewSessionView" bundle:nil];
		reviewSessionController.hidesBottomBarWhenPushed = YES;
		//self.reviewSessionController.wantsFullScreenLayout = YES;
	}
	

	reviewSessionController.reviewCards = [Card findByScheduled];

	[self.navigationController pushViewController:reviewSessionController animated:YES];
}

@end
