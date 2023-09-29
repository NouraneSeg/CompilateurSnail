package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller implements Initializable {
	
	public static java.io.File file;
	
	@FXML
	private Pane pane ;
	@FXML
	private javafx.scene.control.Button closewindow;
	@FXML
	private Label labelMessage;
	@FXML
	private Label labelFilePath;
	@FXML
	private ImageView btnRemove;
	@FXML 
	private TitledPane panInfo;
	@FXML
	private TextFlow text;
	@FXML
	private TextArea textarea;
	@FXML
	private TitledPane panMemory;
	@FXML
	HBox table;
	@FXML
	VBox Variable;
	@FXML
	VBox Type;
	@FXML
	VBox Valeur;
	
	void fillTable(String element,VBox column) {
		Pane pane = new Pane();
		pane.setPrefHeight(25);
		pane.setPrefWidth(85);
		Label label = new Label();
		label.setText(element);
		pane.getChildren().add(label);
		label.setLayoutX(18);
		label.setLayoutY(4);
		column.getChildren().add(pane);
	}
	
	private Text textAnalyseLexicale = new Text("L’analyse lexicale est la première phase de compilation. Son principe est de définir les unités lexicales du fichier source.\r\n\r\n"+ "L’analyse lexicale permet aussi l’élimination des mots inutiles comme les espaces, les tabulations, la fin de lignes et les commentaires.");
	private Text textAnalyseSyntaxique = new Text("Le rôle principal de l’analyse syntaxique est de vérifier si l’écriture du programme source est conforme à la syntaxe du langage à compiler.");
	private Text textAnalyseSemantique = new Text("L'analyse sémantique s’agit de vérifier certaines propriétés sémantiques, c’est-a-dire relatives à la signification de la phrase et de ses constituants.");
	
	@FXML
	public void CloseWindow(ActionEvent event) {
		  // get a handle to the stage
	    Stage stage = (Stage) closewindow.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
	//Choisir un fichier
	@FXML
	void ChargerUnFichier(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Text Files","*.snl"));
		file = fc.showOpenDialog(null);
		
		if (file != null) {
			labelFilePath.setText(file.getAbsolutePath());
			labelMessage.setText("fichier chargé avec succès");
			btnRemove.setVisible(true);
			
			textarea.setText("");
			AnalyseLexicale.text="";
			AnalyseSyntaxique.text="";
			AnalyseSemantique.text="";
		}
	}
	@FXML
	void Remove() {
		labelFilePath.setText("");
		labelMessage.setText("Bienvenue dans le compilateur snail");
		btnRemove.setVisible(false);
		file=null;
		
		textarea.setText("");
		AnalyseLexicale.text="";
		AnalyseSyntaxique.text="";
		AnalyseSemantique.text="";
	}
	@FXML
	void analyseLexicale() {
		if (labelMessage.getText()!="Compilation réussite")labelMessage.setText("Vous devez d'abord compiler");
		else if (AnalyseLexicale.text=="")labelMessage.setText("Vous devez d'abord charger un fichier");
		textarea.setText(AnalyseLexicale.text);
	}
	@FXML
	void analyseSyntaxique() {
		if (AnalyseSyntaxique.text=="")labelMessage.setText("Vous devez d'abord charger un fichier");
		textarea.setText(AnalyseSyntaxique.text);

	}
	@FXML
	void analyseSemantique(){
		if (AnalyseSemantique.text=="")labelMessage.setText("Vous devez d'abord charger un fichier");
		textarea.setText(AnalyseSemantique.text);
		}
	
	//help buttons
	@FXML
	void HelpAnalyseLexicale() {
		panInfo.setVisible(true);
		panInfo.setText("Analyse Lexicale");
		text.getChildren().clear();
		text.getChildren().add(textAnalyseLexicale);
		}
	@FXML
	void HelpAnalyseSyntaxique() {
		panInfo.setVisible(true);
		panInfo.setText("Analyse Syntaxique");
		text.getChildren().clear();
		text.getChildren().add(textAnalyseSyntaxique);
		}
	@FXML
	void HelpAnalyseSemantique() {
		panInfo.setVisible(true);
		panInfo.setText("Analyse Sémantique");
		text.getChildren().clear();
		text.getChildren().add(textAnalyseSemantique);
		}
	@FXML
	void ViewMemory() {
		Variable.getChildren().clear();
		Type.getChildren().clear();
		Valeur.getChildren().clear();
		panMemory.setVisible(true);
		panMemory.setText("Variables declarées");
		String[][] array = Memoire.CsvIntoArray("src\\Memoire.csv");
		for(int i=0;i<array.length;i++) {
			if(array[i].length>=2) {
			fillTable(array[i][0], Variable);
			fillTable(array[i][1], Type);
			fillTable(array[i][2], Valeur);}}
		}
	@FXML
	void ClearMemory() throws IOException {
		Variable.getChildren().clear();
		Type.getChildren().clear();
		Valeur.getChildren().clear();
		FileWriter file = new FileWriter("src\\Memoire.csv");
	    BufferedWriter b = new BufferedWriter(file);
	    b.write("");
	    b.close();
	    file.close();
	}
	@FXML
	void Close() {
		panInfo.setVisible(false);
		panMemory.setVisible(false);
	}
	
	//Compiler
	@FXML
	void Compiler(ActionEvent e) throws IOException {
		
		textarea.setText("");
		AnalyseLexicale.text="";
		AnalyseSyntaxique.text="";
		AnalyseSemantique.text="";
		
		if(file == null) labelMessage.setText("Vous devez d'abord charger un fichier");
		else if (file != null) {
			labelMessage.setText("Compilation réussite");
			

			int c;
			String s = null;
			
			//mettre le fichier dans une chaine de caracteres
			String path = Controller.file.getAbsolutePath();
			try (BufferedReader r = new BufferedReader(new FileReader(path))) {
				while((c=r.read())!=-1){s = s+(char)c;}
			}
			//tableau des mots 
			String[] mot = s.split(" |\n");   
		
			AnalyseLexicale a1 = new AnalyseLexicale();
			a1.analyselexicale(mot);
			
			AnalyseSyntaxique a2 = new AnalyseSyntaxique();
			a2.analysesyntaxique(mot);
			
			AnalyseSemantique a3 = new AnalyseSemantique();
			a3.analysesemantique(mot);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
}
