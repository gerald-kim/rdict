package com.amplio.rdict;

import junit.framework.TestCase;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import com.amplio.rdict.review.Card;

public class NeoDatisTest extends TestCase {
	
	private ODB m_odb;

	@Override
	protected void setUp() throws Exception {
		m_odb = ODBFactory.open( "/tmp/test.odb" );
	}

	@Override
	protected void tearDown() throws Exception {
		m_odb.rollback();
		m_odb.close();
	}

	public void testEmpty() {
		Card card = new Card("question", "answer");
		
		m_odb.store(card);
		
		IQuery query = new CriteriaQuery(Card.class, Where.equal("question", "question"));
        Objects<Card> players = m_odb.getObjects(query);


        int i = 1;
        // display each object
        while (players.hasNext()) {
            System.out.println((i++) + "\t: " + players.next());
        }
	}
}

