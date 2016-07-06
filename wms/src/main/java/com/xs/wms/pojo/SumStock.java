package com.xs.wms.pojo;

public class SumStock {
	String clientCode;
	String clientName;
	Integer orderId;
	String orderCode;
	String cargoName;
	Integer totalNum;
	Double totalVol;
	Double totalRental;
	Double income;
	Double pay;

	public Double getTotalRental() {
		return totalRental;
	}
	public void setTotalRental(Double totalRental) {
		this.totalRental = totalRental;
	}
	
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
		this.orderCode = orderCode;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Double getTotalVol() {
		return totalVol;
	}

	public void setTotalVol(Double totalVol) {
		this.totalVol = totalVol;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}
}
