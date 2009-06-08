//
//  RDictAppDelegate.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "RDictAppDelegate.h"
#import "HomeViewController.h"
#import "Dictionary.h"

@implementation RDictAppDelegate
@synthesize window;
@synthesize homeViewController;
@synthesize reviewViewController;
@synthesize navController;
@synthesize reviewNavController;
@synthesize cards;
@synthesize dic;

- (void)applicationDidFinishLaunching:(UIApplication *)application {
	
	//old/iphone-rdict/RDict/en-brief.db
	self.dic = [[Dictionary alloc] initWithDicPath:@"/Users/evacuee/workspace/rdict/pywiktionary/enwiktionary-lastest.db/word.db"];
	
	self.navController = [[UINavigationController alloc] initWithRootViewController:homeViewController];
    [window addSubview:navController.view];
	[window makeKeyAndVisible];
}


- (void)dealloc {
	[self.dic release];
	[self.cards release];
    [self.homeViewController release];
    [self.navController release];
	[window release];
    [super dealloc];
}

@end