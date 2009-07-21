package com.amplio.rdict.review;

/*
 * $Id: GraphUtils.java,v 1.3 2007-01-15 04:49:21 larry Exp $ 
 */

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
 * @author Larry Ogrodnek <larry@cheesesteak.net>
 * @version $Revision: 1.3 $ $Date: 2007-01-15 04:49:21 $
 */
final class GraphUtils {
  private GraphUtils() { }
  
  static final float getDivisor(final Number[] data, final int height) {
    float max = Float.MIN_VALUE;
    float min = Float.MAX_VALUE;

    for (final Number i : data){
        min = Math.min(min, i.floatValue());    
        max = Math.max(max, i.floatValue());    
    }

    if (max <= min)
        return 1.0f;
    else
    	return (max - min) / (height - 1);
  }
}


