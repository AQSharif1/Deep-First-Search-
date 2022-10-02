import java.awt.Color;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * @author Abdul
 * Deliv B Program performs DFS (Depth First Search)
 *
 */
public class DelivB {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;
	private static final Color WHITE = Color.WHITE;
	private static final Color BLACK = Color.BLACK;
	private static final Color GRAY = Color.GRAY;
	private int time;

	// Constructor - DO NOT MODIFY
	public DelivB(File in, Graph gr) {
		inputFile = in;
		graph = gr;

		// Set up for writing to a file
		try {
			// Use input file name to create output file in the same location
			String inputFileName = inputFile.toString();
			String outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("_out.txt");
			outputFile = new File(outputFileName);

			// A Printwriter is an object that can write to a file
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		// Calls the method that will do the work of deliverable B
		runDelivB();

		output.flush();
	}

	// *********************************************************************************
	// This is where your work starts

	private void runDelivB() {
		dfs();
		print();
	}

	/**
	 * DFS Method
	 */
	public void dfs() {
		Node start = null;
		for (Node n : graph.getNodeList()) {
			if(n.getValue().equalsIgnoreCase("s")){
				start = n;
			}
			n.setColor(WHITE);
			time = 0;
		}
		Collections.sort(graph.getNodeList(), new CompareToDegree() );
		dfsVisit(start);
		for (Node n : graph.getNodeList()) {
			if (n.getColor().equalsIgnoreCase("WHITE")) {
				dfsVisit(n);
			}
		}
	}

	/**
	 * DFS Visit Method 
	 * @param Node u
	 */
	public void dfsVisit(Node u) {
		time = time + 1;
		u.d(time);
		u.setColor(GRAY);
		Collections.sort(u.getOutgoingEdges(), new CompareODegree() );
		for (Edge e : u.getOutgoingEdges()) {
			Node v = e.getHead();

			if (v.getColor().equalsIgnoreCase("WHITE")) {
				e.setType("Tree");
				dfsVisit(v);
			}
			else if(!v.getColor().equalsIgnoreCase("WHITE")) {
				e.setType("Forward");
			}
		}
		u.setColor(BLACK);
		time = time + 1;
		u.f(time);
		type();	
	}

	/**
	 * Type method for cross and back edges
	 */
	public void type() {
		for(Edge e : graph.getEdgeList()) {
			if(e.getHead().getDiscovery()<e.getHead().getFinish() && 
					e.getHead().getFinish() < e.getTail().getDiscovery() &&
					e.getTail().getDiscovery()< e.getTail().getFinish()) 
			{
				e.setType("Cross");
			}

			else if(e.getTail().getDiscovery()> e.getHead().getDiscovery() && 
					e.getTail().getFinish() < e.getHead().getFinish()) {
				e.setType("Back");
			}
		}
	}

	/**
	 * Print Method
	 */
	public void print() {
		System.out.println("DFS of graph\n");
		System.out.println("Node \t Disc \tFinish ");
		for (Node n : graph.getNodeList()) {
			System.out.println(n.getAbbrev() + "\t " + n.getDiscovery() + 
					" \t\t" + n.getFinish());
		}
		System.out.println("\nEdge Classification");
		System.out.println("\nEdge \tType");
		for (Edge e : graph.getEdgeList()) {
			System.out.println(e.getTail().getAbbrev() + "->" +
					e.getHead().getAbbrev() + "\t" + e.getType());
		}
	}

	/**
	 * 
	 * @author Abdul
	 *inner class Comparator
	 */
	class CompareODegree implements Comparator<Edge> {

		public int compare(Edge o1, Edge o2) {
			if (o1.getDistance() > o2.getDistance()) {
				return 1;
			}
			else if (o1.getDistance() < o2.getDistance()) {
				return -1;
			}
			else  if(o1.getDistance() == o2.getDistance()) {
				return o1.getHead().getAbbrev().compareToIgnoreCase(o2.getHead().getAbbrev());
			}
			return 0;
		}
	}
	/**
	 * 
	 * @author Abdul
	 *
	 */
	class CompareToDegree implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.getAbbrev().compareToIgnoreCase(o2.getAbbrev());
		}
	}
}
