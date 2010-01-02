//
//  StudyLogPersistenceTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "ObjectPersistenceTest.h"
#import "Card.h"
#import "StudyLog.h"
#import <unistd.h>
#import <stdlib.h>

@interface StudyLogPersistenceTest : ObjectPersistenceTest {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation StudyLogPersistenceTest

-(void) assertStudyLogEquals:(StudyLog*) expected actual:(StudyLog*) actual {
	STAssertEquals( expected.card, actual.card, nil );
	STAssertEquals( expected.studied, actual.studied, nil );
	STAssertEquals( expected.grade, actual.grade, nil );
}	

-(void) testCardCRD {
	Card* card = [[Card alloc] initWithQuestion:@"q" andAnswer:@"a"];
	[card study:3];
	
	StudyLog* expected = [[StudyLog alloc] initWithCard:card];
	
	STAssertFalse( [expected existsInDB], nil );
	[expected save];
	STAssertTrue( [expected existsInDB], nil );
	
	StudyLog* actual = (StudyLog*) [StudyLog findByPK:expected.pk];
	[self assertStudyLogEquals:expected actual:actual];
	
	//	Card* actual = (Card*) [Card findByPK:expected.pk];
	//	[self assertCardEquals:expected actual:actual];
	//	
	//	expected.question = @"new question";
	//	expected.question = @"new answre";
	//	expected.repsSinceLapse = 33;
	//	expected.easiness = 2.55;
	//	expected.interval = 3;
	//	expected.grade = 4;
	//	expected.finalGrade = 5;
	//	expected.scheduled = [[NSDate alloc] init];
	//	expected.studied = [[NSDate alloc] init];
	//	[expected save];
	//	
	//	actual = (Card*) [Card findByPK:expected.pk];
	//	[self assertCardEquals:expected actual:actual];
	//	STAssertNil( actual.deleted, nil );
}

@end

#endif