//
//  CardBackViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CardViewController : UIViewController {
	IBOutlet UILabel* questionLabel;
	IBOutlet UITextView* answerTextView;
}

@property (nonatomic, retain) IBOutlet UILabel* questionLabel;
@property (nonatomic, retain) IBOutlet UITextView* answerTextView;

@end
