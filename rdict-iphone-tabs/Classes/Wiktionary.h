//
//  Wiktionary.h
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "tcbdb.h"
#import "WordIndex.h"
#import "WordEntry.h"

@interface Wiktionary : NSObject {
	TCBDB *indexDb;
	TCBDB *wordDb;	
	BDBCUR *forwardCursor;
	BDBCUR *backwardCursor;
	BDBCUR *wordCursor;
	NSMutableArray *wordIndexes;
}

@property (nonatomic, readonly) TCBDB *indexDb;
@property (nonatomic, readonly) TCBDB *wordDb;
@property (nonatomic, readonly) NSMutableArray* wordIndexes;


- (WordEntry*) wordEntryByLemma: (NSString*) aLemma;
- (WordIndex*) findIndexByQuery:(NSString*) aQuery;
- (NSUInteger) fillIndexesByKey:(NSString*) aWord;

//- (NSMutableArray*) listBackward:(NSString*) lemma withLimit:(NSUInteger) limit;

- (void) openWordDb;

@end
