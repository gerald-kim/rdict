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
@synthesize sectionInfo;

- (void)viewWillAppear:(BOOL)animated
{
	NSLog(@"HVC.viewWillAppear");
	self.title = @"History";
	self.histories = [History findRecents];
	self.sectionInfo = [History buildHistorySectionInfo:self.histories];
	[self.tableView reloadData];
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
	[sectionInfo release];
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

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	NSNumber *number = [sectionInfo valueForKey:@"sectionCount"];
	NSLog( @"numberOfSections: %@", number );
	return [number intValue];	
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	NSString* title = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];

	NSLog( @"titleForHeaderInSection: %@", title );
	return title;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	NSString *sectionTitle = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];
	NSNumber *number = [sectionInfo valueForKey:sectionTitle];
	
	NSLog( @"numberOfRowsInSection: %@", number );

	return [number intValue];
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
