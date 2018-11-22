/* MSTvsSPT.java
   CSC 226 - Summer 2017
   Assignment 3 - Minimum Weight Spanning Tree versus Shortest Path Spanning Tree Template

   Jordan (Yu-Lin) Wang
   V00786970

   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
    java MSTvsSPT

   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
    java MSTvsSPT file.txt
   where file.txt is replaced by the name of the text file.

   The input consists of a series of graphs in the following format:

    <number of vertices>
    <adjacency matrix row 1>
    ...
    <adjacency matrix row n>

   Entry A[i][j] of the adjacency matrix gives the weight of the edge from
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].

   An input file can contain an unlimited number of graphs; each will be
   processed separately.


   R. Little - 06/23/2017

    answer for question 4 : 
        Single-source longest paths problem in edge-weighted DAGs. We can solve the single-source longest paths problems in edge-weighted DAGs by initializing the distTo[] values to negative infinity and switching the sense of the inequality in relax(). AcyclicLP.java implements this approach. 
*/

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;

//Do not change the name of the MSTvsSPT class
public class MSTvsSPT{

    private static int MAX_VERTICES = 100;
    private static int[] SPTedgeTo;
    private static double[] SPTdistTo;
    private static int[] MSTedgeTo;
    private static double[] MSTdistTo;
    private static boolean[] marked;
    private static IndexMinPQ<Double> pq = new IndexMinPQ<Double>(MAX_VERTICES);
    private static IndexMinPQ<Double> pq1 = new IndexMinPQ<Double>(MAX_VERTICES);
    private static IndexMinPQ<Double> pqMST = new IndexMinPQ<Double>(MAX_VERTICES);

    private static void relax(int v, int w, int weight){

            if (SPTdistTo[w] > SPTdistTo[v] + weight){
                SPTdistTo[w] = SPTdistTo[v] + weight;
                SPTedgeTo[w] = v;
                if (pq.contains(w)){
                	pq.changeKey(w, SPTdistTo[w]);
            	} else {                          
            		pq.insert(w, SPTdistTo[w]);
            	}
        	}
    }

    static boolean MSTvsSPT(int[][] G){
        
        SPTdistTo = new double[G.length];
        SPTedgeTo = new int[G.length];

        MSTdistTo = new double[G.length];
        MSTedgeTo = new int[G.length];

        marked = new boolean[G.length];

        for (int v = 0; v < G.length; v++){
                    MSTdistTo[v] = Double.POSITIVE_INFINITY;
                    SPTdistTo[v] = Double.POSITIVE_INFINITY;
            }
        
        SPTdistTo[0] = 0.0;
        int weight; //for dijk
        int startVertex ; 
        
        
        pq.insert(0,SPTdistTo[0]);


        while(!pq.isEmpty()){
            startVertex = pq.delMin();
            marked[startVertex] = true; 

            for(int v = 0 ; v < G.length  ; v++){ 
                ///dijk
                if (G[startVertex][v] != 0){
                        weight = G[startVertex][v];
                        relax(startVertex, v, weight);             
                }
               

               if(G[startVertex][v] != 0 ){
                    weight = G[startVertex][v] ;
                    if(marked[v]){
                        continue; 
                    }

                    if(weight < MSTdistTo[v]){
                        MSTdistTo[v] = weight; 
                        MSTedgeTo[v] = startVertex ; 
                    }

                    if (pq.contains(v)) {
                        pq.changeKey(v, MSTdistTo[v]);
                    }
                    else {                           
                        pq.insert(v, MSTdistTo[v]);
                    }

               }
                
                if(SPTedgeTo[startVertex] != MSTedgeTo[startVertex])
                    return false ;      
            }
        }
        return true; 
    }
    
    public static void main(String[] args){
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
            System.out.printf("Reading input values from stdin.\n");
        }

        int graphNum = 0;
        double totalTimeSeconds = 0;

        //Read graphs until EOF is encountered (or an error occurs)
        while(true){
            graphNum++;
            if(graphNum != 1 && !s.hasNextInt())
                break;
            System.out.printf("Reading graph %d\n",graphNum);
            int n = s.nextInt();
            int[][] G = new int[n][n];
            int valuesRead = 0;
            for (int i = 0; i < n && s.hasNextInt(); i++){
                for (int j = 0; j < n && s.hasNextInt(); j++){
                    G[i][j] = s.nextInt();
                    valuesRead++;
                }
            }
            if (valuesRead < n*n){
                System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
                break;
            }
            long startTime = System.currentTimeMillis();

            boolean msteqspt = MSTvsSPT(G);
            long endTime = System.currentTimeMillis();
            totalTimeSeconds += (endTime-startTime)/1000.0;

            System.out.printf("Graph %d: Does MST = SPT? %b\n",graphNum,msteqspt);
        }
        graphNum--;
        System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
    }
}
