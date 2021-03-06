package com.hotent.webag.service.bagInfo;
import java.io.File;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.platform.service.util.ServiceUtil;
import com.hotent.webag.dao.bagInfo.WebagBaginfoDao;
import com.hotent.webag.model.bagInfo.WebagBaginfo;
import com.hotent.webag.until.DownLoadFiles;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.GenericService;
import com.hotent.core.util.BeanUtils;

import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;


import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.core.service.WfBaseService;
import com.hotent.core.annotion.WorkFlow;
import com.hotent.platform.service.bpm.util.BpmAspectUtil;
import com.hotent.core.bpm.BpmResult;


@Service
public class WebagBaginfoService extends WfBaseService<WebagBaginfo>
{
	@Resource
	private WebagBaginfoDao dao;
	
	@Resource
	private ProcessRunService processRunService;
	public WebagBaginfoService()
	{
	}
	
	@Override
	protected IEntityDao<WebagBaginfo,Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 重写getAll方法绑定流程runId
	 * @param queryFilter
	 */
	public List<WebagBaginfo> getAll(QueryFilter queryFilter){
		List<WebagBaginfo> webagBaginfoList=super.getAll(queryFilter);
		List<WebagBaginfo> webagBaginfos=new ArrayList<WebagBaginfo>();
		for(WebagBaginfo webagBaginfo:webagBaginfoList){
			ProcessRun processRun=processRunService.getByBusinessKey(webagBaginfo.getId().toString());
			if(BeanUtils.isNotEmpty(processRun)){
				webagBaginfo.setRunId(processRun.getRunId());
			}
			webagBaginfos.add(webagBaginfo);
		}
		return webagBaginfos;
	}
	

		
	
	/**
	 * 流程处理器方法 用于处理业务数据
	 * @param cmd
	 * @throws Exception
	 */
	public void processHandler(ProcessCmd cmd)throws Exception{
		Map data=cmd.getFormDataMap();
		if(BeanUtils.isNotEmpty(data)){
			String json=data.get("json").toString();
			WebagBaginfo webagBaginfo=getWebagBaginfo(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagBaginfo.setId(genId);
				this.add(webagBaginfo);
			}else{
				webagBaginfo.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagBaginfo);
			}
			cmd.setBusinessKey(webagBaginfo.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagBaginfo对象
	 * @param json
	 * @return
	 */
	public WebagBaginfo getWebagBaginfo(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagBaginfo webagBaginfo = (WebagBaginfo)JSONObject.toBean(obj, WebagBaginfo.class);
		return webagBaginfo;
	}
	/**
	 * 保存 回收袋信息 信息
	 * @param webagBaginfo
	 */

	@WorkFlow(flowKey="undefined",tableName="webag_baginfo")
	public void save(WebagBaginfo webagBaginfo) throws Exception{
		Long id=webagBaginfo.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagBaginfo.setId(id);
			webagBaginfo.setIsHaveQR("未生成");
			webagBaginfo.setUseTime(0L);
		    this.add(webagBaginfo);
		}
		else{
		   WebagBaginfo webagBaginfoTemd = getById(webagBaginfo.getId());
		   if(webagBaginfoTemd==null || webagBaginfoTemd.equals("")){
		       this.add(webagBaginfo);
		   }else{
		       this.update(webagBaginfo);
		   }
		}
		BpmResult result=new BpmResult();
		//添加流程变量
//		result.addVariable("", "");
		result.setBusinessKey(id.toString());
		BpmAspectUtil.setBpmResult(result);
	}
	/**
	 * 导出二维码
	 */
	public List<WebagBaginfo> getBagsListByIds(String ids){
		return dao. getBagsListByIds(ids);
	}

	public void discloseTechnical(List<WebagBaginfo> webagBaginfoList,String fileDir) {
		//String attachPath = ServiceUtil.getBasePath().replace("/", File.separator);
		List<File> files =new ArrayList<File>();
		List<String> fileNameList=new ArrayList<String>();
		for(int i=0;i<webagBaginfoList.size();i++){
			WebagBaginfo webagBaginfo =webagBaginfoList.get(i);
			String realPath = webagBaginfo.getQRUrl();
			String fileName = webagBaginfo.getBagNo() + ".png" ;
			File file=new File(realPath);
			files.add(file);
			fileNameList.add(fileName);

			try {
				String zipDir=fileDir+"/"+new Date()+".zip";
			DownLoadFiles.downLoadFiles(files,fileNameList, zipDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void exportZip(String fileDir, HttpServletRequest request, HttpServletResponse response) {
		try {
			DownLoadFiles.downLoadFiles(fileDir, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 用户扫码绑定二维码。
	 * @param userOpenId
	 * @param bagNo
	 * @return
	 * @throws Exception
	 */
    public int setuserBindQR(String userOpenId, String bagNo) {
		return dao.setuserBindQR(userOpenId,bagNo);

    }
}
