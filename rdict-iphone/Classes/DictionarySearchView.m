//
//  DictionarySearchPaneController.m
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "DictionarySearchView.h"

@implementation DictionarySearchView

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
	
	printf("hidey ho");
	
	if (navigationType == UIWebViewNavigationTypeLinkClicked) {
		
		NSURL *URL = [request URL];	
		
		if ([[URL scheme] isEqualToString:@"http"]) {
			[[UIApplication sharedApplication] openURL: URL];
		}
		
		return NO;
	}	
	return YES;   
}

@end
