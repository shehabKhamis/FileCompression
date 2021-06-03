/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Shehab eldeen
 */
class Node {
    Character ch;
    Integer freq;
    Node left = null, right = null;
 
    Node(Character ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }
 
    public Node(Character ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
    
}
public class Huffman {
   static String Sentence="";
   static Map<Character, Integer> freq = new HashMap<>();
static Map<Character, String> code = new HashMap<>();
static HashMap<Character, String> load = new HashMap<>();
static StringBuilder strB=new StringBuilder();

    /**
     * @param args the command line arguments
     */
   static void treeTraverse(Node root,String str)
   {
        if (root == null) {
            return;
        }
 
        // Found a leaf node
        // insert 1 if there is only one node in the tree
        if (root.right==null&&root.left==null) {
            code.put(root.ch, str.length() > 0 ? str : "1");
        }

        treeTraverse(root.left, str + '1');
        treeTraverse(root.right, str + '0');
   }
   static void HuffmanTree(String fileName)
   {
       int numzero=0;
       PriorityQueue<Node> pQueue   = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq)); 
       freq.entrySet().stream().forEach((entry) -> {
           pQueue.add(new Node(entry.getKey(), entry.getValue()));
       });
       while (pQueue.size() != 1)
        {
            // Remove the two nodes of the highest priority
            // (the lowest frequency) from the queue
 
            Node left = pQueue.poll();
            Node right = pQueue.poll();
 
            // Create a new internal node with these two nodes as children
            // and with a frequency equal to the sum of the two nodes'
            // frequencies. Add the new node to the priority queue.
 
            int sum = left.freq + right.freq;
            pQueue.add(new Node(null, sum, left, right));
        }
       treeTraverse(pQueue.peek(),"");
       System.out.println("Huffman Codes are: " + code);
       // System.out.println("Original string is: " +Sentence);
        
       StringBuilder sb = new StringBuilder();
        for (char c: Sentence.toCharArray()) {
            sb.append(code.get(c));
        }
        while(sb.length()%8!=0)
        {
            sb.append('0');
            numzero++;
        }
        System.out.println("Encoded string is: " +sb);
        int i=1;
        String con="";
        StringBuilder compressed=new StringBuilder();
        compressed.append(Integer.toString(numzero));
       //////////////////////////////////////////////////////////// 
         try {
      File myObj = new File(getFileName(fileName));
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
        
   /////////////////////////////////////////////////////////     
       
            try {
      FileWriter myWriter = new FileWriter(getFileName(fileName));
       for(Map.Entry<Character,String> m :code.entrySet()){
          myWriter.write(m.getKey()+m.getValue());
        }
        myWriter.write(System.getProperty("line.separator"));
     
      myWriter.close();
    // System.out.println("file is compressed successfully.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
        while(i<=sb.length())
        {
            
            if(i%8==0)
            {   
                  con+=sb.charAt(i-1);
                int decimal=Integer.parseInt(con,2);
                   // System.out.println(decimal);
                    // System.out.println("//////////");
                 compressed.append( Character.toString((char)decimal));
                 con="";
                 
            }
            else
                con+=sb.charAt(i-1);
            i++;
        }
        //System.out.println(compressed);
           try {
     FileWriter myWriter = new FileWriter(getFileName(fileName),true);
      myWriter.write(compressed.toString());
      myWriter.close();
    // System.out.println("file is compressed successfully.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
               
               
        
       
       // int decimal=Integer.parseInt(sb.toString());
        
       
   }
   
    static void CountFreq(String text)
    {
        for (char c: text.toCharArray()) {
            
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
    }
    
   static void ReadFile(Scanner input,String fileName)
    {
        int count=0;
        try {
            

            File file = new File(fileName);

            input = new Scanner(file);
            

            while (input.hasNextLine()) {
                String line = input.nextLine();
                
                CountFreq(line);
               // System.out.println(line);
                Sentence=Sentence+line;
                count++;
                
            }
           // freq.put('\r\n', count);
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   static String getFileName(String fileName)
   {
       String str=fileName.substring(0, fileName.length()-4);
       str=str+"-compressed.txt";
       return str;
   }
   static void loadFile(String filename)
   {
        char lastKey='c';
       File file = new File(filename);
 
        try (FileReader fr = new FileReader(file)) {
            int content;
            while ((content = fr.read()) != -1&&(char)content!=Character.LINE_SEPARATOR) {
                
               // System.out.print((char)content + " ");
              //  System.out.println(Character.isDigit((char)content));
                
               if(Character.isDigit((char)content))
               {
                   load.replace(lastKey, load.get(lastKey)+(char)content);
               }
               else 
               {
                    
                   load.put((char)content,"");
                    lastKey=(char)content;
                   
                    
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       //  System.out.println(load);
       
   }
   static void decompress(String filename)
   {
       
       int numzeros=0;
       int flag=0;
       //////loadFile bt-read awel satr w t7oto f hashmap
      loadFile(filename);
      
      // File file = new File(getFileName(filename));
        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            int content;
            br.readLine();
            while ((content = br.read()) != -1) {
              if(flag==0)
              {
                  numzeros=Character.getNumericValue((char)content);
                 
                  flag=1;
                  continue;
              }
             // System.out.print((int)(char) content);
             // System.out.println();
              String temp=Integer.toBinaryString((int)(char) content);
              while(temp.length()<8)
              {
                  temp="0"+temp;
              }
              strB.append(temp);
              temp="";
              
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println(strB);
        strB.setLength(strB.length()-numzeros);
      //  System.out.println(strB);
        
        
        
   }
   static void finalDecompress(String filename)
   {
      
         try {
      File myObj = new File("Decompressed.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
         
       decompress(filename);
       System.out.println(strB.length());
       try {
      FileWriter myWriter = new FileWriter("Decompressed.txt");
       
       StringBuilder temp=new StringBuilder();
       
      
       for(int i=0;i<strB.length();i++)
       {
          // System.out.println("remaining : "+(strB.length()-i));
           temp.append(strB.charAt(i));
         //  System.out.println(temp);
           for(Entry<Character, String> entry: load.entrySet()) {      
        if(entry.getValue().equals(temp.toString())) {
        //  System.out.println("heyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        myWriter.write(entry.getKey());
       // System.out.println("Decompressed file : ");
        System.out.print(entry.getKey());
        temp.delete(0, temp.length());
        break;
      }
   
    }
         
       }
      
      
      
      
      
       
     
      myWriter.close();
    // System.out.println("file is compressed successfully.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
       
   }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the file name with extension : ");
        String fileName=input.nextLine();
            System.out.println("Select an option : ");
            System.out.println("Compress");
            System.out.println("Decompress");
            
            
            int option=input.nextInt();
               switch (option) 
      {
          //comparing value of variable against each case
        
        case 1:
          //System.out.println("Enter Directory of the file to be compressed :");
          //  String fileName=input.nextLine();
             long start1 = System.currentTimeMillis();
            ReadFile(input,fileName);
            HuffmanTree(fileName);
             long end2 = System.currentTimeMillis()-start1;
              System.out.println("file was compressed in :"+end2 + "milliseconds");
             
            main(args);
            break;
        case 2:
         // System.out.println("Enter Directory of the file to be decompressed :");
             //String filename=input.nextLine();
            long start = System.currentTimeMillis();
            finalDecompress(fileName);
            long end = System.currentTimeMillis()-start;
            System.out.println("file was decompressed in :"+end+"miliseconds");
            main(args);
            break;
        //optional
        default:
          System.out.println("Invalid Input!");
    }
         //   String fileName=input.nextLine();
         //   ReadFile(input,fileName);
        // System.out.println(input.nextLine());
          //  HuffmanTree(fileName);
          //  finalDecompress(fileName);
         
            //11111111100000000000001010101000
            //11111111100000000000001010101000
    }
    
}
