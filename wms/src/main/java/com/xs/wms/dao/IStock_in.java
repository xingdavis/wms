package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.SumStock;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IStock_in {

	int insert(Stock_in record);

	int insertSelective(Stock_in record);

	Stock_in selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Stock_in record);

	int updateByPrimaryKey(Stock_in record);

	Long getDatagridTotal(@Param("key") String key, @Param("sdate") String sdate, @Param("edate") String edate);

	List<Stock_in> getDatagrid(@Param("page") PageHelper page, @Param("key") String key, @Param("sdate") String sdate,
			@Param("edate") String edate);

	List<Stock_in> getListByOrder(@Param("orderId") Integer orderId);
	
	String updateBill(@Param("billId") Integer billId, @Param("op") String op, @Param("flag") Integer flag);

	Long getSumStockTotal(@Param("key") String key);

	List<SumStock> getSumStock(@Param("page") PageHelper page, @Param("key") String key);
}