//
//  Wiktionary.h
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "tcbdb.h"
#import "WiktionaryIndex.h"
#import "WiktionaryEntry.h"

@interface Wiktionary : NSObject {
	TCBDB *indexDb;
	TCBDB *wordDb;	
	BDBCUR *forwardCursor;
	BDBCUR *backwardCursor;
	BDBCUR *wordCursor;
	NSMutableArray *wordList;
}

@property (nonatomic, readonly) TCBDB *indexDb;
@property (nonatomic, readonly) TCBDB *wordDb;
@property (nonatomic, readonly) NSMutableArray* wordList;


- (WiktionaryEntry*) getWiktionaryEntry: (NSString*) lemma;
- (WiktionaryIndex*) getWiktionaryIndexFromCursor:(BDBCUR*) cursor;
- (NSInteger) jumpToWord:(NSString*) word;

//- (NSMutableArray*) listBackward:(NSString*) lemma withLimit:(NSUInteger) limit;

@end
