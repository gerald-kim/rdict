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
- (void) loadData;
- (void) releaseData;

@end

@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize tableView;
@synthesize sectionTitles;
@synthesize schedules;

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

	self.sectionTitles = [NSArray arrayWithObjects:@"Review Exercises", @"Statistics", @"Upcoming Review Sessions", nil];
							
	[self loadData];
}

- (void) viewDidAppear:(BOOL)animated {

}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	self.navigationController.navigationBarHidden = YES;
	[self releaseData];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}

- (void)dealloc {
	[reviewSessionController release];
    [super dealloc];
}

#pragma mark data loading

- (void) loadData {
	NSLog( @"RVC.loadedData");
	
	scheduledCount = [Card countByScheduled];
	todayCount = [Card countByToday];	
	totalCount = [Card count];
	score = [Card score];
	
	self.schedules = [Card reviewSchedulesWithLimit:7];
	
	[tableView reloadData];	
}

- (void) releaseData {
	[sectionTitles release];
	sectionTitles = nil;
	[schedules release];
	schedules = nil;
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
	NSLog( @"RVC.numberOfRowsInScheduleSection schedule: %d", [schedules count] );
	
	return [schedules count] == 0 ? 1 : [schedules count];
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
		cell.textLabel.text = [NSString stringWithFormat:@"Today's Review: %d %@", scheduledCount, [self getCardString:scheduledCount]];
	} else if ( todayCount > 0 ) {
		cell.textLabel.text = [NSString stringWithFormat:@"Early Practice: %d %@", todayCount, [self getCardString:todayCount]];
	} else {
		cell.textLabel.text = @"None available";
	} 
	return cell;
}

- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"StatisticsCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
	if ( 0 == row ) {
		cell.textLabel.text = [NSString stringWithFormat:@"Total Cards: %d", totalCount];
	} else if ( 1 == row ) {
		cell.textLabel.text = [NSString stringWithFormat:@"Your Score: %d", score];
	}
	return cell;
}

- (UITableViewCell *) cellForScheduleSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"ScheduleCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }

	if ( [schedules count] > 0 ) {
		NSArray* scheduleArray = [schedules objectAtIndex:row];	
		cell.textLabel.text = [NSString stringWithFormat:@"%@ : %@ %@", [scheduleArray objectAtIndex:0], 
																		[scheduleArray objectAtIndex:1], 
																		[self getCardString:[[scheduleArray objectAtIndex:1] intValue]]];		
	} else {
		cell.textLabel.text = @"None scheduled";
	}
	return cell;
}


#pragma mark cell Selected

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog(@"MVC.didSelectRowAtIndexPath, section=%d, row=%d", indexPath.section, indexPath.row);
	[self.tableView deselectRowAtIndexPath:indexPath animated:TRUE];

	if ( 0 != indexPath.section ) {
		return;
	}
	if ( 0 == indexPath.row && [self isCardExerciseAvailable]) {
		if( nil == reviewSessionController ) {
			reviewSessionController = [[ReviewSessionController alloc]initWithNibName:@"ReviewSessionView" bundle:nil];
			reviewSessionController.hidesBottomBarWhenPushed = YES;
			//self.reviewSessionController.wantsFullScreenLayout = YES;
		}	
		
		reviewSessionController.useScheduledCards = scheduledCount > 0;
		[self.navigationController pushViewController:reviewSessionController animated:YES];
	} else if ( 1 == indexPath.row ) {
		SLStmt* stmt = [SLStmt stmtWithSql:@"update card set scheduled = date('now', 'localtime')" ];
		
		[stmt step];
		[stmt close];
		scheduledCount = [Card countByScheduled];
		todayCount = [Card countByToday];
		[self.tableView reloadData];
	}
}

-(BOOL) isCardExerciseAvailable {
	return scheduledCount > 0 || todayCount > 0;
}

-(NSString*) getCardString: (NSInteger) cardCount {
	if (cardCount > 1)
		return @"cards";
	else
		return @"card";
}



@end