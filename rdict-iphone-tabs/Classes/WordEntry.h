//
//  WiktionaryEntry.h
//  RDict
//
//  Created by Jaewoo Kim on 6/8/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface WordEntry : NSObject {
	NSString *lemma;
	NSMutableString *definitionHtml;
}


@property (nonatomic, retain) NSString *lemma;
@property (nonatomic, retain) NSMutableString *definitionHtml;

- (id) initWithLemma:(NSString *) lemmaArg andDefinitionHtml:(NSString *) definitionHtmlArg;
- (void) decorateDefinition;

@end
