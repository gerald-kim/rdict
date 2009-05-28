//
//  RDictAppDelegate.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "RDictAppDelegate.h"
#import "HomeViewController.h"
#import "Card.h"
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
	self.dic = [[Dictionary alloc] initWithDicPath:@"/Users/sbodnar/programming/projects/iphone-rdict/RDict/en-brief.db"];
	
	Card *a = [[Card alloc] initWithQuestion:@"Is it a fish?" Answer:@"Yes, it is."];
	[a save];
	
	Card *b = [[Card alloc] initWithQuestion:@"What colour is it?" Answer:@"It's green."];
	[b save];
	
	Card *c = [[Card alloc] initWithQuestion:@"How big is it?" Answer:@"Very big."];
	[c save];
	
	Card *d = [[Card alloc] initWithQuestion:@"Where's the beef?" Answer:@"In your pocket."];
	[d save];
	
	self.cards = [[NSMutableArray alloc] initWithArray:[Card allObjects]];
		
	[a release];	
	[b release];
	[c release];
	[d release];
	
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