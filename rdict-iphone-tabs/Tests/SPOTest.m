//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <TargetConditionals.h>
#import "GTMSenTestCase.h"
#import "Card.h"
#import <unistd.h>
#import <stdlib.h>
#import "SQLiteInstanceManager.h"

@interface SPOTest : SenTestCase {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation SPOTest

-(void) testCardCRD {
//	unlink("test.db");
    [[SQLiteInstanceManager sharedManager] setDatabaseFilepath:@"test.db"];

	Card* c = [[Card alloc] init];
	c.question = @"question 3";
	c.answer = @"answer 3";
//	c.scheduled = @"schedule";
	STAssertFalse( [c existsInDB], nil );
	[c save];
	STAssertTrue( [c existsInDB], nil );
	double d = [Card performSQLAggregation:@"select count(*) from card"];
	NSLog( @"count : %f", d );
}

@end

#endif