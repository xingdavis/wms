package com.xs.wms.pojo;

import java.util.Date;

public class View_stock_in_detail {
	private String orderCode;
	private String carNo;
	private Date inDate;
	private Date outDate;
	private String cname;
	private Integer num;
	private Double vol;
	private Double weight;
	private String yard;
	private Double rental;
	private String memo;

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getVol() {
		return this.vol;
	}

	public void setVol(Double vol) {
		this.vol = vol;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getYard() {
		return this.yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public Double getRental() {
		return this.rental;
	}

	public void setRental(Double rental) {
		this.rental = rental;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
