package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IStock_in_detail;
import com.xs.wms.pojo.Stock_in_detail;
import com.xs.wms.pojo.View_stock_in_detail;

@Service
public class StockInDetailService {

	@Resource
	private IStock_in_detail stockInDetailMapper;

	public int insert(Stock_in_detail item) {

		return stockInDetailMapper.insert(item);
	}

	public int deleteAllByBillId(Integer id) {
		return stockInDetailMapper.deleteByBillId(id);
	}

	/**根据单id获取明细
	 * @param id
	 * @return
	 */
	public List<Stock_in_detail> getDetailsByBillId(int id) {
		return stockInDetailMapper.getDetailsByBillId(id);
	}
	
	public List<View_stock_in_detail> getDetailView(int id)
	  {
	    return this.stockInDetailMapper.getDetailView(id);
	  }
}
