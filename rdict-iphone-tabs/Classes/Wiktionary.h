//
//  Wiktionary.h
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "tcbdb.h"

@interface Wiktionary : NSObject {

	TCBDB *indexDb;
	TCBDB *wordDb;	
}

@property TCBDB *indexDb;
@property TCBDB *wordDb;

- (id) init;

@end
