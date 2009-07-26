package com.amplio.rdict.review;

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
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		assertEquals(w, s.w);
		assertEquals(h, s.h);

		assertEquals(data.length, s.rectangles.size());		
	}
	
	public void testCalcRectangleWidth () {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
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
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		Sparkline.Rectangle r = s.createRectangle(data[4]);
		
		assertEquals(s.calcRectangleWidth(), r.w);
		assertEquals(new Float(data[4].intValue() * s.divisor).intValue(), r.h);
		assertEquals(0, r.x);
		assertEquals(new Float(h - (data[4].intValue() * s.divisor)).intValue(), r.y);
	}
	
	public void testSetupRectangles() {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		s.setupRectangles();
		
		Sparkline.Rectangle r1 = s.rectangles.get(0);
		
		assertEquals(s.calcRectangleWidth() , r1.w);
		assertEquals(new Float(0 * s.divisor).intValue(), r1.h);
		assertEquals(0, r1.x);
		assertEquals(h - r1.h, r1.y);
		
		Sparkline.Rectangle r2 = s.rectangles.get(1);
		
		assertEquals(new Float(1 * s.divisor).intValue(), r2.h);
		assertEquals(r1.x + r1.w + spacing, r2.x);
		assertEquals(h - r2.h, r2.y);
		
		Sparkline.Rectangle r3 = s.rectangles.get(2);
		
		assertEquals(new Float(2 * s.divisor).intValue(), r3.h);
		assertEquals(r1.x + r1.w + spacing + r2.w + spacing, r3.x);
		assertEquals(h - r3.h, r3.y);
	}
	
	public void testCalcAverage() {
		Number[] data = new Number[]{0,0,0,0, 10, 10, 10, 10};
		
		assertEquals(5, AndroidBarGraph.getAvg(data));
	}
	
	public void testCalcDivisor() {
		int height = 100;
		boolean isPercentageGraph = false;
		
		Number[] data = new Number[]{0, 1};
		
		float expectedDivisor = 100;
		
		assertEquals(expectedDivisor, Sparkline.calcDivisor(data, height, isPercentageGraph));
		
		data = new Number[]{0, 1, 2};
		
		assertEquals((float) 50.0, Sparkline.calcDivisor(data, height, false));
		
		data = new Number[]{0, 1, 2, 3};
		
		assertEquals((float) 33.333332, Sparkline.calcDivisor(data, height, false));
		
		data = new Number[]{0, 1, 2, 3, 4};
		
		assertEquals((float) 25.0, Sparkline.calcDivisor(data, height, false));
	}
	
	public void testCalcDivisorForPercentageGraph() {
		int height = 20;
		boolean isPercentageGraph = true;
		
		Number[] data = new Number[]{0, 1};
		
		assertEquals(20 / (float) 100, Sparkline.calcDivisor(data, height, isPercentageGraph));
	}
	
	public void testCalcDivisorIfDataValueIsGreaterThanHeight() {
		int height = 20;
		
		Number[] data = new Number[]{0, 70};
		
		assertEquals(20 / (float) 70.0, Sparkline.calcDivisor(data, height, false));
	}
	
}
