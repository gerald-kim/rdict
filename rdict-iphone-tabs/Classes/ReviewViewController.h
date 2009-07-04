//
//  ReviewViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ReviewSessionController;

@interface ReviewViewController : UIViewController {
	IBOutlet ReviewSessionController* reviewSessionController;
	IBOutlet UILabel* cardInfomationLabel;
	IBOutlet UITextView* scheduleText;
}

@property (nonatomic, retain) ReviewSessionController* reviewSessionController;
@property (nonatomic, retain) UILabel* cardInfomationLabel;
@property (nonatomic, retain) UITextView* scheduleText;

- (IBAction) studyButtonClicked:(id) sender;

@end
