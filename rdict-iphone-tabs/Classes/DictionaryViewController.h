//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface DictionaryViewController : UIViewController {
	IBOutlet UIButton* titleButton;
}

@property (nonatomic, retain) IBOutlet UIButton* titleButton;

- (IBAction) titleButtonPressed:(id)sender;


@end
