//
//  RDictAppDelegate.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "RDictAppDelegate.h"

#import "SearchViewController.h"
#import "Wiktionary.h"
#import "SQLiteInstanceManager.h"

@implementation RDictAppDelegate

@synthesize window;
@synthesize tabBarController;
@synthesize navigationController;
@synthesize wiktionary;


- (void)applicationDidFinishLaunching:(UIApplication *)application {
	wiktionary = [[Wiktionary alloc] init];

    [window addSubview:tabBarController.view];
	[window makeKeyAndVisible];
	
#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR
	[[SQLiteInstanceManager sharedManager] setDatabaseFilepath:@"/tmp/rdict.sqlite3"];
#endif
}


/*
// Optional UITabBarControllerDelegate method
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
}
*/

/*
// Optional UITabBarControllerDelegate method
- (void)tabBarController:(UITabBarController *)tabBarController didEndCustomizingViewControllers:(NSArray *)viewControllers changed:(BOOL)changed {
}
*/


- (void)dealloc {
	[wiktionary release];
    [tabBarController release];
    [window release];
    [super dealloc];
}

@end

