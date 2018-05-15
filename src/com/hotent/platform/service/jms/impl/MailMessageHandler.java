package com.hotent.platform.service.jms.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;

import com.hotent.core.engine.FreemarkEngine;
import com.hotent.core.jms.IMessageHandler;
import com.hotent.core.model.MessageModel;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.model.system.SysTemplate;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.NodeMsgTemplateService;
import com.hotent.platform.service.system.MessageEngine;
import com.hotent.platform.service.system.SysTemplateService;
import com.hotent.platform.service.util.ServiceUtil;

public class MailMessageHandler implements IMessageHandler {

	private final Log logger = LogFactory.getLog(MailMessageHandler.class);
	@Resource
	private MessageEngine messageEngine;
	@Resource
	SysTemplateService sysTemplateService;
	@Resource
	NodeMsgTemplateService nodeMsgTemplateService;
	@Resource
	FreemarkEngine freemarkEngine;

	@Override
	public String getTitle() {
		return "邮件";
		// return ContextUtil.getMessages("message.mail");//国际化
	}

	@Override
	public void handMessage(MessageModel model) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setSubject(this.getSubject(model));

		if (model.getTo() != null && model.getTo().length > 0) {
			simpleMailMessage.setTo(model.getTo());
			simpleMailMessage.setCc(model.getCc());
			simpleMailMessage.setBcc(model.getBcc());
		} else {
			String eamilStr = "";
			if (model.getReceiveUser() != null)
				eamilStr = ((SysUser) model.getReceiveUser()).getEmail();
			if (StringUtil.isEmpty(eamilStr) || !StringUtil.isEmail(eamilStr))
				return;// 判断一下邮箱是否为空或不是邮箱，则直接返回
			String[] sendTos = { eamilStr };
			simpleMailMessage.setTo(sendTos);
		}
		simpleMailMessage.setText(this.getContent(model));
		simpleMailMessage.setSentDate(model.getSendDate());
		messageEngine.sendMail(simpleMailMessage);
		logger.debug("MailModel");
	}

	/**
	 * 获取右键的模版内容
	 */
	private String getTemplate(MessageModel model) {
		String mailTemplate = "";
		try {
			mailTemplate = model.getTemplateMap().get(SysTemplate.TEMPLATE_TYPE_HTML);
			mailTemplate = StringUtil.jsonUnescape(mailTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailTemplate;
	}

	private String getSubject(MessageModel model) {
		if (model.getTemplateMap() == null)
			return model.getSubject();
		String title = model.getTemplateMap().get(SysTemplate.TEMPLATE_TITLE);
		title = ServiceUtil.replaceTitleTag(title, model.getReceiveUser().getFullname(), model.getSendUser().getFullname(), model.getSubject(), model.getOpinion());
		return title;
	}

	/**
	 * 内容要用模版进行替换， 如果有模版
	 */
	private String getContent(MessageModel model) {
		String content = "";
		if (model.getTemplateMap() == null)
			return model.getContent();

		String sendUserName = "";

		if (model.getSendUser() == null) {
			sendUserName = "系统消息";
		} else {
			sendUserName = model.getSendUser().getFullname();
		}
		String url = "";
		if (BeanUtils.isNotIncZeroEmpty(model.getExtId())) {
			url = ServiceUtil.getUrl(model.getExtId().toString(), model.getIsTask());
		}

		content = ServiceUtil.replaceTemplateTag(this.getTemplate(model), model.getReceiveUser().getFullname(), sendUserName, model.getSubject(), url, model.getOpinion(), false);

		try {
			// 处理nodeMsgTemplate
			content = content.replace("${html表单}", model.getTemplateMap().get("htmlDefForm")).replace("${text表单}", model.getTemplateMap().get("textDefForm"));
		} catch (Exception e) {
		}
		return content;
	}

	/**
	 * 默认不勾选邮件
	 */
	@Override
	public boolean getIsDefaultChecked() {
		return false;
	}

}
