
package com.hotent.webag.dao.userAccount;

import java.util.List;

import com.hotent.webag.model.userAccount.WebagUserAccount;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;



@Repository
public class WebagUserAccountDao extends BaseDao<WebagUserAccount>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagUserAccount.class;
	}

    public WebagUserAccount getByWeChatId(String weChatId) {
		List<WebagUserAccount> list = getBySqlKey("getByWeChatId",weChatId);
		if (list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}

    }

	public void updateByWechatId(WebagUserAccount webagUserAccount) {
		update("updateByWechatId",webagUserAccount);
	}

	public List<WebagUserAccount> getAccountByWechat(String wechatId) {
		return getBySqlKey("getByWeChatId",wechatId);
	}
}