//
//  MiscViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 11/18/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AboutViewController.h"
#import "HelpViewController.h"

@interface MiscViewController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	NSArray *listData;


	AboutViewController *aboutViewController;
	HelpViewController *helpViewController;
	IBOutlet UITableView *tableView;
}

@property (nonatomic, retain) NSArray *listData;
@property (nonatomic, retain) IBOutlet UITableView *tableView;


@end
