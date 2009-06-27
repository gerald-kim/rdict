//
//  ReviewSessionController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CardFrontViewController;
@class CardBackViewController;

@interface ReviewSessionController : UIViewController {
	CardFrontViewController* cardFrontViewController;
	CardBackViewController* cardBackViewController;
}

@property (nonatomic, retain) CardFrontViewController* cardFrontViewController;
@property (nonatomic, retain) CardBackViewController* cardBackViewController;

- (IBAction) showAnswerButtonClicked : (id) sender;
- (IBAction) answerButtonClicked : (id) sender;	

@end
