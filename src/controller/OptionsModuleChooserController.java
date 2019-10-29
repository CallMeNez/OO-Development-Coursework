package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import model.Course;
import model.Delivery;
import model.Module;
import model.Name;
import model.StudentProfile;
import view.CreateProfilePane;
import view.ModulesMenuBar;
import view.OptionsModuleChooserRootPane;
import view.OverviewSelectionPane;
import view.SelectModulesPane;



public class OptionsModuleChooserController {

	//fields to be used throughout the class
	private OptionsModuleChooserRootPane view;
	private StudentProfile model;
	private StudentProfile listView;
	private ModulesMenuBar mmb;
	private CreateProfilePane cpp;
	private SelectModulesPane smp;
	private OverviewSelectionPane osp;
	private int credit;

	public OptionsModuleChooserController(OptionsModuleChooserRootPane view, StudentProfile model) {
		//initialise model and view fields
		this.model = model;
		this.view = view;
		mmb = this.view.getModulesMenuBar();
		cpp = this.view.getCreateProfilePane();
		smp = this.view.getSelectModulesPane();
		osp = this.view.getOverviewSelectionPane();
		//this is here so I could write the file that contains the courses for the combo box within the event thread, all the functionality
		//for it is commented out down below if you want to change the courses available for some reason.
		osp.setSecretVisibility(false);

		cpp.populateComboBoxWithCourses(setupAndRetrieveCoursesByFile());
		//cpp.populateComboBoxWithCourses(setupAndRetrieveCourses("Courses.txt"));
		

		//attach event handlers to view using private helper method
		this.attachEventHandlers();
	}
	
	private void attachEventHandlers() {
		//menu
		mmb.setLoadHandler(new LoadHandler());
		mmb.setSaveHandler(new SaveHandler());
		mmb.setExitHandler(new ExitHandler());
		mmb.setAboutHandler(new AboutHandler());
		
		//CPP
		cpp.setCreateProfileHandler(new CreateProfileHandler());
		cpp.setLoadProfileHandler(new LoadHandler());
		
		//SMP
		smp.setTermOneAddHandler(new TermOneAddHandler());
		smp.setTermTwoAddHandler(new TermTwoAddHandler());
		smp.setTermOneRemHandler(new TermOneRemHandler());
		smp.setTermTwoRemHandler(new TermTwoRemHandler());
		smp.setResetHandler(new ResetHandler());
		smp.setsubmitHandler(new SubmitHandler());
		
		//OSP
		osp.setSaveOverviewHandler(new SaveOverviewHandler());
		//osp.setSecret(new SecretHandler());
	}
	//credit check for SMP
		public Boolean creditCheckTerm(List<Module> modules)
		{
			credit = listView.getAllSelectedModules().stream()
					.filter(x -> x.isMandatory() && x.getRunPlan() == Delivery.YEAR_LONG)
					.map(x -> x.getCredits()/2).collect(Collectors.summingInt(Integer::intValue));
			for (Module m : modules)
			{
				credit += m.getCredits();
			}
			if(credit <= 60)
			{
				return true;
			}
			else return false;
		}
		public Boolean creditCheckYear(List<Module> modules)
		{
			credit = 0;
			for (Module m : modules)
			{
				credit += m.getCredits();
			}
			if(credit == 120)
			{
				return true;
			}
			else return false;
		}
		public void creditReset()
		{
			List<Module> reset = listView.getAllSelectedModules().stream()
					.filter(x -> x.getRunPlan() == Delivery.TERM_1)
					.collect(Collectors.toList());
			creditCheckTerm(reset);
			smp.setTermOneCredit(credit);
			reset = listView.getAllSelectedModules().stream()
					.filter(x -> x.getRunPlan() == Delivery.TERM_2)
					.collect(Collectors.toList());
			creditCheckTerm(reset);
			smp.setTermTwoCredit(credit);
				
		}
	//populating List Views in SMP
	
	private void uTOneCourseModules()
	{
		List<Module> modules = listView.getCourseOfStudy().getAllModulesOnCourse().stream()
		.filter(e -> (!listView.getAllSelectedModules().contains(e)) && (e.getRunPlan() == Delivery.TERM_1))
		.collect(Collectors.toCollection(FXCollections::observableArrayList));
		smp.populateUTOneWithModules((ObservableList<Module>) modules);
	}
	private void sTOneCourseModules()
	{
		List<Module> modules = FXCollections.observableArrayList();
		modules = listView.getCourseOfStudy().getAllModulesOnCourse().stream()
		.filter(e -> (listView.getAllSelectedModules().contains(e)) && (e.getRunPlan() == Delivery.TERM_1))
		.collect(Collectors.toCollection(FXCollections::observableArrayList));
		smp.populateSTOneWithModules((ObservableList<Module>) modules);
		
	}
	private void uTTwoCourseModules()
	{
		List<Module> modules = FXCollections.observableArrayList();
		modules = listView.getCourseOfStudy().getAllModulesOnCourse().stream()
		.filter(e -> (!listView.getAllSelectedModules().contains(e)) && (e.getRunPlan() == Delivery.TERM_2))
		.collect(Collectors.toCollection(FXCollections::observableArrayList));
		smp.populateUTTwoWithModules((ObservableList<Module>) modules);
	}
	private void sTTwoCourseModules()
	{
		List<Module> modules = FXCollections.observableArrayList();
		modules = listView.getCourseOfStudy().getAllModulesOnCourse().stream()
		.filter(e -> (listView.getAllSelectedModules().contains(e)) && (e.getRunPlan() == Delivery.TERM_2))
		.collect(Collectors.toCollection(FXCollections::observableArrayList));
		smp.populateSTTwoWithModules((ObservableList<Module>) modules);
	}
	private void sYearCourseModules()
	{
		List<Module> modules = FXCollections.observableArrayList();
		modules = listView.getCourseOfStudy().getAllModulesOnCourse().stream()
		.filter(e -> (listView.getAllSelectedModules().contains(e)) && (e.getRunPlan() == Delivery.YEAR_LONG))
		.collect(Collectors.toCollection(FXCollections::observableArrayList));
		smp.populateSYearWithModules((ObservableList<Module>) modules);
	}
	private void clearListViews()
	{
		smp.clearSelectedTermOneModules();
		smp.clearSelectedTermTwoModules();
		smp.clearSelectedYearModules();
		smp.clearUnselectedTermOneModules();
		smp.clearUnselectedTermTwoModules();
	}
	
	private void populateListViews()
	{
		clearListViews();
		uTOneCourseModules();
		uTTwoCourseModules();
		sTOneCourseModules();
		sTTwoCourseModules();
		sYearCourseModules();
	}
	private void buildOverview()
	{
		String details ="";
		details += model.getStudentName().getFullName() + " (" + model.getPnumber() + ") Selected Modules Overview";
		
		details += "\n"+model.getEmail()+"\n\n";
		
		details += "Selected " + model.getCourseOfStudy().getCourseName() + " Modules: \n\n";
		for (Module m : model.getAllSelectedModules())
		{
			if (m.getRunPlan() == Delivery.YEAR_LONG)
			{
				details += "Module: " + m.getModuleName() + " - " + m.getModuleCode() + "\n";
				details += "\t\t Credits: " +m.getCredits() +"\n\n";
			}
		}
		for (Module m : model.getAllSelectedModules())
		{
			if (m.getRunPlan() == Delivery.TERM_1)
			{
				details += "Module: " + m.getModuleName() + " - " + m.getModuleCode() + "\n";
				details += "\t\t Credits: " +m.getCredits() +"\n\n";
			}
		}
		for (Module m : model.getAllSelectedModules())
		{
			if (m.getRunPlan() == Delivery.TERM_2)
			{
				details += "Module: " + m.getModuleName() + " - " + m.getModuleCode() + "\n";
				details += "\t\t Credits: " +m.getCredits() +"\n\n";
			}
		}
		osp.setOverviewText(details);
	}
	
	//default stuff that needs replacing with an I/O
	private Course[] setupAndRetrieveCourses() 
	{
		
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Delivery.TERM_1);
		Module imat3451 = new Module("IMAT3451", "Computing Project", 30, true, Delivery.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Delivery.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Delivery.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Delivery.TERM_1);
		Module ctec3426 = new Module("CTEC3426", "Telematics", 15, false, Delivery.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Delivery.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Delivery.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Delivery.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Delivery.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Delivery.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Delivery.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Delivery.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Delivery.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Popular Technology Ethics", 15, false, Delivery.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Delivery.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Delivery.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Delivery.TERM_2);

		Course compSci = new Course("Computer Science");
		compSci.addModuleToCourse(imat3423);
		compSci.addModuleToCourse(imat3451);
		compSci.addModuleToCourse(ctec3902_CompSci);
		compSci.addModuleToCourse(ctec3110);
		compSci.addModuleToCourse(ctec3426);
		compSci.addModuleToCourse(ctec3605);
		compSci.addModuleToCourse(ctec3606);
		compSci.addModuleToCourse(ctec3410);
		compSci.addModuleToCourse(ctec3904);
		compSci.addModuleToCourse(ctec3905);
		compSci.addModuleToCourse(ctec3906);
		compSci.addModuleToCourse(imat3410);
		compSci.addModuleToCourse(imat3406);
		compSci.addModuleToCourse(imat3611);
		compSci.addModuleToCourse(imat3613);
		compSci.addModuleToCourse(imat3614);
		compSci.addModuleToCourse(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModuleToCourse(imat3423);
		softEng.addModuleToCourse(imat3451);
		softEng.addModuleToCourse(ctec3902_SoftEng);
		softEng.addModuleToCourse(ctec3110);
		softEng.addModuleToCourse(ctec3426);
		softEng.addModuleToCourse(ctec3605);
		softEng.addModuleToCourse(ctec3606);
		softEng.addModuleToCourse(ctec3410);
		softEng.addModuleToCourse(ctec3904);
		softEng.addModuleToCourse(ctec3905);
		softEng.addModuleToCourse(ctec3906);
		softEng.addModuleToCourse(imat3410);
		softEng.addModuleToCourse(imat3406);
		softEng.addModuleToCourse(imat3611);
		softEng.addModuleToCourse(imat3613);
		softEng.addModuleToCourse(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}
	//I/O stuff I'm replacing ^ with.
	private Course[] setupAndRetrieveCoursesByFile()
	{
			File file = new File("Courses.txt");
			if (file != null) 
			{
				try 
				{
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					Course[] courses = (Course[]) ois.readObject();
					ois.close();
					return courses;
				} 
				catch (IOException | ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
		
		
			}
			return null;
	}
	//SecretHandler is for the invisible button in the overview pane that I used to save the courses to a file to read in. 
	//Comment the line up top and make the button visible if you want to use it to change the courses saved in the text file or something to test if
	//you want. It's obviously not part of the standard GUI but there's no way to access it without changing the source code. Just left it
	//in for convenience.
	private class SecretHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
				Course[]courses = setupAndRetrieveCourses();
				File file = new File("Courses.txt");
				try
				{
					FileOutputStream fos = new FileOutputStream(file);		
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(courses);
					oos.close();
				}
				catch (IOException fnfe)
				{
					fnfe.printStackTrace();
				}
		}

	}
	//Private Inner Class Event Handlers.
	
	//menu bar EHs
private	class LoadHandler implements EventHandler<ActionEvent> 
	{
		
		public void handle(ActionEvent e) 
		{
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(null);
			if (file != null) 
			{
				try 
				{
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					model = (StudentProfile) ois.readObject();
					listView = model;
					ois.close();
			
					populateListViews();
					creditReset();
					List<Module> modules = listView.getAllSelectedModules().stream().collect(Collectors.toList());
					if(creditCheckYear(modules))
					{
						buildOverview();
						cpp.setCourse(model.getCourseOfStudy());
						cpp.setPNo(model.getPnumber());
						cpp.setFN(model.getStudentName().getFirstName());
						cpp.setSN(model.getStudentName().getFamilyName());
						cpp.setEM(model.getEmail());
						cpp.setDate(model.getSubmissionDate());
					}
				}
				catch (IOException | ClassNotFoundException e1)
				{
					Alert loadError = new Alert(AlertType.INFORMATION);
					loadError.setTitle("Load Error");
					loadError.setHeaderText(e1.getClass().toString());
					loadError.setContentText("Error loading file.");
					loadError.showAndWait();
				}
			}
		}
	}
	
private class SaveHandler implements EventHandler<ActionEvent>

	{
		public void handle(ActionEvent e)
		{
			FileChooser fc = new FileChooser();
			File f = fc.showSaveDialog(null);
			if(f != null)
			{
				try
				{
					FileOutputStream fos = new FileOutputStream(f);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					model = listView;
					oos.writeObject(model);
					oos.close();
				}
				catch (IOException fnfe)
				{
					fnfe.printStackTrace();
					System.out.println("Error Saving Student Profile");
					Alert saveError = new Alert(AlertType.INFORMATION);
					saveError.setTitle("Save Error");
					saveError.setHeaderText("IOException");
					saveError.setContentText("Error saving file.");
					saveError.showAndWait();
				}
			}
		}
	}

	private class ExitHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
		System.exit(0);
		}
	}
	
	private class AboutHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
		Alert info = new Alert(AlertType.INFORMATION);
		info.setTitle("About");
		info.setHeaderText("Super Useful Info Everyone Should Read:");
		info.setContentText("This is the Options Module Chooser Coursework.");
		info.showAndWait();
		}
	}
	//CreateProfilePane EHs
	private class CreateProfileHandler implements EventHandler<ActionEvent>
	{
	
		public void handle(ActionEvent e)
		{			
			if
			(cpp.getCourse() == null
			||cpp.getPNo().length() !=9 
			||!cpp.getPNo().substring(0, 1).equalsIgnoreCase("p")
			|| cpp.getFN() == null
			|| cpp.getSN() == null
			|| cpp.getEM() == null
			|| !cpp.getEM().contains("@")
			|| cpp.getDate() == null
			)
			{
				Alert required = new Alert(AlertType.ERROR);
				required.setTitle("Required Details Missing");
				required.setHeaderText("Fields Not Filled In Correctly");
				required.setContentText("All fields must be filled in correctly to create a profile. A valid P-Number, name and "
						+ "email must be entered.");
				required.showAndWait();
			}
			else
			{
			model.setCourseOfStudy(cpp.getCourse());
			model.setPnumber(cpp.getPNo());
			model.setStudentName(new Name(cpp.getFN(), cpp.getSN()));
			model.setEmail(cpp.getEM());
			model.setSubmissionDate(cpp.getDate());
			model.clearAllSelectedModules();
			List<Module> y = model.getCourseOfStudy().getAllModulesOnCourse().stream()
			.filter(x-> x.isMandatory())
			.collect(Collectors.toList());
			for(Module m : y)
			{
				model.addToSelectedModules(m);
			}
			listView = model;
			populateListViews();
			creditReset();
			}
		}
	}
	//SelectModulePane EHs
	private class TermOneAddHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			List<Module> modules = FXCollections.observableArrayList();
			List<Module> selected = listView.getAllSelectedModules().stream().filter(x -> x.getRunPlan() == Delivery.TERM_1).collect(Collectors.toCollection(FXCollections::observableArrayList));
			modules.addAll(selected);
			modules.addAll(smp.getUnselectedTermOneModules());
			if(creditCheckTerm(modules))
			{	
			for(Module m : smp.getUnselectedTermOneModules())
			{
				listView.addToSelectedModules(m);
			}
			smp.setTermOneCredit(credit);
			clearListViews();
			populateListViews();
			}
			else
			{
				Alert credits = new Alert(AlertType.INFORMATION);
				credits.setTitle("Too Many Credits");
				credits.setHeaderText("You have selected too many modules.");
				credits.setContentText("Only 60 module credits are allowed per term, with 15 being allocated to each term from IMAT3451.");
				credits.showAndWait();
				smp.clearSelection();
				sTTwoCourseModules();
			}
		}
	}
	private class TermTwoAddHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			List<Module> modules = FXCollections.observableArrayList();
			List<Module> selected = listView.getAllSelectedModules().stream().filter(x -> x.getRunPlan() == Delivery.TERM_2).collect(Collectors.toCollection(FXCollections::observableArrayList));
			modules.addAll(selected);
			modules.addAll(smp.getUnselectedTermTwoModules());
			if(creditCheckTerm(modules))
			{
			for(Module m : smp.getUnselectedTermTwoModules())
			{
				listView.addToSelectedModules(m);
			}
			smp.setTermTwoCredit(credit);
			clearListViews();
			populateListViews();
			}
			else
			{
				Alert credits = new Alert(AlertType.INFORMATION);
				credits.setTitle("Too Many Credits");
				credits.setHeaderText("You have selected too many modules.");
				credits.setContentText("Only 60 module credits are allowed per term, with 15 being allocated to each term from IMAT3451.");
				credits.showAndWait();
				smp.clearSelection();
				sTTwoCourseModules();
			}
		}
	}
	private class TermOneRemHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			creditReset();
			Set<Module> modules = listView.getAllSelectedModules().stream().collect(Collectors.toSet());
			for(Module m : smp.getSelectedTermOneModules())
			{
				if(m.isMandatory())
				{
					Alert info = new Alert(AlertType.INFORMATION);
					info.setTitle("Mandatory Module");
					info.setHeaderText("Cannot Remove");
					info.setContentText(m.getModuleName()+" is a Mandatory Module, and cannot be removed.");
					info.showAndWait();
				}
				else
				{
				modules.remove(m);
				}
			}
			listView.clearAllSelectedModules();
			for (Module m: modules)
			{
				listView.addToSelectedModules(m);
			}
			populateListViews();
			creditReset();
		}
	}
	private class TermTwoRemHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			creditReset();
			Set<Module> modules = listView.getAllSelectedModules().stream().collect(Collectors.toSet());
			for(Module m : smp.getSelectedTermTwoModules())
			{
				if(m.isMandatory())
				{
					Alert info = new Alert(AlertType.INFORMATION);
					info.setTitle("Mandatory Module");
					info.setHeaderText("Cannot Remove");
					info.setContentText(m.getModuleName()+" is a Mandatory Module, and cannot be removed.");
					info.showAndWait();
				}
				else
				{
				modules.remove(m);
				}
			}
			listView.clearAllSelectedModules();
			for (Module m: modules)
			{
				listView.addToSelectedModules(m);
			}
			populateListViews();
			creditReset();
		}
	}
	private class ResetHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			List<Module> modules = listView.getAllSelectedModules().stream().filter(x -> x.isMandatory()).collect(Collectors.toCollection(FXCollections::observableArrayList));
			listView.clearAllSelectedModules();
			for(Module m: modules)
			{
				listView.addToSelectedModules(m);
			}
			populateListViews();
			creditReset();
		}
	}
	private class SubmitHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			if(creditCheckYear((List<Module>) listView.getAllSelectedModules().stream().collect(Collectors.toList())))
			{
			model = listView;
			buildOverview();
			}
			else
			{
				Alert notEnough = new Alert(AlertType.INFORMATION);
				notEnough.setTitle("Unspent Credits");
				notEnough.setHeaderText("Choose More Modules");
				notEnough.setContentText("You must have 60 credits spent on each term.\nCredits spent: " + credit);
				notEnough.showAndWait();
			}
			}
	}
	private class SaveOverviewHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			FileChooser fc = new FileChooser();
			File f = fc.showSaveDialog(null);
			if(f != null)
			{
				try
				{
					FileWriter fw = new FileWriter(f);
					fw.write(osp.getOverviewtext());
					fw.close();
				}
				catch (IOException fnfe)
				{
					fnfe.printStackTrace();
					System.out.println("Error Saving Profile Overview");
					Alert saveError = new Alert(AlertType.INFORMATION);
					saveError.setTitle("Save Error");
					saveError.setHeaderText("IOException");
					saveError.setContentText("Error saving file.");
					saveError.showAndWait();
				}
			}
		}
	}
}