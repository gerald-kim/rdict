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

-(void) saveCard: (NSString *) selectedDefinition;
-(void) lookUpDictionary: (NSString *) aLemma lookupMethod: (NSString *) rdictMethod;
	
-(void) showSaveAlert;
-(void) fadeOutSaveAlert;

-(void) startActivityAnimating:(BOOL) localRequest;
-(void) stopActivityAnimating;

@end

#define ALERT_CLIPBOARD 0
#define ALERT_HELP 1

@implementation DictionaryViewController
@synthesize webView;
@synthesize cardAddedNote;
@synthesize lemma;
@synthesize wiktionary;
@synthesize lookupHistory;
@synthesize titleBackup;
@synthesize leftButtonBackup;
@synthesize rightButtonBackup;
@synthesize backButton;
@synthesize forwardButton;


- (void) viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
	DebugLog( @"dvc load" );
}

- (void) viewWillAppear:(BOOL)animated {	
	DebugLog( @"DVC.viewWillAppear lemma[%@]", [lemma stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] );  
	[super viewWillAppear:animated];

	self.navigationController.navigationBarHidden = NO;
	
	self.titleBackup = self.title;
	self.leftButtonBackup = self.navigationItem.leftBarButtonItem;
	self.rightButtonBackup = self.navigationItem.rightBarButtonItem;	
	
	UIImage* backImage = [UIImage imageNamed:@"icon_arrow_left.png"];
	self.backButton = [[UIBarButtonItem alloc] initWithImage:backImage style:UIBarButtonItemStylePlain target:self action:@selector(handleGoBackClick:)];
	
	UIImage* forwardImage = [UIImage imageNamed:@"icon_arrow_right.png"];
	self.forwardButton = [[UIBarButtonItem alloc] initWithImage:forwardImage style:UIBarButtonItemStylePlain target:self action:@selector(handleGoForwardClick:)];
	self.forwardButton.enabled = NO;
	self.navigationItem.rightBarButtonItem = forwardButton;
	
	lookupHistory = [[LookupHistory alloc] init];

	NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookup/?lemma=%@", [lemma stringByReplacingOccurrencesOfString:@" " withString:@"%20"]]];
	NSURLRequest* request = [NSURLRequest requestWithURL:url];
	[webView loadRequest:request];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
										selector:@selector(clipboardChanged:)
										name:UIPasteboardChangedNotification object:nil];
}

- (void) viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
}	

- (void) viewWillDisappear:(BOOL)animated {
	DebugLog(@"DVC.viewWillDisappear");
	
	[super viewWillDisappear:animated];
//	self.navigationController.navigationBarHidden = YES;
	self.title = self.titleBackup;
	self.navigationItem.leftBarButtonItem = self.leftButtonBackup;
	self.navigationItem.rightBarButtonItem = self.rightButtonBackup;
	[self.backButton release];
	[self.forwardButton release];
	
	[lookupHistory release];
	lookupHistory = nil;
	
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UIPasteboardChangedNotification object:nil];
}

- (void) viewDidDisappear:(BOOL)animated {
	DebugLog(@"DVC.viewDidDisappear");
	[webView loadHTMLString:@"" baseURL:nil];
	[super viewDidDisappear:animated];
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
	DebugLog( @"DVC.shouldStartLoadWithRequest type:%d, url: %@", navigationType, [[request URL] absoluteString] );
	
	NSURL *url = [request URL];
	if( [@"rdict" isEqualToString:[url scheme]] ) {
		[self handleRdictRequest: url];
		return NO;
	} 
	
	if ( navigationType == UIWebViewNavigationTypeLinkClicked && [[[request URL] scheme] hasPrefix:@"http"]  ) {
		[self startActivityAnimating:FALSE];
		[lookupHistory addHistory:url];
	}

	return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)aWebView {
	[self stopActivityAnimating];
	[self adjustToolBarButtons];
	
	NSString *url = [[webView.request URL] absoluteString];
	DebugLog( @"url: %@", url );
	if( [url hasPrefix:@"http"] ) {
		self.title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
	} else if( [url hasPrefix:@"file://"] ) {
		[webView stringByEvaluatingJavaScriptFromString:@"document.getElementById('jqueryjs').src = 'jquery-1.3.2.min.js'"];
	}
}

- (void)webView:(UIWebView *)aWebView didFailLoadWithError:(NSError *)error{
	[self stopActivityAnimating];
	
 	DebugLog( @"Error: %@", error );
	
    NSString *errorMsg = nil;
	
    if ([[error domain] isEqualToString:NSURLErrorDomain]) {
        switch ([error code]) {
			case NSURLErrorCancelled:
				//ignore Canceled
				return;
            case NSURLErrorCannotFindHost:
                errorMsg = NSLocalizedString(@"Cannot find specified host.", nil);
                break;
            case NSURLErrorCannotConnectToHost:
                errorMsg = NSLocalizedString(@"Cannot connect to specified host. Server may be down.", nil);
                break;
            case NSURLErrorNotConnectedToInternet:
                errorMsg = NSLocalizedString(@"Cannot connect to the internet. Service may not be available.", nil);
                break;
            default:
                errorMsg = [error localizedDescription];
                break;
        }
    } else {
        errorMsg = [error localizedDescription];
    }
	
    UIAlertView *av = [[UIAlertView alloc] initWithTitle:
					   NSLocalizedString(@"Error Loading Page", nil)
												 message:errorMsg delegate:self
									   cancelButtonTitle:@"Cancel" otherButtonTitles:nil];
    [av show];
    [av release];
}

#pragma mark -
#pragma mark Clipboard

- (BOOL)canPerformAction:(SEL)action withSender:(id)sender {
	if (action == @selector(copy:)) {
		return YES;
	}
	return NO;
}

- (void) clipboardChanged:(NSNotification *)notification
{
	if( saveDialogOpened ) {
		return;
	}
	saveDialogOpened = YES;

	DebugLog(@"DVC.Clipboardchanged : %@", [UIPasteboard generalPasteboard].string);
	
	UIAlertView *alert = [[UIAlertView alloc] init];
	[alert setTitle:@"Save card"];
	[alert setMessage:[NSString stringWithFormat:@"Save the card-'%@' with selected definition?", self.lemma]];
	[alert setDelegate:self];
	[alert addButtonWithTitle:@"Cancel"];
	[alert addButtonWithTitle:@"Save"];
	[alert show];
	[alert release];
	alert.tag = ALERT_CLIPBOARD;
}

#pragma mark -
#pragma mark RDict Request
- (void) handleRdictRequest:(NSURL*) url {
	NSString *rdictMethod = [url host];
	NSArray *paramaters = [[url query] componentsSeparatedByString: @"="];
	NSString *parameter = [[paramaters objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

	DebugLog( @"DVC.handleRdictRequest method[%@], parameter[%@]", rdictMethod, parameter );  
	if ([@"save" isEqualToString: rdictMethod]) {		
		[self saveCard: parameter];
	} else if ( [rdictMethod hasPrefix:@"lookup"] ){
		[self lookUpDictionary:parameter lookupMethod: rdictMethod];
	
	}
}

- (void) saveCard: (NSString *) selectedDefinition  {
	[Card saveCardWithQuestion:self.lemma andAnswer:selectedDefinition];
	[self showSaveAlert];
}

- (void) lookUpDictionary: (NSString *) aLemma lookupMethod: (NSString *) rdictMethod  {
//	[self startActivityAnimating:YES];
	DebugLog(@"lookupDictionary: %@", aLemma);

	WordEntry* entry = [wiktionary wordEntryByLemma:aLemma];	
	if (entry) {
		DebugLog(@"found entry: %@", entry.lemma);

		self.title = aLemma;
		self.lemma = aLemma;
		
		[entry decorateDefinition];
		
		NSString *path = [[NSBundle mainBundle] bundlePath];
		NSURL *baseURL = [NSURL fileURLWithPath:path];	
		
		[webView loadHTMLString:entry.definitionHtml baseURL:baseURL];
	
//		[webView loadHTMLString:@"<html><body>done</body></html>" baseURL:baseURL];
		
		[entry release];
		
		if( [ @"lookup" isEqualToString: rdictMethod] ) {
			NSURL* urlForHistory = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookuphistory/?lemma=%@", [aLemma stringByReplacingOccurrencesOfString:@" " withString:@"%20"]]];
			[lookupHistory addHistory:urlForHistory];
		}
	} else {
		DebugLog(@"not found entry: %@", aLemma);
//		[self stopActivityAnimating];
		
		[webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"alert( \"Sorry, Vocabulator doesn't have a definition for \\\"%@\\\".\" );", aLemma]];
	}
	
}

#pragma mark -
#pragma mark LookupHistory
-(void) adjustToolBarButtons {
 	self.navigationItem.leftBarButtonItem = [lookupHistory canGoBack] ? backButton : leftButtonBackup;
	self.forwardButton.enabled = [lookupHistory canGoForward];
}

- (void)handleGoBackClick:(id)sender {
	DebugLog( @"DVC.handleGoBackClick" );
	[webView stopLoading];
	NSURLRequest* request = [NSURLRequest requestWithURL:[lookupHistory goBack]];
	[webView loadRequest:request];
}

- (void)handleGoForwardClick:(id)sender {
	[webView stopLoading];
	NSURLRequest* request = [NSURLRequest requestWithURL:[lookupHistory goForward]];
	[webView loadRequest:request];
}

#pragma mark -
#pragma mark Animation, Alert

-(void) showSaveAlert {
	DebugLog( @"Card counts : %d", [Card count] );
	NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];  
	if( ![userDefaults boolForKey:@"REMOVE_SAVE_ALERT"] ) {
		UIAlertView *testAlert = [[UIAlertView alloc] initWithTitle:@"Card added" 
					message:@"AmplioWords created a card with the definition you selected. "
							"You'll first see the card in tomorrow's review. "
							"Future appearances depend on your memory's strength: "
							"the system predicts when you will forget the card and schedules it just before.\n\n" 
							"Show this message next time?"
					delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
		[testAlert show];
		testAlert.tag = ALERT_HELP;
	
		((UILabel*)[[testAlert subviews] objectAtIndex:1]).textAlignment = UITextAlignmentLeft;
	} else {
		[UIView beginAnimations:nil context:NULL];
		[UIView setAnimationDuration:0.7];
		cardAddedNote.alpha = 0.9;
		[UIView setAnimationDelegate:self];
		[UIView setAnimationDidStopSelector:@selector(fadeOutSaveAlert)];
		[UIView commitAnimations];
	}
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	DebugLog(@"DVC.clickedButtonAtIndex: %d, %d", alertView.tag, buttonIndex);
	if( alertView.tag == ALERT_HELP ) {
		if (buttonIndex == 0) {
//			DebugLog(@"DVC.don't show again");
			NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];  
			//delayBeforeDialing = [userDefaults floatForKey:@"delayBeforeDialing"];  
			[userDefaults setBool:TRUE forKey:@"REMOVE_SAVE_ALERT"];  
		}
	} else if ( alertView.tag == ALERT_CLIPBOARD ) {
		saveDialogOpened = NO;
		if (buttonIndex == 1) {
			DebugLog(@"Save definition");			
			[self saveCard:[UIPasteboard generalPasteboard].string];
		}
	}
}


-(void) fadeOutSaveAlert {
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationDuration:0.4];
	cardAddedNote.alpha = 0;
	[UIView commitAnimations];
}

- (void) startActivityAnimating:(BOOL) localRequest {
	[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

- (void) stopActivityAnimating {
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

@end
