//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SwitchViewController;

@interface DictionaryViewController : UIViewController {
	SwitchViewController *switchViewController;	
	IBOutlet UIButton *titleButton;
}

@property (nonatomic, retain) SwitchViewController *switchViewController;
@property (nonatomic, retain) IBOutlet UIButton *titleButton;


- (IBAction) prevButtonPressed:(id) sender;
- (IBAction) nextButtonPressed:(id) sender;
- (IBAction) titleButtonPressed:(id) sender;


@end
