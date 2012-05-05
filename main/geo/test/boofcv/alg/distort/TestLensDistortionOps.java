/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.distort;

import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.distort.PointTransform_F32;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestLensDistortionOps {

	Point2D_F32 p = new Point2D_F32();
	int width = 300;
	int height = 350;

	/**
	 * If a transform contains the full view then the border of the transform will only
	 * reference pixels that are on the border or outside of the original image.
	 */
	@Test
	public void fullView() {
		IntrinsicParameters param = new IntrinsicParameters(300,320,0,150,130,
				width,height, false, new double[]{0.1,1e-4});

		PointTransform_F32 adjusted = LensDistortionOps.fullView(param,null);

		checkBorderOutside(adjusted);
	}

	private void checkBorderOutside(PointTransform_F32 tran) {
		for( int y = 0; y < height; y++ ) {
			checkBorderOutside(0, y, tran);
			checkBorderOutside(width - 1, y, tran);
		}

		for( int x = 0; x < width; x++ ) {
			checkBorderOutside(x, 0, tran);
			checkBorderOutside(x, height - 1, tran);
		}
	}

	private void checkBorderOutside(int x, int y, PointTransform_F32 tran) {
		tran.compute(x, y, p);

		float tol = 0.1f;

		String s = x+" "+y+" -> "+p.x+" "+p.y;
		assertTrue(s,
				p.x <= 1+tol || p.x >= width-1-tol ||
				p.y <= 1+tol || p.y >= height-1-tol );
	}


	/**
	 * After the transforms, border pixels should only reference pixels which were inside the original image.
	 */
	@Test
	public void allInside() {
		IntrinsicParameters param = new IntrinsicParameters(300,320,0,150,130,
				width,height, false, new double[]{0.1,1e-4});

		PointTransform_F32 adjusted = LensDistortionOps.allInside(param,null);

		checkInside(adjusted);
	}

	private void checkInside(PointTransform_F32 tran) {
		for( int y = 0; y < height; y++ ) {
			checkInside(0,y,tran);
			checkInside(width-1,y,tran);
		}

		for( int x = 0; x < width; x++ ) {
			checkInside(x,0,tran);
			checkInside(x,height-1,tran);
		}
	}

	private void checkInside( int x , int y , PointTransform_F32 tran ) {
		tran.compute(x, y, p);

		float tol = 0.1f;

		String s = x+" "+y+" -> "+p.x+" "+p.y;
		assertTrue(s,p.x >= -tol && p.x < width+tol );
		assertTrue(s,p.y >= -tol && p.y < height+tol );
	}

	@Test
	public void boundBoxInside() {
		// basic sanity check
		Affine2D_F32 affine = new Affine2D_F32(1,1,0,1,1,2);
		PixelTransformAffine_F32 transform = new PixelTransformAffine_F32(affine);
		Rectangle2D_F32 found = LensDistortionOps.boundBoxInside(20, 10, transform);

		assertEquals(10,found.tl_x,1e-4);
		assertEquals(2 ,found.tl_y,1e-4);
		assertEquals(20-9,found.width,1e-4);
		assertEquals(10, found.height,1e-4);
	}
}