//
//  LookupHistory.h
//  RDict
//
//  Created by Stephen Bodnar on 02/01/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface LookupHistory : NSObject {
	NSUInteger index;
	NSMutableArray* histories;	
}

-(void) addHistory:(NSURL*) url;

-(BOOL)	canGoBack;
-(NSURL*) goBack;

-(BOOL) canGoForward;
-(NSURL*) goForward;

@end
