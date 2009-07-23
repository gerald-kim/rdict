package com.amplio.rdict.review;

import java.util.Vector;

public class Sparkline {

	public int w = 0;
	public int h = 0;
	public int spacing = 0;
	public Vector<Rectangle> rectangles = null;
	
	public int divisor = 0;
	
	public Sparkline(int w, int h, Number[] data, int spacing) {
		this.w = w;
		this.h = h;
		this.spacing = spacing;
		
		this.rectangles = new Vector<Rectangle>(data.length);
		
		for(Number n : data)
			this.rectangles.add(new Rectangle(n));
		
		this.divisor = AndroidBarGraph.getDivisor(data, this.h);
	}
	
	public class Rectangle {

		public int w = 0;
		public int h = 0;
		public int x = 0;
		public int y = 0;
		
		public Number datum = 0;
		
		public Rectangle(Number n) {
			this.datum = n;
		}
		
	}

	public int calcRectangleWidth() {
		int rWidth = (this.w - (this.spacing * (this.rectangles.size() - 1))) / this.rectangles.size();
		
		return rWidth;
	}

	public Rectangle createRectangle(Number datum) {
		Rectangle r = new Rectangle(datum);
		
		r.w = this.calcRectangleWidth();
		r.h = datum.intValue() * this.divisor;
		r.x = 0;
		r.y = this.h - r.h;
		
		return r;
	}

	public void setupRectangles() {
		Vector<Rectangle> tmp = new Vector<Rectangle>();
		
		int tot_width = 0;
		
		for (Rectangle r : this.rectangles){
			Rectangle r_new = this.createRectangle(r.datum);
			
			r_new.x = tot_width;
			
			tmp.add(r_new);
			
			tot_width += this.spacing + r_new.w;
		}
		
		this.rectangles = tmp;
	}

}
