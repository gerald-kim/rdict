//
//  CardFrontViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ReviewSessionController;

@interface CardFrontViewController : UIViewController {
	IBOutlet ReviewSessionController* reviewSessionController;
}

@property (nonatomic, retain) ReviewSessionController* reviewSessionController;

@end
