package com.xs.wms.pojo;


import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonMethod;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Order {
	private Integer id;

	private String code;

	private Integer clientId;
	
	private Client client;

	private String contactMan;

	private String contactTel;

	private Date orderDate;
	private Integer flag;
	String cargoName;
	@JsonManagedReference
	private List<Order_detail> order_details;

	public Order() {
		id = 0;
	}

	public Order(Integer id, String code, Integer clientId, String contactMan, String contactTel, Date orderDate,
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

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
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
	
	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName == null ? null : cargoName.trim();
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}