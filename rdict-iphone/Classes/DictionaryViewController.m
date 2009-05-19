//
//  DictionaryViewController.m
//  RDict
//
//  Created by Stephen Bodnar on 16/05/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "DictionaryViewController.h"
#include "tcutil.h"
#include "tcbdb.h"

@implementation DictionaryViewController
@synthesize searchResultsPane;
@synthesize usersWord;

- (void)textFieldDoneEditing:(id)sender {
	[self.usersWord resignFirstResponder];
	
	self.title = @"did search";
	
	
	TCBDB *bdb;
	
	int ecode;
	char *value;
	
	/* create the object */
	bdb = tcbdbnew();
	
	/* open the database */
	if(!tcbdbopen(bdb, "/Users/sbodnar/programming/projects/iphone-rdict/RDict/en-brief.db", BDBOREADER)){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "open error: %s\n", tcbdberrmsg(ecode));
	}
	
	/* retrieve records */
	
	
	self.title = usersWord.text;
	
	//const char *text = "licorice";
	const char *text = usersWord.text.UTF8String;
	
	value = tcbdbget2(bdb, text);
	
	if(value){
		printf("%s\n", value);
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
	
	
	NSString *nsString = [[NSString alloc] initWithUTF8String:value];
	
	[self.searchResultsPane loadHTMLString:nsString baseURL:nil];
	free(value);
	[nsString release];
	
}

- (void)searchButtonPressed:(id) sender {
	
	self.title = @"did search";
	
	
	TCBDB *bdb;
	
	int ecode;
	char *value;
	
	/* create the object */
	bdb = tcbdbnew();
	
	/* open the database */
	if(!tcbdbopen(bdb, "/Users/sbodnar/programming/projects/iphone-rdict/RDict/en-brief.db", BDBOREADER)){
		ecode = tcbdbecode(bdb);
		fprintf(stderr, "open error: %s\n", tcbdberrmsg(ecode));
	}
	
	/* retrieve records */
	value = tcbdbget2(bdb, "foo");
	if(value){
		printf("%s\n", value);
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
	
	
	NSString *nsString = [[NSString alloc] initWithUTF8String:value];
	
	[self.searchResultsPane loadHTMLString:nsString baseURL:nil];
	free(value);
	[nsString release];
}



// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    self.title = @"Dictionary";
	[super viewDidLoad];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
}


@end
