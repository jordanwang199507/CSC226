/*RedBlackBST.java
* By: Jordan (Yu-Lin) Wang
* V00786970
* CSC 226
*/

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.Random;
import java.io.BufferedInputStream;
import java.util.*;


public class RedBlackBST{

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(int key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackBST() {
    }

   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 

    private int countRed (Node h) {
		int nbRed = 0;
		if (h == null){
			return 0;
		}
		if (isRed(h)) {
			nbRed++;
		}
		nbRed += countRed(h.left);
		nbRed += countRed(h.right);


		return nbRed;
	}

	//count nodes
	private int countNodes (Node h) {
		if (h == null){
			return 0;
		}
		return 1 + countNodes(h.left) + countNodes(h.right);
	}

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }


   /***************************************************************************
    *  Standard BST search.
    ***************************************************************************/

    public int get(int key) {
        if (key == 0) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private int get(Node x, int key) {
        while (x != null) {
            if      (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
        }
        return 0;
    }


    public boolean contains(int key) {
        return get(key) != 0;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    public void put(int key) {
        if (key == 0) throw new IllegalArgumentException("first argument to put() is null");

        root = put(root, key);
        root.color = BLACK;
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key) { 
        if (h == null) return new Node(key, RED, 1);

        if      (key < h.key) h.left  = put(h.left,  key); 
        else if (key > h.key) h.right = put(h.right, key); 

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

  
   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) { 
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        // assert (h != null);

        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != null;
        // assert k >= 0 && k < size(x);
        int t = size(x.left); 
        if      (t > k) return select(x.left,  k); 
        else if (t < k) return select(x.right, k-t-1); 
        else            return x; 
    } 

    public void printTree(){
    	printSubtree(root);
    }

    public void printSubtree (Node root){
    	if (root != null){
    		System.out.print(root.key+ " ");
    		printSubtree(root.left);
    		printSubtree(root.right);
    		
    	}
    }
    //percentage of red nodes in the RedBlackBST
	public float percentage() {
		float x = countRed(root);
		float y = countNodes(root);
		float percent = (x/y)*100;
		return percent;
	}


    public static void main(String[] args) { 

        Scanner s;
        Vector<Integer> inputVector = new Vector<Integer>();
		
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
			int v;
			while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);


		}else{
			// random generator 
		int randomValue;
        Random random;
        int randomFromUser;

        s = new Scanner(System.in);

        System.out.print("Enter lenght for random generator: ");
        randomValue = s.nextInt();

        	for(int i = 0; i<randomValue-1;i++){
        		random = new Random();
        		randomFromUser = random.nextInt(1000000)+1;
        		inputVector.add(randomFromUser);
        	}
		}
		
		RedBlackBST st = new RedBlackBST();

		for (int i =0; i<inputVector.size();i++){
			st.put(inputVector.get(i));
		}
		st.printTree();
		System.out.println();

		long startTime = System.nanoTime();

		System.out.println(st.percentage());

		long endTime = System.nanoTime();

		long totalTime = (endTime-startTime);


		System.out.printf("Total Time (nanoseconds): %d\n",totalTime);

    }
}
