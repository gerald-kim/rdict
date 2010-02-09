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
	IBOutlet UIView* sparklineView;
	
	NSArray *sectionTitles;
	NSArray *schedules;
	NSArray *cardsForReview;
	NSInteger totalCount;
	NSInteger grade;
	NSInteger score;
}

@property (nonatomic, retain) ReviewSessionController* reviewSessionController;
@property (nonatomic, retain) IBOutlet UITableView* tableView;
@property (nonatomic, retain) IBOutlet UIView* sparklineView;
@property (nonatomic, retain) NSArray *sectionTitles;
@property (nonatomic, retain) NSArray *schedules;
@property (nonatomic, retain) NSArray *cardsForReview;

@end