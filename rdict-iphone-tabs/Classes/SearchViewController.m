//
//  SearchViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "SearchViewController.h"
#import "DictionaryViewController.h"

@implementation SearchViewController

@synthesize dictionaryViewController;

- (IBAction) dictButtonPressed:(id)sender {
	NSLog(@"dictButtonPressed");
	
	if( dictionaryViewController == nil ) {
		dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	}
	
	[self.navigationController pushViewController:dictionaryViewController animated:YES];	 
}

/*
- (void)viewWillAppear:(BOOL)animated {
	self.navigationController.navigationBarHidden = YES;
}
*/

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
//	[dictionaryViewController release];
    [super dealloc];
}


@end
