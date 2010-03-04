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

- (void)viewDidLoad {
    [super viewDidLoad];
	NSLog(@"HVC.viewDidLoad");
	self.title = @"History";
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	NSLog(@"HVC.viewWillAppear");
	self.histories = [History findRecents];
	self.sectionInfo = [History buildHistorySectionInfo:self.histories];

	
	//Add the clear button.
	self.navigationItem.rightBarButtonItem = [[[UIBarButtonItem alloc]
											   initWithTitle:@"Clear"style:UIBarButtonSystemItemDone
											   target:self action:@selector(clearButtonClicked:)] autorelease];
	
	if(self.histories.count <=0)
		[self.navigationItem.rightBarButtonItem setEnabled:FALSE];
	else
		[self.navigationItem.rightBarButtonItem setEnabled:TRUE];
	
	[self.tableView reloadData];
}

- (void)viewDidDisappear:(BOOL)animated
{
	NSLog( @"HVC.viewDidDisappear" );

	[super viewDidDisappear:animated];
	[sectionInfo release]; sectionInfo = nil;
	[histories release]; histories = nil;
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	NSLog( @"HVC.viewDidUnload" );
	[super viewDidUnload];

	// Release any retained subviews of the main view.
	if(self.dictionaryViewController != nil)
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


- (IBAction) clearButtonClicked : (id) sender {	
	NSLog( @"RSC.clearButton" );
	
	UIAlertView *alert = [[UIAlertView alloc] init];
	[alert setTitle:@"Confirm"];
	[alert setMessage:@"Clear your search history?"];
	[alert setDelegate:self];
	[alert addButtonWithTitle:@"Cancel"];
	[alert addButtonWithTitle:@"Yes"];
	[alert show];
	[alert release];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if (buttonIndex == 1) {
		[History clearHistory];

		self.histories = [History findRecents];
		self.sectionInfo = [History buildHistorySectionInfo:self.histories];
	
		[self.navigationItem.rightBarButtonItem setEnabled:FALSE];
		
		[self.tableView reloadData];
	}
}


#pragma mark -
#pragma mark Table View Data Source Methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	NSNumber *number = [sectionInfo valueForKey:@"sectionCount"];
//	NSLog( @"numberOfSections: %@", number );
	return [number intValue];	
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	NSString* title = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];

//	NSLog( @"titleForHeaderInSection: %@", title );
	return title;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	NSString *sectionTitle = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];
	NSNumber *number = [sectionInfo valueForKey:sectionTitle];
	
//	NSLog( @"numberOfRowsInSection: %@", number );

	return [number intValue];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	//	NSLog( @"cellForRowAtIndexPath : %d", indexPath.row );

	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
	}

	NSArray *historyArray = [sectionInfo objectForKey:[NSString stringWithFormat:@"H%d", indexPath.section]];
	History* history = [historyArray objectAtIndex:indexPath.row];
	cell.textLabel.text = history.lemma;		
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSArray *historyArray = [sectionInfo objectForKey:[NSString stringWithFormat:@"H%d", indexPath.section]];
	History* history = [historyArray objectAtIndex:indexPath.row];
	[self showDictionaryView:history.lemma];
}


@end
