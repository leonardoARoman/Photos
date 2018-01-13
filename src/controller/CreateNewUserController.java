package controller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import controller.UserLoginController;

/**
 * 
 * CreateNewUser window.
 * @author leonardoroman
 *
 */
public class CreateNewUserController {
	@FXML
	AnchorPane newUserPane,pane;
	@FXML
	TextField fname,lname,uname,pass,rpass;
	@FXML
	Label error;
	private static Stage stage;
	private static String currName;
	private static ArrayList<User> users;
	public void start(Stage mainStage) throws FileNotFoundException, ClassNotFoundException, IOException {
		stage = mainStage;
		currName = UserLoginController.getUser();
		users = readUsers();
	}

	/**
	 * 
	 * createUser method prompts user for info inpu to 
	 * create a new user in program DBMS.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@FXML
	public void createUser() throws IOException, ClassNotFoundException {
		if(uname.getText().isEmpty()&&(pass.getText().isEmpty()||rpass.getText().isEmpty())) {
			error.setText("One or more fields are empty!");
			return;
		}
		
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUserName().equals(uname.getText())
					&&users.get(i).getUserPass().equals(pass.getText())) {
				error.setText("User already exist");
				return;
			}
		}
		
		if(pass.getText().equals(rpass.getText())) {
			System.out.println("Good password");
			User user = new User(uname.getText(),pass.getText());
			UserLoginController.addUser(user);
			users = UserLoginController.userList();
			writeUser(users);
			goBack();
		}
		error.setText("Error password does not match!");
	}
	/**
	 * 
	 * @param usr
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void writeUser(ArrayList<User> usr) throws FileNotFoundException, ClassNotFoundException, IOException {
		ArrayList<User> usrs = new ArrayList<User>();
		usrs = usr;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Users.bin"));
	
		System.out.println("List of users in database:");
		for(int i = 0; i < usrs.size(); i++ ) {
			System.out.println(usrs.get(i).getUserName());
		}
		 
		oos.writeObject(usrs);
	}
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<User> readUsers() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Users.bin"));
		ArrayList<User>  usrs = (ArrayList<User>)ois.readObject();
		//users = usrs;
		/*
		for(int i = 0; i < usrs.size(); i++ ) {
			System.out.println(usrs.get(i).getUserName());
		}
		 */
		return usrs;
	}
	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void goBack() throws IOException, ClassNotFoundException {
		if(currName == null || !currName.equals("admin")) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/userLogin.fxml"));
			newUserPane = (AnchorPane)loader.load();
			UserLoginController userController = 
					loader.getController();
			userController.start(stage);
			Scene scene = new Scene(newUserPane,700,700);
			stage.setScene(scene);
			stage.show();
			return;

		}else if(currName.equals("admin")) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/admin.fxml"));
			newUserPane = (AnchorPane)loader.load();
			AdminController userController = 
					loader.getController();
			userController.start(stage);
			Scene scene = new Scene(newUserPane,700,700);
			stage.setScene(scene);
			stage.show();
			return;
		}
	}
}

