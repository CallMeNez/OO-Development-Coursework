package view;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.DatePicker;
import model.Course;

public class CreateProfilePane extends GridPane
{
	private ComboBox<Course> course;
	private TextField inpPNo;
	private TextField inpFN;
	private TextField inpSN;
	private TextField inpEM;
	private DatePicker inpDate;
	private Button createProfile;
	private Button loadProfile;
	
	CreateProfilePane()
	{
	Label selectCourse = new Label("Select Course:");
	Label inPNo = new Label("Input P Number:");
	Label inFN = new Label("Input First Name:");
	Label inSN = new Label("Input Surname:");
	Label inEM = new Label("Input Email:");
	Label inDate = new Label("Input Date:");
	course = new ComboBox<Course>();
	inpPNo = new TextField();
	inpFN = new TextField();
	inpSN = new TextField();
	inpEM = new TextField();
	inpDate = new DatePicker();
	createProfile = new Button("Create Profile");
	loadProfile = new Button("Load Profile");
	
	this.getChildren().addAll
	(
		selectCourse, 
		inPNo,
		inFN,
		inSN,
		inEM,
		inDate,
		course,
		inpPNo,
		inpFN,
		inpSN,
		inpEM,
		inpDate,
		createProfile,
		loadProfile
	);
	
	this.setConstraints(selectCourse, 0, 0);
	this.setConstraints(inPNo, 0, 1);
	this.setConstraints(inFN, 0, 2);
	this.setConstraints(inSN, 0, 3);
	this.setConstraints(inEM, 0, 4);
	this.setConstraints(inDate, 0, 5);
	this.setConstraints(course, 1, 0, 2, 1);
	this.setConstraints(inpPNo, 1, 1, 2, 1);
	this.setConstraints(inpFN, 1, 2, 2, 1);
	this.setConstraints(inpSN, 1, 3, 2, 1);
	this.setConstraints(inpEM, 1, 4, 2, 1);
	this.setConstraints(inpDate, 1, 5, 2, 1);
	this.setConstraints(createProfile, 1, 6);
	this.setConstraints(loadProfile, 2, 6);
	
	this.setPrefSize(600, 550);
	this.setAlignment(Pos.CENTER);
	this.setHgap(20);
	this.setVgap(20);
	this.setPadding(new Insets(100));
	for(Node c : this.getChildren())
	{
	this.setHgrow(c, Priority.ALWAYS);
	this.setVgrow(c, Priority.ALWAYS);
	}
	}
	
	public void populateComboBoxWithCourses(Course[] courses)
	{
		course.setItems(FXCollections.observableArrayList(courses));
	}
	//gets
	public Course getCourse()
	{
		return course.getSelectionModel().getSelectedItem();
	}
	public String getPNo()
	{
		return inpPNo.getText();
	}
	public String getFN()
	{
		return inpFN.getText();
	}
	public String getSN()
	{
		return inpSN.getText();
	}
	public String getEM()
	{
		return inpEM.getText();
	}
	public LocalDate getDate()
	{
		return inpDate.getValue();
	}
	//sets (for loading a profile)
	public void setCourse(Course c)
	{
		course.setValue(c);
	}
	public void setPNo(String p)
	{
		inpPNo.setText(p);
	}
	public void setFN(String f)
	{
		inpFN.setText(f);
	}
	public void setSN(String s)
	{
		inpSN.setText(s);
	}
	public void setEM(String e)
	{
		inpEM.setText(e);
	}
	public void setDate(LocalDate d)
	{
		inpDate.setValue(d);
	}
	
	//EHs
	public void setCreateProfileHandler(EventHandler<ActionEvent> e)
	{
		createProfile.setOnAction(e);
	}
	public void setLoadProfileHandler(EventHandler<ActionEvent> e)
	{
		loadProfile.setOnAction(e);
	}
}
