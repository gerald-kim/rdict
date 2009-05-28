//
//  DictionaryViewController.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#include "tcutil.h"
#include "tcbdb.h"

#import "DictionaryViewController.h"
#import "RDictAppDelegate.h"
#import "DictionaryEntry.h"
#import "Dictionary.h"
#import "Card.h"

@implementation DictionaryViewController
@synthesize searchResultsPane;
@synthesize usersWord;

- (void)textFieldDoneEditing:(id)sender {
	[self.usersWord resignFirstResponder];

	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	
	DictionaryEntry *dicEntry = [delegate.dic searchByWord:usersWord.text];
	
	self.title = dicEntry.word;
	[self.searchResultsPane loadHTMLString:dicEntry.entry baseURL:nil];
	
	Card *card = [[Card alloc] initWithQuestion:dicEntry.word Answer:dicEntry.entry];
	[card save];
	[card release];	
	
	[dicEntry release];
}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    self.title = @"Dictionary";
	[super viewDidLoad];
}


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
