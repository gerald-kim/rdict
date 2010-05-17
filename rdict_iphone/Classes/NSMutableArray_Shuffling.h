//
//  NSMutableArray_Shuffling.h
//  RDict
//
//  Created by Jaewoo Kim on 5/17/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#if TARGET_OS_IPHONE
#import <UIKit/UIKit.h>
#else
#include <Cocoa/Cocoa.h>
#endif

// This category enhances NSMutableArray by providing
// methods to randomly shuffle the elements.
@interface NSMutableArray (Shuffling)
- (void)shuffle;
@end
