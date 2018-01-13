//must updated
package controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import controller.CreateNewUserController;
/**
 * 
 * Administrator window use only by admin user.
 * @author leonardoroman
 *
 */
public class AdminController {
	@FXML
	private AnchorPane parentPane;
	@FXML
	Button newUsr,deleteUsr,dsplUrs;
	@FXML
	ListView<String> userList;
	private static Stage stage;
	private ObservableList<String> obsList;
	/**
	 * 
	 * @param mainStage
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void start(Stage mainStage) throws FileNotFoundException, ClassNotFoundException, IOException  {
		stage = mainStage;
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<User> usrs = CreateNewUserController.readUsers();
		for(int i = 0; i < usrs.size(); i++) {
			String curr_usr = usrs.get(i).getUserName()+" "+usrs.get(i).getUserPass();
			users.add(curr_usr);
			obsList = FXCollections.observableArrayList(users);
		}
		//display everything in the observable list obj.
		userList.setItems(obsList);
	}
	/**
	 * 
	 * Links admin to create new user window
	 * to Create a new user method.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void createNewUser()throws IOException, ClassNotFoundException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/createNewUser.fxml"));
		parentPane = (AnchorPane)loader.load();
		CreateNewUserController userController = 
				loader.getController();
		userController.start(stage);
		Scene scene = new Scene(parentPane,700,700);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * 
	 * logOut method logs out admin from administrator window.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void logOut()throws IOException, ClassNotFoundException{
		UserLoginController.resetUser();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/userLogin.fxml"));
		parentPane = (AnchorPane)loader.load();
		UserLoginController userController = 
				loader.getController();
		userController.start(stage);
		Scene scene = new Scene(parentPane,700,700);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * 
	 * displayUsers method displays a list of all users in the program database system.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	@FXML
	public void displayUsers() throws FileNotFoundException, ClassNotFoundException, IOException {
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<User> usrs = CreateNewUserController.readUsers();
		for(int i = 0; i < usrs.size(); i++) {
			String curr_usr = usrs.get(i).getUserName()+" "+usrs.get(i).getUserPass();
			users.add(curr_usr);
			obsList = FXCollections.observableArrayList(users);
		}
		//display everything in the observable list obj.
		userList.setItems(obsList);
	}

	/**
	 * 
	 * deleteUser method deletes the current selected user 
	 * in the viewList display.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	@FXML
	public void deleteUser() throws FileNotFoundException, ClassNotFoundException, IOException {

		ArrayList<User> usrs = CreateNewUserController.readUsers();//Get current user list in data base.
		int index = userList.getSelectionModel().getSelectedIndex();
		if(confirmChanges()) {
			if(index == obsList.size()-1){
				obsList.remove(index);//Remove from observable list.
				usrs.remove(index);//Remove from array of users locally.
				CreateNewUserController.writeUser(usrs);//update data base.

			}else{
				obsList.remove(index);//Remove from observable list.
				usrs.remove(index);//Remove from array of users locally.
				CreateNewUserController.writeUser(usrs);//update data base.
			}
		}
		return;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean confirmChanges() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} 
		return false;
	}
}

