//
//  History.m
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "History.h"


@implementation History

@synthesize lemma;
@synthesize created;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"lemma", @"@\"NSString\""),
					DECLARE_PROPERTY( @"created", @"@\"NSDate\"")
)

+ (NSArray*) findRecents
{
	NSString* recentCriteria = @"where created > date('now', '-30 day') order by created desc";
	return [History findByCriteria:recentCriteria];
}

- (id) initWithLemma:(NSString *) aLemma
{
	[super init];
	self.lemma = aLemma;
	self.created = [[NSDate alloc] init];
	
	return self;
}

- (void) dealloc
{
	[lemma dealloc];
	[created dealloc];
	[super dealloc];
}


@end
