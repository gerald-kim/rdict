// CustomUITabBarController.m

#import "CustomUITabBarController.h"

@implementation CustomUITabBarController

@synthesize tabBar1;

- (void)viewDidLoad {
	NSLog( @"CUITBC.viewDidLoad" );
	[super viewDidLoad];

	UIImageView *img = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"UITabBar1.png"]];
	img.frame = CGRectOffset(img.frame, 0, 1);
	[tabBar1 insertSubview:img atIndex:0];
	[img release];
}

@end