//
//  AboutViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 11/21/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AboutViewController : UIViewController <UIWebViewDelegate>{
	IBOutlet UIWebView *webView;
}

@property (nonatomic, retain) UIWebView *webView;
@end