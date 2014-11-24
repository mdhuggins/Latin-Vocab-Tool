package LatinVocab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class LatinVocab {
	public static void main( String[] args ){
		
		String path = JOptionPane.showInputDialog("File Name?") + ".txt";
		String text = JOptionPane.showInputDialog("Latin Text?");
		getAllDefs(text, path);
		System.out.println("All Done!");
		
	}
	
	private static void getAllDefs(String t, String path){
		// Remove Punctuation etc.
		String text = t.replace(".", "");
		text = text.replace(",", "");
		text = text.replaceAll(";", "");
		text = text.replaceAll(":", "");
		text = text.replaceAll("!", "");
		text = text.replaceAll("\\?", "");
		text = text.replaceAll("\'", "");
		text = text.replaceAll("\"", "");
		text = text.replaceAll("\t", "");
		text = text.replaceAll("\n", "");
		text = text.replaceAll("1", "");
		text = text.replaceAll("2", "");
		text = text.replaceAll("3", "");
		text = text.replaceAll("4", "");
		text = text.replaceAll("5", "");
		text = text.replaceAll("6", "");
		text = text.replaceAll("7", "");
		text = text.replaceAll("8", "");
		text = text.replaceAll("9", "");
		text = text.replaceAll("0", "");
		text = text.replaceAll("  ", "");
		text = text.replaceAll("-", "");
		text = text.replace("(", "");
		text = text.replace(")", "");
		text = text.replace("[", "");
		text = text.replace("]", "");
		
		// Split into words
		String[] words = text.split(" ");
		// Remove Duplicates
		words = new HashSet<String>(Arrays.asList(words)).toArray(new String[0]);
		// Sort Alphabetically
		Arrays.sort(words);
		
		String result = "";
		System.out.println("Getting Definitions");
		// Build result
		for( String word: words)
			result += word + ": " + getWordDefs(word) + "\n";
		System.out.println("Done Getting Definitions");
		
		// Write to file
        try {
          File file = new File(path);
          BufferedWriter output = new BufferedWriter(new FileWriter(file));
          output.write(result);
          output.close();
        } catch ( IOException e ) {
           e.printStackTrace();
        }
	}
	
	public static String getWordDefs( String l ){
		String out = "";
		// Download HTML
		try {
			out = new Scanner(new URL("http://www.perseus.tufts.edu/hopper/morph?l=" + l + "&la=la").openStream(), "UTF-8").useDelimiter("\\A").next();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Extract Definitions
		String[] divs = out.split("<span class=\"lemma_definition\"");
		int i = 0;
		for(String s: divs)
			if(s.substring(0,1).equals( ">" ))
				i++;
		String[] defs = new String[i];
		int n = 0;
		for(String s: divs)
			if(s.substring(0,1).equals( ">" )){
				defs[n] = s;
				n++;
			}
		
		// Build result
		String result = "";
		for(String def: defs){
			String[] divs2 = def.split("<");
			out = divs2[0];
			String[] divs3 = out.split("\t");
			String str = divs3[divs3.length-1];
			str = str.replaceAll("\t", "");  
			str = str.replaceAll("\n", "");
			str = str.replaceAll("  ", "");
			if( !str.contains(">"))
				result += str + "; ";
		}
		if( result.equals("") )
			result = "Not Found";
		
		return result;
	}
}
