//
//  ReviewUnfinishedViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/28/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "ReviewUnfinishedViewController.h"
#import "Card.h"

@implementation ReviewUnfinishedViewController

@synthesize scheduledCards;
@synthesize uncertainCards;

@synthesize tableView;

- (void) viewDidDisappear:(BOOL) animated {
	[super viewDidDisappear:animated];
//	[scheduledCards release];
//	[uncertainCards release];
}

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

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [uncertainCards count];
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    DebugLog(@"RUVC.cellForRowAtIndexPath");
    static NSString *CellIdentifier = @"RUVCCell";
    
    UITableViewCell *cell = [aTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		//        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
	Card* card = [uncertainCards objectAtIndex:indexPath.row];
	cell.textLabel.text = card.question;
	
    return cell;
}

@end
