//
//  CardFrontViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/27/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface CardFrontViewController : UIViewController {
	IBOutlet UILabel* statusLabel;
	IBOutlet UILabel* questionLabel;
}

@property (nonatomic, retain) IBOutlet UILabel* statusLabel;
@property (nonatomic, retain) IBOutlet UILabel* questionLabel;


@end
