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

@implementation DictionaryViewController
@synthesize webView;
@synthesize cardAddedNote;
@synthesize lemma;
@synthesize wiktionary;
@synthesize activityIndicatorView;

- (void) viewDidLoad {
	[super viewDidLoad];
	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	self.wiktionary = delegate.wiktionary;
}

- (void) viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	self.navigationController.navigationBarHidden = NO;
	activityIndicatorView.hidden = NO;
	[activityIndicatorView startAnimating];
	
	[self showWordDefinition: lemma];
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
	[webView release];
	[wiktionary release];
	[lemma release];
    [super dealloc];
}

#pragma mark -

- (void) showWordDefinition: (NSString *) query  {
	self.title = query;

	[activityIndicatorView startAnimating];
	activityIndicatorView.hidden = NO;
	
	WordEntry* entry = [wiktionary wordEntryByLemma:query];
	if ( entry ) {
		[entry decorateDefinition];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		
		[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
				
		[entry release];
		//	[baseURL release];
		//	[path release];
	} else {
		[activityIndicatorView stopAnimating];
		activityIndicatorView.hidden = YES;

		[webView stringByEvaluatingJavaScriptFromString:@"alert( 'Sorry. No definition for that word' );"];		
	}
}

- (void) handleWordLookUp: (NSURL *) url  {
	NSArray *strings = [[url query] componentsSeparatedByString: @"="];
	NSString *clickedWord = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	
	[self showWordDefinition:clickedWord];	
}

- (void) handleRdictRequest:(NSURL*) url {
	if ([@"save" isEqualToString: [url host]]) {		
		NSLog( @"Save called %@", [url host] );	
		
		NSArray *strings = [[url query] componentsSeparatedByString: @"="];
		NSString *selectedDefinition = [[strings objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		Card* card = [[Card alloc] initWithQuestion:self.lemma andAnswer:selectedDefinition];
		[card save];
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
	[UIView setAnimationDuration:1.0];
	cardAddedNote.alpha = 0.9;
	[UIView setAnimationDelegate:self];
	[UIView setAnimationDidStopSelector:@selector(fadeAlertOut)];
	[UIView commitAnimations];
}

-(void) fadeAlertOut {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.7];
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


@end
