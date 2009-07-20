/*
 * $Id: SizeParams.java,v 1.2 2006/07/17 20:22:40 larry Exp $ 
 */
package com.amplio.rdict.review;
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
 * Graph size attributes.
 * 
 * @author Larry Ogrodnek <larry@cheesesteak.net>
 * @version $Revision: 1.2 $ $Date: 2006/07/17 20:22:40 $
 */
public final class SizeParams
{
  private final int width;
  private final int height;
  private final int spacing;

  /**
   * Constructor.
   * 
   * @param width graph width (in pixels).
   * @param height graph height (in pixels).
   * @param spacing spacing between data points (in pixels).
   */
  public SizeParams(final int width, final int height, final int spacing)
  {
    this.width = width;
    this.height = height;
    this.spacing = spacing;
  }

  /**
   * Get the graph height.
   * @return graph height (in pixels).
   */
  public int getHeight()
  {
    return this.height;
  }

  /**
   * Get the spacing between data points.
   * @return spacing between data points (in pixels).
   */
  public int getSpacing()
  {
    return this.spacing;
  }

  /**
   * Get the graph width.
   * @return graph width (in pixels).
   */
  public int getWidth()
  {
    return this.width;
  }
}
