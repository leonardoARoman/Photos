package model;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author leonardoroman
 *
 */
public class Album implements Serializable{
	private ArrayList<Photo> photos;
	private String albumName;
	/**
	 * 
	 * @param albumName
	 */
	public Album(String albumName) {
		photos = new ArrayList<Photo>();
		this.albumName = albumName;
	}
	/**
	 * 
	 * @param pic
	 */
	public void addPhotoToAlbum(Photo pic) {
		photos.add(pic);
	}
	/**
	 * 
	 * @param albumName
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	/**
	 * 
	 */
	public void displayPhotos() {
		for(Photo i: photos) {
			System.out.println(i);
		}
	}
	/**
	 * 
	 */
	public String toString() {
		return albumName;
	}
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Photo getPhoto(int index) {
		return photos.get(index);
	}
	/**
	 * 
	 * @return
	 */
	public int getPhotosSize() {
		return photos.size();
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
}
