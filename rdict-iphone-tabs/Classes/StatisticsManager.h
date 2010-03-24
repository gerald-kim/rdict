//
//  StatisticsManager.h
//  RDict
//
//  Created by Jaewoo Kim on 2/25/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StatisticsManager : NSObject {

}

+(void) createTable;
+(void) updateStatisticsOfToday;

+(NSArray*) cardCountsOfRecentDay:(NSUInteger) days;
+(NSArray*) masteredCardsCountOfRecentDay:(NSUInteger) days;

@end
