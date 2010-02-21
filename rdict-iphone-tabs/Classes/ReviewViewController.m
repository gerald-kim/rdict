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
#import "CKSparkline.h"
#import "SLStmt.h"
#import "StatisticsManager.h"

@interface ReviewViewController()

- (NSInteger) numberOfRowsInReviewSection;
- (NSInteger) numberOfRowsInScheduleSection;
- (UITableViewCell *) cellForReviewSectionRowAt:(NSInteger) row;
- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row;
- (UITableViewCell *) cellForScheduleSectionRowAt:(NSInteger) row;
- (void) loadData;
- (void) releaseData;
- (NSString*) getCardString: (NSInteger) cardCount;

@end

@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize tableView;
@synthesize sparklineView;
@synthesize sectionTitles;
@synthesize schedules;
@synthesize cardsForReview;

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

	self.sectionTitles = [NSArray arrayWithObjects:@"Review Exercises", @"Statistics", @"Upcoming Review Sessions", nil];
							
	[self loadData];
	
}

- (void) viewDidAppear:(BOOL)animated {

}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
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

	totalCount = [Card count];
	score = [Card score];
	self.cardsForReview = [Card cardsForReview];
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
	NSInteger rows = ([cardsForReview count] > 0) ? 1 : 1;
#if !TARGET_OS_IPHONE || TARGET_IPHONE_SIMULATOR
	rows = rows+1;
#endif
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
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }
    
	if ( 1 == row ) {
		cell.textLabel.text = @"Reschedule to today";
		return cell;
	}
	
	cell.textLabel.text = [NSString stringWithFormat:@"%@ : %@", [Card messageForReview], [Card countMessageForReview]];
	
	return cell;
}

#define STAT_CELL_LABEL_TAG 1
#define STAT_CELL_COUNT_TAG 2
#define STAT_CELL_SPARKLINE 3

- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"StatisticsCell";
    
	UILabel *mainLabel, *secondLabel;
	CKSparkline *sparkline;
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];

		mainLabel = [[[UILabel alloc] initWithFrame:CGRectMake(30.0, 0.0, 220.0, 50.0)] autorelease];
        mainLabel.tag = STAT_CELL_LABEL_TAG;
        mainLabel.font = [UIFont boldSystemFontOfSize:18.0];
        mainLabel.textAlignment = UITextAlignmentLeft;
        mainLabel.textColor = [UIColor blackColor];
        mainLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
        [cell.contentView addSubview:mainLabel];
		
        secondLabel = [[[UILabel alloc] initWithFrame:CGRectMake(100.0, 15.0, 120.0, 25.0)] autorelease];
        secondLabel.tag = STAT_CELL_COUNT_TAG;
        secondLabel.font = [UIFont systemFontOfSize:16.0];
        secondLabel.textAlignment = UITextAlignmentRight;
        secondLabel.textColor = [UIColor darkGrayColor];
        secondLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
        [cell.contentView addSubview:secondLabel];
		
		sparkline = [[[CKSparkline alloc]
								  initWithFrame:CGRectMake(130, 5.0, 100.0, 30.0)] autorelease];
		sparkline.tag = STAT_CELL_SPARKLINE;
		[cell.contentView addSubview:sparkline];
	} else {
		mainLabel = (UILabel *)[cell.contentView viewWithTag:STAT_CELL_LABEL_TAG];
        secondLabel = (UILabel *)[cell.contentView viewWithTag:STAT_CELL_COUNT_TAG];
		sparkline = (CKSparkline *)[cell.contentView viewWithTag:STAT_CELL_SPARKLINE];
	}
    
	if ( 0 == row ) {
		mainLabel.text = @"Total Cards";
		secondLabel.text = [NSString stringWithFormat:@"%d", totalCount];
		sparkline.data = [StatisticsManager cardCountsOfRecentDay:30];
	} else if ( 1 == row ) {
		mainLabel.text = @"Your Score";
		secondLabel.text = [NSString stringWithFormat:@"%d", score];
		sparkline.data = [StatisticsManager scoreAveragesOfRecentDay:30];
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
	if ( 0 == indexPath.row && [cardsForReview count] > 0 ) {
		if( nil == reviewSessionController ) {
			reviewSessionController = [[ReviewSessionController alloc]initWithNibName:@"ReviewSessionView" bundle:nil];
			reviewSessionController.hidesBottomBarWhenPushed = YES;
			//self.reviewSessionController.wantsFullScreenLayout = YES;
		}	

		reviewSessionController.scheduledCards = cardsForReview;
		//[reviewSessionController initCards:cardsForReview];

		[self.navigationController pushViewController:reviewSessionController animated:YES];
	} else if ( 1 == indexPath.row ) {
		SLStmt* stmt = [SLStmt stmtWithSql:@"update card set scheduled = date('now', 'localtime')" ];
		
		[stmt step];
		[stmt close];
		self.cardsForReview = [Card cardsForReview];
		[self.tableView reloadData];
	}
}


#pragma mark help functions

-(NSString*) getCardString: (NSInteger) cardCount {
	if (cardCount > 1)
		return @"cards";
	else
		return @"card";
}



@end