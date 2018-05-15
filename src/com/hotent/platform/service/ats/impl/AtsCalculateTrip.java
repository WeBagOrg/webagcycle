package com.hotent.platform.service.ats.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hotent.platform.model.ats.AtsAttenceCalculate;
import com.hotent.platform.model.ats.AtsModel;
import com.hotent.platform.model.ats.AtsScheduleShift;
import com.hotent.platform.service.ats.IAtsCalculate;

/**
 * 
 * <pre>
 * 对象功能:考勤出差计算
 * 开发公司:广州宏天软件有限公司
 * 开发人员:hugh zhuang
 * 创建时间:2015-07-10 09:16:09
 * </pre>
 * 
 */
@Service
public class AtsCalculateTrip implements IAtsCalculate {

	@Override
	public void saveData(AtsModel atsModel) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AtsAttenceCalculate> calculate(AtsModel atsModel,
			Map<String, AtsScheduleShift> scheduleShiftMap,
			Set<Date> cardRecordList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
