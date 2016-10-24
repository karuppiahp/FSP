package com.eliqxir.vendor;

public class VendorOrdersData {

	public String storeName, customerName, orderId, time, street, country,
			zipcode, state, phoneNumber, total, city, driverName, closedTime,cust_Notes;

	public VendorOrdersData(String storeName1, String customerName1,
			String orderId1, String time1, String street1, String country1,
			String zipcode1, String state1, String phoneNumber1, String total1,
			String city1, String driverName1, String closedTime1, String customerNote) {
		// TODO Auto-generated constructor stub
		this.storeName = storeName1;
		this.customerName = customerName1;
		this.orderId = orderId1;
		this.time = time1;
		this.street = street1;
		this.country = country1;
		this.zipcode = zipcode1;
		this.state = state1;
		this.phoneNumber = phoneNumber1;
		this.total = total1;
		this.city = city1;
		this.driverName = driverName1;
		this.closedTime = closedTime1;
		this.cust_Notes=customerNote;
	}

}
