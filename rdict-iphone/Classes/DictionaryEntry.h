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
	NSString *entry;
}

@property (nonatomic, retain) NSString *word;
@property (nonatomic, retain) NSString *entry;

- (id) initWithWord:(NSString *) word_arg andEntry:(NSString *) entry_arg;

@end
