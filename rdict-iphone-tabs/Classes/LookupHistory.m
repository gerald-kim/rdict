//
//  LookupHistory.m
//  RDict
//
//  Created by Stephen Bodnar on 02/01/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "rdict.h"
#import "LookupHistory.h"


@implementation LookupHistory



-(id) init {
	index = 0;
	histories = [[NSMutableArray alloc] init];
		
	return self;	
}

- (void) dealloc
{
	[histories release];
	[super dealloc];
}

-(void) addHistory: (NSURL*) url {
	DebugLog( @"ADDED TO LOOKUPHISTORY, %@", url );
	if( index <= [histories count]) {
		NSRange range;
		range.location = index;
		range.length = histories.count - index;
		[histories removeObjectsInRange:range];
	}
		
	[histories addObject: url];
	index++;

}
	
-(BOOL)	canGoBack{
	return index > 1;
}

-(NSURL*) goBack {	
	index--;
	return [histories objectAtIndex:index-1];
}

-(BOOL) canGoForward {
	return index < histories.count;
}
	
-(NSURL*) goForward {
	index++;
	return [histories objectAtIndex:index-1];
}

@end
