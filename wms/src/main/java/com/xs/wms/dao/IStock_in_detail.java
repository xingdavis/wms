package com.xs.wms.dao;

import java.util.List;

import com.xs.wms.pojo.Stock_in_detail;
import com.xs.wms.pojo.View_stock_in_detail;

public interface IStock_in_detail {
    int deleteByBillId(Integer id);

    int insert(Stock_in_detail record);

    int insertSelective(Stock_in_detail record);

    Stock_in_detail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stock_in_detail record);

    int updateByPrimaryKey(Stock_in_detail record);
    
    List<Stock_in_detail> getDetailsByBillId(int id);
    
    List<View_stock_in_detail> getDetailView(int paramInt);
} 