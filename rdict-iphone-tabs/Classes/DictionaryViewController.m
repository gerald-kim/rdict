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
	
	lookupHistory = [[LookupHistory alloc] init];

	NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookuphistory/?lemma=%@", lemma]];
	NSURLRequest* request = [NSURLRequest requestWithURL:url];
	[webView loadRequest:request];
}

- (void) viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	self.navigationController.navigationBarHidden = YES;
	
	[lookupHistory release];
	lookupHistory = nil;
}

- (void) viewDidDisappear:(BOOL)animated {
	NSLog(@"DVC.viewDidDisappear");
	[webView loadHTMLString:@"" baseURL:nil];
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
#pragma mark UIWebViewDelegate Method

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
	NSLog( @"DVC.shouldStartLoadWithRequest url: %@", [[request URL] absoluteString] );
	
	NSURL *url = [request URL];
	if( [@"rdict" isEqualToString:[url scheme]] ) {
		[self handleRdictRequest: url];
		return NO;
	} 
	
	if ( navigationType == UIWebViewNavigationTypeLinkClicked ) {
		activityIndicatorView.hidden = NO;	
		[activityIndicatorView startAnimating];

		[self.lookupHistory addHistory:url];
	}
	return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)aWebView {
	[self adjustToolBarButtons];
	
	[activityIndicatorView stopAnimating];
	activityIndicatorView.hidden = YES;	
}

-(void) adjustToolBarButtons {
	if([self.lookupHistory canGoBack]) {
		self.navigationItem.leftBarButtonItem = backButton;
	} else {
		self.navigationItem.leftBarButtonItem = returnToSearchButton;
	}
	
	self.forwardButton.enabled = [self.lookupHistory canGoForward];
}

- (void) handleRdictRequest:(NSURL*) url {
	if ([@"save" isEqualToString: [url host]]) {		
		NSLog( @"Save called %@", [url host] );	
		
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *selectedDefinition = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		Card* card = [Card saveCardWithQuestion:self.lemma andAnswer:selectedDefinition];
		[card release];

		/*
		[UIView beginAnimations:nil context:NULL];
		[UIView setAnimationDuration:0.7];
		cardAddedNote.alpha = 1.0;
		[UIView setAnimationDelegate:self];
		[UIView setAnimationDidStopSelector:@selector(showAlert)];
		[UIView commitAnimations];
		*/
	} else if ( [[url host] hasPrefix:@"lookup"] ){
		activityIndicatorView.hidden = NO;	
		[activityIndicatorView startAnimating];

		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *query = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		self.title = query;
		
		WordEntry* entry = [wiktionary wordEntryByLemma:query];	
		if (entry) {
			[entry decorateDefinition];
			
			NSString *path = [[NSBundle mainBundle] bundlePath];
			NSURL *baseURL = [NSURL fileURLWithPath:path];	
			
			[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
			
			[entry release];

			if( [ @"lookup" isEqualToString: [url host]] ) {
				NSURL* urlForHistory = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookuphistory/?lemma=%@", query]];
				[lookupHistory addHistory:urlForHistory];
			}
		} else {
			//		[activityIndicatorView stopAnimating];
			//		activityIndicatorView.hidden = YES;		
			[webView stringByEvaluatingJavaScriptFromString:@"alert( \"Sorry, Vocabulator doesn't have a definition for that word.\" );"];
		}	
	}
}

#pragma mark -

- (void)handleGoBackClick:(id)sender {
	NSLog( @"DVC.handleGoBackClick" );
	
	NSURLRequest* request = [NSURLRequest requestWithURL:[lookupHistory goBack]];
	[webView loadRequest:request];
}

- (void)handleGoForwardClick:(id)sender {
	NSURLRequest* request = [NSURLRequest requestWithURL:[lookupHistory goForward]];
	[webView loadRequest:request];
}

#pragma mark -

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

@end
