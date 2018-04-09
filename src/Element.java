import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="Element")
@XmlAccessorType (XmlAccessType.FIELD)
public class Element {
	
	@XmlElement(name = "Name")
	private String name;
	@XmlElement ( name = "Image")
	private String imageSrc;
	@XmlElement (name = "Amount")
	private int amount;
	@XmlElement (name = "Price")
	private double price;
	@XmlElement(name = "DateOfAddition")
	private String dateOfAdd;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double d) {
		this.price = d;
	}

	public String getDateOfAdd() {
		return dateOfAdd;
	}

	public void setDateOfAdd(String dateOfAdd) {
		this.dateOfAdd = dateOfAdd;
	}

	public Element() {
		
	}
	
	
}
