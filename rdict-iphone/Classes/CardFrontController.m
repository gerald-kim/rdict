//
//  CardFrontController.m
//  RDict
//
//  Created by Stephen Bodnar on 25/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "CardFrontController.h"
#import "CardBackController.h"
#import "RDictAppDelegate.h"
#import "Card.h"

@implementation CardFrontController
@synthesize cardBackController;
@synthesize questionLabel;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
-(void) viewWillAppear:(BOOL) animated{
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	
	Card *card = [delegate.cards lastObject];
	
    self.questionLabel.text = card.question;
}

- (IBAction)viewAnswerButtonPressed:(id)sender{
	self.title = @"Viewed Answer";
	
	if(self.cardBackController.view.superview == nil){
		CardBackController *controller = [[CardBackController alloc] initWithNibName:@"CardBack" bundle:nil];
		self.cardBackController = controller;
		[cardBackController release];
	}
	
	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	[delegate.navController pushViewController:self.cardBackController animated:YES];
} 

/*
// The designated initializer. Override to perform setup that is required before the view is loaded.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
*/

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/


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
