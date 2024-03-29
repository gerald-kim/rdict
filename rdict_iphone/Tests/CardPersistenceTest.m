//
//  CardPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "Card.h"
#import <unistd.h>
#import <stdlib.h>

@interface CardPersistenceTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation CardPersistenceTest

-(void) assertCardEquals:(Card*) expected actual:(Card*) actual {
	STAssertEqualStrings( expected.question, actual.question, nil );
	STAssertEqualStrings( expected.answer, actual.answer, nil );
	STAssertEquals( expected.repsSinceLapse, actual.repsSinceLapse, nil );
	STAssertEquals( expected.interval, actual.interval, nil );
	STAssertEquals( expected.easiness, actual.easiness, nil );
	STAssertEquals( expected.grade, actual.grade, nil );
	STAssertEquals( expected.finalGrade, actual.finalGrade, nil );
	STAssertEquals( expected.scheduled, actual.scheduled, nil );
	STAssertEquals( expected.created, actual.created, nil );
	STAssertEquals( expected.studied, actual.studied, nil );	
	STAssertEquals( expected.updated, actual.updated, nil );	
	STAssertEquals( expected.deleted, actual.deleted, nil );	
}	
	
-(void) testCardCRD {
	Card* expected = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	STAssertFalse( [expected existsInDB], nil );
	[expected save];
	STAssertTrue( [expected existsInDB], nil );

	Card* actual = (Card*) [Card findByPK:expected.pk];
	[self assertCardEquals:expected actual:actual];
	
	expected.question = @"new question";
	expected.question = @"new answre";
	expected.repsSinceLapse = 33;
	expected.easiness = 2.55;
	expected.interval = 3;
	expected.grade = 4;
	expected.finalGrade = 5;
	expected.scheduled = [[NSDate alloc] init];
	expected.studied = [[NSDate alloc] init];
	[expected save];
	
	actual = (Card*) [Card findByPK:expected.pk];
	[self assertCardEquals:expected actual:actual];
	STAssertNil( actual.deleted, nil );
	
	[expected deleteObject];
	actual = (Card*) [Card findByPK:expected.pk];
	
	STAssertNotNil( actual.deleted, nil );
}

-(void) testFindFirstByQuestion {
	Card* actual = [Card findFirstByQuestion:@"question"];
	STAssertNULL( actual, nil );

	Card* expected = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	[expected save];
	
	actual = [Card findFirstByQuestion:@"question"];
	[self assertCardEquals:expected actual:actual];
}
-(void) testUpdatedWillChangeWhenSave {
	Card* expected = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	[expected save];
	STAssertNotNil( expected.updated, nil );
	
	NSDate* lastUpdated = expected.updated;
	[expected save];
	STAssertNotEquals( expected.updated, lastUpdated, nil );
}

-(void) testUpdatedWillChangeWhenStudy {
	Card* expected = [[Card alloc] initWithQuestion:@"question" andAnswer:@"answer"];
	[expected save];
	STAssertNotNil( expected.updated, nil );
	
	NSDate* lastUpdated = expected.updated;
	[expected study:3];
	STAssertNotEquals( expected.updated, lastUpdated, nil );
}

-(void) testDeletedCardWillBeIgnoredInAllObjectsAndFindScheduledAndFindToday {
	//TODO
	
}

@end

#endif