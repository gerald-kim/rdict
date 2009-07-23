package com.amplio.rdict.review;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sparkline {

	public int w = 0;
	public int h = 0;
	public int spacing = 0;
	public Vector<Rectangle> rectangles = null;
	
	public float divisor = 0;
	
	public Sparkline(int w, int h, Number[] data, int spacing, boolean isPercentageGraph) {
		this.w = w;
		this.h = h;
		this.spacing = spacing;
		
		this.rectangles = new Vector<Rectangle>(data.length);
		
		for(Number n : data)
			this.rectangles.add(new Rectangle(n));
		
		this.divisor = calcDivisor(data, this.h, isPercentageGraph);
	}
	
	public int calcRectangleWidth() {
		int rWidth = (this.w - (this.spacing * (this.rectangles.size() - 1))) / this.rectangles.size();
		
		return rWidth;
	}

	public Rectangle createRectangle(Number datum) {
		Rectangle r = new Rectangle(datum);
		
		r.w = this.calcRectangleWidth();
		r.h = new Float(datum.intValue() * this.divisor).intValue();
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
	
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLUE);
		
		for( Sparkline.Rectangle r : this.rectangles) {
		    canvas.drawRect(r.toAndroidRect(), paint);
	    }		
	}
	
	public static float calcDivisor(Number[] data, int height, boolean isPercentageGraph) {
		if( isPercentageGraph) {
			float oneHundred = 100;
			return height / oneHundred;
		}
		else {
			float max = 0;
			
			for(Number val : data)
				max = Math.max(max, val.intValue());
		
			return height / max;
		}
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
		
		public Rect toAndroidRect(){
			Rect r = new Rect();
		    r.left = this.x;
		    r.right = this.x + this.w;
		    r.bottom = this.y + this.h;
		    r.top = this.y;
		    
		    return r;
		}
		
		public String toString() {
			return "(" + this.w + ", " + this.h + ", " + this.x + ", " + this.y + ", " + this.datum.intValue() + ")";
		}
	}
}
