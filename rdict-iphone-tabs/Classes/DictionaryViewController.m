//
//  DictionaryViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "DictionaryViewController.h"
#import "RDictAppDelegate.h"
#import "Wiktionary.h"

@implementation DictionaryViewController
@synthesize webView;
@synthesize lemma;
@synthesize wiktionary;

- (void)viewDidLoad {
	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	self.wiktionary = delegate.wiktionary;
//	[delegate release];
	[super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated {
	self.title = lemma;
	self.navigationController.navigationBarHidden = NO;
//	[entry release];
	
	[super viewWillAppear:animated];
}

- (void) viewDidAppear:(BOOL)animated {
	WordEntry* entry = [wiktionary wordEntryByLemma:lemma];
	
	NSString *path = [[NSBundle mainBundle] bundlePath];
	NSURL *baseURL = [NSURL fileURLWithPath:path];	
	
	[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
}	

- (void)viewWillDisappear:(BOOL)animated {
	self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[webView release];
	[wiktionary release];
	[lemma release];
    [super dealloc];
}



@end
