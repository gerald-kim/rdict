//
//  HistoryViewControler.m
//  RDict
//
//  Created by Jaewoo Kim on 9/16/09.
//  Copyright 2009 ampliostudios. All rights reserved.
//

#import "HistoryViewControler.h"
#import "History.h"
#import "RDictAppDelegate.h"


@implementation HistoryViewControler

@synthesize tableView;
@synthesize dictionaryViewController;
@synthesize histories;
@synthesize sectionInfo;

- (void)viewDidLoad {
    [super viewDidLoad];
	DebugLog(@"HVC.viewDidLoad");
//	self.navigationController.navigationBarHidden = NO;
	
	self.title = @"History";
	
	//Add the clear button.
	self.navigationItem.rightBarButtonItem = [[[UIBarButtonItem alloc]
											   initWithTitle:@"Clear"style:UIBarButtonSystemItemDone
											   target:self action:@selector(clearButtonClicked:)] autorelease];	
}

- (void)viewWillAppear:(BOOL)animated
{
	DebugLog(@"HVC.viewWillAppear");
	[super viewWillAppear:animated];
	[(RDictAppDelegate*) [[UIApplication sharedApplication] delegate] updateReviewTab];

	self.histories = [History findRecents];
	self.sectionInfo = [History buildHistorySectionInfo:self.histories];

	if(self.histories.count <=0)
		self.navigationItem.rightBarButtonItem.enabled = FALSE;
	else
		self.navigationItem.rightBarButtonItem.enabled = YES;	
	
	[self.tableView reloadData];
}

- (void)viewDidDisappear:(BOOL)animated
{
	DebugLog( @"HVC.viewDidDisappear" );

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
	DebugLog( @"HVC.viewDidUnload" );
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
	DebugLog( @"RSC.clearButton" );
	
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
//	DebugLog( @"numberOfSections: %@", number );
	return [number intValue];	
}

- (UIView *) tableView:(UITableView *)aTableView viewForHeaderInSection:(NSInteger)section 
{
	UIView *headerView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width, 30)] autorelease];
	
	[headerView setBackgroundColor:[UIColor colorWithRed:0.000 green:0.251 blue:0.502 alpha:1.000]];
	
	UILabel *label = [[[UILabel alloc] initWithFrame:CGRectMake(10, 3, tableView.bounds.size.width - 10, 18)] autorelease];
	label.text = [self tableView:aTableView titleForHeaderInSection:section];
	label.font = [UIFont boldSystemFontOfSize:16.0];
	label.textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:0.75];
	label.backgroundColor = [UIColor clearColor];
	[headerView addSubview:label];
	return headerView;
	
}

-
(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	NSString* title = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];

//	DebugLog( @"titleForHeaderInSection: %@", title );
	return title;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	NSString *sectionTitle = [sectionInfo objectForKey:[NSString stringWithFormat:@"%d", section]];
	NSNumber *number = [sectionInfo valueForKey:sectionTitle];
	
//	DebugLog( @"numberOfRowsInSection: %@", number );

	return [number intValue];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
	//	DebugLog( @"cellForRowAtIndexPath : %d", indexPath.row );

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
