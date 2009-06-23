//
//  ReviewSessionController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/23/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CardFrontViewController;

@interface ReviewSessionController : UIViewController {
	IBOutlet CardFrontViewController* cardFrontViewController;
}

@property (nonatomic, retain) CardFrontViewController* cardFrontViewController;

@end
