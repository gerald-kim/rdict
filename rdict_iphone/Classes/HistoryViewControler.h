//
//  HistoryViewControler.h
//  RDict
//
//  Created by Jaewoo Kim on 9/16/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DictionaryViewController.h"

@interface HistoryViewControler : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView *tableView;
	DictionaryViewController *dictionaryViewController;

	NSArray* histories;
	NSMutableDictionary* sectionInfo;
}

@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, retain) IBOutlet DictionaryViewController *dictionaryViewController;
@property (nonatomic, retain) NSArray* histories;
@property (nonatomic, retain) NSMutableDictionary* sectionInfo;

- (IBAction) clearButtonClicked:(id) sender;

@end
