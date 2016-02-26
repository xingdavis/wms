package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Bill;
import com.xs.wms.pojo.Fee;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IFee {
	int deleteByPrimaryKey(Integer id);

	int insert(Fee record);

	int insertSelective(Fee record);

	Fee selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Fee record);

	int updateByPrimaryKey(Fee record);

	Long getDatagridTotal(@Param("client") String client, @Param("key") String key, @Param("sdate") String sdate,
			@Param("edate") String edate);

	List<Fee> datagrid(@Param("page") PageHelper page, @Param("client") String client, @Param("key") String key,
			@Param("sdate") String sdate, @Param("edate") String edate);

	int repeatNum(Fee fee);

	Long getDeliveryBillTotal(@Param("fflag") String fflag, @Param("bflag") String bflag,
			@Param("client") String client, @Param("key") String key, @Param("sdate") String sdate,
			@Param("edate") String edate);

	List<Fee> datagridDeliveryBill(@Param("page") PageHelper page, @Param("fflag") String fflag,
			@Param("bflag") String bflag, @Param("client") String client, @Param("key") String key,
			@Param("sdate") String sdate, @Param("edate") String edate);

	Long getStockInBillTotal(@Param("fflag") String fflag, @Param("bflag") String bflag, @Param("client") String client,
			@Param("key") String key, @Param("sdate") String sdate, @Param("edate") String edate);

	List<Fee> datagridStockInBill(@Param("page") PageHelper page, @Param("fflag") String fflag,
			@Param("bflag") String bflag, @Param("client") String client, @Param("key") String key,
			@Param("sdate") String sdate, @Param("edate") String edate);

	Long getBillTotal(@Param("client") String client, @Param("sdate") String sdate, @Param("edate") String edate);

	List<Bill> datagridBill(@Param("page") PageHelper page, @Param("client") String client,
			@Param("sdate") String sdate, @Param("edate") String edate);
}