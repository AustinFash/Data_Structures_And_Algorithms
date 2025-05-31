package avengers;

/**
 * Given an adjacency matrix, use a random() function to remove half of the nodes. 
 * Then, write into the output file a boolean (true or false) indicating if 
 * the graph is still connected.
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * PredictThanosSnapInputFile name is passed through the command line as args[0]
 * Read from PredictThanosSnapInputFile with the format:
 *    1. seed (long): a seed for the random number generator
 *    2. p (int): number of people (vertices in the graph)
 *    2. p lines, each with p edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note: the last p lines of the PredictThanosSnapInputFile is an ajacency matrix for
 * an undirected graph. 
 * 
 * The matrix below has two edges 0-1, 0-2 (each edge appear twice in the matrix, 0-1, 1-0, 0-2, 2-0).
 * 
 * 0 1 1 0
 * 1 0 0 0
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * Delete random vertices from the graph. You can use the following pseudocode.
 * StdRandom.setSeed(seed);
 * for (all vertices, go from vertex 0 to the final vertex) { 
 *     if (StdRandom.uniform() <= 0.5) { 
 *          delete vertex;
 *     }
 * }
 * Answer the following question: is the graph (after deleting random vertices) connected?
 * Output true (connected graph), false (unconnected graph) to the output file.
 * 
 * Note 1: a connected graph is a graph where there is a path between EVERY vertex on the graph.
 * 
 * Note 2: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, isConnected is true if the graph is connected,
 *   false otherwise):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(isConnected);
 * 
 * @author Yashas Ravi
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/PredictThanosSnap predictthanossnap.in predictthanossnap.out
*/

public class PredictThanosSnap {

    private static int getNextAliveVertex(int[] array) {
        int next = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != -1) {
                next = i;
                break;
            }
        }
        return next;

    }

    private static void dfs(int source, boolean[] visited, int[][] matrix) {
        visited[source] = true; // set current node as visited
        for (int i = 0; i < matrix[source].length; i++) { // for every node in graph
            if (matrix[source][i] == 1 && (!visited[i])) { // if node is adjacent to current node and hasn't been visited yet
                dfs(i, visited, matrix);
            }
        }
    }

	 
    public static void main (String[] args) {
 
        if ( args.length < 2 ) {
            StdOut.println("Execute: java PredictThanosSnap <INput file> <OUTput file>");
            return;
        }
        
        StdIn.setFile(args[0]);

    	long seed = StdIn.readLong();
        StdRandom.setSeed(seed);

        int numNodes = StdIn.readInt();
        int[][] matrix = new int[numNodes][numNodes];
        int[] vertices = new int[numNodes]; // array where each index represents a vertex

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = i;
        } // set up array of vertices, works
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = StdIn.readInt();
            }
        } // set up adjacency matrix

        for (int i = 0; i < matrix.length; i++) {
            if (StdRandom.uniform() <= 0.5) { // delete vertex
                vertices[i] = -1; // works, so deleted vertices will be -1
                for (int j = 0; j < matrix.length; j++) {
                    matrix[i][j] = 0;
                    matrix[j][i] = 0;
                }
            }
        } // random deletion of vertices

        StdOut.setFile(args[1]);
        boolean connected = false;
        boolean[] visited = new boolean[numNodes]; // visited has to align with vertices array bc need to have same indexes
        dfs(getNextAliveVertex(vertices), visited, matrix); // perform dfs on any vertex
        for (int j = 0; j < visited.length; j++) {
            if (vertices[j] == -1) continue;
            if (visited[j] == false) {
                connected = false;
                break;
            } else {
                connected = true;
            }
        }
        StdOut.print(connected);

    }
}
