package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList<CharFreq> ();
        int[] frequnecy = new int[128];
        int count = 0; 
        int zero = 0;
        while(StdIn.hasNextChar()){
            char c = StdIn.readChar();
            int index = (int) c;
            frequnecy[index] += 1;
            count++;
        }

        for(int i = 0; i< frequnecy.length; i++){
            if(frequnecy[i] > 0){
                CharFreq feq = new CharFreq();
                char fq = (char) i;
                feq.setCharacter(fq);
                feq.setProbOcc(((double)frequnecy[i])/count);
                sortedCharFreqList.add(feq);
            }
        }

        if(sortedCharFreqList.size() == 1){
            char c =sortedCharFreqList.get(0).getCharacter();
            int n = (int) c;
            int newNum = (n + 1)%128;
            CharFreq feq = new CharFreq();
            feq.setCharacter((char) newNum);
            feq.setProbOcc(0);
            sortedCharFreqList.add(feq);
        }

        Collections.sort(sortedCharFreqList);

    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
        Queue<TreeNode> sQ = new Queue<>();
        Queue<TreeNode> tQ = new Queue<>();
        int zero = 0;
        
        for(int i = 0; i < sortedCharFreqList.size(); i++){
            TreeNode charNode = new TreeNode(sortedCharFreqList.get(i), null, null);
            sQ.enqueue(charNode);            //fills source q from list
        }
       

        while((sQ.isEmpty() == true && tQ.size() == 1)==false){
            

            ArrayList<TreeNode> tNodes= new ArrayList<TreeNode> ();
           
            
            for(int i = 0; i < 2; i++){
                if(tQ.isEmpty()){
                    TreeNode qNodes = sQ.dequeue();
                    tNodes.add(qNodes);
                }
                else if(sQ.isEmpty()){
                    TreeNode qNodes = tQ.dequeue();
                    tNodes.add(qNodes);   
                }
                else if(sQ.peek().getData().getProbOcc() > tQ.peek().getData().getProbOcc()){
                    TreeNode qNodes = tQ.dequeue();
                    tNodes.add(qNodes);
                }
                else if(sQ.peek().getData().getProbOcc() < tQ.peek().getData().getProbOcc()){
                    TreeNode qNodes = sQ.dequeue();
                    tNodes.add(qNodes);
                }
                else if((sQ.peek().getData().getProbOcc() == tQ.peek().getData().getProbOcc())){
                    TreeNode qNodes = sQ.dequeue();   
                    tNodes.add(qNodes);
                    
                }
            }

            CharFreq newCharNodes = new CharFreq(null, tNodes.get(0).getData().getProbOcc()+tNodes.get(1).getData().getProbOcc());
            

            TreeNode newTreeNodes = new TreeNode(newCharNodes, tNodes.get(0), tNodes.get(1));
            

            tQ.enqueue(newTreeNodes);
            
            tNodes.clear();
            
            
        }

        
        huffmanRoot = new TreeNode();
        huffmanRoot = tQ.peek();

	/* Your code goes here */
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        int zero = 0;
        encodings = new String[128];
        postOrder(huffmanRoot, "");
        for(int i = 0; i<128;i++){
            if(encodings[i]==""){
                encodings[i] = null;
            }
        }
	/* Your code goes here */
    }
    private void postOrder(TreeNode root, String code){
        if (root == null) return;
        if( root.getData().getCharacter() == null){
            postOrder(root.getLeft(), code+"0");
            postOrder(root.getRight(), code+"1"); 
            return;
        }
        int idx = (int) root.getData().getCharacter();
        encodings[idx] = code;
         // counted towards the running time
    }


    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String fString = "";
        while(StdIn.hasNextChar()){
            char c = StdIn.readChar();
            int idx = (int) c;
            String code = (String) encodings[idx];
            fString +=code;
        }
        writeBitString(encodedFile, fString);
        int zero = 0;
	/* Your code goes here */
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
        int zero = 0;
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        StdOut.setFile(decodedFile);
        makeEncodings();
        TreeNode rt = huffmanRoot;
        String code = readBitString(encodedFile);
        decodedFile = "";
        int zero = 0;

        if(code != null){
            for(int i = 0; i < code.length(); i++){
                if(code.charAt(i)=='1'){
                    rt=rt.getRight();
                }
                if(code.charAt(i)=='0'){
                    rt=rt.getLeft();
                }
                if(rt.getData().getCharacter() != null){
                    decodedFile+= rt.getData().getCharacter();
                    rt = huffmanRoot;
                }
            }
        }
        StdOut.print(decodedFile);
        
	/* Your code goes here */
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
