//
//  ReviewViewController.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RDictAppDelegate.h"

@class ReviewHomeViewController;
@class CardFrontController;

@interface ReviewViewController : UIViewController {
	ReviewHomeViewController *reviewHomeViewController;
	CardFrontController *cardFrontController;
	
	IBOutlet UILabel *mesg;
	IBOutlet UIButton *startReviewButton;
}

@property (retain, nonatomic) ReviewHomeViewController *reviewHomeViewController;
@property (retain, nonatomic) CardFrontController *cardFrontController;
@property (retain, nonatomic) UILabel *mesg;
@property (retain, nonatomic) UIButton *startReviewButton;

- (IBAction)startReviewButtonPressed:(id)sender;

@end
