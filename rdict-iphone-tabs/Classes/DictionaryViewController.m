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

@interface DictionaryViewController()

- (void) saveCard: (NSString *) selectedDefinition;
- (void) lookUpDictionary: (NSString *) aLemma lookupMethod: (NSString *) rdictMethod;
	
-(void) showSaveAlert;
-(void) fadeOutSaveAlert;

-(void) startActivityAnimating;
-(void) stopActivityAnimating;

@end

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
	NSLog( @"DVC.shouldStartLoadWithRequest type:%d, url: %@", navigationType, [[request URL] absoluteString] );
	
	NSURL *url = [request URL];
	if( [@"rdict" isEqualToString:[url scheme]] ) {
		[self handleRdictRequest: url];
		return NO;
	} 
	
	if ( navigationType == UIWebViewNavigationTypeLinkClicked && [[[request URL] scheme] hasPrefix:@"http"]  ) {
		[self startActivityAnimating];
		[lookupHistory addHistory:url];
	}
	return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)aWebView {
	[self adjustToolBarButtons];
	
	//TODO change title when connected to external site
	//self.title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
	
	[activityIndicatorView stopAnimating];
	activityIndicatorView.hidden = YES;	
}


#pragma mark -
#pragma mark RDict Request

- (void) handleRdictRequest:(NSURL*) url {
	NSString *rdictMethod = [url host];
	NSArray *paramaters = [[url query] componentsSeparatedByString: @"="];
	NSString *parameter = [[paramaters objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

	if ([@"save" isEqualToString: rdictMethod]) {		
		[self saveCard: parameter];
	} else if ( [rdictMethod hasPrefix:@"lookup"] ){
		[self lookUpDictionary: parameter lookupMethod: rdictMethod];
	
	}
}

- (void) saveCard: (NSString *) selectedDefinition  {
	Card* card = [Card saveCardWithQuestion:self.lemma andAnswer:selectedDefinition];
	[card release];
	[self showSaveAlert];
	
}
- (void) lookUpDictionary: (NSString *) aLemma lookupMethod: (NSString *) rdictMethod  {
	[self startActivityAnimating];
	
	WordEntry* entry = [wiktionary wordEntryByLemma:aLemma];	
	if (entry) {
		self.title = aLemma;
		
		[entry decorateDefinition];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		
		[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
		
		[entry release];
		
		if( [ @"lookup" isEqualToString: rdictMethod] ) {
			NSURL* urlForHistory = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookuphistory/?lemma=%@", aLemma]];
			[lookupHistory addHistory:urlForHistory];
		}
	} else {
		[self stopActivityAnimating];
		
		[webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"alert( \"Sorry, Vocabulator doesn't have a definition for \\\"%@\\\".\" );", aLemma]];
	}
	
}

#pragma mark -
#pragma mark LookupHistory
-(void) adjustToolBarButtons {
	self.navigationItem.leftBarButtonItem = [lookupHistory canGoBack] ? backButton : returnToSearchButton;
	self.forwardButton.enabled = [self.lookupHistory canGoForward];
}

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
#pragma mark Animation, Alert

-(void) showSaveAlert {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.7];
	cardAddedNote.alpha = 0.9;
	[UIView setAnimationDelegate:self];
	[UIView setAnimationDidStopSelector:@selector(fadeOutSaveAlert)];
	[UIView commitAnimations];
}

-(void) fadeOutSaveAlert {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.4];
	cardAddedNote.alpha = 0;
	[UIView commitAnimations];
}

- (void) startActivityAnimating {
	activityIndicatorView.hidden = NO;	
	[activityIndicatorView startAnimating];
}

- (void) stopActivityAnimating {
	[activityIndicatorView stopAnimating];
	activityIndicatorView.hidden = YES;	
}

@end
