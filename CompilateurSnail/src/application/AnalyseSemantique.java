package application;

import java.io.IOException;

public class AnalyseSemantique {
	
	static String text="Analyse Sémantique";

	public void analysesemantique(String mot[]) throws IOException {
		
		for(int i=1;i<mot.length;i++) {
			
		if(mot[i]=="") {}
		else {
			
			text = text + mot[i] + " ";
			String typevar="";
			String typemot = AnalyseLexicale.typeMot(mot[i]);
		
			switch(typemot) { 
			
			//declaration 
			case "Mot réservé pour déclaration d'un entier": case "Mot réservé pour déclaration d'un réel": {
				if(typemot.equals("Mot réservé pour déclaration d'un entier")){typevar="entier";}
				else if(typemot.equals("Mot réservé pour déclaration d'un réel")){typevar="reel";}
				int j=i+1;
				String temp;
				if(j<mot.length) {
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==true) {Memoire.DeclarerVariable(mot[j],typevar);}
					if(temp.equals("declaration multiple")==true) {
						String[] mot_ = mot[j].split(",");
						for(int k=0;k<mot_.length;k++) {
							if(AnalyseLexicale.typeMot(mot_[k]).equals("identificateur")==true){
								Memoire.DeclarerVariable(mot_[k],typevar);
							}
						}
					}
				}
				break;
			}
				
			//affectation
			case "Mot réservé pour affectation" : {
				int j=i+1;
				int c=0;
				String temp;
				String var="";
				if(j<mot.length) { // Identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==true) {var=mot[j];	
				j=j+1;
				if(j<mot.length && c!=1) { // Valeur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("nombre reel")==true){Memoire.Affectation(var, mot[j],"reel");}
					else if(temp.equals("nombre entier")==true){Memoire.Affectation(var, mot[j],"entier");}
					}}
				}break;}
			
			//affectation entre variables
			case "Mot réservé pour debut d'affectation entre variables" : {
				int j=i+1;
				int c=0;
				String temp;
				String var="";
				if(j<mot.length) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					if(temp.equals("identificateur")==true) {var=mot[j];}
					}
				j=j+2;
				if(j<mot.length) { // identificateur
					temp = AnalyseLexicale.typeMot(mot[j]);
					String val = Memoire.getValueVar(mot[j]);
					String type = Memoire.getTypeVar(mot[j]);
					if(temp.equals("identificateur")==true) {Memoire.Affectation(var,val,type);}
					}
				break;
				}
			}
		}
		
			if(AnalyseLexicale.checkFinLigne(mot[i])==true) {
			if(Memoire.erreur.equals("")==false) text = text + "[ERROR] " + Memoire.erreur;
			Memoire.erreur="";
			text = text + "\n";
			}
	}
}
}
