//
//  HistoryViewControler.h
//  RDict
//
//  Created by Jaewoo Kim on 9/16/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface HistoryViewControler : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	NSArray* histories;
	IBOutlet UITableView *tableView;

}

@property (nonatomic, retain) NSArray* histories;
@property (nonatomic, retain) IBOutlet UITableView *tableView;

@end
