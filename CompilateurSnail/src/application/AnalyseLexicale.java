package application;

public class AnalyseLexicale {
	
	static String text="" ;
	
	//mot sans \r
	static String cleanMot(String mot) {
		
		char before[]=mot.toCharArray();
		char after[] = new char[before.length-1];
		
        for (int i = 0, k = 0; i < before.length; i++) {
            if (before[i] == '\r') {continue;}
            after[k] = before[i];
            k++;
        }
        return String.copyValueOf(after);
    }
	
	//test de fin de ligne
	static boolean checkFinLigne(String mot) {
		char mot_[]=mot.toCharArray();
		for(int i=0;i<mot_.length;i++) {if(mot_[i]=='\r') {return true;}}
		return false;
	}
	
	//test de fin de chaine de caracteres
	boolean checkFinChaine(String mot) {
		char mot_[]=mot.toCharArray();
		for(int i=0;i<mot_.length;i++) {if(mot_[i]=='"') {return true;}}
		return false;
	}

	//type d'un caractere
	static String typeChar(char c) {
		if((c>='a' && c<='z')||(c>='A' && c<='Z')) return "lettre";
		else if(c>='0' && c<='9') return "chiffre";
		else if(c=='_') return "underscore";
		else if(c==',') return "virgule";
		else return "autre";
	}
	
	//type d'un mot
	static String typeMot(String mot) {
		
		if (checkFinLigne(mot)==true)mot=cleanMot(mot);
		
		switch(mot) {
		
			// les mots réservés
			case "Snl_Start": return "Mot réservé pour début du programme";
			case "Snl_Close": return "Mot réservé pour fin du programme";
			case "Snl_Int":   return "Mot réservé pour déclaration d'un entier";
			case "Snl_Real":  return "Mot réservé pour déclaration d'un réel";
			case "Snl_Put":   return "Mot réservé pour affichage";
			case "Set":       return "Mot réservé pour affectation";
			case "Get":       return "Mot réservé pour debut d'affectation entre variables";
			case "from":      return "Mot réservé pour affectation entre variables";
			case "If":        return "Mot réservé pour debut d'une condition";
			case "%":         return "Mot réservé pour condition";
			case "do":		  return "Mot réservé pour faire";
			case "Else":      return "Mot réservé pour sinon";
			case "Start":     return "Mot réservé pour debut d'un bloc d'instructions";
			case "Finish":    return "Mot réservé pour fin d'un bloc d'instructions";
			case "%.":        return "Mot réservé pour fin d'une instruction";
			case "%..":       return "Mot réservé pour debut d'un commentaire";
			case "<" : 		  return "Operateur logique 'inférieur que'";
			case ">" : 		  return "Operateur logique 'supérieur que'";
			case "<=" :       return "Operateur logique 'inférieur ou egal'";
			case ">=" : 	  return "Operateur logique 'supérieur ou egal'";
			case "==" : 	  return "Operateur logique 'égal'";
			case "!=" : 	  return "Operateur logique 'non égal'";
			
			default://le reste des mots
		{
			  //tableau des caracteres du mot
			  char[] motcpc = mot.toCharArray(); 
			  //System.out.println(Arrays.toString(motcpc));
			  
			  //voir le premier caractere du mot
			  String type= typeChar(motcpc[0]);
			  
		    //si le mot commence par une lettre
			  if (type=="lettre") {
				  int j=0;
				  while(j<motcpc.length && (type !="autre"))
					  {
					  if (type=="virgule") return "declaration multiple";
					  else if(type=="operateur logique") return "expression";
					  type = typeChar(motcpc[j]);
					  j++;
					  }
					  if (j<motcpc.length) return "[ERROR] non reconnu";
					  else return "identificateur";
				}
		
			//si le mot commence par un nombre
			  else if (type=="chiffre") {
				  type = typeChar(motcpc[0]);
				  int v = 0;
				  int j=0;
				while(j<motcpc.length && (type=="chiffre" || type=="virgule")) 
					{
					if(type=="operateur logique") return "expression";
					type = typeChar(motcpc[j]);
					if (type=="virgule") {
						if (j+1==motcpc.length) return "[ERROR] non reconnu";
						v++;}
					j++;
					}
				if (j<motcpc.length || v>1) return "[ERROR] non reconnu";
				else if (v==0) return "nombre entier";
				else if (v==1) return "nombre reel";
			  }
			  
			  //debut d'une chaine de caracteres
			  else if (motcpc[0]=='"') return "chaine";
			  else return "[ERROR] non reconnu";
		 }			
		
	}
		return "[ERROR] non reconnu";	
		}
	
	void analyselexicale(String[] mot){
		
		for(int i=1;i<mot.length;i++) {
			
			if(mot[i]=="") {/*ignorer les blancs et les lignes vides*/}
			
			else {
			String typemot = typeMot(mot[i]);
			
			//commentaire
			if (typemot=="Mot réservé pour debut d'un commentaire") {
				text= text + mot[i] +" : "+ typemot + "\n";
				String comment = "" ;
				if(checkFinLigne(mot[i])==true) {}
				else if (i<mot.length) {i++;
				while(i<mot.length && checkFinLigne(mot[i])==false) {comment = comment +" "+ mot[i];i++;}
				if(i<mot.length && checkFinLigne(mot[i])==true)comment = comment +" "+ mot[i];
				text= text + comment +" : commentaire\n";
			}}
			
			//chaine de caracteres
			else if (typemot=="chaine") {
				String chaine = mot[i] ;
				if (i<mot.length) {i++;
				while(i<mot.length && checkFinChaine(mot[i])==false) {chaine = chaine +" "+ mot[i];i++;}
				if (i<mot.length) chaine = chaine +" "+ mot[i];
				text= text + chaine +" : chaine de caracteres\n";
			}}
			
			//reconnaitre les identificateurs dans une declaration multiple
			else if(typemot=="declaration multiple") {
				String[] mot_ = mot[i].split(",");
				//System.out.println(Arrays.toString(mot_));
				for(int k=0;k<mot_.length;k++) { text= text + mot_[k] +" : "+ typeMot(mot_[k])+ "\n";}
			}
			else text= text + mot[i] +" : "+ typemot + "\n";
			}
		if (i<mot.length && checkFinLigne(mot[i])==true)text= text + "-----------------\n";	
	}
}}



















