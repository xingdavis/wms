package com.xs.wms.pojo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Stock_in {
	private Integer id;

	private String code;

	private Integer orderId;

	private String orderCode;

	private Integer clientId;
	
	private Client client;

	private String carNo;

	private Date inDate;

	private Date crDate;

	private Integer flag;
	@JsonManagedReference
	private List<Stock_in_detail> items;

	private Integer uid;
	private Date outDate;
	Double rental;
	
	public Double getRental() {
		return rental;
	}

	public void setRental(Double rental) {
		this.rental = rental;
	}

	public Stock_in() {
		id = 0;
	}

	public Stock_in(Integer id, String code, Integer orderId, String orderCode, Integer clientId, String carNo,
			Date inDate, Date crDate, Integer flag, List<Stock_in_detail> items,Integer uid, Date outDate) {

		this.id = id;
		this.code = code;
		this.orderId = orderId;
		this.orderCode = orderCode;
		this.clientId = clientId;
		this.carNo = carNo;
		this.inDate = inDate;
		this.crDate = crDate;
		this.flag = flag;
		this.items = items;
		this.uid=uid;
		this.outDate=outDate;
	}
	
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode == null ? null : orderCode.trim();
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo == null ? null : carNo.trim();
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public List<Stock_in_detail> getItems() {
		return items;
	}

	public void setItems(List<Stock_in_detail> items) {
		this.items = items;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}