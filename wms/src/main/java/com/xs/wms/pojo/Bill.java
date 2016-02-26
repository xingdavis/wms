package com.xs.wms.pojo;

import java.util.Date;

public class Bill {

	private Integer id;

	private String code;

	private Integer clientId;
	
	private Client client;

	private Date sdate;

	private Date edate;

	private Integer op;
	
	private Date crDate;
	
	private Double total;
	
	private String memo;
	
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
	
	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}


	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}
	
	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	public Date getCrDate() {
		return crDate;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
