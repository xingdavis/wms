package com.xs.wms.pojo;

public class BackupData {
//d.ddate,c.cname,u.uname,d.memo,d.`code`,d.case_model,d.destination,d.case_no,d.seal_no,d.car_no,f.fee
	String ddate;
	String cname;
	String uname;
	String memo;
	String code;
	String caseModel;
	String dport;
	String destination;
	String rport;
	String caseNo;
	String sealNo;
	String carNo;
	String income;
	String pay;
	public String getDdate() {
		return ddate;
	}
	public void setDdate(String ddate) {
		this.ddate = ddate == null ? null : ddate.trim();
	}
	
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname == null ? null : uname.trim();
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
	
	public String getCaseModel() {
		return caseModel;
	}
	public void setCaseModel(String caseModel) {
		this.caseModel = caseModel == null ? null : caseModel.trim();
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
	public void setRport(String dport) {
		this.rport = rport == null ? null : rport.trim();
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
	
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo == null ? null : carNo.trim();
	}
	
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income == null ? null : income.trim();
	}
	
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay == null ? null : pay.trim();
	}
}
