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
	self.interval = -1;
	
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) self.interval * 60*60*24];
	self.created = [NSDate init];
	self.modified = [NSDate init];
	self.studied = [NSDate init];
	self.effective_ended = [NSDate init];
	
	return self;
}

- (void) calcInterval {
	if (self.reps_since_lapse == 0)
		self.interval = 1;
	else if (self.reps_since_lapse == 1)
		self.interval = 6;
	else
		self.interval = (int) ceil(self.interval * self.easiness);
	
	// the difference between today and today + the interval	
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) 60*60*24];
}

- (void) calcEasinessByGrade: (int) grade {
	if (grade > 2){
		float newEasiness = self.easiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
	
		if(newEasiness <= 1.3)
			self.easiness = 1.3;
		else
			self.easiness = newEasiness;
	}
}

- (void) forget {
	//Should be trackable
	self.reps_since_lapse = 0;
	[self calcInterval];
}
@end
