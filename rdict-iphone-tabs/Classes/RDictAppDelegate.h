//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright NHN 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SearchViewController.h"
#import "ReviewViewController.h"

@class SearchViewController;
@class ReviewViewController;

@interface RDictAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    UITabBarController *tabBarController;
	UINavigationController *navigationController;
//    SearchViewController *searchViewController;
//    ReviewViewController *reviewViewController;	
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;
//@property (nonatomic, retain) IBOutlet SearchViewController *searchViewController;
//@property (nonatomic, retain) IBOutlet ReviewViewController *reviewViewController;

@end
