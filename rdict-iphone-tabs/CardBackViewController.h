//
//  CardBackViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface CardBackViewController : UIViewController {

	IBOutlet UISegmentedControl* segmentControl;
}

@property (nonatomic, retain) IBOutlet UISegmentedControl* segmentControl;

@end
