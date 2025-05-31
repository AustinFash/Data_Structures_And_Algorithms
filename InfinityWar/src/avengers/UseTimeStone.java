package avengers;
import java.util.*;
/**
 * Given a starting event and an Adjacency Matrix representing a graph of all possible 
 * events once Thanos arrives on Titan, determine the total possible number of timelines 
 * that could occur AND the number of timelines with a total Expected Utility (EU) at 
 * least the threshold value.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * UseTimeStoneInputFile name is passed through the command line as args[0]
 * Read from UseTimeStoneInputFile with the format:
 *    1. t (int): expected utility (EU) threshold
 *    2. v (int): number of events (vertices in the graph)
 *    3. v lines, each with 2 values: (int) event number and (int) EU value
 *    4. v lines, each with v (int) edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note 1: the last v lines of the UseTimeStoneInputFile is an ajacency matrix for a directed
 * graph. 
 * The rows represent the "from" vertex and the columns represent the "to" vertex.
 * 
 * The matrix below has only two edges: (1) from vertex 1 to vertex 3 and, (2) from vertex 2 to vertex 0
 * 0 0 0 0
 * 0 0 0 1
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * UseTimeStoneOutputFile name is passed through the command line as args[1]
 * Assume the starting event is vertex 0 (zero)
 * Compute all the possible timelines, output this number to the output file.
 * Compute all the posssible timelines with Expected Utility higher than the EU threshold,
 * output this number to the output file.
 * 
 * Note 2: output these number the in above order, one per line.
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for total number of timelines
 *     //Call StdOut.print() for number of timelines with EU >= threshold EU 
 * 
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/UseTimeStone usetimestone.in usetimestone.out
 * 
 * @author Yashas Ravi
 * 
 */

public class UseTimeStone {

    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java UseTimeStone <INput file> <OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
    	int thresholdEU = StdIn.readInt(); // threshold to win
        int numEvents = StdIn.readInt(); // num of nodes
        int counter = 0;
        int[] EUvalues = new int[numEvents];
        while (counter < numEvents) {
            int index = StdIn.readInt();
            EUvalues[index] = StdIn.readInt();
            counter++;
        } // array of desirability or EU values
        int[][] matrix = new int[numEvents][numEvents];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = StdIn.readInt();
            }
        } // adjacency matrix
        int[] vertices = new int[numEvents];
        //Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = i;
        } // array of vertices

        Deque<Pair> queue = new ArrayDeque<Pair>();
        int source = vertices[0];
        Pair pair = new Pair(source, EUvalues[source]);
        queue.addFirst(pair);
        int timelineCount = 0;
        int winningCount = 0;

        while (!(queue.isEmpty())) {
            Pair curr = queue.removeFirst();
            timelineCount++;
            if (curr.val >= thresholdEU) {
                winningCount++;
            }
            ArrayList<Integer> adjNodesToCurr = new ArrayList<>();
            for (int i = 0; i < numEvents; i++) {
                if (matrix[curr.key][i] == 1) {
                    adjNodesToCurr.add(i);
                }
            }
            for (int i = 0; i < adjNodesToCurr.size(); i++) {
                Pair newPair = new Pair(adjNodesToCurr.get(i), EUvalues[adjNodesToCurr.get(i)] + curr.val);
                queue.addLast(newPair); // enqueue to end
            }
        }

        StdOut.setFile(args[1]);
        StdOut.println(timelineCount);
        StdOut.print(winningCount);
        
    }
}
