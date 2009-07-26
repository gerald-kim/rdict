/*
 * $Id: BarGraph.java,v 1.6 2007-03-11 20:40:58 larry Exp $ 
 */
package com.amplio.rdict.review;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/*
 * 
 * Copyright 2006 Larry Ogrodnek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Bargraph generation.
 * 
 * @author Larry Ogrodnek <larry@cheesesteak.net>
 * @version $Revision: 1.6 $ $Date: 2007-03-11 20:40:58 $
 */
public class AndroidBarGraph
{
  public static final int DEFAULT_WIDTH = 100;
  public static final int DEFAULT_HEIGHT = 25;
  
  public static final int DEFAULT_SPACING = 2;
  
  public static final SizeParams DEFAULT_SIZE = new SizeParams(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SPACING);
  
  public static final int DEFAULT_COLOR = Color.GRAY;
  public static final int DEFAULT_HIGH_COLOR = Color.BLACK;
  public static final int DEFAULT_LAST_COLOR = Color.RED;
  
  
  /**
   * Create a Bargraph from an array of numbers, using default colors and sizes.
   *    
   * @param data Array of Number Objects.
   * 
   * @return BufferedImage containing a Bargraph of data.
   */
  public static Bitmap createGraph(final Number[] data) {
    return createGraph(data, DEFAULT_SIZE, DEFAULT_COLOR, DEFAULT_HIGH_COLOR, DEFAULT_LAST_COLOR);
  }
  
  
  /**
   * Create a Bargraph.
   * 
   * @param data Array of Number Objects to graph.
   * @param size SizeParams specifying graph size attributes.
   * @param color main graph color
   * @param highColor color for above average data points (or null).
   * @param lastColor color for last data point (or null).
   * 
   * @return BufferedImage containing a Bargraph of data.
   */
  public static Bitmap createGraph(final Number[] data, final SizeParams size, final int color, final int highColor, final int lastColor){
    return createGraph(data, size, color, highColor, lastColor, -1);
  }
  
  /**
   * Create a Bargraph.
   * 
   * @param data Array of Number Objects to graph.
   * @param size SizeParams specifying graph size attributes.
   * @param color main graph color
   * @param highColor color for above average data points (or null).
   * @param lastColor color for last data point (or null).
   * @param background background color, or null for transparency.
   * 
   * @return BufferedImage containing a Bargraph of data.
   */
  public static Bitmap createGraph(final Number[] data, final SizeParams size, final int color, final int highColor, final int lastColor, final int background) 
  {
    final Bitmap bi = Bitmap.createBitmap(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_4444);
    
    if (data == null || data.length < 1){
      return bi;
    }
    
    //final Graphics2D g = bi.createGraphics();
    Canvas canvas = new Canvas(bi);
    Paint paint = new Paint();
    
    final float d = GraphUtils.getDivisor(data, size.getHeight());
    final int a = getAvg(data);
    
    final int w = (size.getWidth() - (size.getSpacing() * data.length)) / data.length;
    
    int x = 0; 
    
    int c = 0;
    
    for (final Number i : data) {
      if (c == (data.length - 1) && lastColor != -1)
      {
        //g.setPaint(lastColor);
    	  paint.setColor(lastColor);
      }
      else if (i.intValue() < a || (highColor == -1))
      {
        //g.setPaint(color);
        paint.setColor(color);
      }
      else
      {
        //g.setPaint(highColor);
        paint.setColor(highColor);
      }
      
      //g.fill(new Rectangle2D.Double(x, y + (size.getHeight() - h), w, i.intValue() / d));
      Rect r = new Rect();
      r.left = x;
      r.right = x + w;
      r.bottom = size.getHeight();
      r.top = (d == 0) ? r.bottom : r.bottom - new Float((i.floatValue() / d)).intValue(); // 4 / 19
      
      canvas.drawRect(r, paint);
      
      x += (w + size.getSpacing());
      c++;
    }
    
    return bi;
  }
  
  public static final int getAvg(final Number[] data){
    int total = 0;
    
    for (final Number i : data)
    	total += i.intValue(); 
    
    return (total / data.length);    
  }


	public static int getDivisor(Number[] data, int height) {
		
		int max = 0;
		
		for(Number val : data)
			max = Math.max(max, val.intValue());
		
		return height / max;
	}
}
