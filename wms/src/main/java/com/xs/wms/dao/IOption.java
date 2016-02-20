package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Option;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IOption {
	int deleteByPrimaryKey(Integer id);

	int insert(Option record);

	int insertSelective(Option record);

	Option selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Option record);

	int updateByPrimaryKey(Option record);

	List<Option> getList(@Param("option") Option option);
	
	Long getDatagridTotal(@Param("option") Option option);

	List<Option> datagrid(@Param("page") PageHelper page, @Param("option") Option option);
}