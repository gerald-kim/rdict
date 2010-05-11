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


#pragma mark private methods

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

#define STAT_CELL_SPARKLINE 3

#pragma mark -

@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize tableView;
@synthesize sparklineView;
@synthesize sectionTitles;
@synthesize schedules;
@synthesize cardsForReview;


#pragma mark -

- (void)viewDidLoad {
	[super viewDidLoad];

	UIImage *backgroundImage = [UIImage imageNamed:@"contents_bg.png"];
	UIColor *backgroundColor = [[UIColor alloc] initWithPatternImage:backgroundImage];
	self.tableView.backgroundColor = backgroundColor;

	[backgroundColor release];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	DebugLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	[(RDictAppDelegate*) [[UIApplication sharedApplication] delegate] updateReviewTabAndBadge];

	self.sectionTitles = [NSArray arrayWithObjects:@"Review Exercise", @"Statistics", @"Review Schedule", nil];
	[self loadData];
//	tableView.allowsSelection = NO;
	
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
	DebugLog( @"RVC.loadedData");

	totalCount = [Card count];
	score = [Card countByMastered];
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


- (NSString *)tableView:(UITableView *)tableView titleForFooterInSection:(NSInteger)section
{
	if ( 0 == section ) {
		return [Card footerForReview];
//	} else if ( 1 == section ) {
//		return @"Mastered card means you know that well. (Scored above 4 in recent two reviews)";
	} else {
		return nil;
	}
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
#if DEBUG
	rows = rows+1;
#endif
	return rows;
}

- (NSInteger) numberOfRowsInScheduleSection {
	DebugLog( @"RVC.numberOfRowsInScheduleSection schedule: %d", [schedules count] );
	
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
	static NSString *ReviewCellIdentifier = @"ReviewCell";
//	static NSString *NACellIdentifier = @"ReviewCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ReviewCellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:ReviewCellIdentifier] autorelease];
		cell.backgroundColor = BGCOLOR;
	}
    
	if ( 1 == row ) {
		cell.textLabel.text = @"Reschedule to today";
		return cell;
	}
	if ( [[Card countMessageForReview] isEqualToString:@""] ) {
		cell.accessoryType = UITableViewCellAccessoryNone;
	} else {
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
	cell.textLabel.text = [Card messageForReview];
	cell.detailTextLabel.text = [NSString stringWithFormat:@"%@", [Card countMessageForReview]];
	return cell;
}


- (UITableViewCell *) cellForStatisticsSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"StatisticsCell";
    
	CKSparkline *sparkline;
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
		cell.backgroundColor = BGCOLOR;
	} else {
		[[cell.contentView viewWithTag:STAT_CELL_SPARKLINE] removeFromSuperview];
	}
	sparkline = [[[CKSparkline alloc]
				  initWithFrame:CGRectMake(150, 10.0, 100.0, 20.0)] autorelease];
	sparkline.tag = STAT_CELL_SPARKLINE;
	[cell.contentView addSubview:sparkline];
    
	if ( 0 == row ) {
		cell.textLabel.text = @"Total Cards";
		cell.detailTextLabel.text = [NSString stringWithFormat:@"%d", totalCount];
		NSArray* data = [StatisticsManager cardCountsOfRecentDay:30];
		sparkline.data = data;
		[data release];
	} else if ( 1 == row ) {
		cell.textLabel.text = @"Mastered Cards";
		cell.detailTextLabel.text = [NSString stringWithFormat:@"%d", score];
		NSArray* data = [StatisticsManager masteredCardsCountOfRecentDay:30];
		sparkline.data = data;
		[data release];
	}
	
	return cell;
}

- (UITableViewCell *) cellForScheduleSectionRowAt:(NSInteger) row {
	static NSString *CellIdentifier = @"ScheduleCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
		cell.backgroundColor = BGCOLOR;	
    }

	if ( [schedules count] > 0 ) {
		NSArray* scheduleArray = [schedules objectAtIndex:row];	
		cell.textLabel.text = [scheduleArray objectAtIndex:0];
		cell.detailTextLabel.text = [scheduleArray objectAtIndex:1];
	} else {
		cell.textLabel.text = @"None scheduled";
		cell.detailTextLabel.text = @"";
	}
	return cell;
}

#pragma mark cell Selected

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	DebugLog(@"MVC.didSelectRowAtIndexPath, section=%d, row=%d", indexPath.section, indexPath.row);
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