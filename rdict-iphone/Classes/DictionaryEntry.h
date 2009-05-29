//
//  DictionaryEntry.h
//  RDict
//
//  Created by Stephen Bodnar on 28/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface DictionaryEntry : NSObject {
	NSString *word;
	NSMutableString *entry;
}

@property (nonatomic, retain) NSString *word;
@property (nonatomic, retain) NSMutableString *entry;

- (id) initWithWord:(NSString *) word_arg andEntry:(NSString *) entry_arg;
- (void) htmlifyEntry;

@end
