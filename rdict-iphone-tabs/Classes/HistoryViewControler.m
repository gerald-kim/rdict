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

@synthesize tableView;
@synthesize dictionaryViewController;
@synthesize histories;


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
	[histories release];
	[dictionaryViewController release];	
}


- (void)dealloc {
    [super dealloc];
}

- (void)showDictionaryView:(NSString*) lemma {
	if( self.dictionaryViewController == nil ) {
		self.dictionaryViewController = [[DictionaryViewController alloc]initWithNibName:@"DictionaryView" bundle:nil];		
	}
	
	dictionaryViewController.lemma = lemma;
	[self.navigationController pushViewController:dictionaryViewController animated:YES];
}


#pragma mark -
#pragma mark Table View Data Source Methods

- (NSInteger)numberOfSections {
	return 1;
}

- (NSInteger)numberOfRowsInSection:(NSInteger)section {
	return [histories count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	if(section == 0)
		return @"Countries to visit";
	else
		return @"Countries visited";
}

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
	History* history = [histories objectAtIndex:indexPath.row];
	[self showDictionaryView:history.lemma];
}


@end
