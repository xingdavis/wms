package com.xs.wms.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Stock_in_detail {
    private Integer id;

    private Integer billId;

    private String cname;

    private Integer num;

    private Double vol;

    private Double weight;

    private String yard;
    
    private Stock_in stock_in;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard == null ? null : yard.trim();
    }
    
    public Stock_in getStockIn() {
        return stock_in;
    }

    @JsonBackReference
    public void setStockIn(Stock_in o) {
        this.stock_in = o;
    }
}