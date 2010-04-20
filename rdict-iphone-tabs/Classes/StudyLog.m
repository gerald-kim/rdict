//
//  StudyLog.m
//  RDict
//
//  Created by Jaewoo Kim on 1/2/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "StudyLog.h"
#import "SLStmt.h"

@implementation StudyLog
@synthesize card;
@synthesize studied;
@synthesize deleted;
@synthesize grade;
@synthesize studyIndex;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"card", @"@\"Card\""),
					DECLARE_PROPERTY( @"studied", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"deleted", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"grade", @"@\"NSInteger\""),
					DECLARE_PROPERTY( @"studyIndex", @"@\"NSUInteger\"")
)					
					
+ (StudyLog*) lastStudyLogOfCard:(Card *)card {
	NSArray* array = [card findRelated:[StudyLog class] filter:@"study_index = 0"];
	return [array count] > 0 ? [array objectAtIndex:0] : nil;
}

+ (void) increaseStudyIndex:(Card *)card {
	NSArray* array = [card findRelated:[StudyLog class]];
	for( StudyLog* studyLog in array ) {
		studyLog.studyIndex++;
		[studyLog save];
	}
}

+ (void) deleteStudyLogs:(Card*) card {
	NSArray* array = [card findRelated:[StudyLog class]];
	for( StudyLog* studyLog in array ) {
		studyLog.deleted = [NSDate date];
		[studyLog save];
	}	
}

- (id) initWithCard:(Card *)theCard {
	[super init];
	
	self.card = theCard;
	self.studied = theCard.studied;
	self.grade = theCard.grade;
	self.studyIndex = 0;
	self.deleted = NULL;
	
	return self;
}

- (void)dealloc
{
	[card release];
	[studied release];
	[super dealloc];
}
	
@end
