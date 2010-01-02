//
//  Card.h
//  RDict
//
//  Created by Jaewoo Kim on 6/17/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"

#define SECONDS_IN_ONE_DAY 60*60*24

@interface Card : SQLitePersistentObject {
	NSString* question;
	NSString* answer;

	int repsSinceLapse;
	double easiness;
	int interval;
	int grade;
	int finalGrade;

	NSDate* scheduled;
	NSDate* studied;
	
	NSDate* created;
	NSDate* updated;
	NSDate* deleted;
}

@property (nonatomic, retain) NSString* question;
@property (nonatomic, retain) NSString* answer;

@property int repsSinceLapse;
@property double easiness;
@property int interval;
@property int grade;
@property int finalGrade;

@property (nonatomic, retain) NSDate* scheduled;
@property (nonatomic, retain) NSDate* studied;
@property (nonatomic, retain) NSDate* created;
@property (nonatomic, retain) NSDate* updated;
@property (nonatomic, retain) NSDate* deleted;

+ (NSInteger) countByScheduled;
+ (NSArray*) findByScheduled;
+ (NSString*) searchedTodayCriteria;

+ (NSInteger) countByToday;
+ (NSArray*) findByToday;
+ (NSString*) scheduledCardCriteria;

+ (NSInteger) score; 
+ (NSArray*) reviewSchedulesWithLimit:(NSUInteger) limit;

- (id) initWithQuestion:(NSString *) q andAnswer:(NSString *) a;
- (void) study:(NSUInteger) grade;

@end
