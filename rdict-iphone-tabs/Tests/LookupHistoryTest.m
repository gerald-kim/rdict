//
//  LookupHistory.m
//  RDict
//
//  Created by Jaewoo Kim on 2/5/10.
//  Copyright 2010 ampliostudios. All rights reserved.
//



#import <TargetConditionals.h>
#import "GTMSenTestCase.h"

#import "LookupHistory.h"

@interface LookupHistoryTest : SenTestCase {
	
}
@end

#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR

@implementation LookupHistoryTest

- (void) testInit {
	LookupHistory* history = [[LookupHistory alloc] init];
	
	
	STAssertFalse( [history canGoBack], nil );
	STAssertFalse( [history canGoForward], nil );
}

- (void) testWhenFirstAddHistory {
	LookupHistory* history = [[LookupHistory alloc] init];
	NSURL* url = [NSURL URLWithString:@"http://1.net"];
	
	[history addHistory:url];

	STAssertFalse( [history canGoBack], nil );
	STAssertFalse( [history canGoForward], nil );	
}

- (void) testWhenSecondAddHistory {
	LookupHistory* history = [[LookupHistory alloc] init];
	NSURL* url0 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url0];
	
	NSURL* url1 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url1];
	
	STAssertTrue( [history canGoBack], nil );
	STAssertFalse( [history canGoForward], nil );	
	STAssertEquals( url0, [history goBack], nil);
	STAssertTrue( [history canGoForward], nil );
	STAssertEquals( url1, [history goForward], nil);
}

- (void) testWhenAddAddBackAdd {
	LookupHistory* history = [[LookupHistory alloc] init];
	NSURL* url0 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url0];
	
	NSURL* url1 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url1];
	
	STAssertEquals( url0, [history goBack], nil);
	
	NSURL* url2 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url2];
	NSURL* url3 = [NSURL URLWithString:@"http://1.net"];
	[history addHistory:url3];

	STAssertTrue( [history canGoBack], nil );
	STAssertEquals( url2, [history goBack], nil);
	STAssertEquals( url0, [history goBack], nil);
	STAssertEquals( url2, [history goForward], nil);
	STAssertEquals( url3, [history goForward], nil);
}

@end

#endif