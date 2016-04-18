package com.training.hibernate.dto;

public class AddressDto {
	
	private String street;
	
	private int houseNo;

	private String barangay;
	
	private String subdivision;
	
	private String city;
	
	private String zipCode;

	public AddressDto(){};

	public AddressDto(String street, int houseNo, String barangay, String subdivision, String city, String zipCode){
		this.street = street;
		this.houseNo = houseNo;
		this.barangay = barangay;
		this.subdivision = subdivision;
		this.city = city;
		this.zipCode = zipCode;
	}

	public String getStreet(){
		return this.street;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public int getHouseNo(){
		return this.houseNo;
	}

	public void setHouseNo(int houseNo){
		this.houseNo = houseNo;
	}

	public String getBarangay(){
		return this.barangay;
	}

	public void setBarangay(String barangay){
		this.barangay = barangay;
	}
	
	public String getSubdivision(){
		return this.subdivision;
	}

	public void setSubdivision(String subdivision){
		this.subdivision = subdivision;
	}
	
	public String getCity(){
		return this.city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String toString(){
		return this.houseNo + " " + this.street + " " + this.subdivision + " " + this.city; 
	}
}
