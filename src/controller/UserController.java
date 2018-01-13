package controller;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javafx.collections.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import controller.CreateNewUserController;

/**
 * 
 * UserController controls all activity inside the users 
 * window. This is controll by the user.
 * @author leonardoroman
 *
 */
public class UserController {	
	@FXML
	AnchorPane userPane;
	@FXML
	private Label displayUser,displayDateTime;
	@FXML
	private ListView<Album> albumListView;
	@FXML
	private ListView<String> taglistview;
	@FXML 
	ImageView imageView,filteredImage;
	@FXML
	private ListView<ImageView> imageListView;
	@FXML
	private MenuItem flipVertical,flipHorizontal,flipDiagonal;

	private static Stage stage;
	private static int counter;
	private ObservableList<ImageView> imageObsList;
	private ObservableList<Album> obsList;
	private ArrayList<ImageView> imageList;
	private ArrayList<Image> photos;
	private ArrayList<User> users;
	private User user;
	/**
	 * 
	 * @param mainStage
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void start(Stage mainStage) throws FileNotFoundException, ClassNotFoundException, IOException  {
		//createTempAlbums();
		stage = mainStage;
		displayUserInfo();
	}
	/**
	 * 
	 * For debugging to see if right user is in profile
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	@FXML
	public void displayUserInfo() throws FileNotFoundException, ClassNotFoundException, IOException {
		imageList = new ArrayList<ImageView>();
		photos = new ArrayList<Image>();
		counter = 0;//reset counter
		System.out.println("Welcome "+UserLoginController.getUser());
		displayUser.setText("Welcome "+UserLoginController.getUser());
		users = UserLoginController.userList();//get current list of users.
		for(User i:users) {
			if(i.getUserName().equals(UserLoginController.getUser())) {
				user = i;//user = logged in user
				i.displayAlbums();
				break;
			}
		}
		obsList = FXCollections.observableArrayList(user.getAlbum());//To add user's albums.
		//display everything in the observable list obj.
		albumListView.setItems(obsList);//to display user's list of albums.
		albumListView.getSelectionModel().select(0);
	}
	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void displayAlbumPhotos() throws IOException, ClassNotFoundException {

		BufferedImage bi;
		Image img;
		int index = albumListView.getSelectionModel().getSelectedIndex();//gets index of album selected.
		ArrayList<Photo> pics = user.getAlbumAtIndex(index).getPhotos();//uses the index to retrieve user's ith album.
		displayUserInfo();//reset
		displayUserInfo(index);//selects working album
		//this algorithm scales down the size of original picture to a thumbnail size
		for(int i = 0; i < pics.size(); i++) {
			ImageView iv = new ImageView();
			iv.setFitWidth(50);
			iv.setFitHeight(50);
			iv.setPreserveRatio(true);
			File file2 = new File(pics.get(i).toString());
			bi = ImageIO.read(file2);//reads the content of the file throws except if not a photo.
			img = SwingFXUtils.toFXImage(bi,null);
			photos.add(img);
			imageList.add(iv);
		}

		imageObsList = FXCollections.observableArrayList(imageList);
		imageListView.setItems(imageObsList);
		for(int j = 0; j < imageList.size(); j++) {
			imageList.get(j).setImage(photos.get(j));//set all imageViews
		}
		imageListView.getSelectionModel().select(0);
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void createNewAlbum() throws FileNotFoundException, ClassNotFoundException, IOException {
		Album album = new Album(getAlbumName());
		user.addAlbum(album);
		CreateNewUserController.writeUser(users);
		displayUserInfo();
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void renameAlbum() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		//int lastIndex = obsList.size()-1;
		//user.addAlbum(new Album("dummyAlbum"));
		String albumName = getAlbumName();
		if(confirmChanges()) {
			user.getAlbumAtIndex(index).setAlbumName(albumName);
			CreateNewUserController.writeUser(users);
			//obsList.remove(lastIndex);//Remove from observable list.
			//user.getAlbum().remove(lastIndex);
			displayUserInfo();
		}
		return;
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void deleteAlbum() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		System.out.println(index);
		if(confirmChanges()) {
			if(index == obsList.size()-1){
				obsList.remove(index);//Remove from observable list.
				user.getAlbum().remove(index);//Remove from array of users locally.
				CreateNewUserController.writeUser(users);//update data base.
			}else{
				obsList.remove(index);//Remove from observable list.
				user.getAlbum().remove(index);//Remove from array of users locally.
				CreateNewUserController.writeUser(users);//update data base.
			}
		}
		return;
	}
	/**
	 * 
	 * upload method access computer files and prompts user
	 * to choose a file.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void uploadPhoto() throws IOException, ClassNotFoundException {

		ArrayList<Album> albums = user.getAlbum();
		FileChooser fc = new FileChooser();//fileChooser obj to get access to computer files.
		Album album = albumListView.getSelectionModel().getSelectedItem();
		File file = fc.showOpenDialog(null);//gets access to files and prompts user to select a file and stores it in File obj.
		int index = albumListView.getSelectionModel().getSelectedIndex();
		if(file != null) {
			//JavaFX Image would not take a BufferedImage directly because belongs to Swing/AWT package. 
			//We need to convert the bufferedImage obj into a FX format using class "SwingFXUtils" method toFXImage.
			for(Album a: albums) {
				if(a.equals(album)) {
					a.addPhotoToAlbum(new Photo(file.getAbsolutePath()));//adds new photo to selected album
					a.displayPhotos();
				}
			}
			CreateNewUserController.writeUser(users);
			displayUserInfo();//reset
			displayUserInfo(index);//selects working album
			displayAlbumPhotos();//displays photos working album
		}
	}
	/**
	 * 
	 */
	public void displayPhoto() {
		int index =  imageListView.getSelectionModel().getSelectedIndex();
		imageView.setImage(photos.get(index));
		displaydatetime();
		displayTags();
	}
	/**
	 * 
	 */
	public void nextPhoto() {
		int index1 =  albumListView.getSelectionModel().getSelectedIndex();
		int size = user.getAlbumAtIndex(index1).getPhotosSize();
		counter++;
		imageView.setImage(photos.get((counter)%size));
		System.out.println("next photo");
	}
	/**
	 * 
	 */
	public void prevPhoto() {
		int index =  albumListView.getSelectionModel().getSelectedIndex();
		int size = user.getAlbumAtIndex(index).getPhotosSize();
		counter--;
		imageView.setImage(photos.get((Math.abs(counter))%size));
		System.out.println("next photo");
		System.out.println("prev photo");
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void copyPhoto() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index1 = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		String album = chooseAlbum();
		if(user.getAlbum(album) == null) {
			return;
		}else {
			user.getAlbum(album).addPhotoToAlbum(user.getAlbumAtIndex(index1).getPhoto(index2));
			CreateNewUserController.writeUser(users);
			displayUserInfo();
		}
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void movePhoto() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index1 = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		String album = chooseAlbum();
		if(confirmChanges()) {
			if(user.getAlbum(album) == null) {
				return;
			}else {
				user.getAlbum(album).addPhotoToAlbum(user.getAlbumAtIndex(index1).getPhoto(index2));
				CreateNewUserController.writeUser(users);
				deletePhoto();
				displayUserInfo();//reset
				displayUserInfo(index1);//selects working album
				displayAlbumPhotos();//displays photos working album
				return;
			}
		}
		return;
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void deletePhoto() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index1 = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		if(confirmChanges()) {
			if(index1 == imageObsList.size()-1){
				user.getAlbumAtIndex(index1).getPhotos().remove(index2);
				imageObsList.remove(index2);//Remove from observable list.
				CreateNewUserController.writeUser(users);//update data base.
				displayUserInfo();//reset
				displayUserInfo(index1);//selects working album
				displayAlbumPhotos();//displays photos working album
				return;
			}else{
				user.getAlbumAtIndex(index1).getPhotos().remove(index2);
				imageObsList.remove(index2);//Remove from observable list.
				CreateNewUserController.writeUser(users);//update data base.
				displayUserInfo();//reset
				displayUserInfo(index1);//selects working album
				displayAlbumPhotos();//displays photos working album
				return;
			}
		}
		return;
	}
	/**
	 * 
	 * Logout method to go back to login window.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@FXML
	public void logOut() throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/userLogin.fxml"));
		userPane = (AnchorPane)loader.load();
		UserLoginController userController = 
				loader.getController();
		userController.start(stage);
		Scene scene = new Scene(userPane,700,700);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void photoFilter1() throws IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		filteredImage.setImage(Filter.flipVertically(user.getAlbumAtIndex(index).getPhoto(index2).toString()));
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void photoFilter2() throws IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		filteredImage.setImage(Filter.flipHorizontally(user.getAlbumAtIndex(index).getPhoto(index2).toString()));
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void photoFilter3() throws IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		int index2 = imageListView.getSelectionModel().getSelectedIndex();
		filteredImage.setImage(Filter.flipDiagonally(user.getAlbumAtIndex(index).getPhoto(index2).toString()));  
	}

	//Helper funtions:
	/**
	 * Prompts user to enter the album name to be created.
	 * Method returns the name of the album to be added to the list of albums.
	 * @return
	 */
	public static String getAlbumName() {
		TextInputDialog dialog = new TextInputDialog("walter");
		dialog.setTitle("New Album");
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter name of album:");
		Optional<String> result = dialog.showAndWait();
		return result.get();
	}
	/**
	 * 
	 * @return
	 */
	public String chooseAlbum() {
		List<String> choices = new ArrayList<>();
		ArrayList<Album> albums = user.getAlbum();
		for(int i = 0; i < albums.size(); i++) {
			choices.add(user.getAlbumAtIndex(i).toString());
			System.out.println(choices.get(i));
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("albums", choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			System.out.println("Your choice: " + result.get());
		}
		return result.get();
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void createTempAlbums() throws FileNotFoundException, ClassNotFoundException, IOException {
		Album album = new Album("album1");
		Album album2 = new Album("album2");
		Album album3 = new Album("album3");
		Album album4 = new Album("album4");
		Album album5 = new Album("album5");

		user = UserLoginController.getUser(UserLoginController.getUser());

		user.addAlbum(album);
		user.addAlbum(album2);
		user.addAlbum(album3);
		user.addAlbum(album4);
		user.addAlbum(album5);
		CreateNewUserController.writeUser(users);
	}
	/**
	 * 
	 * @param albumIndex
	 */
	public void displayUserInfo(int albumIndex) {
		displayUser.setText("Welcome "+UserLoginController.getUser());
		users = UserLoginController.userList();//get current list of users.
		for(User i:users) {
			if(i.getUserName().equals(UserLoginController.getUser())) {
				user = i;//user = logged in user
				i.displayAlbums();
				break;
			}
		}
		obsList = FXCollections.observableArrayList(user.getAlbum());//To add user's albums.
		albumListView.setItems(obsList);//to display user's list of albums.
		albumListView.getSelectionModel().select(albumIndex);
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
	/**
	 * 
	 */
	@FXML
	public void displaydatetime() {
		int index =  albumListView.getSelectionModel().getSelectedIndex();
		int index1= imageListView.getSelectionModel().getSelectedIndex();

		Photo p= obsList.get(index).getPhoto(index1);
		File f = new File(p.toString());
		Calendar cal = Calendar.getInstance();
		if (f.exists()) {
			f.setLastModified(cal.getTimeInMillis());

		} else {
			System.out.println("C!nnot Modify File, it does not exists");
		}

		displayDateTime.setText("Created:"+( new SimpleDateFormat("MM/dd/yy KK:mm a")).format(cal
				.getTime()));
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void addtag() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index1 = albumListView.getSelectionModel().getSelectedIndex();
		int index2 =  imageListView.getSelectionModel().getSelectedIndex();
		String tagname=gettagName();
		String tagvalue=gettagValue();
		Photo p= obsList.get(index1).getPhoto(index2);
		p.settagname(tagname);
		p.settagval(tagvalue);
		p.addtag(tagname, tagvalue);
		CreateNewUserController.writeUser(users);
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void deletetag() throws FileNotFoundException, ClassNotFoundException, IOException {
		int index1 = albumListView.getSelectionModel().getSelectedIndex();
		int index2 =  imageListView.getSelectionModel().getSelectedIndex();
		Photo p= obsList.get(index1).getPhoto(index2);
		String tagname=gettagName();
		String tagvalue=gettagValue();
		p.removetag(tagname, tagvalue);
		CreateNewUserController.writeUser(users);
	}
	@FXML
	/**
	 * 
	 */
	public void displayTags() {
		int index =  albumListView.getSelectionModel().getSelectedIndex();
		int index1= imageListView.getSelectionModel().getSelectedIndex();

		Photo p= obsList.get(index).getPhoto(index1);
		ObservableList<String>  tagObsList = FXCollections.observableArrayList(p.gettagarr());
		taglistview.setItems(tagObsList);
	}
	/**
	 * 
	 * @return
	 */
	public static String gettagName() {
		TextInputDialog dialog = new TextInputDialog("location");
		dialog.setTitle("Tagname");
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter tagname of photo:");
		Optional<String> result = dialog.showAndWait();
		//result.ifPresent(name -> System.out.println("Your name: " + name));
		return result.get();
	}
	/**
	 * 
	 * @return
	 */
	public static String gettagValue() {
		TextInputDialog dialog = new TextInputDialog("Boston");
		dialog.setTitle("Tagvalue");
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter tagvalue of photo:");
		Optional<String> result = dialog.showAndWait();
		//result.ifPresent(name -> System.out.println("Your name: " + name));
		return result.get();
	}
}

