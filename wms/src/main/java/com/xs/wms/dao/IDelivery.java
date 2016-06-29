package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.BackupData;
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Option;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IDelivery {
    int deleteByPrimaryKey(Integer id);

    int insert(Delivery record);

    int insertSelective(Delivery record);

    Delivery selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Delivery record);

    int updateByPrimaryKey(Delivery record);
    
    Long getDatagridTotal(@Param("delivery") Delivery delivery);

	List<Delivery> datagrid(@Param("page") PageHelper page, @Param("delivery") Delivery delivery);
	
	List<BackupData> backup(@Param("sdate") String sdate,@Param("edate") String edate);
}