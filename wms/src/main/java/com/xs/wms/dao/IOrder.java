package com.xs.wms.dao;

import com.xs.wms.pojo.Order;

public interface IOrder {
    int deleteByPrimaryKey(Integer id);

    int insert(Order order);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);
    
    Order selectByCode(String code);
    
    int repeatCodeNum(String code,Integer client);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}