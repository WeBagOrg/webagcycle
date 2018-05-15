package com.hotent.platform.ip.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hotent.platform.model.ip.PatentExcelModel;


/**
 * 从txt中读取申请号
 * @author Administrator
 *
 */
public class ReadTxt {
	/**
	 * 从输入流中获取申请号
	 * @param in
	 * @return
	 */
	public List<PatentExcelModel> getPatentExcelModelList(InputStream in){
		List<PatentExcelModel> appNumbers=new ArrayList<PatentExcelModel>();
		if(null!=in){
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			String appNumber="";
			try {
				while((appNumber=br.readLine())!=null){
					if(StringUtils.isNotEmpty(appNumber)){
						PatentExcelModel model=new PatentExcelModel();
						model.setApplycode(appNumber.trim());
						appNumbers.add(model);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appNumbers;
	}
	/**
	 * 从输入流中获取申请号
	 * @param in
	 * @return
	 */
	public List<String> getPatentList(InputStream in){
		List<String> appNumbers=new ArrayList<String>();
		if(null!=in){
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			String appNumber="";
			try {
				while((appNumber=br.readLine())!=null){
					if(StringUtils.isNotEmpty(appNumber)){
						appNumbers.add(appNumber.trim());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appNumbers;
	}
}
