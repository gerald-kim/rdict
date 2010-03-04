//
//  Card.m
//  RDict
//
//  Created by Jaewoo Kim on 6/17/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "Card.h"
#import "StudyLog.h"
#import "StatisticsManager.h"
#import "SLStmt.h"

@implementation Card
@synthesize question, answer;
@synthesize repsSinceLapse, easiness, interval, grade, finalGrade;
@synthesize scheduled, studied, created, updated, deleted;

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

					DECLARE_PROPERTY( @"created", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"updated", @"@\"NSDate\""),
					DECLARE_PROPERTY( @"deleted", @"@\"NSDate\"")
)

+ (Card*) saveCardWithQuestion:(NSString*) question andAnswer:(NSString*) answer {
	Card* card = [Card findFirstByQuestion:question];
	if ( NULL == card ) {
		Card* card = [[Card alloc] initWithQuestion:question andAnswer:answer];
		[card save];
	} else {
		card.answer = [answer stringByAppendingFormat:@"----------\n%@", answer];
		[card study:0];
	}
	return card;
}

+ (NSArray *) allObjects {
	return [Card findByCriteria:@"where deleted is null"];
}

+ (NSInteger) count {
	return [Card countByCriteria:@"where deleted is null"];
}

+ (Card*) findFirstByQuestion:(NSString*) question {
	return (Card*) [Card findFirstByCriteria:@"where deleted is null and question = '%@'", question];
}

+ (NSInteger) countByScheduled {
	return [Card countByCriteria:[self scheduledCardCriteria]];
}

+ (NSArray*) findByScheduled {
	return [Card findByCriteria:[self scheduledCardCriteria]];	
}

+ (NSString*) scheduledCardCriteria {
	return [NSString stringWithString:@"where scheduled < date('now', 'localtime', '+1 day') and deleted is null"];	
}

+ (NSInteger) countByToday {
	return [Card countByCriteria:[self todayCardCriteria]];
}

+ (NSArray*) findByToday {
	return [Card findByCriteria:[self todayCardCriteria]];	
}

+ (NSString*) todayCardCriteria {
	return [NSString stringWithString:@"where created > date('now', 'localtime') "
		"	and ( studied is null or studied < date('now', 'localtime') ) and deleted is null"];	
}

+ (NSInteger) score {	
	SLStmt* stmt = [SLStmt stmtWithSql:@"select avg( grade ) * 20 from card where deleted is null"];
	
	NSInteger score = 0;
	if( [stmt step] ) {
		score = [[stmt stringValue] intValue];
	}
	[stmt close];
	
	return score;	
}

+ (NSArray*) reviewSchedulesWithLimit:(NSUInteger) limit {
	SLStmt* stmt = [SLStmt stmtWithSql:[NSString stringWithFormat:
				   @"select date( scheduled ), count(*) count " 
					"from card WHERE deleted is null AND scheduled > date('now', 'localtime', '+1 day') "
					"group by date( scheduled ) limit %d", limit]];
									
	NSMutableArray *scheduleArray = [NSMutableArray array];
	
	NSDateFormatter* inputDateFormatter = [[NSDateFormatter alloc]init];
	[inputDateFormatter setDateFormat:@"yyyy-MM-dd"];
	
	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateStyle:NSDateFormatterMediumStyle];
	[dateFormatter setTimeStyle:NSDateFormatterNoStyle];

	
	while( [stmt step] ) {
		NSDate* date = [inputDateFormatter dateFromString:[stmt stringValue:0]];
		NSArray *row = [NSArray arrayWithObjects: [dateFormatter stringFromDate:date], [stmt stringValue:1], nil];
		[scheduleArray addObject:row];
	}
	[stmt close];
	
	[inputDateFormatter release];
	[dateFormatter release];
	
	return scheduleArray;	
}

//TODO functions for review can be extracted to new class
+ (NSString*) messageForReview {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];
	
	if ( scheduledCount > 0 ) {
		return @"Scheduled";
	} else if ( todayCount > 0 ) {
		return @"Early Practice";
	} else {
		return @"None Available";		
	}
}

+ (NSString*) countMessageForReview {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];
	NSInteger count = scheduledCount > 0 ? scheduledCount : todayCount;
	
	if (count > REVIEW_LIMIT) {
		return [NSString stringWithFormat:@"%d/%d", REVIEW_LIMIT, count];
	} else if ( count > 0 ) {
		return [NSString stringWithFormat:@"%d", count];
	} else {
		return @"";
	}
}

+ (NSArray*) cardsForReview {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];
	
	if ( scheduledCount == 0 && todayCount == 0 ) {
		return nil;
	}
	NSMutableString* criteria = [NSMutableString string];
	if ( scheduledCount > 0 ) {
		[criteria appendString:[Card scheduledCardCriteria]];
	} else if ( todayCount > 0 ) {
		[criteria appendString:[Card todayCardCriteria]];
	}
	[criteria appendFormat:@" order by random() limit %d", REVIEW_LIMIT];
	
	return [Card findByCriteria:criteria];
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
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY];	
	self.created = [NSDate date];
	self.studied = nil;
	self.deleted = nil;
	
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
	[updated release];
	[deleted release];
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
	
	self.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY * self.interval];	
	self.studied = [NSDate date];
	
	StudyLog* lastLog = [StudyLog lastStudyLogOfCard:self];
	[lastLog unsetLastLog];
	[[[StudyLog alloc]initWithCard:self] save];

	[self save];
}

- (void) save {
	self.updated = [NSDate date];
	[super save];
	[StatisticsManager updateStatisticsOfToday];	
}

- (void) deleteObject {
	self.deleted = [NSDate date] ;
	[super save];
	[StatisticsManager updateStatisticsOfToday];		
}

/*
+ (NSArray*) findScheduled {
}
*/

@end
