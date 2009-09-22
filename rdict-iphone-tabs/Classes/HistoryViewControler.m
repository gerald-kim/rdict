//
//  HistoryViewControler.m
//  RDict
//
//  Created by Jaewoo Kim on 9/16/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "HistoryViewControler.h"
#import "History.h"


@implementation HistoryViewControler

@synthesize histories;
@synthesize tableView;


- (void)viewWillAppear:(BOOL)animated
{
	self.title = @"History";
	self.histories = [History findRecents];
}

- (void)viewDidLoad {
    [super viewDidLoad];
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
	[histories dealloc];
    [super dealloc];
}

#pragma mark -
#pragma mark Table View Data Source Methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [histories count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	//	NSLog( @"cellForRowAtIndexPath : %d", indexPath.row );
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	History* history = [histories objectAtIndex:indexPath.row];
	cell.textLabel.text = history.lemma;
		
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
}


@end
