//
//  RDictAppDelegate.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright NHN 2009. All rights reserved.
//

#import "RDictAppDelegate.h"

#import "SearchViewController.h"

@implementation RDictAppDelegate

@synthesize window;
@synthesize tabBarController;
@synthesize navigationController;
//@synthesize searchViewController;
//@synthesize reviewViewController;


- (void)applicationDidFinishLaunching:(UIApplication *)application {
	/*
	navigationController = [[UINavigationController alloc] init];
//	navigationController.navigationBarHidden = TRUE;
	
	searchViewController = [[SearchViewController alloc] initWithNibName:@"SearchView" bundle:nil];
	searchViewController.tabBarItem.title = @"Search";
	searchViewController.navigationItem.title = @"Search";
	navigationController.viewControllers = [NSArray arrayWithObjects:searchViewController, nil];

	reviewViewController = [[SearchViewController alloc] initWithNibName:@"ReviewView" bundle:nil];
	reviewViewController.tabBarItem.title = @"Review";
	reviewViewController.navigationItem.title = @"Review";
	
//	navigationController = [[[UINavigationController alloc]
//							 initWithRootViewController:searchViewController] autorelease];

	tabBarController = [[UITabBarController alloc]init];
	tabBarController.viewControllers = [NSArray arrayWithObjects:searchViewController, reviewViewController, nil];
	*/
    [window addSubview:tabBarController.view];
	[window makeKeyAndVisible];
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
    [tabBarController release];
    [window release];
    [super dealloc];
}

@end

