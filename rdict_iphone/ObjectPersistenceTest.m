//
//  ObjectPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "SQLiteInstanceManager.h"

@implementation ObjectPersistenceTest

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

-(void) setUp {
	DebugLog( @"setUp" );
	[[SQLiteInstanceManager sharedManager] deleteDatabase];
	[self populate];
}

-(void) tearDown {
	DebugLog( @"tearDown" );
}

-(void) populate {
	
}

#endif

@end
