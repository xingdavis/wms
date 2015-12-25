package com.xs.wms.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IOrder;
import com.xs.wms.pojo.Order;

/**
 * @author davis
 *
 */
@Service
public class OrderService {
	@Resource
	private IOrder orderMapper;

	/**
	 * 新增订单
	 * 
	 * @param order
	 * @return
	 */
	public int insert(Order order) {

		return orderMapper.insert(order);
	}

	
	/**
	 * 同一客户相同订单号是否存在
	 * @param code
	 * @param client
	 * @return
	 */
	public Boolean existClientCode(String code, Integer client) {
		return orderMapper.repeatCodeNum(code, client) > 0;
	}
}
