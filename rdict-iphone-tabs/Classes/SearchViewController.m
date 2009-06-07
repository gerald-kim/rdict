//
//  SearchViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "SearchViewController.h"
#import "DictionaryViewController.h"

@implementation SearchViewController

@synthesize dictionaryViewController;
@synthesize searchBar;
@synthesize listData;

- (IBAction) dictButtonPressed:(id)sender {
	NSLog(@"dictButtonPressed");
	
	if( dictionaryViewController == nil ) {
		dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	}
	
	[self.navigationController pushViewController:dictionaryViewController animated:YES];	 
}

- (void)viewWillAppear:(BOOL)animated {
//	self.navigationController.navigationBarHidden = YES;
//	[searchBar becomeFirstResponder];
//	[searchBar resignFirstResponder];
}

- (void)viewDidAppear:(BOOL)animated {
//	[searchBar becomeFirstResponder];
//	[searchBar resignFirstResponder];	
}

        
- (void)viewDidLoad {
	NSLog(@"viewDidLoad called");
	//Initialize the array.
	//listData = [[NSArray alloc] initWithObjects:@"Test", "@Test1"];
	//listData = [[NSMutableArray alloc] init];
	NSArray *array = [[NSArray alloc] initWithObjects:@"Test", @"Test2", nil];
	self.listData = array;
	NSLog(@"viewDidLoad called2", [array count]);
	[array release];
	
	[super viewDidLoad];
	
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[listData release];
	[dictionaryViewController release];
    [super dealloc];
}

#pragma mark -
#pragma mark Table View Data Source Methos

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	NSLog(@"numberOfRows called");
	//return [self.listData count];
	return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	NSLog(@"cellForRowAtIndexPath called " );
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	// Set up the cell...
	NSString *cellValue = @"Test";//[self.listData objectAtIndex:indexPath.row];
	cell.text = cellValue;
	
	return cell;
}


@end
