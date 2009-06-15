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


- (void)viewDidLoad {
	[super viewDidLoad];
	
	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	self.wiktionary = delegate.wiktionary;
	[delegate release];
	
	[wiktionary fillIndexesByKey:@"a"];
}


- (void)viewWillAppear:(BOOL)animated {

}

- (void)viewDidAppear:(BOOL)animated {
	searchBar.autocapitalizationType =  UITextAutocapitalizationTypeNone;
	searchBar.autocorrectionType = UITextAutocorrectionTypeNo;
	[searchBar becomeFirstResponder];
}

- (void)dealloc {
	[wiktionary release];
	[dictionaryViewController release];
    [super dealloc];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}

- (void)showDictionaryView:(NSString*) lemma {
	if( self.dictionaryViewController == nil ) {
		self.dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	}
	
	dictionaryViewController.lemma = lemma;
	
	[self.navigationController pushViewController:dictionaryViewController animated:YES];	 	
	
}

#pragma mark -
#pragma mark Search Bar Methods

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
	NSUInteger row = [wiktionary fillIndexesByKey:[searchText lowercaseString]];
	
	[tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:0]  atScrollPosition:UITableViewScrollPositionTop animated:false];	
	[tableView reloadData];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)aSearchBar {
	WordIndex* index = [[wiktionary findIndexByQuery:searchBar.text] autorelease];
	[self showDictionaryView: index.lemma];	
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) aSearchBar {
	if ( [searchBar.text length] > 0 ) {
		searchBar.text = @"";
	} else {
		[searchBar resignFirstResponder];
	}
}



#pragma mark -
#pragma mark Table View Data Source Methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [wiktionary.wordIndexes count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
//	NSLog( @"cellForRowAtIndexPath : %d", indexPath.row );
	
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
	WordIndex *index = [wiktionary.wordIndexes objectAtIndex:indexPath.row];
	if( ![searchBar isFirstResponder] ) {
		searchBar.text = index.lemma;
	}
	[self showDictionaryView:index.lemma];
}

#pragma mark -
#pragma mark ScrollViewDelegate  Methods

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
	[searchBar resignFirstResponder];
}

- (void)scrollViewDidScrollToTop:(UIScrollView *)scrollView {
	
}




@end