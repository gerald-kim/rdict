//
//  Card.m
//  RDict
//
//  Created by Stephen Bodnar on 17/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Card.h"

@implementation Card
@synthesize question;
@synthesize answer;
@synthesize reps_since_lapse;
@synthesize easiness;
@synthesize interval;
@synthesize scheduled;
@synthesize created;
@synthesize modified;
@synthesize studied;
@synthesize effective_ended;

- (id) initWithQuestion:(NSString *) q Answer:(NSString *) a{
	self.question = q;
	self.answer = a;
	
	self.reps_since_lapse = 0;
	self.easiness = (float) 2.5;
	self.interval = 1;
	
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) self.interval * 60*60*24];
	self.created = [NSDate init];
	self.modified = [NSDate init];
	self.studied = [NSDate init];
	self.effective_ended = [NSDate init];
	
	return self;
}

- (void) calc_interval {
	if (self.reps_since_lapse == 0)
		self.interval = 1;
	else if (self.reps_since_lapse == 1)
		self.interval = 6;
	else
		self.interval = self.interval * self.easiness;
	
	// the difference between today and today + the interval	
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) 60*60*24];
}

- (void) forget {
	//Should be trackable
	self.reps_since_lapse = 0;
	[self calc_interval];
}
@end
