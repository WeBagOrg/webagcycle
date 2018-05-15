package com.hotent.demo.service.demopackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.demo.model.demopackage.Ypxxdemo;
import com.hotent.demo.dao.demopackage.YpxxdemoDao;
import com.hotent.platform.dao.system.SysOrgDao;
import com.hotent.platform.dao.system.SysUserDao;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.thread.TaskThreadService;
import com.hotent.platform.service.bpm.util.BpmUtil;
import com.hotent.platform.service.system.SysUserService;
import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;


import com.hotent.core.service.BaseService;


@Service
public class YpxxdemoService extends BaseService<Ypxxdemo>
{
	@Resource
	private YpxxdemoDao dao;
	
	@Resource
	private SysUserDao sysuserdao;
	public YpxxdemoService()
	{
	}
	
	@Override
	protected IEntityDao<Ypxxdemo,Long> getEntityDao() 
	{
		return dao;
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
			Ypxxdemo ypxxdemo=getYpxxdemo(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				ypxxdemo.setId(genId);
				this.add(ypxxdemo);
			}else{
				ypxxdemo.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(ypxxdemo);
			}
			cmd.setBusinessKey(ypxxdemo.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取Ypxxdemo对象
	 * @param json
	 * @return
	 */
	public Ypxxdemo getYpxxdemo(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		Ypxxdemo ypxxdemo = (Ypxxdemo)JSONObject.toBean(obj, Ypxxdemo.class);
		return ypxxdemo;
	}
	/**
	 * 保存 ypxxdemo 信息
	 * @param ypxxdemo
	 */

	public void save(Ypxxdemo ypxxdemo) throws Exception{
		Long id=ypxxdemo.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			ypxxdemo.setId(id);
		    this.add(ypxxdemo);
		}
		else{
		   Ypxxdemo ypxxdemoTemd = getById(ypxxdemo.getId());
		   if(ypxxdemoTemd==null || ypxxdemoTemd.equals("")){
		       this.add(ypxxdemo);
		   }else{
		       this.update(ypxxdemo);
		   }
		}
	}
	
	public void ypxxsave()throws Exception{
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		 Map<String, Object> data = cmd.getFormDataMap();
		 JdbcTemplate jdbcTemplate=(JdbcTemplate )AppUtil.getBean("jdbcTemplate"); 
		 Object json = data.get("formData");
		 JSONObject jsonObj = JSONObject.fromObject(json);
		 JSONArray array = JSONArray.fromObject(jsonObj.get("sub"));
		 JSONObject yp =JSONObject.fromObject(array.get(0));
		 JSONArray yparray = JSONArray.fromObject(yp.get("fields"));
		 
		 List<Ypxxdemo> ypxxdemoList = JSONArray.toList(yparray, Ypxxdemo.class);
		 for (Ypxxdemo demo:ypxxdemoList) {
			this.save(demo);
		}
		 //int size=yparray.size();
		// JSONObject obj = null;
		 /*Ypxxdemo ypxxdemo =new Ypxxdemo();
		 for(int i=0;i<size;i++){
			 obj=yparray.getJSONObject(i);			 
			 ypxxdemo.setYpbh((String) obj.get("ypbh"));
			 ypxxdemo.setYpmc((String) obj.get("ypbh"));
			 ypxxdemo.setCyr((String) obj.get("cyr"));
			 System.out.println(ypxxdemo);
			 this.save(ypxxdemo);
		 }*/
		 
	}
	
	
	public void tostarttask()throws Exception{
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Map<String, Object> data = cmd.getFormDataMap();
		Object json = data.get("formData");
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject main = JSONObject.fromObject(jsonObj.get("main"));
		JSONObject fields = JSONObject.fromObject(main.get("fields"));
		String str=fields.getString("tbdxzID");
		String[] strarry=str.split(",");
		for(int i=0;i<strarry.length;i++){
			ProcessCmd processcmd=new ProcessCmd();
			processcmd.setFlowKey("dwtb");
			SysUser sysuser = sysuserdao.getById(Long.parseLong(strarry[i]));
			BpmUtil.startNewFlow(processcmd, sysuser);
		}
	}
	
	
}
