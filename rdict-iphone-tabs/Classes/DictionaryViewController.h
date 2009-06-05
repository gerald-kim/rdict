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
	IBOutlet UINavigationItem *navigationItem;

	SwitchViewController *switchViewController;	
}

@property (nonatomic, retain) SwitchViewController *switchViewController;
@property (nonatomic, retain) UINavigationItem *navigationItem;

- (IBAction) testButtonPressed:(id) sender;


@end
