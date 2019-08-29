/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics;

import java.util.*;
import java.util.List;
 
public class SutherlandHodgman{
 
    static List<float[]> subject, clipper, result;
    
    public static List<float[]> clipPolygon(float[] subjectPoints,float[] clipPoints){
    	
    	float[][] Sub = new float[subjectPoints.length/2][2];
    	float[][] Clp = new float[clipPoints.length/2][2];
    	
    	for (int i = 0; i < subjectPoints.length; i++) {
			if(i % 2 == 0) Sub[i/2][0] = subjectPoints[i];
			else Sub[i/2][1] = subjectPoints[i];
		}
    	for (int i = 0; i < clipPoints.length; i++) {
			if(i % 2 == 0) Clp[i/2][0] = clipPoints[i];
			else Clp[i/2][1] = clipPoints[i];
		}
    	
    	return clipPolygon(Sub, Clp);
    }
    public static List<float[]> clipPolygon(float[][] subjectPoints,float[][] clipPoints) {
    	
    	subject = new ArrayList<>(Arrays.asList(subjectPoints));
        result  = new ArrayList<>(subject);
        clipper = new ArrayList<>(Arrays.asList(clipPoints));
          
        int len = clipper.size();
        for (int i = 0; i < len; i++) {
 
            int len2 = result.size();
            List<float[]> input = result;
            result = new ArrayList<>(len2);
 
            float[] A = clipper.get((i + len - 1) % len);
            float[] B = clipper.get(i);
 
            for (int j = 0; j < len2; j++) {
 
                float[] P = input.get((j + len2 - 1) % len2);
                float[] Q = input.get(j);
 
                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P))
                        result.add(intersection(A, B, P, Q));
                    result.add(Q);
                } else if (isInside(A, B, P))
                    result.add(intersection(A, B, P, Q));
            }
        }
        
        return result;
    }
 
    public static boolean isInside(float[] a, float[] b, float[] c) {
        return (a[0] - c[0]) * (b[1] - c[1]) > (a[1] - c[1]) * (b[0] - c[0]);
    }
 
    public static float[] intersection(float[] a, float[] b, float[] p, float[] q) {
        float A1 = b[1] - a[1];
        float B1 = a[0] - b[0];
        float C1 = A1 * a[0] + B1 * a[1];
 
        float A2 = q[1] - p[1];
        float B2 = p[0] - q[0];
        float C2 = A2 * p[0] + B2 * p[1];
 
        float det = A1 * B2 - A2 * B1;
        float x = (B2 * C1 - B1 * C2) / det;
        float y = (A1 * C2 - A2 * C1) / det;
 
        return new float[]{x, y};
    }
}