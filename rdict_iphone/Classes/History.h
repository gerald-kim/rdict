//
//  History.h
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"


@interface History : SQLitePersistentObject {
	NSString* lemma;
	NSDate* created;
}

@property (nonatomic, retain) NSString* lemma;
@property (nonatomic, retain) NSDate* created;

+ (NSArray*) findRecents;
+ (void) clearHistory;
+ (NSMutableDictionary*) buildHistorySectionInfo:(NSArray*) histories;

- (id) initWithLemma:(NSString *) lemma;

@end
