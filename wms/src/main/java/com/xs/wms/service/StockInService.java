package com.xs.wms.service;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IStock_in;
import com.xs.wms.pojo.Stock_in;

@Service
public class StockInService {

	private IStock_in stockInMapper;
	
	/**
	 * 新增入仓单
	 * 
	 * @param Stock_in
	 * @return
	 */
	public int insert(Stock_in obj) {

		return stockInMapper.insert(obj);
	}
}
