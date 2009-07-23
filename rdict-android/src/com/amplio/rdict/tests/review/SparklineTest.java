package com.amplio.rdict.tests.review;

import junit.framework.TestCase;

import com.amplio.rdict.review.AndroidBarGraph;
import com.amplio.rdict.review.Sparkline;

public class SparklineTest extends TestCase{

	public void testSparklineFactory () {		
//		SparklineFactory f = new SparklineFactory(width, height);
//		
//		assertEquals(width, f.getWidth(), f.getHeight());
//		
//		Sparkline g = f.create(data);

		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
	
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing);
		
		assertEquals(w, s.w);
		assertEquals(h, s.h);

		assertEquals(data.length, s.rectangles.size());		
	}
	
	public void testCalcRectangleWidth () {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing);
		
		// 100 - (1 * 4) / 5
		// 100 - 4/5
		
		int totalWidthForSpacing = spacing * (s.rectangles.size() - 1); 
		
		assertEquals((w - totalWidthForSpacing) / s.rectangles.size(), s.calcRectangleWidth());
	}
	
	public void testCreateRectangle() {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing);
		
		Sparkline.Rectangle r = s.createRectangle(data[4]);
		
		assertEquals(s.calcRectangleWidth(), r.w);
		assertEquals(data[4].intValue() * s.divisor , r.h);
		assertEquals(0, r.x);
		assertEquals(h - (data[4].intValue() * s.divisor), r.y);
		
//		
//		g.rectangles.get(0).x
//		g.rectangles.get(0).y
//		g.rectangles.get(0).w
//		g.rectangles.get(0).h
//		
//		g.toBitmap();
	
	}
	
	public void testSetupRectangles() {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing);
		
		s.setupRectangles();
		
		Sparkline.Rectangle r1 = s.rectangles.get(0);
		
		assertEquals(s.calcRectangleWidth() , r1.w);
		assertEquals(0 * s.divisor, r1.h);
		assertEquals(0, r1.x);
		assertEquals(h - r1.h, r1.y);
		
		Sparkline.Rectangle r2 = s.rectangles.get(1);
		
		assertEquals(1 * s.divisor, r2.h);
		assertEquals(r1.x + r1.w + spacing, r2.x);
		assertEquals(h - r2.h, r2.y);
		
		Sparkline.Rectangle r3 = s.rectangles.get(2);
		
		assertEquals(2 * s.divisor, r3.h);
		assertEquals(r1.x + r1.w + spacing + r2.w + spacing, r3.x);
		assertEquals(h - r3.h, r3.y);
	}
	
	public void testCalcAverage() {
		Number[] data = new Number[]{0,0,0,0, 10, 10, 10, 10};
		
		assertEquals(5, AndroidBarGraph.getAvg(data));
	}
	
	public void testGetDivisor() {
		int height = 100;
		
		Number[] data = new Number[]{0, 1};
		
		int expectedDivisor = 100;
		
		assertEquals(expectedDivisor, AndroidBarGraph.getDivisor(data, height));
		
		data = new Number[]{0, 1, 2};
		
		assertEquals(50, AndroidBarGraph.getDivisor(data, height));
		
		data = new Number[]{0, 1, 2, 3};
		
		assertEquals(33, AndroidBarGraph.getDivisor(data, height));
		
		data = new Number[]{0, 1, 2, 3, 4};
		
		assertEquals(25, AndroidBarGraph.getDivisor(data, height));
	}
	
}
