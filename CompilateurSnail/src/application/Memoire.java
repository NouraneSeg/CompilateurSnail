package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Memoire {
	
	static String erreur="";
	
	static String[][] CsvIntoArray(String file){
		
		BufferedReader reader = null;
		String line="";
		List<String[]> rows = new ArrayList<String[]>();
		try {
		reader = new BufferedReader(new FileReader(file));
		while((line=reader.readLine()) != null) {
			rows.add(line.split(";"));
			}
		String[][] array = new String[rows.size()][0];
		rows.toArray(array);
		reader.close();
		return array;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static void ArrayIntoCsv(String[][] array,String filename) throws IOException{
		FileWriter file = new FileWriter(filename);
	    BufferedWriter b = new BufferedWriter(file);
	    for(int i=0;i<array.length;i++) {
	    if(array[i].length>=2) {	
	    b.write(array[i][0]+";"+array[i][1]+";"+array[i][2]+";");
	    b.newLine();
	    }}
	    b.close();
	    file.close();
	}
	
	public static boolean CheckVarExist(String var) throws IOException {
		FileReader file = new FileReader("src\\Memoire.csv");
	    BufferedReader b = new BufferedReader(file);
	    String line="";
	    while((line = b.readLine()) != null) {
	    	String[] row = line.split(";");
	    	if(row[0].equals(var)) {return true;} 
	    }
	    b.close();
	    file.close();
		return false;
		}
	
	public static void DeclarerVariable(String var ,String type) throws IOException {
		erreur="";
		if(CheckVarExist(var)==true) {erreur=erreur+"Nom variable deja utilisé  ";}
		else {
		FileWriter file = new FileWriter("src\\Memoire.csv",true);
	    BufferedWriter b = new BufferedWriter(file);
	    b.write(var+";"+type+";"+"0");
	    b.newLine();
	    b.close();
	    file.close();
		}}
	
	public static void Affectation(String var,String val,String type) throws IOException {
		String[][] array = CsvIntoArray("src\\Memoire.csv");
		int j=0;
		int i=0;
		for(i=0;i<array.length;i++) {
			if(array[i][0].equals(var)==true) {
				if (array[i].length>1) {
				if(array[i][1].equals(type)==true) {array[i][2]=val;}
				else erreur=erreur+"types non compatibles  ";
			}}
			else j++;
		}
		if (j==i) erreur=erreur+"Variable non declarée  ";
		ArrayIntoCsv(array,"src\\\\Memoire.csv");
	}
	
	public static String getValueVar(String var) throws IOException {
		FileReader file = new FileReader("src\\Memoire.csv");
	    BufferedReader b = new BufferedReader(file);
	    String line="";
	    while((line = b.readLine()) != null) {
	    	String[] row = line.split(";");
	    	if(row[0].equals(var)) {return row[2];} 
	    }
	    erreur=erreur+"Variable non declarée";
	    b.close();
	    file.close();
		return "not found";
		}
	
	public static String getTypeVar(String var) throws IOException {
		FileReader file = new FileReader("src\\Memoire.csv");
	    BufferedReader b = new BufferedReader(file);
	    String line="";
	    while((line = b.readLine()) != null) {
	    	String[] row = line.split(";");
	    	if(row[0].equals(var)) {return row[1];} 
	    }
	    erreur=erreur+"Variable non declarée";
	    b.close();
	    file.close();
		return "not found";
		}
}
