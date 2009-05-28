//
//  Dictionary.h
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "tcutil.h"
#include "tcbdb.h"

@class DictionaryEntry;

@interface Dictionary : NSObject {
	TCBDB *bdb;
}

@property TCBDB *bdb;

- (DictionaryEntry *) searchByWord: (NSString *) word;

@end
