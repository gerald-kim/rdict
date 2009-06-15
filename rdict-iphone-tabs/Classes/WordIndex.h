//
//  WordIndex.h
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface WordIndex : NSObject {
	NSString *key;
	NSString *lemma;
}

@property (nonatomic, retain) NSString *key;
@property (nonatomic, retain) NSString *lemma;

- (id) initWithKeyString:(const char *) keyArg andLemmaString:(const char*) lemmaArg;

@end
