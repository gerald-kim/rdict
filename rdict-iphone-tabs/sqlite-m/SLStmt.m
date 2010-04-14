//
//  SLStmt.m
//
//  Copyright 2008 Steven Fisher.
//
//  This file is covered by the MIT/X11 License.
//  See LICENSE.TXT for more information.
//

#import "SLStmt.h"
#import "SQLiteInstanceManager.h"

@implementation SLStmt

@synthesize extendedErr=_err, stmt=_stmt;

- (int)simpleErr {
	return _err & 0xFF;
}

- (void)setResult:(int)err {
	_err = err;
	_msg = sqlite3_errmsg(_database);
	if ( ( _err != SQLITE_OK ) && ( self.simpleErr < 100 ) )
		DebugLog( @"SLStmt: (%d) %s", _err, _msg );
}

+ (id)stmtWithSql:(NSString*)sql {
    return [[[SLStmt alloc] initWithSql:sql] autorelease];
}

- (id)initWithSql:(NSString*)sql {
	self = [super init];
	if (!self) return self;
	
	_database = [[SQLiteInstanceManager sharedManager]database];
	
	[self prepareSql:sql];
	return self;
}

- (void)dealloc {
	[self close];
	[_sql release];
	//[_database release];
	[super dealloc];
}

- (SLStmt*)prepareSql:(NSString*)sql {
	[sql retain];
	[self close];
	[_sql release];
	_sql = sql;
	[self setResult:sqlite3_prepare_v2(_database, [_sql UTF8String], -1, &_stmt, &_nextSql)];
	_bind = 0;
	return self;
}

- (SLStmt*)prepareNext {
	if ( ( _nextSql == NULL ) || ( *_nextSql == 0 ) )
		return nil;
	[self setResult:sqlite3_prepare_v2(_database, _nextSql, -1, &_stmt, &_nextSql)];
	return self;
}

- (SLStmt*)reset {
	if ( _stmt ) {
		_bind = 0;
		_column = 0;
		sqlite3_reset( _stmt );
	}
	return self;
}


- (SLStmt*)close {
	if ( _stmt ) {
		int err = sqlite3_finalize( _stmt );
		if ( err != SQLITE_OK )
			DebugLog( @"Error %d while finalizing query as part of close.", err );
		_stmt = NULL;
	}
	return self;
}

- (sqlite3_stmt*)stmt {
	return _stmt;
}

- (SLStmt*)step {
	[self setResult:sqlite3_step( _stmt )];
	_column = 0;
	return ( [self simpleErr] == SQLITE_ROW ) ? self : nil;
}

- (long long)columnCount {
	return sqlite3_column_count( _stmt );
}

- (NSString*)columnName:(int)column {
	const char *text = sqlite3_column_name( _stmt, column );
	if ( text == NULL )
		return nil;
	return [NSString stringWithUTF8String:text];
}

- (NSString*)columnName {
	const char *text = sqlite3_column_name( _stmt, _column );
	if ( text == NULL )
		return nil;
	return [NSString stringWithUTF8String:text];
}

- (long long)longLongValue:(int)column {
	return sqlite3_column_int64( _stmt, column );
}

- (long long)longLongValue {
	return [self longLongValue:_column++];
}

- (NSString*)stringValue:(int)column {
	const char *text = (char*)sqlite3_column_text( _stmt, column);
	if ( text == NULL )
		return nil;
	return [NSString stringWithUTF8String:text];
}

- (NSString*)stringValue {
	const char *text = (char*)sqlite3_column_text( _stmt, _column++);
	if ( text == NULL )
		return nil;
	return [NSString stringWithUTF8String:text];
}

- (int)columnType:(int)column {
	return sqlite3_column_type( _stmt, column );
}

- (int)columnType {
	return sqlite3_column_type( _stmt, _column );
}

- (id)value:(int)column {
	int type = sqlite3_column_type( _stmt, column );
	switch (type) {
		case SQLITE_INTEGER:
			return [NSNumber numberWithLongLong:sqlite3_column_int64( _stmt, column )];
		case SQLITE_FLOAT:
			return [NSNumber numberWithDouble:sqlite3_column_double( _stmt, column )];
		case SQLITE_BLOB: {
			const void *bytes = sqlite3_column_blob( _stmt, column );
			return [NSData dataWithBytes:bytes
								  length:sqlite3_column_bytes( _stmt, column )];
		}
		case SQLITE_NULL:
			return nil;
		case SQLITE_TEXT: {
			const unsigned char *text = sqlite3_column_text( _stmt, column );
			return [NSString stringWithUTF8String:(char*)text];
		}
		default:
			return nil;
	}
}

- (id)value {
	return [self value:_column++];
}

- (NSDictionary*)allValues {
	NSMutableDictionary *temp_values = [[NSMutableDictionary alloc] init];
	int n = [self columnCount];
	for ( int i = 0; i < n; i++ ) {
		id value = [self value:i];
		if (!value)
			continue;
		NSString *name = [self columnName:i];
		[temp_values setObject:value forKey:name];
	}
	NSDictionary *values = [[NSDictionary alloc] initWithDictionary:temp_values];
	[temp_values release];
	return [values autorelease];
}

- (SLStmt*)bindLongLong:(long long)value
			   forIndex:(int)index {
	[self setResult:sqlite3_bind_int64( _stmt, index+1, value )];
	return self;
}

- (SLStmt*)bindLongLong:(long long)value {
	[self bindLongLong:value forIndex:_bind++];
	return self;
}

- (SLStmt*)bindString:(NSString*)value
			 forIndex:(int)index {
	[self setResult:sqlite3_bind_text( _stmt, index+1, [value UTF8String], -1, SQLITE_TRANSIENT )];
	return self;
}

- (SLStmt*)bindString:(NSString*)value {
	[self bindString:value forIndex:_bind++];
	return self;
}

@end
