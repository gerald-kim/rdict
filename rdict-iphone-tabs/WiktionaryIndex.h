//
//  WordIndex.h
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface WiktionaryIndex : NSObject {
	NSString *key;
	NSString *lemma;
}

@property (nonatomic, retain) NSString *key;
@property (nonatomic, retain) NSString *lemma;

- (id) initWithUTF8KeyString:(char *) keyArg andUTF8LemmaString:(char*) lemmaArg;

@end
