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
	NSDate* deleted;
	NSInteger grade;	
	NSUInteger studyIndex;
}

@property (nonatomic, retain) Card* card;
@property (nonatomic, retain) NSDate* studied;
@property (nonatomic, retain) NSDate* deleted;

@property NSInteger grade;
@property NSUInteger studyIndex;

+ (StudyLog*) lastStudyLogOfCard:(Card *)card;
+ (void) increaseStudyIndex:(Card*) card;
+ (void) deleteStudyLogs:(Card*) card;
- (id) initWithCard:(Card *)theCard;

@end