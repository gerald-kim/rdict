//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright ampliostudios 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SearchViewController.h"
#import "ReviewViewController.h"

@class SearchViewController;
@class ReviewViewController;
@class Wiktionary;

@interface RDictAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    IBOutlet UITabBarController *tabBarController;
//    SearchViewController *searchViewController;
//    ReviewViewController *reviewViewController;	
	
	Wiktionary *wiktionary;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;
@property (nonatomic, retain) Wiktionary *wiktionary;

//@property (nonatomic, retain) IBOutlet SearchViewController *searchViewController;
//@property (nonatomic, retain) IBOutlet ReviewViewController *reviewViewController;

@end
