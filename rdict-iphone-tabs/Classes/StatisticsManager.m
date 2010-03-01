//
//  StatisticsManager.m
//  RDict
//
//  Created by Jaewoo Kim on 2/25/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import "StatisticsManager.h"
#import "SLStmt.h"

@interface StatisticsManager() 

+(NSArray*) factors:(NSString*) factor OfRecentDay:(NSUInteger) days;

@end

@implementation StatisticsManager

+(void) createTable {
	SLStmt* stmt = [SLStmt stmtWithSql:@"CREATE TABLE IF NOT EXISTS statistics (pk DATE PRIMARY KEY, card_count INTEGER, score_average REAL)"];
	[stmt step];
	[stmt close];
}

+(void) updateStatisticsOfToday {
	[StatisticsManager createTable];

	SLStmt* stmt = [SLStmt stmtWithSql:@"REPLACE INTO statistics (pk, card_count) SELECT date('now', 'localtime') pk, count(*) card_count FROM card WHERE deleted is null"];
	[stmt step];
	[stmt prepareSql:@"UPDATE statistics SET score_average = (SELECt avg(grade*20) FROM card WHERE deleted is null) WHERE pk = date('now', 'localtime')"];
	[stmt step];
	[stmt close];	
}

+(NSArray*) cardCountsOfRecentDay:(NSUInteger) days {
	return [StatisticsManager factors:@"card_count" OfRecentDay:days];
}

+(NSArray*) scoreAveragesOfRecentDay:(NSUInteger) days {
	return [StatisticsManager factors:@"score_average" OfRecentDay:days];
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
//	NSLog(@"dict: %@", dict);
//	NSLog(@"array: %@", array);
	
	return array;
}

@end
