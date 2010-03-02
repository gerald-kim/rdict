//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Wiktionary;
@class LookupHistory;

@interface DictionaryViewController : UIViewController <UIWebViewDelegate> {
	IBOutlet UIWebView* webView;
	IBOutlet UIImageView* cardAddedNote;
	
	UIBarButtonItem* returnToSearchButton;
	UIBarButtonItem* backButton;
	UIBarButtonItem* forwardButton;
	
	NSString* lemma;
	Wiktionary* wiktionary;
	LookupHistory* lookupHistory;
	bool saveDialogOpened;
}

@property (nonatomic, retain) IBOutlet UIWebView* webView;
@property (nonatomic, retain) IBOutlet UIBarButtonItem* returnToSearchButton;
@property (nonatomic, retain) IBOutlet UIBarButtonItem* backButton;
@property (nonatomic, retain) IBOutlet UIBarButtonItem* forwardButton;
@property (nonatomic, retain) IBOutlet UIImageView* cardAddedNote;
@property (nonatomic, retain) NSString* lemma;
@property (nonatomic, retain) Wiktionary* wiktionary;
@property (nonatomic, retain) LookupHistory* lookupHistory;

-(void) adjustToolBarButtons;
-(void) handleRdictRequest:(NSURL*) url;
@end
