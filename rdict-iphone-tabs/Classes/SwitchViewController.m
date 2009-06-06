//
//  SwitchViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "SwitchViewController.h"
#import "SearchViewController.h"
#import "DictionaryViewController.h"

@implementation SwitchViewController
@synthesize searchViewController;
@synthesize dictionaryViewController;


- (void)viewWillAppear:(BOOL)animated {
	NSLog(@"viewWillAppear");
	if( nil != dictionaryViewController.view.superview ) {
		[dictionaryViewController.view removeFromSuperview];
	}
	[self.view insertSubview:searchViewController.view atIndex:0];
}

- (void)viewDidLoad {
	NSLog(@"viewDidLoad");	

	searchViewController = [[SearchViewController alloc]
								 initWithNibName:@"SearchView" bundle:nil];
	searchViewController.switchViewController = self;
	dictionaryViewController = [[DictionaryViewController alloc]
									 initWithNibName:@"DictionaryView" bundle:nil];	
	dictionaryViewController.switchViewController = self;
}

-(void) showDictionary
{
	NSLog(@"showDictionary");
	[searchViewController.view removeFromSuperview];
	[self.view insertSubview:dictionaryViewController.view	atIndex:0];
}


/*
 // Override to allow orientations other than the default portrait orientation.
 - (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
 // Return YES for supported orientations
 return (interfaceOrientation == UIInterfaceOrientationPortrait);
 }
 */

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; 
}


- (void)dealloc {
	[searchViewController release];
	[dictionaryViewController release];
    [super dealloc];
}


@end
