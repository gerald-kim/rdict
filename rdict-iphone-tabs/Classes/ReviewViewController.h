//
//  ReviewViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ReviewSessionController;

@interface ReviewViewController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	ReviewSessionController* reviewSessionController;
	IBOutlet UITableView* tableView;

	NSArray *sectionTitles;
	NSInteger scheduledCount;
	NSInteger todayCount;
	NSInteger totalCount;
	NSInteger grade;
}

@property (nonatomic, retain) ReviewSessionController* reviewSessionController;
@property (nonatomic, retain) IBOutlet UITableView* tableView;
@property (nonatomic, retain) NSArray *sectionTitles;

- (IBAction) studyButtonClicked:(id) sender;
- (IBAction) rescheduleButtonClicked:(id) sender;


@end