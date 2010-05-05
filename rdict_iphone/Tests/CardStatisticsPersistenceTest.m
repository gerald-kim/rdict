//
//  CardStatisticsPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 2/24/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//



#import "ObjectPersistenceTest.h"
#import "CardStatistics.h"

@interface CardStatisticsPersistenceTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardStatisticsPersistenceTest

-(void) assertCardStatisticsEquals:(CardStatistics*) expected actual:(CardStatistics*) actual {
	STAssertEquals( expected.date, actual.date, nil );
	STAssertEquals( expected.cardCount, actual.cardCount, nil );
	STAssertEquals( expected.averageScore, actual.averageScore, nil );
}	

-(void) testCardStatisticsCRD {
	CardStatistics* cs = [[CardStatistics alloc] initWithDate:[[NSDate alloc]init] cardCount:10 averageScore:3.2];
	[cs save];
	
	
}

@end

#endif