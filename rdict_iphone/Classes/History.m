//
//  History.m
//  RDict
//
//  Created by Jaewoo Kim on 9/22/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "rdict.h"
#import "History.h"


@implementation History

@synthesize lemma;
@synthesize created;

DECLARE_PROPERTIES (
					DECLARE_PROPERTY( @"lemma", @"@\"NSString\""),
					DECLARE_PROPERTY( @"created", @"@\"NSDate\"")
)

+(NSArray *)indices
{
	NSArray *index1 = [NSArray arrayWithObject:@"created"];
	return [NSArray arrayWithObjects:index1, nil];
}

+ (NSArray*) findRecents
{
	NSString* recentCriteria = @"where created > date('now', '-30 day') order by created desc";
	return [History findByCriteria:recentCriteria];
}

+ (void) clearHistory {
	NSArray* historyRecords = [History allObjects];
	
	for( History* h in historyRecords) {
		[h deleteObject];
	}
}

+ (NSMutableDictionary*) buildHistorySectionInfo:(NSArray*) histories {
	NSMutableDictionary* d = [NSMutableDictionary dictionary];
	
	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateStyle:NSDateFormatterLongStyle];
	[dateFormatter setTimeStyle:NSDateFormatterNoStyle];

	NSString* previousDateString = NULL;
	int count = 0;
	int sectionIndex = 0;
	NSString* historyKey = [NSString stringWithFormat:@"H%d", sectionIndex];
	NSMutableArray* historyArray = nil;
	for( History* h in histories ) {
		NSString* dateString = [dateFormatter stringFromDate:h.created];
		
		if (![dateString isEqual:previousDateString]) {
			count = 0;
			[d setValue:dateString forKey:[NSString stringWithFormat:@"%d", sectionIndex]];
			historyKey = [NSString stringWithFormat:@"H%d", sectionIndex];
			historyArray = [NSMutableArray array];
			[d setValue:historyArray forKey:historyKey];
			
			sectionIndex++;
			
		}  
		historyArray = [d objectForKey:historyKey];
		[historyArray addObject:h];
//		DebugLog( @"History %@", historyArray );
		count++;
		[d setValue:[NSNumber numberWithInt:count] forKey:dateString];
		previousDateString = dateString;
	}
	if ( count != 0 ) {
		[d setValue:[NSNumber numberWithInt:count] forKey:previousDateString];
	}
	[d setValue:[NSNumber numberWithInt:sectionIndex] forKey:@"sectionCount"];
	 
//	DebugLog( @"Dictionary %@", d );
	[dateFormatter release];
	return d;
}

- (id) initWithLemma:(NSString *) aLemma
{
	[super init];
	self.lemma = aLemma;
	self.created = [[NSDate alloc] init];
	
	return self;
}

- (void) dealloc
{
	[lemma dealloc];
	[created dealloc];
	[super dealloc];
}

@end
