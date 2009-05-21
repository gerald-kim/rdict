//
//  Card.h
//  RDict
//
//  Created by Stephen Bodnar on 17/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

@interface Card : NSObject {
	NSString *question;
	NSString *answer;
	
	int reps_since_lapse;
	float easiness;
	int interval;
	
	NSDate *scheduled;
	NSDate *created;
	NSDate *modified;
	NSDate *studied;
	NSDate *effective_ended;
	
}

@property (nonatomic, retain) NSString *question;
@property (nonatomic, retain) NSString *answer;

@property int reps_since_lapse;
@property float easiness;
@property int interval;

@property (nonatomic, retain) NSDate *scheduled;
@property (nonatomic, retain) NSDate *created;
@property (nonatomic, retain) NSDate *modified;
@property (nonatomic, retain) NSDate *studied;
@property (nonatomic, retain) NSDate *effective_ended;

- (id) initWithQuestion:(NSString *)question Answer:(NSString *) answer;
- (id) initWithCard:(Card *) card;
- (void) calcInterval;
- (void) calcEasinessByGrade: (int) grade;
- (void) forget;
@end
