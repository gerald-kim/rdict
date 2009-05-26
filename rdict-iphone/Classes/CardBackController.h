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
	IBOutlet UILabel *answerLabel;
}

@property(nonatomic, retain) UILabel *questionLabel;
@property(nonatomic, retain) UILabel *answerLabel;

- (IBAction)viewOKButtonPressed:(id) sender;
@end
