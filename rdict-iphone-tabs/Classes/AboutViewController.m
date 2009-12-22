//
//  AboutViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 11/21/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "AboutViewController.h"

@implementation AboutViewController

@synthesize webView;

-(void) viewWillAppear:(BOOL)animated
{
	NSLog( @"HVC.viewWillAppear" );
	NSString *path = [[NSBundle mainBundle] bundlePath];
	NSURL *baseURL = [NSURL fileURLWithPath:path];	
	
	NSString *infoSouceFile = [[NSBundle mainBundle] pathForResource:@"rdict_about" ofType:@"html"];
	NSString *infoText = [NSString stringWithContentsOfFile:infoSouceFile encoding:NSUTF8StringEncoding error:nil];
	NSLog( @"%s", infoText );
    [webView loadHTMLString:infoText baseURL:baseURL];
	
	//	[webView loadHTMLString:<#(NSString *)string#> baseURL:baseURL];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"About";
}

/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end
