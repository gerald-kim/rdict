//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "Wiktionary.h"

@interface WiktionaryTest : SenTestCase {
}

@end


@implementation WiktionaryTest

- (void) testInit {
	Wiktionary* wiktionary = [[Wiktionary alloc] init];
	STAssertNotNULL( wiktionary.indexDb, nil ); 
	STAssertNotNULL( wiktionary.wordDb, nil ); 
	char* value = tcbdbget2(wiktionary.indexDb, "you");
	STAssertEqualStrings( @"you", [NSString stringWithUTF8String:value], nil );
	free( value );
	
	[wiktionary release];
}

@end
