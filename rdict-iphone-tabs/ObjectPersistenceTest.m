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
//	NSLog( @"setUp" );
}

-(void) tearDown {
//	NSLog( @"tearDown" );
	[[SQLiteInstanceManager sharedManager] deleteDatabase];
}

#endif

@end
