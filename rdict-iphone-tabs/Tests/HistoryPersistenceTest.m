//
//  HistoryPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "History.h"

@interface HistoryPersistenceTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation HistoryPersistenceTest


//@encode
-(void) assertHistoryEquals:(History*) expected actual:(History*) actual {
	STAssertEqualStrings( expected.lemma, actual.lemma, nil );
	STAssertEquals( expected.created, actual.created, nil );
}	

-(void) testHistoryCRD {
	History* expected = [[History alloc] initWithLemma:@"question"];
	STAssertFalse( [expected existsInDB], nil );
	[expected save];
	STAssertTrue( [expected existsInDB], nil );
	
	History* actual = (History*) [History findByPK:expected.pk];
	[self assertHistoryEquals:expected actual:actual];
}


@end

#endif