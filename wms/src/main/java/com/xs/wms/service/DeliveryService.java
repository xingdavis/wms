package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.xs.wms.dao.IDelivery;
import com.xs.wms.pojo.BackupData;
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Option;
import com.xs.wms.pojo.easyui.PageHelper;

@Service
public class DeliveryService {

	@Resource
	private IDelivery deliveryMapper;

	public Delivery getById(Integer id) {
		return this.deliveryMapper.selectByPrimaryKey(id);
	}

	public int add(Delivery obj) {
		return this.deliveryMapper.insert(obj);
	}

	public int delete(Integer id) {
		return this.deliveryMapper.deleteByPrimaryKey(id);
	}

	public int update(Delivery obj) {
		return this.deliveryMapper.updateByPrimaryKey(obj);
	}

	public Long getDatagridTotal(Delivery obj) {
		return this.deliveryMapper.getDatagridTotal(obj);
	}

	/**
	 * 获取列表
	 * 
	 * @param page
	 * @return
	 */
	public List<Delivery> datagrid(PageHelper page, Delivery obj) {
		// page.setStart((page.getPage() - 1) * page.getRows());
		// page.setEnd(page.getPage() * page.getRows());
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getRows());
		return this.deliveryMapper.datagrid(page, obj);
	}

	/**
	 * 导出备份数据
	 * 
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<BackupData> backup(String sdate, String edate) {
		return this.deliveryMapper.backup(sdate, edate);
	}

	/**
	 * 克隆提货单
	 * 
	 * @param dId
	 * @return
	 */
	public int CloneDelivery(Integer dId) {
		return deliveryMapper.CloneDelivery(dId);
	}
}
