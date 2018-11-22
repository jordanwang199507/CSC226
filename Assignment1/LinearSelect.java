/*LinearSelect.java
* By: Jordan (Yu-Lin) Wang
* V00786970
* CSC 226
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.Random;

public class LinearSelect {
	//Function to invoke LinearSelect
	public static int LS(int[] S, int k){
		if (S.length == 1)
			return S[0];
		return linearSelect(0, S.length-1, S, k);
	}

	//do linearSelect in recursive
	private static int linearSelect(int left, int right, int[] array, int k){
		//if there is only one element now, just record.
		if (left>=right) {
			return array[left];
		} 

		//pick Clever Pivot
		int pivot = pickCleverPivot(left, right, array);
		//pick partition 
		int par = partition(left, right, array, pivot);

		if (k<=par){
			return linearSelect(left, par-1, array, k);
		} else if (k == par+1){
			return array[par];
		} else {
			return linearSelect(par+1, right, array, k);
		}
	}

	//perform partition with clever pivot
	private static int partition (int left, int right, int[] array, int pIndex){
		swap(array, pIndex, right);

		int p = array[right];
		int l = left;
		int r = right - 1;

		while (l <= r){
			while (l <= r && array[l] <= p){
				l++;
			}
			while (l <= r && array[r] >= p){
				r--;
			}
			if (l<r){
				swap(array, l, r);
			}
		}

		swap(array, l, right);
		return l;
	}

	//get median value
	private static int getMedianValue(int left, int right, int[] array){
		//sort subarray with insertion sort
		for (int i = left; i <= right; i++){
			int j = i;
			while (j > left && array[j-1] > array[j]){
				swap(array, j, j-1);
				j -= 1;
			}
		}

		int med = 0;
		//check if length of subarray is even 
		if ((right - left) % 2 == 0){
			med = ((right - left) / 2) - 1;
		} else {
			med = (right - left) / 2;
		}

		return left + med;
	}

	//pick random clever pivot for the linearSelect
	private static int pickCleverPivot(int left, int right, int[] array){
		int n = array.length;

		//base case - if array length less than 5, median
		if ((right - left) < 5){
			return getMedianValue(left, right, array);
		}

		int count = left;

		//divide array into 5 subgroups
		for (int i = left; i <= right; i += 5){
			int tRight = i + 4;
			if (tRight > right){
				tRight = right;
			}

			int medSubgroup; 
			if((tRight - i) <= 2){
				continue;
			} else {
				medSubgroup = getMedianValue(i, tRight, array);
			}

			swap(array, medSubgroup, count);
			count++;
		}
		return pickCleverPivot(left, count, array);
	}

		//swap two elements in the array
	private static void swap(int[]array, int a, int b){
 		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}

	public static void main(String[] args){
		//array one has duplicate elements			// 0,1,2,3,4,5,6,7,8,9,10
  		int [] array1 ={12,13,17,14,21,3,4,9,21,8}; //{3,4,8,9,12,13,14,17,21,21}
  		int [] array2 ={14,8,22,18,6,2,15,84,13,12}; //{2,6,8,12,13,14,15,18,22,84}
  		int [] array3 ={6,8,14,18,22,2,12,13,15,84};
   
  		System.out.println(LS(array1,5));
  		System.out.println(LS(array2,7));
  		System.out.println(LS(array3,5));  

		Scanner s;
		
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}

		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);
		
		int k = inputVector.get(0);

		int[] array = new int[inputVector.size()-1];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i+1);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.nanoTime();

		int kthsmallest = LS(array,k);

		long endTime = System.nanoTime();

		long totalTime = (endTime-startTime);

		System.out.printf("The %d-th smallest element in the input list of size %d is %d.\n",k,array.length,kthsmallest);
		System.out.printf("Total Time (nanoseconds): %d\n",totalTime);
	}
}