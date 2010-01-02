//
//  LookupHistory.m
//  RDict
//
//  Created by Stephen Bodnar on 02/01/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "LookupHistory.h"


@implementation LookupHistory

@synthesize index;
@synthesize words;

- (id) init {
	self.index = 0;
	self.words = [[NSMutableArray alloc] init];
	
	return self;
}

-(void) addWord: (NSString*) word {
	while(words.count > index + 1) {
		[words removeLastObject];
	}
		
	[self.words addObject: word];
	index = words.count - 1;
}
	
-(BOOL)	canGoBack{
	return index > 0;
}

-(BOOL) canGoForward {
	return index < words.count - 1;
}
	
-(void) goForward {
	index++;
}
	
-(void) goBack {
	int prevIndex = index;
	
	if(nil == [self.words objectAtIndex: prevIndex])
		[self.words removeObjectAtIndex: prevIndex];
	
	index--;
}

	
-(NSString*) getWord {
	return [words objectAtIndex: index];
}
	
-(BOOL) isEmpty {
	return 0 == words.count;
}

-(void) clear {
	[self.words removeAllObjects];
}
	
-(int) size {
	return words.count;
}

-(BOOL) containsWord: (NSString*) headword {
	return [words containsObject:headword];
}

- (void) dealloc
{
	//[words dealloc];
	[super dealloc];
}

@end
