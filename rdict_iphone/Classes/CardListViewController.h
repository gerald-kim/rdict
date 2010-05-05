//
//  CardListViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 11/21/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CardEditViewController.h"
#import "Card.h"

@interface CardListViewController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	NSArray *cards;
	
	IBOutlet UITableView *tableView;
}

@property (nonatomic, retain) NSArray *cards;
@property (nonatomic, retain) UITableView *tableView;

@end
