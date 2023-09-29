package application;

public class AnalyseSyntaxique {
	
	static String text="";
	String erreur="";
	
	//check si instruction se termine par %.
	void checkEndInst(String mot){
		if (AnalyseLexicale.checkFinLigne(mot)==true) {
			mot=AnalyseLexicale.cleanMot(mot);
			switch (mot) {
			case "Snl_Start": case "Snl_Close": case "%.": case "do": case "%":
			case "Else": case "Start": case "Finish" : case "If": {break;}
			default: {erreur=erreur+"%. manquante  ";}
			}
		}}
	
	//check pas plus d'une seule instruction par ligne
	void checkOneInst(String mot){
		if (AnalyseLexicale.checkFinLigne(mot)==false) {
			switch (mot) {
			case "Snl_Start": case "Snl_Close": case "%.":
			case "Else": case "Start": case "Finish" : {erreur=erreur+"une seule instruction par ligne  ";}
			}
		}}
	
	
	public void analysesyntaxique(String[] mot) {
		
		int If=0;
		int Else=0;
		int Start=0;
		int Finish=0;
		String typemot;
		
		//check si programme commence par Snl_Start
		if(mot[1].equals("Snl_Start")==false) text=text+"[ERROR] Snl_Start expected au debut du programme\n";
		
		for(int i=1;i<mot.length;i++) {
			
		if(mot[i]=="") {}
		else {
			
			text = text + mot[i] + " ";
		
			typemot=AnalyseLexicale.typeMot(mot[i]);
			
			//check si instruction se termine par %.
			checkEndInst(mot[i]);
			//check pas plus d'une seule instruction par ligne
			checkOneInst(mot[i]);	
			
			switch(typemot) { 
			
			//comment
			case "Mot réservé pour debut d'un commentaire" : {
				i++;
				while(i<mot.length && AnalyseLexicale.checkFinLigne(mot[i])==false) {text = text + mot[i] + " ";i++;}
				if(i<mot.length && AnalyseLexicale.checkFinLigne(mot[i])==true) {text = text + mot[i];}
				break;
			}
			
			//declaration
			case "Mot réservé pour déclaration d'un entier": case "Mot réservé pour déclaration d'un réel": {
				int j=i+1;
				String temp;
				if(j<mot.length) {
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false && temp.equals("declaration multiple")==false) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true)erreur=erreur+"identificateur expected  ";
						else erreur=erreur+"voir Analyse Lexicale (identificateur non reconnu)  ";
					}
				if (AnalyseLexicale.checkFinLigne(mot[j])==false) {
				j++;
				if(j<mot.length) {
					temp=mot[j];
					if (AnalyseLexicale.checkFinLigne(mot[j])==true) {temp=AnalyseLexicale.cleanMot(mot[j]);}
					if (temp.equals("%.")==false) {erreur=erreur+"erreur non definie  ";}}}}
				break;}
			
			//affectation
			case "Mot réservé pour affectation" : {
				int j=i+1;
				int c=0;
				String temp;
				if(j<mot.length) { // Identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true) {
							erreur=erreur+"identificateur et valeur expected  ";
							c=1;
						}
						else if (temp.equals("nombre reel")==true || temp.equals("nombre entier")==true ) {
							erreur=erreur+"identificateur expected avant valeur  ";
							c=2;
						}
						else erreur=erreur+"voir Analyse Lexicale (identificateur non reconnu)  ";
					}	
				j++;
				if(j<mot.length && c!=1) { // Valeur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("nombre reel")==false && temp.equals("nombre entier")==false ) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true){
							if (c==0) erreur=erreur+"nombre expected  ";
							c=3;
							}
						else erreur=erreur+"voir Analyse Lexicale (nombre non reconnu)  ";
					}
				j++;
				if(j<mot.length) {
				if (AnalyseLexicale.checkFinLigne(mot[j])==false && c!=3 ) {
					temp=mot[j];
					if (AnalyseLexicale.checkFinLigne(mot[j])==true) {temp=AnalyseLexicale.cleanMot(mot[j]);}
					if (temp.equals("%.")==false) {erreur=erreur+"erreur non definie  ";}}}}}
				break;}
			
			//affectation entre variables
			case "Mot réservé pour debut d'affectation entre variables" : {
				int j=i+1;
				int c=0;
				String temp;
				if(j<mot.length) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true) {
							erreur=erreur+"identificateur from identificateur expected  ";
							c=1;
						}
						else if (temp.equals("Mot réservé pour affectation entre variables")==true) {
							erreur=erreur+"identificateur expected avant from  ";
							c=2;
						}
						else erreur=erreur+"voir Analyse Lexicale (identificateur non reconnu)  ";
					}	
				j++;
				if(j<mot.length && c!=1) { // from
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("Mot réservé pour affectation entre variables")==false) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true){
							if (c==0) erreur=erreur+"from identificateur expected  ";
							c=3;
							}
						else erreur=erreur+"voir Analyse Lexicale (mot reservé pour affectation non reconnu)  ";
					}
				j++;
				if(j<mot.length && c!=3) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true){
						if (c==0) erreur=erreur+"identificateur expected  ";
							c=4;
							}
						else erreur=erreur+"voir Analyse Lexicale (identificateur non reconnu)  ";
					}
				}
				j++;
				if(j<mot.length) { 
				if (AnalyseLexicale.checkFinLigne(mot[j])==false && c!=4 ) {
					temp=mot[j];
					if (AnalyseLexicale.checkFinLigne(mot[j])==true) {temp=AnalyseLexicale.cleanMot(mot[j]);}
					if (temp.equals("%.")==false) {erreur=erreur+"erreur non definie  ";}}}}}
				break;}
			
			//affichage
			case "Mot réservé pour affichage" : {
				int j=i+1;
				int c=0;
				String temp;
				if(j<mot.length) { // chaine ou identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("chaine")==false && (temp.equals("identificateur")==false && temp.equals("declaration multiple")==false)) {
						if (temp.equals("Mot réservé pour fin d'une instruction")==true) {
							erreur=erreur+"chaine ou variable expected  ";
							c=1;
						}
						else erreur=erreur+"voir Analyse Lexicale (chaine ou identificateur non reconnu)  ";
					}	
				j++;
				if(j<mot.length) {
				if (AnalyseLexicale.checkFinLigne(mot[j])==false && c!=1) {
					temp=mot[j];
					if (AnalyseLexicale.checkFinLigne(mot[j])==true) {temp=AnalyseLexicale.cleanMot(mot[j]);}
					if (temp.equals("%.")==false) {erreur=erreur+"erreur non definie  ";}}}}
				break;}
			
			//Condition
			case "Mot réservé pour debut d'une condition" : {
				If++;
				int j=i+1;
				int c=0;
				if (AnalyseLexicale.checkFinLigne(mot[i])==true) {
					erreur=erreur+"condition expected  ";
					c=1;
				}
				String temp;
				if(j<mot.length && c!=1) { // %
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("Mot réservé pour condition")==false) {
							erreur=erreur+"expression logique incorrecte  ";
							c=1;
						}
					}	
				j++;
				if(j<mot.length && c!=1) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false) {
						if (temp.equals("Mot réservé pour condition")==true) {
							if (c==0) erreur=erreur+"expression logique expected between % %  ";
							c=1;
							}
						else {
							if (c==0) erreur=erreur+"expression logique incorrecte  ";
							c=1;
					}}
				j++;
				if(j<mot.length && c!=1) { // operateur logique
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.contains("Operateur logique")==false) {
						if (c==0) erreur=erreur+"expression logique incorrecte  ";
						c=1;
					}
				}
				j++;
				if(j<mot.length && c!=1) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==false) {
						if (c==0) erreur=erreur+"expression logique incorrecte  ";
						c=1;
					}
				}
				j++;
				if(j<mot.length && c!=1) { // %
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("Mot réservé pour condition")==false) {
					 erreur=erreur+"expression logique incorrecte  ";
					 c=1;
					}}
				j++;
				if(j<mot.length && c!=1) { // do
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("Mot réservé pour faire")==false) {
						erreur=erreur+"do expected  ";
						c=1;
					}
				}}
				break;}
			
			//Else
			case "Mot réservé pour sinon" : {
				Else++;
				if (If < Else) {erreur=erreur+"If expected before Else  ";
				Else--;}
				break;
			}
			//Start
			case "Mot réservé pour debut d'un bloc d'instructions" : {
				Start++;
				if (If < Start && Else < Start) erreur=erreur+"condition expected before Start  ";
				break;
			}
			//Finish
			case "Mot réservé pour fin d'un bloc d'instructions" : {
				Finish++;
				if (Start < Finish) erreur=erreur+"Start expected before Finish  ";
				break;
			}
			
			default : {
				if(typemot.equals("Mot réservé pour début du programme")==false 
				&& typemot.equals("Mot réservé pour fin du programme")==false) {
				if(AnalyseLexicale.checkFinLigne(mot[i-1])==true)erreur=erreur+"erreur non definie  ";}}
			}	
		}
		if(i<mot.length && AnalyseLexicale.checkFinLigne(mot[i])==true) {
			if(erreur.equals("")==false) text = text + "[ERROR] " + erreur;
			erreur="";
			text = text + "\n";
			}
		}
		int n=Start-Finish;
		if(n>0)text=text+"[ERROR] "+n+" missing Finish\n";
		//check si programme se termine par Snl_Close
		if(mot[mot.length-1].equals("Snl_Close")==false) text=text+"[ERROR] Snl_Close expected a la fin du programme\n";
	}
}
		
		

