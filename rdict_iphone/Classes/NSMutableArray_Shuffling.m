//
//  NSMutableArray_Shuffling.m
//  RDict
//
//  Created by Jaewoo Kim on 5/17/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "NSMutableArray_Shuffling.h"


@implementation NSMutableArray (Shuffling)
- (void)shuffle
{
    NSUInteger count = [self count];
    for (NSUInteger i = 0; i < count; ++i) {
        // Select a random element between i and end of array to swap with.
        int nElements = count - i;
        int n = (random() % nElements) + i;
        [self exchangeObjectAtIndex:i withObjectAtIndex:n];
    }
}
@end
