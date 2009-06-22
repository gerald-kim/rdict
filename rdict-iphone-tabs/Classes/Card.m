//
//  Card.m
//  RDict
//
//  Created by Jaewoo Kim on 6/17/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "Card.h"

@implementation Card
@synthesize question, answer;
@synthesize repsSinceLapse, easiness, interval, grade, finalGrade;
@synthesize scheduled, studied, created;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"question", @"@\"NSString\""),
					DECLARE_PROPERTY( @"answer", @"@\"NSString\""),
					DECLARE_PROPERTY( @"repsSinceLapse", @"@\"int\""),
					DECLARE_PROPERTY( @"interval", @"@\"int\""),
					DECLARE_PROPERTY( @"easiness", @"@\"double\""),
					DECLARE_PROPERTY( @"grade", @"@\"int\""),
					DECLARE_PROPERTY( @"finalGrade", @"@\"int\""),
					DECLARE_PROPERTY( @"scheduled", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"studied", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"created", @"@\"NSDate\"")
)

/*
+(NSArray *)transients
{
    return [NSArray arrayWithObject:@"transientBit"];
}
*/

- (NSString *)description
{
    return [NSString stringWithFormat:@"<Card.%d %@>", [self pk], question];
}

- (id) initWithQuestion:(NSString *) q andAnswer:(NSString *) a{
	[super init];

	self.question = q;
	self.answer = a;
	
	self.repsSinceLapse = 0;
	self.easiness = (float) 2.5;
	self.interval = 1;	
	self.grade = 0;
	self.finalGrade = 0;
	
	//TODO - ignore time parts of scheduled
	self.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY];	
	self.created = [[NSDate alloc] init];
	self.studied = nil;
	
	return self;
}

- (void)dealloc
{
    [question release];
    [answer release];
	[scheduled release];
	[studied release];
	[created release];
    [super dealloc];
}



@end
