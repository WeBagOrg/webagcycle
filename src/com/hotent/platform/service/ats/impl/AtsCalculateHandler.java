package com.hotent.platform.service.ats.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.platform.model.ats.AtsAttenceCalculate;
import com.hotent.platform.model.ats.AtsModel;
import com.hotent.platform.model.ats.AtsScheduleShift;
import com.hotent.platform.service.ats.IAtsCalculate;

/**
 * 处理容器
 * 
 * @author hugh zhuang
 * 
 */
public class AtsCalculateHandler implements IAtsCalculate {

	@Resource
	private AtsCalculateContainer atsCalculateContainer;

	@Override
	public void saveData(AtsModel atsModel) throws Exception {
		IAtsCalculate cal = atsCalculateContainer
				.getHandler(atsModel.getType());
		cal.saveData(atsModel);
	}

	@Override
	public List<AtsAttenceCalculate> calculate(AtsModel atsModel,
			Map<String, AtsScheduleShift> scheduleShiftMap,
			Set<Date> cardRecordList) throws Exception {
		IAtsCalculate cal = atsCalculateContainer
				.getHandler(atsModel.getType());
		return cal.calculate(atsModel, scheduleShiftMap, cardRecordList);
	}

}
