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
	BOOL lastLog;
}

@property (nonatomic, retain) Card* card;
@property (nonatomic, retain) NSDate* studied;
@property NSInteger grade;
@property BOOL lastLog;

+ (StudyLog*) lastStudyLogOfCard:(Card *)card;
- (id) initWithCard:(Card *)theCard;
- (void) unsetLastLog;

@end