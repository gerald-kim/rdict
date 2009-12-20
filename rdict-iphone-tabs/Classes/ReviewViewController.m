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

static NSString *sectionTitleKey = @"sectionTitle";
static NSString *scheduledCardsKey = @"scheduledCards";
//static NSString *todayCardsKey = @"todayCards";
static NSString *totalCountKey = @"totalCount";
//static NSString *scoreKey = @"score";

@implementation ReviewViewController
@synthesize reviewSessionController;
@synthesize tableView;
@synthesize dataSourceArray;

- (void)viewDidLoad {
	[super viewDidLoad];
//	RDictAppDelegate *delegate = (RDictAppDelegate*) [[UIApplication sharedApplication] delegate];
//	self.wiktionary = delegate.wiktionary;
}

- (void)viewWillAppear:(BOOL)animated {
	NSLog( @"RVC.viewWillAppear" );
	self.title = @"Review";
	self.navigationController.navigationBarHidden = NO;	

	self.dataSourceArray = [NSArray arrayWithObjects:
							[NSDictionary dictionaryWithObjectsAndKeys:
							 @"Review", sectionTitleKey,
							 [NSNumber numberWithInt:[Card countByScheduled]], scheduledCardsKey,
							 //							 [NSNumber numberWithInt:[Card countByToday]], todayCardsKey,
							 nil],
							
							[NSDictionary dictionaryWithObjectsAndKeys:
							 @"Statistics", sectionTitleKey,
							 [NSNumber numberWithInt:[Card count]], totalCountKey,
							 //							 [NSNumber numberWithInt:[Card countByToday]], todayCardsKey,
							 nil],
							nil];
							
	
	NSArray* schedule = [Card reviewSchedulesWithLimit:7];
	NSMutableString* text = [NSMutableString string];
	for( NSArray* row in schedule ) {
		[text appendFormat:@"%@ : %@\n", [row objectAtIndex:0], [row objectAtIndex:1]];
	}
	
}

- (void) viewDidAppear:(BOOL)animated {

}	

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	self.navigationController.navigationBarHidden = YES;
	[dataSourceArray release];
	dataSourceArray = nil;
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
    return [dataSourceArray count];;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
	return [[self.dataSourceArray objectAtIndex: section] valueForKey:sectionTitleKey];
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
		//        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:CellIdentifier] autorelease];
    }
    
    // Set up the cell...
	cell.textLabel.text = [NSString stringWithString:@"Text"];
	
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSLog(@"MVC.didSelectRowAtIndexPath, row=%d", indexPath.row);
	
	
}

@end
