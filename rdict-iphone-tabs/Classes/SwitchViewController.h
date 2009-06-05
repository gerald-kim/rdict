//
//  SwitchViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SearchViewController;
@class DictionaryViewController;

@interface SwitchViewController : UIViewController {
	SearchViewController* searchViewController;
	DictionaryViewController* dictionaryViewController;
}

@property (nonatomic, retain) IBOutlet SearchViewController* searchViewController;
@property (nonatomic, retain) IBOutlet DictionaryViewController* dictionaryViewController;

-(void) showDictionary;

@end
