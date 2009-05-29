//
//  DictionaryViewController.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DictionarySearchView;

@interface DictionaryViewController : UIViewController {
	IBOutlet UITextField *usersWord;
	IBOutlet UIWebView *searchResultsPane;
}

@property (retain, nonatomic) UITextField *usersWord;
@property (retain, nonatomic) UIWebView *searchResultsPane;

- (IBAction)textFieldDoneEditing:(id)sender;

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType;

@end
