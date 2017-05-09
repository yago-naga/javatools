package javatools.datatypes;

/** 
Copyright 2016 Fabian M. Suchanek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

Some utility methods for arrays
*/
public class ArrayUtils {

  /**
   * Returns the size of the intersection of two SORTED arrays.
   * The arrays NEED TO BE PASSED SORTED.
   * 
   * @param a First array
   * @param b Second array
   * @return  Size of intersection of a and b
   */
  public static int intersectArrays(int[] a, int[] b) {
    int intersectCount = 0;

    int aIndex = 0;
    int bIndex = 0;

    while (aIndex < a.length && bIndex < b.length) {
      if (a[aIndex] == b[bIndex]) {
        intersectCount++;

        aIndex++;
        bIndex++;
      } else if (a[aIndex] < b[bIndex]) {
        aIndex++;
      } else {
        bIndex++;
      }
    }

    return intersectCount;
  }
}
