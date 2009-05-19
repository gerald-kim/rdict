//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HomeViewController;

@interface RDictAppDelegate : NSObject <UIApplicationDelegate> {
    IBOutlet UIWindow *window;
	IBOutlet UINavigationController *navController;
	
	IBOutlet HomeViewController *homeViewController;
}

@property (nonatomic, retain) UIWindow *window;
@property (nonatomic, retain) HomeViewController *homeViewController;
@property (nonatomic, retain) UINavigationController *navController;

@end


