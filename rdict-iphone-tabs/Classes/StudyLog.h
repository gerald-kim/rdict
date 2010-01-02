//
//  StudyLog.h
//  RDict
//
//  Created by Jaewoo Kim on 1/2/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"
#import "Card.h"

@interface StudyLog : SQLitePersistentObject {
	Card* card;
	NSDate* studied;

	NSInteger grade;	
}

@property (nonatomic, retain) Card* card;
@property (nonatomic, retain) NSDate* studied;
@property NSInteger grade;

- (id) initWithCard:(Card *)theCard;

@end