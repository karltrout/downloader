package gov.faa.web;

import java.io.File;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;

public class FileObject{
	private String name;
	private Date date;
	private double size;
	private String type = "unknown";
	private String icon = "/images/default.png";
	
	private String action;
	
	public FileObject(File file){
		this.setName(file.getName());
		this.setDate(new Date(file.lastModified()));
		this.setSize((file.length()/1024));
		this.setType(URLConnection.guessContentTypeFromName(file.getName()));
		if (this.type == null || this.type.isEmpty()) setType("unknown");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return DateFormat.getDateTimeInstance().format(date);
		
	}

	/**
	 * @param date the date to set
	 */
	private void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return String.format("%.0f kb", size);
	}

	/**
	 * @param size the size to set
	 */
	private void setSize(double size) {
		this.size = size;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	private void setType(String type) {
		this.type = type;
		setIcon();
	}

	private void setIcon() {
		if(this.type != null){
			switch (this.type){
			case "application/pdf":
				this.icon = "/images/pdf.png";
				break;
			case "application/rtf":
				this.icon = "/images/doc.png";
				break;
			case "text/plain":
				this.icon = "/images/doc.png";
				break;
			default:
				this.icon = "/images/zip.png";
				break;
			}
		}
	}
	
	public String getIcon(){
		return this.icon;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}