package com.hotent.platform.service.system.impl.curuser;

import java.util.ArrayList;
import java.util.List;

import com.hotent.core.model.CurrentUser;
import com.hotent.platform.service.system.ICurUserService;


public class PosUserService implements ICurUserService {

	@Override
	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<Long> list=new ArrayList<Long>();
		list.add(currentUser.getPosId());
		return list;
	}

	@Override
	public String getKey() {
		return "pos";
	}

	@Override
	public String getTitle() {
		return "岗位授权";
	}



	

}
