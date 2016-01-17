package com.xs.wms.pojo;

import java.util.Date;
import java.util.List;

public class Order {
	private Integer id;

	private String code;

	private Integer clientId;

	private String contactMan;

	private String contactTel;

	private Integer orderDate;
	private Integer flag;
	private List<Order_detail> order_details;

	public Order() {
		id = 0;
	}

	public Order(Integer id, String code, Integer clientId, String contactMan, String contactTel, Integer orderDate,
			Integer flag, List<Order_detail> order_details) {

		this.id = id;
		this.code = code;
		this.clientId = clientId;
		this.contactMan = contactMan;
		this.contactTel = contactTel;
		this.orderDate = orderDate;
		this.order_details = order_details;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getContactMan() {
		return contactMan;
	}

	public void setContactMan(String contactMan) {
		this.contactMan = contactMan == null ? null : contactMan.trim();
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel == null ? null : contactTel.trim();
	}

	public Integer getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Integer orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public List<Order_detail> getOrder_details() {
		return order_details;
	}

	public void setOrder_details(List<Order_detail> details) {
		this.order_details = details;
	}
}