//
//  HomeViewController.h

//
//  Created by Stephen Bodnar on 14/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DictionaryViewController.h"
#import "ReviewViewController.h"
#import "RDictAppDelegate.h"

@class DictionaryViewController;
@class ReviewViewController;

@interface HomeViewController : UIViewController {
	DictionaryViewController *dicViewController;
	ReviewViewController *reviewViewController;
	
	IBOutlet UIButton *goToReviewButton;
	
	NSArray *controllers;
}

@property (retain, nonatomic) DictionaryViewController *dicViewController;
@property (retain, nonatomic) ReviewViewController *reviewViewController;
@property (nonatomic, retain) NSArray *controllers;
@property (retain, nonatomic) UIButton *goToReviewButton;

- (IBAction)dicButtonPressed:(id)sender;
- (IBAction)reviewButtonPressed:(id)sender;

@end
