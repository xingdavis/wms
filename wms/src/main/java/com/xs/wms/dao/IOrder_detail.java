package com.xs.wms.dao;

import java.util.List;

import com.xs.wms.pojo.Order_detail;

public interface IOrder_detail {
    /*int deleteByPrimaryKey(Integer id);*/

    int insert(Order_detail record);
    
    List<Order_detail> getDetailsByOrderId(int id);

/*    int insertSelective(Order_detail record);

    Order_detail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order_detail record);

    int updateByPrimaryKey(Order_detail record);*/
}