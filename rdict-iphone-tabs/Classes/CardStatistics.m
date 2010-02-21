//
//  CardStatistics.m
//  RDict
//
//  Created by Jaewoo Kim on 2/22/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "CardStatistics.h"

@implementation CardStatistics

@synthesize date;
@synthesize cardCount;
@synthesize averageScore;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"date", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"cardCount", @"@\"NSUInteger\""),
					DECLARE_PROPERTY( @"averageScore", @"@\"double\"")
)	

- (id) initWithDate:(NSDate*)theDate cardCount:(NSUInteger) theCardCount averageScore:(double) theAverageScore {
	[super init];
	self.date = theDate;
	self.cardCount = theCardCount;
	self.averageScore = theAverageScore;
	
	return self;
}

- (void)dealloc
{
	[date release];
	[super dealloc];
}
@end
