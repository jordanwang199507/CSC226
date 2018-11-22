/*
CSC 226 Assignment 4
By: Jordan (Yu-Lin) Wang
V00786970
*/

import java.util.*;
import java.io.File;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination{

	public ArrayList<String> eliminated = new ArrayList<String>();

	public BaseballElimination(Scanner s){

		/* ... Your code here ... */
		int number = s.nextInt(); //number of teams
		int V = number*(number-1)/2 + 2; //number of vertices
		int E = 0;

		int[][] edges = new int[number][number];
		boolean[][] marked = new boolean[number][number];
		String[] teams = new String[number];
		int[] wins = new int[number];
		int[] remain = new int[number];

		FlowNetwork flow;
		int source = 0;
		int t = V-1;

		int n[] = new int[(number-1)*(number-2)/2];
		int k;
		int index;
		boolean[][] markedTwo ;
		int total;


		for (int i = 0; i < number; i++){
			teams[i] = s.next();
			wins[i] = s.nextInt();
			remain[i] = s.nextInt();

			for (int j = 0; j < number; j++){
				int edge = s.nextInt();
				if (edge != 0 && !marked[i][j]){
					edges[i][j] = edge;
					marked[i][j] = true;
					marked[j][i] = true;
					E++;
				}
			}
		}


		//for each team, create a flow network with the edges
		for (int w = 0; w < number; w++){

			System.out.println("Flow Network for " + teams[w]);
			markedTwo = new boolean[number][number];
			flow = new FlowNetwork(V);
			index = 1;

			//add vertices 1 - 6
			for (int i = 0; i < number; i++){
				if (i != w){
					for (int j = 0; j < number; j++){
						if (j != w && i != j && !markedTwo[i][j]){
							n[index - 1] = i*10 + j;
							flow.addEdge(new FlowEdge(source, index, edges[i][j]));
							index++;
							markedTwo[j][i] = true;
							markedTwo[i][j] = true;
						}
					}
				}
			}

			//index--;

			//add vertices 7 - 10
			for (int i = 0; i < n.length; i++){
				int teamOne = n[i] / 10 + index;
				int teamTwo = n[i] % 10 + index;
				flow.addEdge(new FlowEdge(i+1, teamOne, Double.POSITIVE_INFINITY));
				flow.addEdge(new FlowEdge(i+1, teamTwo, Double.POSITIVE_INFINITY));
			}

			//add vertex 11
			int count = 0;
			int value = 0;
			for (int i = 0; i < number; i++){
				if (i != w){
					total = wins[w] + remain[w] - wins[i];
					if(total < 0){
						eliminated.add(teams[w]);
						continue;
					}
					flow.addEdge(new FlowEdge(count+index, t, total));
					count++;
					value += total;
				}
			}

			//find maxflow
			FordFulkerson maxflow = new FordFulkerson(flow, source, t);

			System.out.println(flow.toString());

			if((int)maxflow.value() >= value){
				eliminated.add(teams[w]);
			}
		}
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

		BaseballElimination be = new BaseballElimination(s);

		if (be.eliminated.size() == 0)
			System.out.println("No teams have been eliminated.");
		else
			System.out.println("Teams eliminated: " + be.eliminated);
	}
}
