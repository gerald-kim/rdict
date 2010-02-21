//
//  CardStatistics.h
//  RDict
//
//  Created by Jaewoo Kim on 2/22/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"


@interface CardStatistics : SQLitePersistentObject {
	NSDate* date;
	NSUInteger cardCount;
	double averageScore;
}

@property (nonatomic, retain) NSDate* date;
@property NSUInteger cardCount;
@property double averageScore;

- (id) initWithDate:(NSDate*)theDate cardCount:(NSUInteger) theCardCount averageScore:(double) theAverageScore;

@end
