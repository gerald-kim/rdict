#import <Foundation/Foundation.h>
#import "SQLitePersistentObject.h"

@interface Person : SQLitePersistentObject {
	NSString *name;
}
@property (nonatomic, retain) NSString * name;
@end
