//
//  CardFrontController.h
//  RDict
//
//  Created by Stephen Bodnar on 25/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CardBackController;

@interface CardFrontController : UIViewController {
	CardBackController *cardBackController;
	
	IBOutlet UILabel *questionLabel;
}

@property (retain, nonatomic) CardBackController *cardBackController;
@property (retain, nonatomic) UILabel *questionLabel;

- (IBAction)viewAnswerButtonPressed:(id) sender;
@end
