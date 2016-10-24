package com.eliqxir.vendor;

public class VendorOrderViewData {

	String price,productId,productName,optionValue,optionId,sku,size,qty;
	public VendorOrderViewData(String price1, String productId1,
			String productName1, String optionValue1, String optionId1,
			String sku1, String size1,String qty1) {
		// TODO Auto-generated constructor stub
		this.price=price1;
		this.productId=productId1;
		this.productName=productName1;
		this.optionValue=optionValue1;
		this.optionId=optionId1;
		this.sku=sku1;
		this.size=size1;
		this.qty=qty1;
	}
	

}
