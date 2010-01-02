//
//  LookupHistory.h
//  RDict
//
//  Created by Stephen Bodnar on 02/01/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface LookupHistory : NSObject {
	int index;
	NSMutableArray* words;	
}

@property int index;
@property (nonatomic, retain) NSMutableArray* words;

-(void) addWord: (NSString*) word;
-(BOOL)	canGoBack;
-(BOOL) canGoForward;
-(void) goForward;
-(void) goBack;
-(NSString*) getWord;
-(BOOL) isEmpty;
-(void) clear;
-(int) size;
-(BOOL) containsWord: (NSString*) headword;

@end
