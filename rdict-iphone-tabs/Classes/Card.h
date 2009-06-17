//
//  Card.h
//  RDict
//
//  Created by Jaewoo Kim on 6/17/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"

@interface Card : SQLitePersistentObject {
	NSString* question;
	NSString* answer;
//	NSString* scheduled;
}

@property (nonatomic, retain) NSString* question;
@property (nonatomic, retain) NSString* answer;
//@property (nonatomic, retain) NSString* scheduled;

@end
