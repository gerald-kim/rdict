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

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"question", @"@\"NSString\""),
					DECLARE_PROPERTY( @"answer", @"@\"NSString\"")
//					DECLARE_PROPERTY( @"scheduled", @"@\"NSString\"")
)

/*
+(NSArray *)transients
{
    return [NSArray arrayWithObject:@"transientBit"];
}
*/

- (void)dealloc
{
    [question release];
    [answer release];
    [super dealloc];
}

- (NSString *)description
{
    return [NSString stringWithFormat:@"<Card.%d %@>", [self pk], question];
}

@end
