//
//  DicSearchResultsViewController.h
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface DicSearchResultsViewController : UIViewController {
	IBOutlet UIWebView *searchResultPane;
}

@property (retain, nonatomic) UIWebView *searchResultsPane;

- (void)setContent;
@end
