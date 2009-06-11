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
}

@property (nonatomic, readonly) TCBDB *indexDb;
@property (nonatomic, readonly) TCBDB *wordDb;

- (id) init;
- (WiktionaryEntry*) getWiktionaryEntry: (NSString*) lemma;

- (NSMutableArray*) listForward:(NSString*) lemma;
- (NSMutableArray*) listForward:(NSString*) lemma withLimit:(NSUInteger) limit;

- (NSMutableArray*) listBackward:(NSString*) lemma;
- (NSMutableArray*) listBackward:(NSString*) lemma withLimit:(NSUInteger) limit;

@end
