package com.xs.wms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Client;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IClient {
	int deleteByPrimaryKey(Integer id);

	int insert(Client record);

	int insertSelective(Client record);

	Client selectByPrimaryKey(Integer id);

	List<Client> selectByNameKey(String cname);

	int updateByPrimaryKeySelective(Client record);

	int updateByPrimaryKey(Client record);
	
	int repeatClientNum(String cname);

	Long getDatagridTotal(@Param("client") Client client);

	List<Client> datagridClient(@Param("page") PageHelper page, @Param("client") Client client);
}