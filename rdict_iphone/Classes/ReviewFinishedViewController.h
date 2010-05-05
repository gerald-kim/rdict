//
//  ReviewFinishedViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/28/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ReviewFinishedViewController : UIViewController<UINavigationControllerDelegate> {
	NSArray* scheduledCards;
	
	IBOutlet UILabel* totalCardLabel;
}

@property (nonatomic, retain) NSArray* scheduledCards;
@property (nonatomic, retain) IBOutlet UILabel* totalCardLabel;

@end
