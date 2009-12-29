//
//  ReviewViewController.m
//  RDict
//
//  Created by Jaewoo Kim on 6/6/09.
//  Copyright 2009 Amplio Studios. All rights reserved.
//

#import "RDictAppDelegate.h"
#import "ReviewViewController.h"
#import "ReviewSessionController.h"
#import "Card.h"

#import "SLStmt.h"

@interface ReviewViewController()

- (NSInteger) numberOfRowsInReviewSection;
- (NSInteger) numberOfRowsInScheduleSection;
- (UITableViewCell *) cellForReviewSectionRowAt:(NSInteger) row;
- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row;
- (UITableViewCell *) cellForScheduleSectionRowAt:(NSInteger) row;


@end


@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize tableView;
@synthesize sectionTitles;


#pragma mark private methods

#pragma mark -

- (void)viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	NSLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	self.navigationController.navigationBarHidden = NO;	

	self.sectionTitles = [NSArray arrayWithObjects:@"Review", @"Statistics", @"Schedule", nil];
							
	scheduledCount = [Card countByScheduled];
	todayCount = [Card countByToday];
	
	NSArray* schedule = [Card reviewSchedulesWithLimit:7];
	NSMutableString* text = [NSMutableString string];
	for( NSArray* row in schedule ) {
		[text appendFormat:@"%@ : %@\n", [row objectAtIndex:0], [row objectAtIndex:1]];
	}
	
	[tableView reloadData];
}

- (void) viewDidAppear:(BOOL)animated {

}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	self.navigationController.navigationBarHidden = YES;
	[sectionTitles release];
	sectionTitles = nil;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}

- (void)dealloc {
	[reviewSessionController release];
    [super dealloc];
}

- (IBAction) studyButtonClicked:(id) sender {
	if( nil == reviewSessionController ) {
		reviewSessionController = [[ReviewSessionController alloc]initWithNibName:@"ReviewSessionView" bundle:nil];
		reviewSessionController.hidesBottomBarWhenPushed = YES;
		//self.reviewSessionController.wantsFullScreenLayout = YES;
	}	

	[self.navigationController pushViewController:reviewSessionController animated:YES];
}

- (IBAction) rescheduleButtonClicked:(id) sender {
	SLStmt* stmt = [SLStmt stmtWithSql:@"update card set scheduled = date( 'now', 'localtime' )" ];

	[stmt step];
	[stmt close];
}

#pragma mark Table view methods


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return [sectionTitles count];;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
	return [sectionTitles objectAtIndex:section];
}

#pragma mark NumberOfSections 

// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	if (0 == section) {
		return [self numberOfRowsInReviewSection];
	} else if ( 1 == section ) {
		return 2;
	} else if ( 2 == section ) {
		return [self numberOfRowsInScheduleSection];
	} else {
		return 0;
	}
}

- (NSInteger) numberOfRowsInReviewSection {
	NSInteger rows = (scheduledCount > 0 || todayCount > 0) ? 1 : 1;
	if ( TRUE )
		rows = rows+1;
	return rows;
}

- (NSInteger) numberOfRowsInScheduleSection {
	return 0;
}

#pragma mark cellForRow


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger section = indexPath.section;
	NSInteger row = indexPath.row;
	
	
	if( 0 == section) {
		return [self cellForReviewSectionRowAt:row];		
	} else if ( 1 == section ) {
		return [self cellForStatisticsSectionRowAt:row];
	} else {
		return [self cellForScheduleSectionRowAt:row];
	}
}

- (UITableViewCell *) cellForReviewSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"ReviewCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
	if ( 1 == row ) {
		cell.textLabel.text = @"Reschedule to today";
		return cell;
	}
	
	if ( scheduledCount > 0 ) {
		cell.textLabel.text = [NSString stringWithFormat:@"Scheduled: %d", scheduledCount];
	} else if ( todayCount > 0 ) {
		cell.textLabel.text = [NSString stringWithFormat:@"Today searched: %d", todayCount];
	} else {
		cell.textLabel.text = @"No card to study";
	} 
	return cell;
}

- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"StatisticsCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
	cell.textLabel.text = [NSString stringWithFormat:@"Text %d", row];
	return cell;
}

- (UITableViewCell *) cellForScheduleSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"ScheduleCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
	cell.textLabel.text = [NSString stringWithFormat:@"Text %d", row];
	return cell;
}


#pragma mark cell Selected

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog(@"MVC.didSelectRowAtIndexPath, row=%d", indexPath.row);
	
	
}

@end
