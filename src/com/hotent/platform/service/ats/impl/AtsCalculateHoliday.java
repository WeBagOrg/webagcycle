package com.hotent.platform.service.ats.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.ats.AtsAttenceCalculateDao;
import com.hotent.platform.dao.ats.AtsHolidayDao;
import com.hotent.platform.model.ats.AtsAttenceCalculate;
import com.hotent.platform.model.ats.AtsConstant;
import com.hotent.platform.model.ats.AtsHoliday;
import com.hotent.platform.model.ats.AtsModel;
import com.hotent.platform.model.ats.AtsScheduleShift;
import com.hotent.platform.service.ats.IAtsCalculate;

/**
 * 
 * <pre>
 * 对象功能:考勤请假计算
 * 开发公司:广州宏天软件有限公司
 * 开发人员:hugh zhuang
 * 创建时间:2015-07-10 09:16:09
 * </pre>
 * 
 */
public class AtsCalculateHoliday implements IAtsCalculate {
	@Resource
	private AtsAttenceCalculateDao atsAttenceCalculateDao;
	@Resource
	private AtsHolidayDao atsHolidayDao;

	@Override
	public void saveData(AtsModel atsModel) throws Exception {
		AtsHoliday atsHoliday = new AtsHoliday();

		atsHoliday.setId(UniqueIdUtil.genId());
		atsHoliday.setStartTime(atsModel.getStartTime());
		atsHoliday.setStartTime(atsModel.getEndTime());
		atsHoliday.setRunId(atsModel.getRunId());
		atsHoliday.setDuration(atsModel.getTimeDuration());
		atsHolidayDao.add(atsHoliday);
	}

	@Override
	public List<AtsAttenceCalculate> calculate(AtsModel atsModel,
			Map<String, AtsScheduleShift> scheduleShiftMap,
			Set<Date> cardRecordList) throws Exception {
		Long userId = atsModel.getUserId();
		Long fileId = atsModel.getFileId();
		Date startTime = atsModel.getStartTime();
		Date endTime = atsModel.getEndTime();

		List<AtsAttenceCalculate> list = new ArrayList<AtsAttenceCalculate>();

		return list;
	}

	private AtsAttenceCalculate setAtsAttenceCalculate(Long fileId,
			Date attenceTime) {
		AtsAttenceCalculate calculate = atsAttenceCalculateDao
				.getByFileIdAttenceTime(fileId, attenceTime);
		if (BeanUtils.isEmpty(calculate))
			return null;

		calculate.setAbsentNumber(null);
		calculate.setAbsentTime(null);
		calculate.setAbsentRecord(null);
		String attenceType = AtsConstant.ATTENDANCE_TYPE_HOLIDAY + "";
		calculate.setAttenceType(attenceType);
		calculate.setHolidayNumber(1d);
		calculate.setHolidayTime(1d);
		calculate.setHolidayUnit((short) 1);
		return calculate;
	}

}
