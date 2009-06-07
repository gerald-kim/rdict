//
//  TokyoCabinetTest.m
//  RDict
//
//  Created by Jaewoo Kim on 6/7/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "GTMSenTestCase.h"
#import "tcbdb.h"

@interface TokyoCabinetTest : SenTestCase {
	
}

@end


@implementation TokyoCabinetTest

- (void) testSearch {

	STAssertTrue( 1 == 1, nil );
	STAssertEqualStrings( @"Abc", @"Abc", nil );

	TCBDB *bdb = tcbdbnew();
	int errorCode;
	
	if( !tcbdbopen( bdb, "test.db", BDBFOPEN ) ){ 
		errorCode = tcbdbecode(bdb);
		fprintf(stderr, "open error: %s\n",	tcbdberrmsg(errorCode));
	}
	
	char *value = tcbdbget2(bdb, "key1");
	NSString *valueString = [NSString stringWithUTF8String:value];

	STAssertEqualStrings( @"val1", valueString, nil );

	free(value);

	STAssertEqualStrings( @"val1", valueString, nil );
	
	tcbdbclose(bdb);
	tcbdbdel(bdb);
 
}

@end
