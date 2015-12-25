package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IClient;
import com.xs.wms.pojo.Client;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

/**
 * @author davis
 *
 */
/**
 * @author davis
 *
 */
/**
 * @author davis
 *
 */
@Service
public class ClientService {
	@Resource
	private IClient clientMapper;

	/**
	 * 新增客户
	 * 
	 * @param client
	 * @return
	 */
	public int insert(Client client) {
		return clientMapper.insert(client);
	}

	/**
	 * 查找客户
	 * 
	 * @param cname
	 * @return
	 */
	public List<Client> find(String cname) {
		return clientMapper.selectByNameKey(cname);
	}

	public Long getDatagridTotal(Client client) {
		return clientMapper.getDatagridTotal(client);
	}

	public List<Client> datagridClient(PageHelper page, Client client) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getPage() * page.getRows());
		return clientMapper.datagridClient(page, client);
	}

	
	/**
	 * 是否重复客户名称
	 * @param cname
	 * @return
	 */
	public Boolean repeatClientName(String cname) {
		return clientMapper.repeatClientNum(cname) > 0;
	}
}
