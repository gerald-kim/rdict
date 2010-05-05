//
//  CardEditViewController.h
//  RDict
//
//  Created by Jaewoo Kim on 11/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Card.h"

@interface CardEditViewController : UIViewController {
	Card *card;

	IBOutlet UIButton *deleteButton;
	IBOutlet UITextView *cardAnswerView;
}

@property (nonatomic, retain) Card *card;
@property (nonatomic, retain) IBOutlet UITextView *cardAnswerView;
@property (nonatomic, retain) IBOutlet UIButton *deleteButton;

- (IBAction) doneButtonClicked : (id) sender;
- (IBAction) deleteButtonClicked : (id) sender;

@end
