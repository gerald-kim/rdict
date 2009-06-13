//
//  SearchViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DictionaryViewController;
@class Wiktionary;

@interface SearchViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate> {
	DictionaryViewController *dictionaryViewController;
	IBOutlet UISearchBar *searchBar;
	IBOutlet UITableView *tableView;
	Wiktionary* wiktionary;
	NSMutableArray* wordList;
}

@property (nonatomic, retain) IBOutlet DictionaryViewController *dictionaryViewController;
@property (nonatomic, retain) IBOutlet UISearchBar *searchBar;
@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, retain) Wiktionary *wiktionary; 
@property (nonatomic, retain) NSMutableArray *wordList;
@end
