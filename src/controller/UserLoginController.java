package controller;
import java.io.*;
import java.io.Serializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import controller.*;
import java.util.ArrayList;
/**
 * User controller, controls the mechanics
 * of all user interaction in the login page.
 * @author leonardoroman
 *
 */
public class UserLoginController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String curr_usr;
	private static ArrayList<User> users; 
	@FXML
	private TextField userName, password;
	@FXML
	private AnchorPane parentPane;
	@FXML
	private Label invalidError;
	private static Stage stage;
	/**
	 * 
	 * @param mainStage
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public void start(Stage mainStage) throws FileNotFoundException, ClassNotFoundException, IOException  {
		//createUsers();
		stage = mainStage;
		users = CreateNewUserController.readUsers();
		System.out.println("List of users in database:");
		for(int i = 0; i < users.size(); i++ ) {
			System.out.println(users.get(i).getUserName());
		}
	}
	/**
	 * To get the current list of users.
	 * @return
	 */
	public static ArrayList<User> userList(){
		return users;
	}

	/**
	 * To add a user locally into the arrayList.
	 * @param urs
	 * @throws IOException
	 */
	public static void addUser(User urs) throws IOException {
		users.add(urs);
		System.out.println(urs.getUserName()+" was added to database");

		for(int i = 0; i < users.size(); i++ ) {
			System.out.println(users.get(i).getUserName());
		}
	}
	/**
	 * Gets text enter by user in the useName text field
	 * @return
	 */
	@FXML
	public String getUserNameText() {
		return userName.getText();
	}
	/**
	 * Gets text enter by user in the passWord text field
	 * @return
	 */
	@FXML
	public String getPasswordText() {
		return password.getText();
	}
	/**
	 * 
	 * goToCreateNewUserPane takes the user to the 
	 * create new user window if user clicks the "create new user" link.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void goToCreateNewUserPane()throws IOException, ClassNotFoundException{
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
	 */
	public static void resetUser() {
		curr_usr = null;
	}
	/**
	 * 
	 * @return
	 */
	public static String getUser() {
		return curr_usr;
	}
	/**
	 * To get user 
	 * @param userName
	 * @return
	 */
	public static User getUser(String userName) {
		for(User i: users) {
			if(i.getUserName().equals(userName))
				return i;
		}
		return null;
	}
	/**
	 * 
	 * logInOnClick makes sure user name and pass matches
	 * the data in the DBMS and grant access if true by 
	 * letting user pass to the next UI page "user.fxl".
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void logInOnClick() throws IOException, ClassNotFoundException {
		AnchorPane pane;
		//Accessing DBMS
		for(int i = 0; i < users.size(); i++) {
			if(getUserNameText().equals(users.get(i).getUserName()) && getPasswordText().equals(users.get(i).getUserPass())) {
				if(getUserNameText().equals("admin")) {
					System.out.println("Hello admin");
					curr_usr = getUserNameText();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/admin.fxml"));
					parentPane = (AnchorPane)loader.load();
					AdminController userController = 
							loader.getController();
					userController.start(stage);
					Scene scene = new Scene(parentPane,700,700);
					stage.setScene(scene);
					stage.show();
					break;
				}else {
				curr_usr = getUserNameText();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/userView.fxml"));
				parentPane = (AnchorPane)loader.load();
				UserController userController = 
						loader.getController();
				userController.start(stage);
				Scene scene = new Scene(parentPane,550,400);
				stage.setScene(scene);
				stage.show();
				}
			}
			invalidError.setText("Invalid user name or password");
		}
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void createUsers() throws FileNotFoundException, ClassNotFoundException, IOException{
		users = new ArrayList<User>();
		User user1 = new User("leonardo","leo123");
		users.add(user1);
		User user2 = new User("admin","admin");
		users.add(user2);
		User user3 = new User("marcos","mar123");
		users.add(user3);
		User user4 = new User("rubens","rub123");
		users.add(user4);
		User user5 = new User("jael","jae123");
		users.add(user5);
		CreateNewUserController.writeUser(users);
	}
	 
}

