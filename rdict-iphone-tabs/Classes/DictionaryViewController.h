//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Wiktionary;

@interface DictionaryViewController : UIViewController {
	IBOutlet UIWebView* webView;
	NSString* lemma;
	Wiktionary* wiktionary;
}

@property (nonatomic, retain) IBOutlet UIWebView* webView;
@property (nonatomic, retain) NSString* lemma;
@property (nonatomic, retain) Wiktionary* wiktionary;

@end
