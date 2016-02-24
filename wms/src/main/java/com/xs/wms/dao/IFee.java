package com.xs.wms.dao;

import com.xs.wms.pojo.Fee;

public interface IFee {
    int deleteByPrimaryKey(Integer id);

    int insert(Fee record);

    int insertSelective(Fee record);

    Fee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Fee record);

    int updateByPrimaryKey(Fee record);
}