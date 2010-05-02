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
#import "Card.h"

@implementation RDictAppDelegate

@synthesize window;
@synthesize tabBarController;
@synthesize wiktionary;


- (void)applicationDidFinishLaunching:(UIApplication *)application {
	wiktionary = [[Wiktionary alloc] init];

	[window addSubview:tabBarController.view];
	[window makeKeyAndVisible];

	// DebugLog( @"PATH: %@", [[SQLiteInstanceManager sharedManager] databaseFilepath] );
#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR
	[[SQLiteInstanceManager sharedManager] setDatabaseFilepath:@"/tmp/rdict.sqlite3"];
#endif	
	[[UIApplication sharedApplication] setApplicationIconBadgeNumber:[Card countByScheduled]];
	previousTabIndex = 0;
}

- (void)applicationSignificantTimeChange:(UIApplication *)application {
	DebugLog(@"timechange");
#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR
	[[SQLiteInstanceManager sharedManager] setDatabaseFilepath:@"/tmp/rdict.sqlite3"];
#endif
	[[UIApplication sharedApplication] setApplicationIconBadgeNumber:[Card countByScheduled]];	
}



// Optional UITabBarControllerDelegate method
- (void)tabBarController:(UITabBarController *) aTabBarController didSelectViewController:(UIViewController *) aViewController {
	DebugLog( @"previous = %d, current = %d", previousTabIndex, aTabBarController.selectedIndex );
	if( 0 == previousTabIndex && 0 == aTabBarController.selectedIndex ) {
		UINavigationController* navigationController = (UINavigationController*) aViewController;
		[navigationController popViewControllerAnimated:TRUE];
		SearchViewController* searchViewController = [navigationController.viewControllers objectAtIndex:0];
		[searchViewController resetSearchBar];
	}
	previousTabIndex = aTabBarController.selectedIndex;
}

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

- (void)updateReviewTab {
	NSUInteger reviewCount = [Card countByScheduled];
	NSString* title;
	if ( 0 == reviewCount ) {
		title = @"Review";
	} else {
		title = [NSString stringWithFormat:@"Review(%d)", reviewCount];
	}
	[[tabBarController.tabBar.items objectAtIndex:1] setTitle:title];		

}
@end
/*
@implementation UINavigationBar (UINavigationBarCategory)
- (void)drawRect:(CGRect)rect {
	UIColor *color = [UIColor redColor];
	CGContextRef context = UIGraphicsGetCurrentContext();
	CGContextSetFillColor(context, CGColorGetComponents( [color CGColor]));
	CGContextFillRect(context, rect);
	self.tintColor = [UIColor colorWithRed:.7 green:.5 blue:.2 alpha:1];
}
@end
*/