package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

//You may change this class to extend another type if you wish
public class OptionsModuleChooserRootPane extends BorderPane 
{
	private ModulesMenuBar mmb;
	private CreateProfilePane cpp;
	private SelectModulesPane smp;
	private OverviewSelectionPane osp;

	public OptionsModuleChooserRootPane()
	{
		mmb = new ModulesMenuBar();
		cpp = new CreateProfilePane();
		smp = new SelectModulesPane();
		osp = new OverviewSelectionPane();
		
		TabPane base = new TabPane();
		
		Tab[] tabs = new Tab[3];
		tabs[0] = new Tab("Create Profile", cpp);
		tabs[1] = new Tab("Select Modules", smp);
		tabs[2] = new Tab("Overview Selection", osp);
		base.getTabs().addAll(tabs);
		base.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		this.setTop(mmb);
		this.setCenter(base);
		
		this.setPrefSize(700, 700);
	}

	public ModulesMenuBar getModulesMenuBar() {
		return mmb;
	}

	public CreateProfilePane getCreateProfilePane() {
		// TODO Auto-generated method stub
		return cpp;
	}

	public SelectModulesPane getSelectModulesPane() {
		// TODO Auto-generated method stub
		return smp;
	}

	public OverviewSelectionPane getOverviewSelectionPane() {
		// TODO Auto-generated method stub
		return osp;
	}
}