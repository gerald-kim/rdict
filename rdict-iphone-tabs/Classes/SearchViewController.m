//
//  SearchViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 NHN. All rights reserved.
//

#import "SearchViewController.h"
#import "DictionaryViewController.h"
#import "RDictAppDelegate.h"
#import "Wiktionary.h"
#import "WiktionaryIndex.h"

@implementation SearchViewController

@synthesize dictionaryViewController;
@synthesize searchBar;
@synthesize listData;
@synthesize wiktionary;

- (void)viewWillAppear:(BOOL)animated {
}

- (void)viewDidAppear:(BOOL)animated {
	[searchBar becomeFirstResponder];
}


- (void)viewDidLoad {
	[super viewDidLoad];

	NSLog(@"viewDidLoad called");
	//RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	//[delegate release];
	
	//wiktionary = delegate.wiktionary;
	wiktionary = [[Wiktionary alloc]init];
	
	listData = [[NSMutableArray alloc] init];
	[listData addObjectsFromArray: [wiktionary listForward:nil withLimit:20]];
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
#pragma mark Search Bar Methods

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
	NSLog( @"Input text : %@", searchText ); 
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBarArg {
	[searchBarArg resignFirstResponder];
//	[tableView becomeFirstResponder];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
	if( dictionaryViewController == nil ) {
		dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	}
	
	[self.navigationController pushViewController:dictionaryViewController animated:YES];	 
	
}



#pragma mark -
#pragma mark Table View Data Source Methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [self.listData count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	WiktionaryIndex *index = [self.listData objectAtIndex:indexPath.row];
	cell.text = index.lemma;
	[index release];
	
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog( @"Index : %d", indexPath.row );
}


@end