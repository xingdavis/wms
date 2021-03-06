package com.xs.wms.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Delivery {
	private Integer id;

	private String code;

	private String clientCode;

	private Integer clientId;

	private Integer orderId;

	private String orderCode;

	private Order order;

	private Client client;

	private String carNo;

	private String driver;

	private String driverPhone;

	private String dport;

	private String rport;

	private String consignee;

	private String ddate;

	private Date crDate;

	private Integer op;

	private String goodsName;

	private String weigh;

	private String caseModel;

	private String caseNo;

	private String sealNo;

	private String address;

	private String contact;

	private Date arrivalTime;

	private String attention;

	private Date signTime;

	private String startPort;

	private String destination;

	private String endPort;

	private String memo;

	private Integer flag;

	Double totalWeight;
	Double netWeight;
	Double caseWeight;
	Double carWeight;

	public Delivery() {
		id = 0;
	}

	public Delivery(Integer id, String code, String clientCode, Integer clientId, Integer orderId, String orderCode,
			String caseModel, String carNo, String driver, String driverPhone, String dport, String rport,
			String consignee, String ddate, Date crDate, Integer op, String goodsName, String weigh, String caseNo,
			String sealNo, String address, String contact, Date arrivalTime, String attention, Date signTime,
			String startPort, String destination, String endPort, String memo, Integer flag, Double totalWeight,
			Double netWeight, Double caseWeight, Double carWeight) {
		this.id = id;
		this.code = code;
		this.clientCode = clientCode;
		this.clientId = clientId;
		this.orderId = orderId;
		this.orderCode = orderCode;
		this.caseModel = caseModel;
		this.carNo = carNo;
		this.driver = driver;
		this.driverPhone = driverPhone;
		this.dport = dport;
		this.rport = rport;
		this.consignee = consignee;
		this.ddate = ddate;
		this.crDate = crDate;
		this.op = op;
		this.goodsName = goodsName;
		this.weigh = weigh;
		this.caseNo = caseNo;
		this.sealNo = sealNo;
		this.address = address;
		this.contact = contact;
		this.arrivalTime = arrivalTime;
		this.attention = attention;
		this.signTime = signTime;
		this.startPort = startPort;
		this.destination = destination;
		this.endPort = endPort;
		this.memo = memo;
		this.flag = flag;
		this.totalWeight = totalWeight;
		this.netWeight = netWeight;
		this.caseWeight = caseWeight;
		this.carWeight = carWeight;
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

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode == null ? null : clientCode.trim();
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo == null ? null : carNo.trim();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver == null ? null : driver.trim();
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone == null ? null : driverPhone.trim();
	}

	public String getDport() {
		return dport;
	}

	public void setDport(String dport) {
		this.dport = dport == null ? null : dport.trim();
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination == null ? null : destination.trim();
	}

	public String getRport() {
		return rport;
	}

	public void setRport(String rport) {
		this.rport = rport == null ? null : rport.trim();
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee == null ? null : consignee.trim();
	}

	public String getDdate() {
		return ddate;
	}

	public void setDdate(String ddate) {
		this.ddate = ddate;
	}

	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	public String getWeigh() {
		return weigh;
	}

	public void setWeigh(String weigh) {
		this.weigh = weigh == null ? null : weigh.trim();
	}

	public String getCaseModel() {
		return caseModel;
	}

	public void setCaseModel(String caseModel) {
		this.caseModel = caseModel == null ? null : caseModel.trim();
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo == null ? null : caseNo.trim();
	}

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo == null ? null : sealNo.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact == null ? null : contact.trim();
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention == null ? null : attention.trim();
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getStartPort() {
		return startPort;
	}

	public void setStartPort(String startPort) {
		this.startPort = startPort == null ? null : startPort.trim();
	}

	public String getEndPort() {
		return endPort;
	}

	public void setEndPort(String endPort) {
		this.endPort = endPort == null ? null : endPort.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}
	
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}
	
	public void setCaseWeight(Double caseWeight) {
		this.caseWeight = caseWeight;
	}

	public Double getCaseWeight() {
		return caseWeight;
	}
	
	public void setCarWeight(Double carWeight) {
		this.carWeight = carWeight;
	}

	public Double getCarWeight() {
		return carWeight;
	}
}