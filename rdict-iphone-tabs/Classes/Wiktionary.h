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
}

@property TCBDB *indexDb;
@property TCBDB *wordDb;

- (id) init;
- (WiktionaryEntry*) getWiktionaryEntry: (NSString*) lemma;
- (NSMutableArray*) listForward:(NSString*) lemma;
- (NSMutableArray*) listForward:(NSString*) lemma withLimit:(NSUInteger) limit;

@end
