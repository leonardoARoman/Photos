package model;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author leonardoroman
 *
 */
public class User implements Serializable{
	
	private ArrayList<Album> albums;
	private String userName;
	private String userPass;
	/**
	 * 
	 * @param userName
	 * @param userPass
	 */
	public User(String userName, String userPass) {
		albums = new ArrayList<Album>();
		this.userName = userName;
		this.userPass = userPass;
	}
	/**
	 * 
	 * @param album
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}
	/**
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 
	 * @param userPass
	 */
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	/**
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 
	 * @return
	 */
	public String getUserPass() {
		return userPass;
	}
	/**
	 * 
	 */
	public void displayAlbums() {
		for(int i = 0; i < albums.size(); i++) {
			int numberOfPhotosInAlbum = albums.get(i).getPhotosSize();
			System.out.println(albums.get(i));//To display album name.
			for(int j = 0; j < numberOfPhotosInAlbum; j++) {
				Photo photo = albums.get(i).getPhoto(j);
				System.out.println(photo);//To display photos in album.
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Album> getAlbum() {
		return albums;
	}
	/**
	 * 
	 * @param albumName
	 * @return
	 */
	public Album getAlbum(String albumName) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).toString().equals(albumName))
				return albums.get(i);
		}
		return null;
	} 
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Album getAlbumAtIndex(int index) {
		return albums.get(index);
	}
}
