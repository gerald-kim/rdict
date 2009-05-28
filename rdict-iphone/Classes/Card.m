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
@synthesize repsSinceLapse;
@synthesize easiness;
@synthesize interval;
@synthesize scheduled;
@synthesize created;
@synthesize modified;
@synthesize studied;
@synthesize effectiveEnded;

- (id) initWithQuestion:(NSString *) q Answer:(NSString *) a{
	self.question = [[NSString alloc] initWithString: q];
	self.answer = [[NSString alloc] initWithString: a];
	
	self.repsSinceLapse = 0;
	self.easiness = (float) 2.5;
	self.interval = -1;
	
	self.scheduled = [[NSDate alloc] init];
	self.created = [[NSDate alloc] init];
	self.modified = [[NSDate alloc] init];
	self.studied = [[NSDate alloc] init];
	self.effectiveEnded = [[NSDate alloc] init];
	
	[super init];
	
	return self;
}

- (id) initWithCard:(Card *) card {
	Card *newCard = [[Card alloc] init];
	
	newCard.question = [[NSString alloc] initWithString:card.question];
	newCard.answer = [[NSString alloc] initWithString:card.answer];
	
	newCard.repsSinceLapse = card.repsSinceLapse;
	newCard.easiness = card.easiness;
	newCard.interval = card.interval;
	
	newCard.scheduled = [[NSDate alloc] init];
	newCard.created = [[NSDate alloc] init];
	newCard.modified = [[NSDate alloc] init];
	newCard.studied = [[NSDate alloc] init];
	newCard.effectiveEnded = [[NSDate alloc] init];
	
	[newCard retain];
	return newCard;
}

- (void) calcInterval {
	if (self.repsSinceLapse == 0)
		self.interval = 1;
	else if (self.repsSinceLapse == 1)
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
	self.repsSinceLapse = 0;
	[self calcInterval];
}
@end
