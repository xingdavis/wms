package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IOrder;
import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

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

	public Order get(int id){
		return orderMapper.selectByPrimaryKey(id);
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
	
	/**
	 * 获取总数
	 * @param order
	 * @return
	 */
	public Long getDatagridTotal(Order order) {
		return orderMapper.getDatagridTotal(order);  
	}

	/**
	 * 获取列表
	 * @param page
	 * @return
	 */
	public List<Order> datagridOrder(PageHelper page,Order order) {
		page.setStart((page.getPage()-1)*page.getRows());
		page.setEnd(page.getPage()*page.getRows());
		return orderMapper.datagridOrder(page,order);  
	}
}
