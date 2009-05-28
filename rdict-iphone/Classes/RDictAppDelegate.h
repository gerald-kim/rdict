//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HomeViewController;
@class ReviewViewController;
@class Dictionary;

@interface RDictAppDelegate : NSObject <UIApplicationDelegate> {
    IBOutlet UIWindow *window;
	IBOutlet UINavigationController *navController;
	IBOutlet UINavigationController *reviewNavController;
	
	IBOutlet HomeViewController *homeViewController;
	
	ReviewViewController *reviewViewController;
	
	NSMutableArray *cards;
	
	Dictionary *dic;
}

@property (nonatomic, retain) UIWindow *window;
@property (nonatomic, retain) HomeViewController *homeViewController;
@property (nonatomic, retain) ReviewViewController *reviewViewController;
@property (nonatomic, retain) UINavigationController *navController;
@property (nonatomic, retain) UINavigationController *reviewNavController;
@property (nonatomic, retain) NSMutableArray *cards;
@property (nonatomic, retain) Dictionary *dic;

@end


