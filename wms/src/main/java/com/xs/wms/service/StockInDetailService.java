package com.xs.wms.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.xs.wms.dao.IStock_in_detail;

import com.xs.wms.pojo.Stock_in_detail;

@Service
public class StockInDetailService {

	@Resource
	private IStock_in_detail stockInDetailMapper;

	public int insert(Stock_in_detail item) {

		return stockInDetailMapper.insert(item);
	}
	
	public int deleteAllByBillId(Integer id){
		return stockInDetailMapper.deleteByBillId(id);
	}
}
