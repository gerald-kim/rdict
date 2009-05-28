//
//  CardBackController.h
//  RDict
//
//  Created by Stephen Bodnar on 25/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface CardBackController : UIViewController {
	IBOutlet UILabel *questionLabel;
	IBOutlet UIWebView *answerView;
	
	IBOutlet UIButton *easyButton;
	IBOutlet UIButton *mediumButton;
	IBOutlet UIButton *hardButton;
	IBOutlet UIButton *iForgotButton;
}

@property(nonatomic, retain) UILabel *questionLabel;
@property(nonatomic, retain) UIWebView *answerView;
@property(nonatomic, retain) UIButton *easyButton;
@property(nonatomic, retain) UIButton *mediumButton;
@property(nonatomic, retain) UIButton *hardButton;
@property(nonatomic, retain) UIButton *iForgotButton;

- (IBAction)gradeButtonPressed: (id) sender;
- (int) getGradeByButton: (UIButton *) button;
@end
