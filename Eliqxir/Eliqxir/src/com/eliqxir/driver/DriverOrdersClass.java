package com.eliqxir.driver;

public class DriverOrdersClass {
	String orderId, total, phone_number, product_id, driver_name, store_phone,
			customer_phone, zipcode, street, product_name, customer_name,
			state, qty, option_id, sku, size, country, time, price,grand_total,
			option_value, store_name,cust_Notes;
	
	String specificproductName,specificprice2,specificqty2,specificsize2,specificproductId,specificoptionValue,
	specificoptionId,specificsku2;

	public DriverOrdersClass(String orderId1, String total1,
			String phone_number1, String product_id1, String driver_name1,
			String store_phone1, String customer_phone1, String zipcode1,
			String street1, String product_name1, String customer_name1,
			String state1, String qty1, String option_id1, String sku1,
			String size1, String country1, String time1, String price1,
			String option_value1, String store_name1, String cust_note, String grand_total) {
		// TODO Auto-generated constructor stub
		this.orderId = orderId1;
		this.total = total1;
		this.phone_number = phone_number1;
		this.product_id = product_id1;
		this.driver_name = driver_name1;
		this.store_phone = store_phone1;
		this.customer_phone = customer_phone1;
		this.zipcode = zipcode1;
		this.street = street1;
		this.product_name = product_name1;
		this.customer_name = customer_name1;
		this.state = state1;
		this.qty = qty1;
		this.option_id = option_id1;
		this.sku = sku1;
		this.size = size1;
		this.country = country1;
		this.time = time1;
		this.price = price1;
		this.option_value = option_value1;
		this.store_name = store_name1;
		this.cust_Notes=cust_note;
		this.grand_total=grand_total;
	}

	public DriverOrdersClass(String productName, String price2, String qty2,String size2, String productId, String optionValue,
			String optionId, String sku2) {
		this.specificproductName = productName;
		this.specificprice2=price2;
		this.specificqty2=qty2;
		this.specificsize2=size2;
		this.specificproductId=productId;
		this.specificoptionValue=optionValue;
		this.specificoptionId=optionId;
		this.specificsku2=sku2;
	}

	

}
