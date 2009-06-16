//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Wiktionary;

@interface DictionaryViewController : UIViewController <UIWebViewDelegate> {
	IBOutlet UIWebView* webView;
	IBOutlet UIActivityIndicatorView *activityIndicatorView;
	NSString* lemma;
	Wiktionary* wiktionary;
}

@property (nonatomic, retain) IBOutlet UIWebView* webView;
@property (nonatomic, retain) 	IBOutlet UIActivityIndicatorView *activityIndicatorView;
@property (nonatomic, retain) NSString* lemma;
@property (nonatomic, retain) Wiktionary* wiktionary;

@end
