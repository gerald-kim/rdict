//
//  RDictAppDelegate.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "RDictAppDelegate.h"
#import "HomeViewController.h"

@implementation RDictAppDelegate
@synthesize window;
@synthesize homeViewController;
@synthesize navController;

- (void)applicationDidFinishLaunching:(UIApplication *)application {    
	self.navController = [[UINavigationController alloc] initWithRootViewController:homeViewController];
    [window addSubview:navController.view];
	[window makeKeyAndVisible];	
}


- (void)dealloc {
    [self.homeViewController release];
    [self.navController release];
	[window release];
    [super dealloc];
}

@end