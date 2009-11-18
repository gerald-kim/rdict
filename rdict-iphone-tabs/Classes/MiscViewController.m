//
//  MiscViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 11/18/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "MiscViewController.h"


@implementation MiscViewController

@synthesize listData;
//@synthesize aboutViewController;
@synthesize tableView;


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	listData = [[NSArray alloc] initWithObjects:@"About", @"Help", @"Card Management", nil];
	
}

- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[listData release];
	listData = nil;
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


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
//        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
    // Set up the cell...
	cell.textLabel.text = [listData objectAtIndex:indexPath.row];

    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog(@"MVC.didSelectRowAtIndexPath, row=%d", indexPath.row);
	
	UIViewController *childViewController;
	
	if( 0 == indexPath.row ) {
		childViewController = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
	} else if( 1 == indexPath.row ) {
		childViewController = [[HelpViewController alloc] initWithNibName:@"HelpView" bundle:nil];
	} else if( 2 == indexPath.row ) {
		childViewController = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
	} 
	[self.navigationController pushViewController:childViewController animated:TRUE];
	[childViewController release];
}

- (void)dealloc {
    [super dealloc];
}		


@end

