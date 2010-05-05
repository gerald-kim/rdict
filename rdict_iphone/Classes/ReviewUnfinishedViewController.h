//
//  ReviewUnfinishedViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/28/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ReviewUnfinishedViewController : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	NSArray* scheduledCards;
	NSArray* uncertainCards;
	
	IBOutlet UITableView* tableView;
}

@property (nonatomic, assign) NSArray* scheduledCards;
@property (nonatomic, assign) NSArray* uncertainCards;

@property (nonatomic, retain) UITableView* tableView;
@end
