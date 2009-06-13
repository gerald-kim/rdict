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
@synthesize tableView;
@synthesize wiktionary;
@synthesize wordList;

- (void)viewWillAppear:(BOOL)animated {

}

- (void)viewDidAppear:(BOOL)animated {
	//[searchBar becomeFirstResponder];
	[self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:33 inSection:0]  atScrollPosition:UITableViewScrollPositionTop animated:false];	
}


- (void)viewDidLoad {
	[super viewDidLoad];

	NSLog(@"viewDidLoad called");
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	wiktionary = delegate.wiktionary;
//	[delegate release];
	wiktionary = [[Wiktionary alloc] init];
	[wiktionary fillWordList:@"a"];
	//wordList = [wiktionary.wordList mutableCopy];

	[tableView reloadData];
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

	NSUInteger row = [wiktionary fillWordList:searchText];
	[self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:0]  atScrollPosition:UITableViewScrollPositionTop animated:false];	
	[tableView reloadData];
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
	return [wiktionary.wordList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	NSLog( @"cellForRowAtIndexPath : %d", indexPath.row );
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	WiktionaryIndex *index = [wiktionary.wordList objectAtIndex:indexPath.row];
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