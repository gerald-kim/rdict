package com.amplio.rdict.review;

import junit.framework.TestCase;

public class SparklineTest extends TestCase{

	public void testSparkline () {		
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
	
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		assertEquals(w, s.w);
		assertEquals(h, s.h);

		assertEquals(data.length, s.data.length);		
	}
	
	public void testGetAvailableSpace () {		
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
	
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		assertEquals(w - 2*Sparkline.LEFT_MARGIN - 2*Sparkline.RIGHT_MARGIN - (data.length - 1) * spacing, s.getWidthAvailableToRectangles());
		assertEquals(h - Sparkline.TOP_MARGIN - Sparkline.BOTTOM_MARGIN, s.getHeightAvailableToRectangles());
	}
	
	public void testCalcWidthForRectangles() {
		int w = 100;
		int h = 30;
		int spacing = 1;
		Number[] data = new Number[]{0, 1, 2, 3, 4};
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		int totalWidthForSpacing = spacing * (data.length - 1); 
		
		int expectedWidth = (w - 2*Sparkline.LEFT_MARGIN - 2*Sparkline.RIGHT_MARGIN - totalWidthForSpacing) / data.length;
		
		assertEquals(expectedWidth, s.rectangleWidth);
	}
	
	public void testSetupRectangles() {
		int w = 100;
		int h = 30;
		Number[] data = new Number[]{0, 1, 2};
		
		int spacing = 1;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		s.setupRectangles();
		
		Sparkline.Rectangle r1 = s.rectangles.get(0);
		
		assertEquals(s.rectangleWidth , r1.w);
		assertEquals(new Float(0 * s.divisor).intValue(), r1.h);
		assertEquals(Sparkline.LEFT_MARGIN*2, r1.x);
		assertEquals(h - Sparkline.BOTTOM_MARGIN - r1.h, r1.y);
		
		Sparkline.Rectangle r2 = s.rectangles.get(1);
		
		assertEquals(new Float(1 * s.divisor).intValue(), r2.h);
		assertEquals(r1.x + r1.w + spacing, r2.x);
		assertEquals(h - Sparkline.BOTTOM_MARGIN - r2.h, r2.y);
		
		Sparkline.Rectangle r3 = s.rectangles.get(2);
		
		assertEquals(new Float(2 * s.divisor).intValue(), r3.h);
		assertEquals(r1.x + r1.w + spacing + r2.w + spacing, r3.x);
		assertEquals(h - Sparkline.BOTTOM_MARGIN - r3.h, r3.y);
		
		assertEquals(w, 2*Sparkline.LEFT_MARGIN + r1.w + spacing + r2.w + spacing + r3.w + 2*Sparkline.RIGHT_MARGIN);
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
	
	public void testForBug() {
		int w = 230;
		int h = 110;
		int spacing = 2;
		
		int dataCount = 8;
		Number[] data = new Number[dataCount];
		
		for(int i = 0; i < data.length; i++)
			data[i] = 4;
		
		Sparkline s = new Sparkline(w, h, data, spacing, false);
		
		int widthAvailable = w - 2*Sparkline.LEFT_MARGIN - 2*Sparkline.RIGHT_MARGIN - ((dataCount - 1) * spacing);
		
		assertEquals(widthAvailable / dataCount, s.rectangleWidth);
		
		s.setupRectangles();
		
		Sparkline.Rectangle r = s.rectangles.get(s.rectangles.size() - 1);
	
		// spacing shouldn't be needed below
		assertEquals(w - 2*Sparkline.RIGHT_MARGIN - spacing, r.x + r.w);
	}
	
}
