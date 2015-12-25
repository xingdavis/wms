package com.xs.wms.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IOrder_detail;
import com.xs.wms.pojo.Order_detail;
@Service
public class Order_detailService {
	@Resource
	private IOrder_detail order_detailMapper;

	public int insert(Order_detail item) {

		return order_detailMapper.insert(item);
	}
}
