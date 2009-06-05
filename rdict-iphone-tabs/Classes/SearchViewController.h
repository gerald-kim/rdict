//
//  SearchViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SwitchViewController;

@interface SearchViewController : UIViewController {
	SwitchViewController *switchViewController;

}

- (IBAction) testButtonPressed:(id) sender;
@property (nonatomic, retain) SwitchViewController *switchViewController;


@end
