package com.eliqxir.utils;

import com.eliqxir.R;
import com.eliqxir.application.EliqxirApplication;

public class UrlGenerator {

	public static String getBrowseWine() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.browse_wine));
	}

	public static String getBrowseFeatured() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.browse_featured));
	}

	public static String getBrowseBeer() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.browse_beer));
	}

	public static String getBrowseMixers() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.browse_mixers));
	}

	public static String getBrowseLiquor() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.browse_liquor));
	}
	public static String getCustomerBrowse() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.customer_browse));
	}

	public static String getItemView() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.item_view));
	}

	public static String addCart() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.add_cart));
	}

	public static String createAccount() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.create_acc));
	}

	public static String loginAccount() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.login_acc));
	}

	public static String createOrder() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.create_order));
	}

	public static String UserMyAccount() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.user_myaccount));
	}

	public static String UserPendingOrders() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.user_pending_orders));
	}
	public static String customeFavorites() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.customer_favorites));
	}
	
	public static String UserOrderDetails() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.user_order_details));
	}

	public static String searchUserPdt() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.user_porduct_search));
	}

	public static String scanCreditCard() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.scan_ccard));
	}

	public static String changeUserPwd() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication
						.getAppString(R.string.customer_changepassword));
	}

	public static String updateUser() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.customer_update));
	}

	public static String forgotPwdUser() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication
						.getAppString(R.string.customer_forgotpassword));
	}
	public static String customerPromocodeList() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.customer_promocode_list));
	}


	public static String vendorLogin() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.vendor_loginapi));
	}

	public static String vendorDashboard() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.vendor_dashboard));
	}

	public static String vendorPendingOrders() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication.getAppString(R.string.vendor_order_pending));
	}

	public static String vendorProcessingOrders() {
		return EliqxirApplication.getAppString((R.string.base_url),
				EliqxirApplication
						.getAppString(R.string.vendor_order_processing));
	}

	public static String vendorCompletedOrder() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_order_complete));
	}
	
	public static String vendorPendingOrderView() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_order_pending_overview));
	}
	public static String vendorProcessingOrderView() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_processing_orderview));
	}
	public static String vendorCompletedOrderView() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_complete_orderview));
	}
	
	public static String vendorListDrivers() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_list_drivers));
	}
	
	public static String vendorAcceptOrder() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_accept_order));
	}
	
	public static String vendorRemovDriver() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_remove_driver));
	}
	
	
	public static String vendorMarkDelivered() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_mark_delivered));
	}
	
	public static String vendorBrowse() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_browse));
	}
	public static String vendorAddItem() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_item_add));
	}
	public static String vendorDeleteItem() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_item_delete));
	}
	public static String vendorUpdateItem() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_item_update));
	}
	public static String vendorGetQuantity() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_item_qty));
	}
	
	public static String vendorDeleteList() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_delete_item_list));
	}
	
	public static String vendorAddDriver() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.vendor_add_driver));
	}
	public static String driverLogin() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.driverlogin));
	}
	public static String driverOrderProcessing() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.driver_order_processing));
	}
	public static String driverOrderComplete() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.driver_order_complete));
	}
	public static String driverProcessingOrderView() {
		return EliqxirApplication
				.getAppString((R.string.base_url), EliqxirApplication
						.getAppString(R.string.driver_processing_orderview));
	}
	
	 public static String vendorStoreInfoHrs() {
		  return EliqxirApplication
		    .getAppString((R.string.base_url), EliqxirApplication
		      .getAppString(R.string.vender_store_info_hrs));
		 }
	 
	 public static String vendorStoreInfoUpdateHrs() {
		  return EliqxirApplication
		    .getAppString((R.string.base_url), EliqxirApplication
		      .getAppString(R.string.vender_store_info_update_hrs));
		 }
	 public static String getSearchLocation() {
		  return EliqxirApplication.getAppString((R.string.base_url),
		    EliqxirApplication.getAppString(R.string.search_location));
		 }

	 public static String driverOrderView() {
			return EliqxirApplication
					.getAppString((R.string.base_url), EliqxirApplication
							.getAppString(R.string.driver_processing_orderview));
		}
}
