//
//  StudyLog.m
//  RDict
//
//  Created by Jaewoo Kim on 1/2/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "StudyLog.h"


@implementation StudyLog
@synthesize card;
@synthesize studied;
@synthesize grade;
@synthesize lastLog;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"card", @"@\"Card\""),
					DECLARE_PROPERTY( @"studied", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"grade", @"@\"NSInteger\""),
					DECLARE_PROPERTY( @"lastLog", @"@\"BOOL\"")
)					
					
+ (StudyLog*) lastStudyLogOfCard:(Card *)card {
	NSArray* array = [card findRelated:[StudyLog class] filter:@"last_log = 1"];
	return [array count] > 0 ? [array objectAtIndex:0] : nil;
}

- (id) initWithCard:(Card *)theCard {
	[super init];
	
	self.card = theCard;
	self.studied = theCard.studied;
	self.grade = theCard.grade;
	self.lastLog = YES;
	
	return self;
}

- (void) unsetLastLog {
	self.lastLog = NO;
	[self save];
}

- (void)dealloc
{
	[card release];
	[studied release];
	[super dealloc];
}
	
@end
