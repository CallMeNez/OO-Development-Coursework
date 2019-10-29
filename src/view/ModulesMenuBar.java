package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class ModulesMenuBar extends MenuBar
{
	MenuItem loadStudentData;
	MenuItem saveStudentData;
	MenuItem exit;
	MenuItem about;
	
	public ModulesMenuBar()
	{
		Menu menu = new Menu("File");
		loadStudentData = new MenuItem("Load Student Data");
		loadStudentData.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
		
		saveStudentData = new MenuItem("Save Student Data");
		saveStudentData.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
		
		exit = new MenuItem("Exit");
		exit.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		
		menu.getItems().addAll(loadStudentData, saveStudentData, exit);
		this.getMenus().add(menu);
		
		menu = new Menu("Help");
		about = new MenuItem("About");
		
		menu.getItems().add(about);
		this.getMenus().add(menu);
	}

	public void setLoadHandler(EventHandler<ActionEvent> e)
	{
		loadStudentData.setOnAction(e);
	}
	public void setSaveHandler(EventHandler<ActionEvent> e)
	{
		saveStudentData.setOnAction(e);
	}
	public void setExitHandler(EventHandler<ActionEvent> e)
	{
		exit.setOnAction(e);
	}
	public void setAboutHandler(EventHandler<ActionEvent> e)
	{
		about.setOnAction(e);
	}
}