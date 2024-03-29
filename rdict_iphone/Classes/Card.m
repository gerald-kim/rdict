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

+(NSArray *)indices
{
	NSArray *index1 = [NSArray arrayWithObject:@"deleted"];
	NSArray *index2 = [NSArray arrayWithObjects:@"deleted", @"question", nil];
	NSArray *index3 = [NSArray arrayWithObjects:@"deleted", @"scheduled", nil];
	NSArray *index4 = [NSArray arrayWithObjects:@"deleted", @"created", @"studied", nil];
	return [NSArray arrayWithObjects:index1, index2, index3, index4, nil];
}

+ (Card*) saveCardWithQuestion:(NSString*) question andAnswer:(NSString*) answer {
	Card* card = [Card findFirstByQuestion:question];
	if ( NULL == card ) {
		Card* card = [[Card alloc] initWithQuestion:question andAnswer:answer];
		[card save];
	} else {
		if ( ![card.answer hasSuffix:@"\n"] ) {
			card.answer = [card.answer stringByAppendingString:@"\n"];			
		}
		card.answer = [card.answer stringByAppendingFormat:@"-------------\n%@", answer];			
		//card.scheduled = [NSDate dateWithTimeIntervalSinceNow:(NSTimeInterval) SECONDS_IN_ONE_DAY];
		[card save];
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
	return [NSString stringWithString:@"where deleted is null and scheduled < date('now', 'localtime', '+1 day')"];	
}

+ (NSInteger) countByToday {
	return [Card countByCriteria:[self todayCardCriteria]];
}

+ (NSArray*) findByToday {
	return [Card findByCriteria:[self todayCardCriteria]];	
}

+ (NSString*) todayCardCriteria {
	return [NSString stringWithString:@"where deleted is null and created > date('now', 'localtime') "
		"	and ( studied is null or studied < date('now', 'localtime') )"];	
}

+ (NSInteger) countByMastered {	
	SLStmt* stmt = [SLStmt stmtWithSql:@"select count(*) from ( select card, avg(grade) grade from study_log where deleted is null and study_index in (0, 1) group by card ) where grade >= 4;"];
	
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
	[dateFormatter setDateStyle:NSDateFormatterLongStyle];
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
+ (NSString*) messageForReview  {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];
	
	if ( scheduledCount > 0 ) {
		return @"Scheduled Exercise";
	} else if ( todayCount > 0 ) {
		return @"Early Practice";
	} else {
		return @"None Available";		
	}
}

+ (NSString*) footerForReview {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];


	NSMutableString* message = [NSMutableString string];
	if ( scheduledCount > 0  ) {
//		return @"You have scheduled flashcards for review.";
	} else if ( todayCount > 0 ) {
		[message appendFormat:@"No cards are scheduled for review,\nbut you can practice cards you created today. "];
	} else {
		[message appendFormat:@"There are no scheduled flashcards\nfor review. "];
	}
	
	NSInteger count = scheduledCount > 0 ? scheduledCount : todayCount;
	if ( count > REVIEW_LIMIT ) {
		[message appendFormat:@"Each review exercise is limited by %d cards. %d card(s) remains.", REVIEW_LIMIT, (count-REVIEW_LIMIT)];	
	}
	
	return message;
	
}

+ (NSString*) countMessageForReview {
	NSInteger todayCount = [Card countByToday];
	NSInteger scheduledCount = [Card countByScheduled];
	NSInteger count = scheduledCount > 0 ? scheduledCount : todayCount;
	
	if (count > REVIEW_LIMIT) {
		return [NSString stringWithFormat:@"%d", REVIEW_LIMIT, count];
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
	DebugLog( @"Card(0x%x).dealloc", self ); 
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
	
	[StudyLog increaseStudyIndex:self];
	[[[StudyLog alloc]initWithCard:self] save];

	self.updated = [NSDate date];
	[super save];
}

- (void) save {
	self.updated = [NSDate date];
	[super save];
	[StatisticsManager updateCardCountsOfToday];			
}

- (void) deleteObject {
	[StudyLog deleteStudyLogs:self];
	self.deleted = [NSDate date] ;
	[super save];
	[StatisticsManager updateCardCountsOfToday];		
}

/*
+ (NSArray*) findScheduled {
}
*/

@end
