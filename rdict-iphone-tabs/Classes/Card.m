//
//  Card.m
//  RDict
//
//  Created by Jaewoo Kim on 6/17/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "Card.h"
#import "SLStmt.h"

@implementation Card
@synthesize question, answer;
@synthesize repsSinceLapse, easiness, interval, grade, finalGrade;
@synthesize scheduled, studied, created;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"question", @"@\"NSString\""),
					DECLARE_PROPERTY( @"answer", @"@\"NSString\""),
					DECLARE_PROPERTY( @"repsSinceLapse", @"@\"int\""),
					DECLARE_PROPERTY( @"interval", @"@\"int\""),
					DECLARE_PROPERTY( @"easiness", @"@\"double\""),
					DECLARE_PROPERTY( @"grade", @"@\"int\""),
					DECLARE_PROPERTY( @"finalGrade", @"@\"int\""),
					DECLARE_PROPERTY( @"scheduled", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"studied", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"created", @"@\"NSDate\"")
)

+ (NSInteger) countByScheduled {
	return [Card countByCriteria:[self scheduledCardCriteria]];
}

+ (NSArray*) findByScheduled {
	return [Card findByCriteria:[self scheduledCardCriteria]];	
}

+ (NSString*) scheduledCardCriteria {
	return [NSString stringWithString:@"where scheduled < date('now', 'localtime', '+1 day') order by random()"];	
}

+ (NSInteger) countByToday {
	return [Card countByCriteria:[self searchedTodayCriteria]];
}

+ (NSArray*) findByToday {
	return [Card findByCriteria:[self searchedTodayCriteria]];	
}

+ (NSString*) searchedTodayCriteria {
	return [NSString stringWithString:@"where created > date('now', 'localtime') and ( studied is null or studied < date('now', 'localtime') ) order by random()"];	
}

+ (NSInteger) score {	
	SLStmt* stmt = [SLStmt stmtWithSql:@"select avg( grade ) * 20 from card"];
	
	NSInteger score = 0;
	if( [stmt step] ) {
		score = [[stmt stringValue] intValue];
	}
	[stmt close];
	
	return score;	
}

+ (NSArray*) reviewSchedulesWithLimit:(NSUInteger) limit {
	SLStmt* stmt = [SLStmt stmtWithSql:[NSString stringWithFormat:
				   @"select date( scheduled ) date, count(*) count " 
					"from card where scheduled > date('now', 'localtime', '+1 day') "
					"group by date(scheduled) limit %d", limit]];
									
	NSMutableArray *scheduleArray = [NSMutableArray array];
	
	while( [stmt step] ) {
		NSArray *row = [NSArray arrayWithObjects: [stmt stringValue:0], [stmt stringValue:1], nil];
		[scheduleArray addObject:row];
	}
	[stmt close];
	
	return scheduleArray;	
}

/*
+(NSArray *)transients
{
    return [NSArray arrayWithObject:@"transientBit"];
}
*/


- (NSString *)description
{
    return [NSString stringWithFormat:@"<Card.%d %@>", [self pk], question];
}

- (id) initWithQuestion:(NSString *)q andAnswer:(NSString *)a {
	[super init];

	self.question = q;
	self.answer = a;
	
	self.repsSinceLapse = 0;
	self.easiness = (float) 2.5;
	self.interval = 1;	
	self.grade = 0;
	self.finalGrade = 0;
	
	//TODO - ignore time parts of scheduled
	self.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY];	
	self.created = [[NSDate alloc] init];
	self.studied = nil;
	
	return self;
}


- (void)dealloc
{
	NSLog( @"Card(0x%x).dealloc", self ); 
    [question release];
    [answer release];
	[scheduled release];
	[studied release];
	[created release];
    [super dealloc];
}


- (void) study:(NSUInteger) theGrade {
	self.grade = theGrade;
	if ( theGrade < 3 ) {
		self.repsSinceLapse = 0;
	} else {
		self.repsSinceLapse += 1;
		float newEasiness = self.easiness + (0.1 - (5 - theGrade) * (0.08 + (5 - theGrade) * 0.02));
		self.easiness = newEasiness >= 1.3 ? newEasiness : 1.3;
	}
	
	if ( self.repsSinceLapse == 0 ) {
		self.interval = 1;
	} else if ( self.repsSinceLapse == 1 ) {
		self.interval = 6;		
	} else {
		self.interval = self.interval * self.easiness;
	}
	
	self.scheduled = [[NSDate alloc] initWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY * self.interval];	
	self.studied = [[NSDate alloc] init];	
	[self save];
}
/*
+ (NSArray*) findScheduled {
}
*/

@end
