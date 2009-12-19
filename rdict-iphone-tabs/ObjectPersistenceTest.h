//
//  ObjectPersistenceTest.h
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <TargetConditionals.h>
#import "GTMSenTestCase.h"


@interface ObjectPersistenceTest : SenTestCase {

}

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

-(void) setUp;
-(void) tearDown;
-(void) populate;

#endif

@end
