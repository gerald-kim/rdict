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
#import "Card.h"
#import "LookupHistory.h"

@implementation DictionaryViewController
@synthesize webView;
@synthesize cardAddedNote;
@synthesize lemma;
@synthesize wiktionary;
@synthesize lookupHistory;
@synthesize activityIndicatorView;
@synthesize returnToSearchButton;
@synthesize backButton;
@synthesize forwardButton;

- (void) viewDidLoad {
	[super viewDidLoad];
	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	self.wiktionary = delegate.wiktionary;
	self.lookupHistory = [[LookupHistory alloc] init];
	
	self.returnToSearchButton = self.navigationItem.leftBarButtonItem;
	
	self.backButton = [[UIBarButtonItem alloc] initWithTitle:@"<" 
												style: UIBarButtonItemStylePlain
												target: self action: @selector(handleGoBackClick:)];
		
	self.forwardButton = [[UIBarButtonItem alloc] initWithTitle:@">" 
												style: UIBarButtonItemStylePlain
												target: self action: @selector(handleGoForwardClick:)];
	self.forwardButton.enabled = NO;
	self.navigationItem.rightBarButtonItem = forwardButton;
}

- (void) viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	self.navigationController.navigationBarHidden = NO;
		
	activityIndicatorView.hidden = NO;
	[activityIndicatorView startAnimating];
	
	[self.lookupHistory clear];
	[self showWordDefinition: lemma RecordHistory: YES];
}

- (void) viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
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
	[lookupHistory release];
	[webView release];
	[wiktionary release];
	[lemma release];
    [super dealloc];
}

#pragma mark -

- (void) showWordDefinition: (NSString *) query RecordHistory: (BOOL) recordHistory {
	self.title = query;

	[activityIndicatorView startAnimating];
	activityIndicatorView.hidden = NO;
	
	WordEntry* entry = [wiktionary wordEntryByLemma:query];
	
	if (entry) {
		[entry decorateDefinition];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		
		[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];

		if(recordHistory)
			[self.lookupHistory addWord:query];
		
		[entry release];
	} else {
		[activityIndicatorView stopAnimating];
		activityIndicatorView.hidden = YES;
		
		[webView stringByEvaluatingJavaScriptFromString:@"alert( \"Sorry, Vocabulator doesn't have a definition for that word.\" );"];
	}
	
	[self adjustToolBarButtons];
}

-(void) adjustToolBarButtons {
	if(! [self.lookupHistory canGoBack])
		self.navigationItem.leftBarButtonItem = returnToSearchButton;
	else
		self.navigationItem.leftBarButtonItem = backButton;
	
	self.forwardButton.enabled = [self.lookupHistory canGoForward];
}

- (void) handleWordLookUp: (NSURL *) url  {
	NSArray *strings = [[url query] componentsSeparatedByString: @"="];
	NSString *clickedWord = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	
	[self showWordDefinition:clickedWord RecordHistory: YES];
}

- (void) handleRdictRequest:(NSURL*) url {
	if ([@"save" isEqualToString: [url host]]) {		
		NSLog( @"Save called %@", [url host] );	
		
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *selectedDefinition = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		Card* card = [Card saveCardWithQuestion:self.lemma andAnswer:selectedDefinition];
		[card release];
		
		[UIView beginAnimations:nil context:NULL];
		[UIView setAnimationDuration:0.7];
		cardAddedNote.alpha = 1.0;
		[UIView setAnimationDelegate:self];
		[UIView setAnimationDidStopSelector:@selector(showAlert)];
		[UIView commitAnimations];
		
	} else if ([@"lookup" isEqualToString:[url host]]){
		[self handleWordLookUp: url];
	}
}

-(void) showAlert {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.7];
	cardAddedNote.alpha = 0.9;
	[UIView setAnimationDelegate:self];
	[UIView setAnimationDidStopSelector:@selector(fadeAlertOut)];
	[UIView commitAnimations];
}

-(void) fadeAlertOut {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.4];
	cardAddedNote.alpha = 0;
	[UIView commitAnimations];
}

#pragma mark -
#pragma mark UIWebViewDelegate Method

- (void)webViewDidFinishLoad:(UIWebView *)aWebView {
	[activityIndicatorView stopAnimating];
	activityIndicatorView.hidden = YES;	
}

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
	NSURL *url = [request URL];
	NSLog( @"request called %@://%@/%@?%@", [url scheme], [url host], [url relativePath], [url query] );	

	if( [@"rdict" isEqualToString:[url scheme]] ) {
		[self handleRdictRequest: url];
		return NO;
	}
	return YES;
}

- (void)handleGoBackClick:(id)sender {
	[self.lookupHistory goBack];
	[self showWordDefinition: [self.lookupHistory getWord] RecordHistory: NO];
}

- (void)handleGoForwardClick:(id)sender {
	[self.lookupHistory goForward];
	[self showWordDefinition: [self.lookupHistory getWord] RecordHistory: NO];
}


@end
