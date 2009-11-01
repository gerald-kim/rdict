package com.amplio.vcbl8r;

import junit.framework.TestCase;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import com.amplio.vcbl8r.review.Card;

public class NeoDatisTest extends TestCase {

	private ODB odb;

	@Override
	protected void setUp() throws Exception {
		odb = ODBFactory.open( "/tmp/test.odb" );
	}

	@Override
	protected void tearDown() throws Exception {
		odb.rollback();
		odb.close();
	}

	public void testSaveObject() {
		Card card = new Card( "question", "answer" );

		odb.store( card );

		IQuery query = new CriteriaQuery( Card.class, Where.equal( "question", "question" ) );
		Objects<Card> players = odb.getObjects( query );

		int i = 1;
		// display each object
		while( players.hasNext()) {
			System.out.println( (i++) + "\t: " + players.next() );
		}
	}

	public void testCommitAndRepoen() {
		Card card = new Card( "question", "answer" );
		OID cardId = odb.store( card );
		
		odb.commit();
		odb.close();
		
		odb = ODBFactory.open( "/tmp/test.odb" );

		Card actual = (Card) odb.getObjectFromId( cardId );
		assertEquals( "question", actual.question );
		
		odb.delete( actual.sh, false );
		odb.delete( actual, false );
		odb.commit();
	}
}
