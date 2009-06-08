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

- (void)viewDidLoad {
    self.title = @"Dictionary";
	[super viewDidLoad];
}

- (void)textFieldDoneEditing:(id)sender {
	[self.usersWord resignFirstResponder];

	RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
	
	DictionaryEntry *dicEntry = [delegate.dic searchByWord: usersWord.text];
	
	self.title = dicEntry.word;
	
	[dicEntry htmlifyEntry];
	
	NSString *path = [[NSBundle mainBundle] bundlePath];
	NSURL *baseURL = [NSURL fileURLWithPath:path];	
	[self.searchResultsPane loadHTMLString: dicEntry.entry baseURL:baseURL];
	
	[dicEntry release];
}

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
	NSURL *url = [request URL];
	NSLog( @"request called %@", [url relativePath] );	
	if ([@"/save" isEqualToString: [url relativePath]]) {		
		
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *selectedDefinition = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		Card *card = [[Card alloc] initWithQuestion: self.usersWord.text Answer: selectedDefinition];
		
		[card schedule];
		[card save];
		[card release];
		
		NSLog( @"Save called %@", [url relativePath] );	
		return FALSE;
	}
	else if ([@"/lookup" isEqualToString:[url relativePath]]){
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *clickedWord = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		
		RDictAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
		
		DictionaryEntry *dicEntry = [delegate.dic searchByWord: clickedWord];
		
		self.title = dicEntry.word;
		
		[dicEntry htmlifyEntry];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		[self.searchResultsPane loadHTMLString: dicEntry.entry baseURL:baseURL];
		
		[dicEntry release];
		return FALSE;
	}
	
	return YES;
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
