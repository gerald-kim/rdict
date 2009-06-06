//
//  SearchViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DictionaryViewController;

@interface SearchViewController : UIViewController {

	DictionaryViewController *dictionaryViewController;
}

@property (nonatomic, retain) IBOutlet DictionaryViewController *dictionaryViewController;

- (IBAction) dictButtonPressed:(id)sender;

@end
