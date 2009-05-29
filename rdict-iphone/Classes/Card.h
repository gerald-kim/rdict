//
//  Card.h
//  RDict
//
//  Created by Stephen Bodnar on 17/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SQLitePersistentObject.h"

@interface Card : SQLitePersistentObject {
	NSString *question;
	NSString *answer;
	
	int repsSinceLapse;
	float easiness;
	int interval;
	
	NSString *scheduled;
	NSDate *created;
	NSDate *modified;
	NSDate *studied;
	NSDate *effectiveEnded;
}

@property (nonatomic, retain) NSString *question;
@property (nonatomic, retain) NSString *answer;

@property int repsSinceLapse;
@property float easiness;
@property int interval;

@property (nonatomic, retain) NSString *scheduled;
@property (nonatomic, retain) NSDate *created;
@property (nonatomic, retain) NSDate *modified;
@property (nonatomic, retain) NSDate *studied;
@property (nonatomic, retain) NSDate *effectiveEnded;

+ (NSMutableArray *) loadScheduledCards;
+ (NSMutableArray *) loadCardsByScheduledDate: (NSString *) dateString;
- (id) initWithQuestion:(NSString *)question Answer:(NSString *) answer;
- (id) initWithCard:(Card *) card;
- (void) calcInterval;
- (void) schedule;
- (void) adjustEasinessByGrade: (int) grade;
- (void) forget;
@end
