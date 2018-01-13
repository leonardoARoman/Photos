package model;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;
/**
 * 
 * @author leonardoroman
 *
 */
public class Photo  implements Serializable{
	private String url;
	private String tagname,tagval;
	private ArrayList<String> tagarr=new  ArrayList<String>() ;
	public Photo(String url) {
		this.url = url;
	}
	/**
	 * 
	 */
	public String toString() {
		return url;
	}
	/**
	 * 
	 * @return
	 */
	public String gettagname() {
		return tagname;
	}
	/**
	 * 
	 * @return
	 */
	public String gettagval() {
		return tagval;
	}
	/**
	 * 
	 * @param tagname
	 */
	public void settagname(String tagname) {
		this.tagname=tagname;
	}
	/**
	 * 
	 * @param tagval
	 */
	public void settagval(String tagval) {
		this.tagval=tagval;
	}
	/**
	 * 
	 * @param tagname
	 * @param tagval
	 */
	public void addtag(String tagname, String tagval) {
		tagarr.add(tagname+","+tagval);
	}
	/**
	 * 
	 * @param tagname
	 * @param tagval
	 */
	public void removetag(String tagname, String tagval) {
		tagarr.remove(tagname+","+tagval);
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> gettagarr(){
		return tagarr;
	}
}

