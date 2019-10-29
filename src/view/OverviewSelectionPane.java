package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OverviewSelectionPane extends VBox
{
	private TextArea overview;
	private Button saveChanges;
	private Button secret;
	
	public OverviewSelectionPane()
	{
		overview = new TextArea();
		overview.setEditable(false);
		
		overview.setPrefSize(500, 600);

		saveChanges = new Button("Save Changes");
		secret = new Button("save courses");
		this.getChildren().addAll(overview, saveChanges, secret);
		
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.setPadding(new Insets(20));
		this.setVgrow(overview, Priority.ALWAYS);
	}
	public void setOverviewText(String text)
	{
		overview.setText(text);
	}
	public String getOverviewtext()
	{
		return overview.getText();
	}
	public void setSaveOverviewHandler(EventHandler<ActionEvent> e)
	{
		saveChanges.setOnAction(e);
	}
	public void setSecretVisibility(boolean b)
	{
		secret.setVisible(b);
	}
	public void setSecret(EventHandler<ActionEvent> e)
	{
		secret.setOnAction(e);
	}
}
