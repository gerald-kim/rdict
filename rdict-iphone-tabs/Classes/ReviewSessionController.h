//
//  ReviewSessionController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ReviewSessionController : UIViewController {
	UIViewController* cardFrontViewController;
	UIViewController* cardBackViewController;
}

@property (nonatomic, retain) UIViewController* cardFrontViewController;
@property (nonatomic, retain) UIViewController* cardBackViewController;

- (IBAction) showAnswerButtonClicked : (id) sender;
- (IBAction) answerButtonClicked : (id) sender;	

@end
