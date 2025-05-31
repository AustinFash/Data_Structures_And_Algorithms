package avengers;

/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {

    private static int getMinCostNode(int[] array, boolean[] visited) {
        int min = 0;
        for (int k = 1; k < array.length; k++) { // getMinCostNode()
            if (visited[min] == true) min++;
            if (array[k] < array[min] && visited[k] != true) {
                min = k; // get node with minimum cost, skip visited nodes
            }
        }
        return min;
    }
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        int numGenerators = StdIn.readInt(); // number of vertices
        int count = 0;
        int[][] matrix = new int[numGenerators][numGenerators];
        double[] associatedFunctionality = new double[numGenerators];
        while (count != numGenerators) {
            StdIn.readInt();
            associatedFunctionality[count] = StdIn.readDouble();
            count++;
        } // get associated functionalities
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = StdIn.readInt(); // energy cost
            }
        } // create adjacency matrix
        int[][] update = new int[numGenerators][numGenerators];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int energyCost = matrix[i][j];
                int totalCost = (int) (energyCost / (associatedFunctionality[i] * associatedFunctionality[j]));
                update[i][j] = totalCost;
            }
        } // fix adjacency matrix, tested and is correct
        int[] minCost = new int[numGenerators]; // tracks minimum cost to reach every vertex from vertex 0
        boolean[] DijkstraSet = new boolean[numGenerators]; // track which are in path or have been visited already
        for (int i = 0; i < numGenerators; i++) {
            if (i == 0) {
                minCost[i] = 0;
            } else {
                minCost[i] = Integer.MAX_VALUE;
            }
        } // set each minimum cost to infinity except for vertex 0 because that is our start point, works

        for (int i = 0; i < numGenerators; i++) { // n - 1 iterations
            int currentSource = getMinCostNode(minCost, DijkstraSet); // find min value vertex not visited in mincost array
            DijkstraSet[currentSource] = true; // include min in processed set
            for (int j = 0; j < numGenerators; j++) { // controls column or adj node you're looking at
                if (update[currentSource][j] == 0) continue; // if not connected, skip
                if (minCost[currentSource] != Integer.MAX_VALUE && DijkstraSet[j] != true && minCost[j] > (minCost[currentSource] + update[currentSource][j])) {
                    minCost[j] = minCost[currentSource] + update[currentSource][j];
                }
            }
        }

        StdOut.setFile(args[1]);
        StdOut.print(minCost[minCost.length - 1]); // returns cost to reach last vertex
    }
}