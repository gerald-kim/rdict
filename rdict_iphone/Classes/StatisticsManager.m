//
//  StatisticsManager.m
//  RDict
//
//  Created by Jaewoo Kim on 2/25/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "StatisticsManager.h"
#import "Card.h"
#import "StudyLog.h"
#import "SLStmt.h"

@interface StatisticsManager() 

+(NSArray*) factors:(NSString*) factor OfRecentDay:(NSUInteger) days;

@end

@implementation StatisticsManager

+(void) createTableAndCopyPreviousData {
	SLStmt* stmt = [SLStmt stmtWithSql:@"CREATE TABLE IF NOT EXISTS statistics (pk DATE PRIMARY KEY, card_count INTEGER, mastered_card_count INTEGER)"];
	[stmt step];
	[stmt prepareSql:@"select card_count,  mastered_card_count from statistics order by pk desc limit 1"];
	
	int totalCount = 0;
	int masteredCount = 0;
	if( [stmt step] ) {
		totalCount = [[stmt stringValue:0] intValue];
		masteredCount = [[stmt stringValue:1] intValue];
	}
	[stmt prepareSql:[NSString stringWithFormat:@"INSERT INTO statistics values (date('now', 'localtime'), %d, %d)", totalCount, masteredCount]];
	[stmt step];
	[stmt close];
}

+(void) updateCardCountsOfToday {
	[StatisticsManager createTableAndCopyPreviousData];
	
	SLStmt* stmt = [SLStmt stmtWithSql:@"UPDATE statistics SET card_count = (SELECT count(*) card_count FROM card WHERE deleted is null) WHERE pk = date('now', 'localtime')"];
	[stmt step];
	[stmt close];	
}

+(void) updateMasteredCardCountsOfToday {
	[StatisticsManager createTableAndCopyPreviousData];
	[StudyLog count];
	
	SLStmt* stmt = [SLStmt stmtWithSql:@"UPDATE statistics SET mastered_card_count = (select count(*) from ( select card, avg(grade) grade from study_log where deleted is null and study_index in (0, 1) group by card ) where grade >= 4) WHERE pk = date('now', 'localtime')"];
	[stmt step];
	[stmt close];	
}

+(NSArray*) cardCountsOfRecentDay:(NSUInteger) days {
	return [StatisticsManager factors:@"card_count" OfRecentDay:days];
}

+(NSArray*) masteredCardsCountOfRecentDay:(NSUInteger) days {
	return [StatisticsManager factors:@"mastered_card_count" OfRecentDay:days];
}

+(NSArray*) factors:(NSString*) factor OfRecentDay:(NSUInteger) days {
	NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
	
	NSString* sql = [NSString stringWithFormat:@"SELECT pk, %@ FROM statistics WHERE pk > date('now', '-%d days', 'localtime')", factor, days];
	SLStmt* stmt = [SLStmt stmtWithSql:sql];

	while ([stmt step]) {
		[dict setObject:[NSNumber numberWithDouble:[[stmt stringValue:1] doubleValue]] forKey:[stmt stringValue:0]];
	}
	[stmt close];
	
	NSMutableArray* array = [[NSMutableArray alloc] init];
	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	
	NSNumber* lastValue = [NSNumber numberWithInt:0];
	for( int i = days - 1; i >= 0; i-- ) {
		NSDate* date = [NSDate dateWithTimeIntervalSinceNow:-i*60*60*24];
		NSNumber* value = [dict valueForKey:[dateFormatter stringFromDate:date]];
		if( nil == value ) {
			[array addObject:lastValue];
		} else {
			[array addObject:value];
			lastValue = value;
		}
	}
	[dict release];
	[dateFormatter release];
//	DebugLog(@"dict: %@", dict);
//	DebugLog(@"array: %@", array);
	
	return array;
}

@end
