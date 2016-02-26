package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IFee;
import com.xs.wms.pojo.Bill;
import com.xs.wms.pojo.Fee;
import com.xs.wms.pojo.easyui.PageHelper;

@Service
public class FeeService {
	@Resource
	private IFee feeMapper;

	public Fee get(Integer id) {
		return feeMapper.selectByPrimaryKey(id);
	}

	/**
	 * 新增
	 * 
	 * @param
	 * @return
	 */
	public int insert(Fee obj) {
		return feeMapper.insert(obj);
	}

	public int update(Fee obj) {
		return feeMapper.updateByPrimaryKey(obj);
	}

	public int delete(Integer id) {
		return feeMapper.deleteByPrimaryKey(id);
	}

	public Long getDatagridTotal(String client, String key, String sdate, String edate) {
		return feeMapper.getDatagridTotal(client, key, sdate, edate);
	}

	public List<Fee> datagrid(PageHelper page, String client, String key, String sdate, String edate) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return feeMapper.datagrid(page, client, key, sdate, edate);
	}

	public Boolean repeat(Fee obj) {
		return feeMapper.repeatNum(obj) > 0;
	}
	
	public int burnBill(String ids) {
		return feeMapper.burnBill(ids);
	}

	public Long getDeliveryBillTotal(String fflag, String bflag, String client, String key, String sdate,
			String edate) {
		return feeMapper.getDeliveryBillTotal(fflag, bflag, client, key, sdate, edate);
	}

	public List<Fee> datagridDeliveryBill(PageHelper page, String fflag, String bflag, String client, String key,
			String sdate, String edate) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return feeMapper.datagridDeliveryBill(page, fflag, bflag, client, key, sdate, edate);
	}

	public Long getStockInBillTotal(String fflag, String bflag, String client, String key, String sdate, String edate) {
		return feeMapper.getStockInBillTotal(fflag, bflag, client, key, sdate, edate);
	}

	public List<Fee> datagridStockInBill(PageHelper page, String fflag, String bflag, String client, String key,
			String sdate, String edate) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return feeMapper.datagridStockInBill(page, fflag, bflag, client, key, sdate, edate);
	}

	public Long getBillTotal(String client, String sdate, String edate) {
		return feeMapper.getBillTotal(client, sdate, edate);
	}

	public List<Bill> datagridBill(PageHelper page, String client, String sdate, String edate) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return feeMapper.datagridBill(page, client, sdate, edate);
	}
}
