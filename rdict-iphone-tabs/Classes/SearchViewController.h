//
//  SearchViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DictionaryViewController;

@interface SearchViewController : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	DictionaryViewController *dictionaryViewController;
	IBOutlet UISearchBar *searchBar;
	NSArray *listData;
}

@property (nonatomic, retain) IBOutlet DictionaryViewController *dictionaryViewController;
@property (nonatomic, retain) IBOutlet UISearchBar *searchBar;
@property (nonatomic, retain) NSArray *listData; 

- (IBAction) dictButtonPressed:(id)sender;

@end
