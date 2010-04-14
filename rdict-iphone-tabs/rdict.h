/*
 *  rdict.h
 *  RDict
 *
 *  Created by Jaewoo Kim on 4/14/10.
 *  Copyright 2010 ampliostudios. All rights reserved.
 *
 */

#ifdef DEBUG
#define DebugLog( s, ... ) NSLog( @"<%p %@:(%d)> %@", self, [[NSString stringWithUTF8String:__FILE__] lastPathComponent], __LINE__, [NSString stringWithFormat:(s), ##__VA_ARGS__] )
#else
#define DebugLog( s, ... ) 
#endif
