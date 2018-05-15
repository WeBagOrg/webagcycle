package com.hotent.platform.event.listener;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hotent.core.db.datasource.JdbcTemplateUtil;
import com.hotent.core.util.MapUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.event.def.NodeSqlContext;
import com.hotent.platform.event.def.NodeSqlEvent;
import com.hotent.platform.model.bpm.BpmNodeSql;
import com.hotent.platform.model.form.BpmFormData;
import com.hotent.platform.service.bpm.BpmNodeSqlService;
import com.hotent.platform.service.form.BpmFormHandlerService;

/**
 * 流程结束监听事件。
 * 
 * @author ray
 * 
 */
@Service
public class NodeSqlEventListener implements ApplicationListener<NodeSqlEvent>, Ordered {
	
	@Resource
	BpmNodeSqlService bpmNodeSqlService;
	@Resource
	BpmFormHandlerService bpmFormHandlerService;

	@Override
	public void onApplicationEvent(NodeSqlEvent event) {
		NodeSqlContext source = (NodeSqlContext) event.getSource();
		Map<String, Object> dataMap = (Map<String, Object>) source.getDataMap();// 主表数据
		String actdefId = source.getActdefId();// 流程id
		String nodeId =  source.getNodeId();// 节点id
		String action = source.getAction();// 流程id
		
		if(StringUtil.isEmpty(actdefId))// 确保获取到的事件是正确的	
			return ;
		
		List<BpmNodeSql> nodeSqls = bpmNodeSqlService.getByNodeIdAndActdefIdAndAction(nodeId, actdefId, action);
		if(nodeSqls.isEmpty()){
			return;
		}
		try {
			try{//tryCatch是为了防止null异常，这里null异常不处理，继续执行sql
				BpmFormData data = bpmFormHandlerService.getBpmFormData(dataMap.get("id").toString(),nodeId);//这才是拿到全部主数据
				dataMap.putAll(data.getMainFields());
			}catch (Exception e) {
			}
			for (BpmNodeSql nodeSql : nodeSqls) {
				String sql = nodeSql.getSql();
				if (dataMap != null && !dataMap.isEmpty()) {
					// 1.先根据主表数据拼装sql
					Pattern pattern = Pattern.compile("<#(.*?)#>");
					Matcher matcher = pattern.matcher(sql);
					while (matcher.find()) {
						String str = matcher.group();
						String key = matcher.group(1);
						String val =MapUtil.getString(dataMap, key);
						sql = sql.replace(str, val);
					}
				}
				// 2.开始执行sql
				JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getNewJdbcTemplate(nodeSql.getDsAlias());
				jdbcTemplate.execute(sql);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public int getOrder() {
		return 2;
	}

}
