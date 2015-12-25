package com.xs.wms.dao;

import com.xs.wms.pojo.Stock_in_detail;

public interface IStock_in_detail {
    int deleteByPrimaryKey(Integer id);

    int insert(Stock_in_detail record);

    int insertSelective(Stock_in_detail record);

    Stock_in_detail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stock_in_detail record);

    int updateByPrimaryKey(Stock_in_detail record);
}