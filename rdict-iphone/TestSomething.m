//
//  TestSomething.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "GTMSenTestCase.h"
#include "tcutil.h"
#include "tcbdb.h"

@interface TestSomething : SenTestCase {}
@end

@implementation TestSomething

-(void) testSomething{
	
	TCBDB *bdb;

	int ecode;
	char *value;
	
	/* create the object */
	bdb = tcbdbnew();
	
	/* open the database */
	if(!tcbdbopen(bdb, "/Users/sbodnar/programming/projects/old/iphone-rdict/RDict/en-brief.db", BDBOREADER)){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "open error: %s\n", tcbdberrmsg(ecode));
	}
	
	/* retrieve records */
	value = tcbdbget2(bdb, "foo");
	if(value){
		printf("%s\n", value);
		free(value);
	} else {
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "get error: %s\n", tcbdberrmsg(ecode));
	}
	
	/* close the database */
	if(!tcbdbclose(bdb)){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "close error: %s\n", tcbdberrmsg(ecode));
	}
	
	/* delete the object */
	tcbdbdel(bdb);	
}

@end
