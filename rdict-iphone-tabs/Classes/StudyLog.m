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

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"card", @"@\"Card\""),
					DECLARE_PROPERTY( @"studied", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"grade", @"@\"NSInteger\"")
)					
					
- (id) initWithCard:(Card *)theCard {
	[super init];
	
	self.card = theCard;
	self.studied = theCard.studied;
	self.grade = theCard.grade;
	
	return self;
}


- (void)dealloc
{
	[card release];
	[studied release];
	[super dealloc];
}
	
@end
