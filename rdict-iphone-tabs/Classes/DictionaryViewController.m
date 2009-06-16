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
	
	WordEntry* entry = [wiktionary wordEntryByLemma:lemma];
	[entry decorateDefinition];
	
	NSString *path = [[NSBundle mainBundle] bundlePath];
	NSURL *baseURL = [NSURL fileURLWithPath:path];	
	
	[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
	
	[entry release];
	//	[baseURL release];
	//	[path release];
	
	[super viewWillAppear:animated];
}

- (void) viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
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

#pragma mark -
#pragma mark UIWebViewDelegate Method

- (void)webViewDidFinishLoad:(UIWebView *)aWebView {
	NSLog( @"did finish load" );
//	[webView stringByEvaluatingJavaScriptFromString:@"documentReady(); alert( 123 );"];
}

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
	NSURL *url = [request URL];
	NSLog( @"request called %@, %@", [url scheme], [url query] );	
	if ([@"/save" isEqualToString: [url relativePath]]) {		
		
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *selectedDefinition = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		/*
		Card *card = [[Card alloc] initWithQuestion: self.usersWord.text Answer: selectedDefinition];
		
		[card schedule];
		[card save];
		[card release];
		*/
		
		NSLog( @"Save called %@", [url relativePath] );	
		return FALSE;
	}
	else if ([@"/lookup" isEqualToString:[url relativePath]]){
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *clickedWord = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		
		/*
		DictionaryEntry *dicEntry = [delegate.dic searchByWord: clickedWord];
		
		self.title = dicEntry.word;
		
		[dicEntry htmlifyEntry];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		[self.searchResultsPane loadHTMLString: dicEntry.entry baseURL:baseURL];
		
		[dicEntry release];
		 */
		return FALSE;
	}
	
	return YES;
}



@end
