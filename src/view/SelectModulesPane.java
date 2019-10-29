package view;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;

public class SelectModulesPane extends HBox
{
	private ListView<Module> unTerm1;
	private ListView<Module> unTerm2;
	private ListView<Module> seYearLong;
	private ListView<Module> seTerm1;
	private ListView<Module> seTerm2;
	private Button t1Add;
	private Button t1Rem;
	private Button t2Add;
	private Button t2Rem;
	private Button reset;
	private Button submit;
	private TextField t1Creds;
	private TextField t2Creds;
	
	public SelectModulesPane()
	{		
		VBox leftHalf = new VBox();
		
			VBox unselectedTermOne = new VBox();
			Label uTerm1 = new Label("Unselected Term One Modules");
			unTerm1 = new ListView<Module>();
			unTerm1.setEditable(false);
			unTerm1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			unselectedTermOne.getChildren().addAll(uTerm1, unTerm1);
			
			unselectedTermOne.setSpacing(10);
		
			HBox termOne = new HBox();
			Label term1 = new Label("Term One:");
			t1Add = new Button("Add");
			t1Rem = new Button("Remove");
			termOne.getChildren().addAll(term1, t1Add, t1Rem);
			
			termOne.setSpacing(20);
			termOne.setAlignment(Pos.CENTER);
		
			VBox unselectedTermTwo = new VBox();
			Label uTerm2 = new Label("Unselected Term Two Modules");
			unTerm2 = new ListView<Module>();
			unTerm2.setEditable(false);
			unTerm2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			unselectedTermTwo.getChildren().addAll(uTerm2, unTerm2);
			
			unselectedTermTwo.setSpacing(10);
		
			HBox termTwo = new HBox();
			Label term2 = new Label("Term Two:");
			t2Add = new Button("Add");
			t2Rem = new Button("Remove");
			termTwo.getChildren().addAll(term2, t2Add, t2Rem);
			
			termTwo.setSpacing(20);
			termTwo.setAlignment(Pos.CENTER);
		
			HBox termOneCredits = new HBox();
			Label credits = new Label("Current Term One Credits:");
			t1Creds = new TextField();
			t1Creds.setEditable(false);
			t1Creds.setPrefWidth(50);
			termOneCredits.getChildren().addAll(credits, t1Creds);
			
			termOneCredits.setSpacing(20);
			
			reset = new Button("Reset");
						
		leftHalf.getChildren().addAll(unselectedTermOne, termOne, unselectedTermTwo, termTwo, termOneCredits, reset);
		
		leftHalf.setSpacing(30);
		leftHalf.setAlignment(Pos.CENTER_RIGHT);
		
		VBox rightHalf = new VBox();
		
			VBox selectedYearLong = new VBox();
			Label sYearLong = new Label("Selected Year Long Modules");
			seYearLong = new ListView<Module>();
			seYearLong.setEditable(false);
			selectedYearLong.getChildren().addAll(sYearLong, seYearLong);
			
			selectedYearLong.setSpacing(10);
		
			VBox selectedTermOne = new VBox();
			Label sTerm1 = new Label("Selected Term One Modules");
			seTerm1 = new ListView<Module>();
			seTerm1.setEditable(false);
			seTerm1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			selectedTermOne.getChildren().addAll(sTerm1, seTerm1);
			
			selectedTermOne.setSpacing(10);
		
			VBox selectedTermTwo = new VBox();
			Label sTerm2 = new Label("Selected Term Two Modules");
			seTerm2 = new ListView<Module>();
			seTerm2.setEditable(false);
			seTerm2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			selectedTermTwo.getChildren().addAll(sTerm2, seTerm2);
			selectedTermTwo.setSpacing(10);
		
			HBox termTwoCredits = new HBox();
			Label credits2 = new Label("Current Term Two Credits:");
			t2Creds = new TextField();
			t2Creds.setEditable(false);
			t2Creds.setPrefWidth(50);
			termTwoCredits.getChildren().addAll(credits2, t2Creds);
			
			termTwoCredits.setSpacing(20);
		
			submit = new Button("Submit");
			
			
		rightHalf.getChildren().addAll(selectedYearLong, selectedTermOne, selectedTermTwo, termTwoCredits, submit);
		
		rightHalf.setSpacing(30);
		
		this.getChildren().addAll(leftHalf, rightHalf);
		
		this.setPadding(new Insets(30));
		this.setSpacing(20);
		this.setHgrow(leftHalf, Priority.ALWAYS);
		this.setHgrow(rightHalf, Priority.ALWAYS);
	}
	//GETS
	public ObservableList<Module> getUnselectedTermOneModules()
	{
		return unTerm1.getSelectionModel().getSelectedItems();
	}
	public ObservableList<Module> getUnselectedTermTwoModules()
	{
		return unTerm2.getSelectionModel().getSelectedItems();
	}
	public ObservableList<Module> getSelectedTermOneModules()
	{
		return seTerm1.getSelectionModel().getSelectedItems();
	}
	public ObservableList<Module> getSelectedTermTwoModules()
	{
		return seTerm2.getSelectionModel().getSelectedItems();
	}
	public ObservableList<Module> getYearLongModules()
	{
		return seYearLong.getItems();
	}
	
	//SETS
	public void setTermOneCredit(int i) {
		t1Creds.textProperty().setValue(""+i);
	}
	public void setTermTwoCredit(int i) {
		t2Creds.textProperty().setValue(""+i);;
	}
	
	//CLEARS
	public void clearUnselectedTermOneModules()
	{
		unTerm1.getItems().clear();
	}
	public void clearUnselectedTermTwoModules()
	{
		unTerm2.getItems().clear();
	}
	public void clearSelectedTermOneModules()
	{
		seTerm1.getItems().clear();
	}
	public void clearSelectedTermTwoModules()
	{
		seTerm2.getItems().clear();
	}
	public void clearSelectedYearModules()
	{
		seYearLong.getItems().clear();
	}
	public void clearSelection()
	{
		unTerm1.getSelectionModel().clearSelection();
		unTerm2.getSelectionModel().clearSelection();
		seTerm1.getSelectionModel().clearSelection();
		seTerm2.getSelectionModel().clearSelection();
		seYearLong.getSelectionModel().clearSelection();
	}
	
	//POPULATING LIST VIEWS
	public void populateUTOneWithModules(ObservableList<Module> m) {
		unTerm1.setItems(m);
	}
	public void populateUTTwoWithModules(ObservableList<Module> m) {
		unTerm2.setItems(m);
	}
	public void populateSTOneWithModules(ObservableList<Module> m) {
		seTerm1.setItems(m);
	}
	public void populateSTTwoWithModules(ObservableList<Module> m) {
		seTerm2.setItems(m);
	}
	public void populateSYearWithModules(ObservableList<Module> m) {
		seYearLong.setItems(m);
	}
	//EHs
	public void setTermOneAddHandler(EventHandler<ActionEvent> e) {
		t1Add.setOnAction(e);
	}
	public void setTermOneRemHandler(EventHandler<ActionEvent> e) {
		t1Rem.setOnAction(e);
	}
	public void setTermTwoAddHandler(EventHandler<ActionEvent> e) {
		t2Add.setOnAction(e);
	}
	public void setTermTwoRemHandler(EventHandler<ActionEvent> e) {
		t2Rem.setOnAction(e);
	}
	public void setResetHandler(EventHandler<ActionEvent> e) {
		reset.setOnAction(e);
	}
	public void setsubmitHandler(EventHandler<ActionEvent> e) {
		submit.setOnAction(e);
	}
}
