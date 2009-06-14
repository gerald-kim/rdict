//
//  SearchViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "SearchViewController.h"
#import "DictionaryViewController.h"
#import "RDictAppDelegate.h"
#import "Wiktionary.h"
#import "WordIndex.h"

@implementation SearchViewController

@synthesize dictionaryViewController;
@synthesize searchBar;
@synthesize tableView;
@synthesize wiktionary;

- (void)viewWillAppear:(BOOL)animated {

}

- (void)viewDidAppear:(BOOL)animated {
	[searchBar becomeFirstResponder];
}


- (void)viewDidLoad {
	[super viewDidLoad];

	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	wiktionary = delegate.wiktionary;
	
	[wiktionary fillIndexesByKey:@"a"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[wiktionary release];
	[dictionaryViewController release];
    [super dealloc];
}

#pragma mark -
#pragma mark Search Bar Methods

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
	NSLog( @"Input text : %@", searchText ); 

	NSUInteger row = [wiktionary fillIndexesByKey:searchText];
	[self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:0]  atScrollPosition:UITableViewScrollPositionTop animated:false];	
	[tableView reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBarArg {
	[searchBarArg resignFirstResponder];
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
	return [wiktionary.wordIndexes count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	NSLog( @"cellForRowAtIndexPath : %d", indexPath.row );
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	WordIndex *index = [wiktionary.wordIndexes objectAtIndex:indexPath.row];
	cell.text = index.lemma;
	
//	if( [listData count] - 1 == indexPath.row ) {
//		[self.listData addObjectsFromArray:[wiktionary listForward:nil withLimit:10]];
//		[self.listData removeObjectAtIndex:(NSUInteger)0];
//		[tableView reloadData];
//	}
	
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog( @"Index : %d", indexPath.row );
}


@end