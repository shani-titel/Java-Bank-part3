package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessageBox extends Application{

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		
		Text msg = new Text("ERROR!");
		msg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(msg, 0, 0, 3, 1);
		
		//build scene
		Scene scene = new Scene(grid, 350, 200);
        
        primaryStage.setTitle("Message");
        primaryStage.setScene(scene);
        primaryStage.show();		
	}
	
	public void start(Stage primaryStage, String msg) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		
		Text txt = new Text(msg);
		txt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(txt, 0, 0, 3, 1);
		
		//build scene
		Scene scene = new Scene(grid, 450, 200);
        
        primaryStage.setTitle("Message");
        primaryStage.setScene(scene);
        primaryStage.show();		
		
	}

}
