//
//  Person.m
//  RDict
//
//  Created by Stephen Bodnar on 21/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Person.h"

@implementation Person
@synthesize name;

- (void) dealloc {
	[name release];
	[super dealloc];
}

@end
