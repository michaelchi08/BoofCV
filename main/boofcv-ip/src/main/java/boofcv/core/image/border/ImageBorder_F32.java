/*
 * Copyright (c) 2011-2017, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.core.image.border;

import boofcv.struct.image.GrayF32;

/**
 * Child of {@link ImageBorder} for {@link GrayF32}.
 *
 * @author Peter Abeles
 */
public abstract class ImageBorder_F32 extends ImageBorder<GrayF32> {

	public ImageBorder_F32(GrayF32 image) {
		super(image);
	}

	protected ImageBorder_F32() {
	}

	public void set( int x , int y , float val ) {
		if( image.isInBounds(x,y) )
			image.set(x,y,val);

		setOutside(x,y,val);
	}

	public float get( int x , int y ) {
		if( image.isInBounds(x,y) )
			return image.get(x,y);

		return getOutside( x , y );
	}

	@Override
	public void getGeneral(int x, int y, double[] pixel ) {
		pixel[0] = get(x, y);
	}

	@Override
	public void setGeneral(int x, int y, double[] pixel ) {
		set(x, y, (int)pixel[0]);
	}

	public abstract float getOutside( int x , int y );

	public abstract void setOutside( int x , int y , float val );
}
