package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IOrder {
	int deleteByPrimaryKey(Integer id);

	int insert(Order order);

	int insertSelective(Order record);

	Order selectByPrimaryKey(Integer id);

	Order selectByCode(String code);

	int repeatCodeNum(@Param("code") String code, @Param("client") Integer client);

	int updateByPrimaryKeySelective(Order record);

	int updateByPrimaryKey(Order record);

	Long getDatagridTotal(@Param("order") Order order);

	List<Order> datagridOrder(@Param("page") PageHelper page, @Param("order") Order order);
	
	Long getSelfDatagridTotal(@Param("clientId") String clientId,@Param("code") String code);

	List<Order> selfDatagridOrder(@Param("page") PageHelper page, @Param("clientId") String clientId,@Param("code") String code);
}