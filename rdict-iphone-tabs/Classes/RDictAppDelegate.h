//
//  RDictAppDelegate.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright ampliostudios 2009. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "rdict.h"
#import "SearchViewController.h"
#import "ReviewViewController.h"

#define HEXCOLOR(c) [UIColor colorWithRed:((c>>24)&0xFF)/255.0 \
									green:((c>>16)&0xFF)/255.0 \
									blue:((c>>8)&0xFF)/255.0 \
									alpha:((c)&0xFF)/255.0];
#define BGCOLOR HEXCOLOR(0xFCFBF6FF);

@class SearchViewController;
@class ReviewViewController;
@class Wiktionary;

@interface RDictAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    IBOutlet UITabBarController *tabBarController;
//    SearchViewController *searchViewController;
//    ReviewViewController *reviewViewController;	
	
	NSUInteger previousTabIndex;
	Wiktionary *wiktionary;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;
@property (nonatomic, retain) Wiktionary *wiktionary;

//@property (nonatomic, retain) IBOutlet SearchViewController *searchViewController;
//@property (nonatomic, retain) IBOutlet ReviewViewController *reviewViewController;
- (void)updateReviewTab;


@end
