package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import controller.UserLoginController;
/**
 * 
 * Main class 
 * @author leonardoroman
 *
 */
public class Photos extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/userLogin.fxml"));
		AnchorPane root = (AnchorPane)loader.load();


		UserLoginController userController = 
				loader.getController();
		userController.start(primaryStage);

		//Runnable runThis = ()->System.out.println("Hello, I am a labda expression :)"); runThis.run();
		
		Scene scene = new Scene(root,610,500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}