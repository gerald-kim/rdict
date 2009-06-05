//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Jaewoo Kim on 6/5/09.
//  Copyright NHN 2009. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface RDictAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    UITabBarController *tabBarController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;

@end
