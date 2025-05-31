package avengers;

/**
 * Given a Set of Edges representing Vision's Neural Network, identify all of the 
 * vertices that connect to the Mind Stone. 
 * List the names of these neurons in the output file.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * MindStoneNeighborNeuronsInputFile name is passed through the command line as args[0]
 * Read from the MindStoneNeighborNeuronsInputFile with the format:
 *    1. v (int): number of neurons (vertices in the graph)
 *    2. v lines, each with a String referring to a neuron's name (vertex name)
 *    3. e (int): number of synapses (edges in the graph)
 *    4. e lines, each line refers to an edge, each line has 2 (two) Strings: from to
 * 
 * Step 2:
 * MindStoneNeighborNeuronsOutputFile name is passed through the command line as args[1]
 * Identify the vertices that connect to the Mind Stone vertex. 
 * Output these vertices, one per line, to the output file.
 * 
 * Note 1: The Mind Stone vertex has out degree 0 (zero), meaning that there are 
 * no edges leaving the vertex.
 * 
 * Note 2: If a vertex v connects to the Mind Stone vertex m then the graph has
 * an edge v -> m
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
 *     //Call StdOut.print() for EVERY neuron (vertex) neighboring the Mind Stone neuron (vertex)
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/MindStoneNeighborNeurons mindstoneneighborneurons.in mindstoneneighborneurons.out
 *
 * @author Yashas Ravi
 * 
 */


public class MindStoneNeighborNeurons {
    
    public static void main (String [] args) {
        
    	if ( args.length < 2 ) {
            StdOut.println("Execute: java MindStoneNeighborNeurons <INput file> <OUTput file>");
            return;
        }
    	
    	StdIn.setFile(args[0]);
        int numNeurons = StdIn.readInt(); // number of vertices
        int count = 0;
        String[] neurons = new String[numNeurons];
        while (count != numNeurons) {
            String neuron = StdIn.readString();
            neurons[count] = neuron;
            count++;
        } // name of each neuron
        
        int numSynapses = StdIn.readInt(); // number of edges
        int count2 = 0;
        String[] synapses = new String[numSynapses*2];
        while (count2 != (numSynapses * 2)) {
            String synap = StdIn.readString();
            synapses[count2] = synap;
            for (int i = 0; i < neurons.length; i++) {
                if (neurons[i] != null && neurons[i].equals(synapses[count2])) { // remove overlap of "from" synapses in neurons array, leaving you with the mindstone neuron
                    System.out.println(neurons[i]);
                    neurons[i] = null;
                } 
            }
            count2++;
            synap = StdIn.readString();
            synapses[count2] = synap;
            count2++;
        }

        // then go back into synapses array and search for mind stone neuron, and whenever you reach it, print the one before it
        StdOut.setFile(args[1]);
        String mindStone = "";
        for (int i = 0; i < neurons.length; i++) {
            if (neurons[i] != null) {
                mindStone = neurons[i];
            }
        }
        for (int i = 0; i < synapses.length; i++) {
            if (synapses[i] != null && synapses[i].equals(mindStone)) {
                StdOut.println(synapses[i - 1]);
            }
        }
        
    }
}