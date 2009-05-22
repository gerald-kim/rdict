//
//  ReviewViewController.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ReviewViewController : UIViewController {
	IBOutlet UIView *homeView;
}

@property (retain, nonatomic) UIView *homeView;

- (IBAction)reviewButtonPressed:(id)sender;

@end
