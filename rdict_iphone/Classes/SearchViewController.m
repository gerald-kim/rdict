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
#import "History.h"

@implementation SearchViewController

@synthesize dictionaryViewController;
@synthesize searchBar;
@synthesize tableView;
@synthesize wiktionary;

- (void) search: (NSString *) searchText  {
	NSUInteger row = [wiktionary fillIndexesByKey:[searchText lowercaseString]];	
	[tableView reloadData];
	[tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:0]  atScrollPosition:UITableViewScrollPositionTop animated:false];		
}


-(void) preloadDictionaryView:(id)anObject {
	NSAutoreleasePool *autoreleasepool = [[NSAutoreleasePool alloc] init];

	self.dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	self.dictionaryViewController.wiktionary = wiktionary;
	NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"rdict://lookupprepare/?lemma=a"]];
	[self.dictionaryViewController handleRdictRequest:url];
	
    [autoreleasepool release];
    [NSThread exit];
}


- (void)viewDidLoad {
	[super viewDidLoad];

	self.title = @"Search";
	
	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
	self.wiktionary = delegate.wiktionary;
	
	[self search:@"a"];
	
	searchBar.autocapitalizationType =  UITextAutocapitalizationTypeNone;
	searchBar.autocorrectionType = UITextAutocorrectionTypeNo;	
	if( self.dictionaryViewController == nil ) {
		[NSThread detachNewThreadSelector:@selector(preloadDictionaryView:) toTarget:self withObject:nil]; 
	}
}


- (void)viewWillAppear:(BOOL) animated {
	DebugLog( @"SVC.viewWillappear()" );
	[super viewWillAppear:animated];
	[(RDictAppDelegate*) [[UIApplication sharedApplication] delegate] updateReviewTabAndBadge];
	self.navigationController.navigationBarHidden = YES;
}

- (void)viewDidAppear:(BOOL)animated {	
	[super viewDidAppear:animated];
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

- (void) resetSearchBar {
	searchBar.text = @"";
	[searchBar becomeFirstResponder];
}


- (void)showDictionaryView:(NSString*) lemma {
	History* history = [[History alloc] initWithLemma: lemma];
	[history save];
	[history release];
	
	dictionaryViewController.lemma = lemma;
	[self.navigationController pushViewController:dictionaryViewController animated:YES];
}

#pragma mark -
#pragma mark Search Bar Methods


- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
	DebugLog( @"SVC.textDidChanged called" );
	if( [searchText isEqualToString:@"" ] ) {
		searchText = @"a";
	}
	[self search: searchText];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)aSearchBar {
	WordIndex* index = [[wiktionary findIndexByQuery:searchBar.text] autorelease];
	[self showDictionaryView: index.lemma];		
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) aSearchBar {
	DebugLog( @"SVC.searchBarCancelButtonClicked" );
	if ( [searchBar.text length] > 0 ) {
		searchBar.text = @"";
//		[self search:@"a"];
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
//	DebugLog( @"cellForRowAtIndexPath : %d", indexPath.row );
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}
	
	WordIndex *index = [wiktionary.wordIndexes objectAtIndex:indexPath.row];
	cell.textLabel.text = index.lemma;
	
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


@implementation UISearchBar (CustomBG)
- (void)drawRect:(CGRect)rect {
	UIColor *color = [UIColor redColor];
	CGContextRef context = UIGraphicsGetCurrentContext();
	CGContextSetFillColor(context, CGColorGetComponents( [color CGColor]));
	CGContextFillRect(context, rect);
}
@end
