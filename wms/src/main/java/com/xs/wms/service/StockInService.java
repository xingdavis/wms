package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.xs.wms.dao.IStock_in;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.SumStock;
import com.xs.wms.pojo.easyui.PageHelper;

@Service
public class StockInService {
	@Resource
	private IStock_in stockInMapper;

	/**
	 * 新增入仓单
	 * 
	 * @param Stock_in
	 * @return
	 */
	public int insert(Stock_in obj) {

		return stockInMapper.insert(obj);
	}

	public int update(Stock_in obj) {
		return stockInMapper.updateByPrimaryKey(obj);
	}

	public Stock_in get(Integer id) {
		return stockInMapper.selectByPrimaryKey(id);
	}

	/**
	 * 获取总数
	 * 
	 * @param
	 * @return
	 */
	public Long getDatagridTotal(String key, String sdate, String edate) {
		return stockInMapper.getDatagridTotal(key, sdate, edate);
	}

	/**
	 * 获取列表
	 * 
	 * @param
	 * @return
	 */
	public List<Stock_in> getDatagrid(PageHelper page, String key, String sdate, String edate) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return stockInMapper.getDatagrid(page, key, sdate, edate);
	}
	
	/**
	 * 根据订单id查询该订单的入仓单
	 * @param orderId
	 * @return
	 */
	public List<Stock_in> getListByOrder(Integer orderId) {
		return stockInMapper.getListByOrder(orderId);
	}

	public String updateBill(Integer billId, String op, Integer flag) {
		return stockInMapper.updateBill(billId, op, flag);
	}

	public Long getSumStockTotal(String key) {
		return stockInMapper.getSumStockTotal(key);
	}

	public List<SumStock> getSumStock(PageHelper page, String key) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return stockInMapper.getSumStock(page, key);
	}
	
	public int CloneBill(Integer orderId){
		return stockInMapper.CloneBill(orderId);
	}
}
