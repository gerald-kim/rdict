//
//  CardStatisticsUpdateTest.m
//  RDict
//
//  Created by Jaewoo Kim on 2/24/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//



#import "ObjectPersistenceTest.h"
#import "StatisticsManager.h"
#import "SLStmt.h"

@interface StatisticsManagerTest : ObjectPersistenceTest {	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation StatisticsManagerTest

-(void) testGetCardCountStatistics {
	[StatisticsManager createTable];

	for (int i = 0; i < 10; i++) {
		SLStmt *stmt = [SLStmt stmtWithSql:[NSString stringWithFormat:@"INSERT INTO statistics VALUES (date('now', '-%d days', 'localtime'), %d, 0)"
						  , i*2, (10-i)]];
		[stmt step];
		[stmt close];
	}
	
	NSArray* cardCounts = [StatisticsManager cardCountsOfRecentDay:20];
	STAssertEquals( [cardCounts count], (NSUInteger) 20, nil );
}

@end

#endif