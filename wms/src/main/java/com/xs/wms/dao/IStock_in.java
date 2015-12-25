package com.xs.wms.dao;

import com.xs.wms.pojo.Stock_in;

public interface IStock_in {
    int deleteByPrimaryKey(Integer id);

    int insert(Stock_in record);

    int insertSelective(Stock_in record);

    Stock_in selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stock_in record);

    int updateByPrimaryKey(Stock_in record);
}