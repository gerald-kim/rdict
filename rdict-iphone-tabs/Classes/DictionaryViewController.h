//
//  DictionaryViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface DictionaryViewController : UIViewController {
	IBOutlet UIButton* backButton;
//	IBOutlet UINavigationItem* navigationItem;
	NSString* lemma;
}

@property (nonatomic, retain) IBOutlet UIButton* backButton;
//@property (nonatomic, retain) IBOutlet UINavigationItem* navigationItem;
@property (nonatomic, retain) NSString* lemma;

- (IBAction) titleButtonPressed:(id)sender;


@end
