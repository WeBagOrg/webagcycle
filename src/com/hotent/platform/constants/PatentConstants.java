package com.hotent.platform.constants;

public interface PatentConstants {
	public static String PATENT_INVENTION="fmzl";
	public static String NEW_TYPE="syxx";
	public static String APPEARANCE_DESIGN="wgzl";
	
	public static String RED="red";//红色，年费有滞纳金的体系颜色
	public static String GREEN="green";//绿色表示距离下次缴费日期一个月内的体系颜色
	public static String YELLOW="yellow";//缴费日期的下一个月内，此时没有滞纳金
	public static String GRAY="gray";//无效的专利申请，灰色提醒
	
	public static String NOPATENTINFO="/platform/common/noPatentInfo.jsp";
	
	public static String PATENTANALYSISTREEPATH="conf/ip/patentAnalysisTree.xml";
	
	public static String PATENTFILEMODELPATH="conf/ip/ipFile.xml";
	
	public static String PROPOSALTEMPLATE="proposalTemplate";//提案的模板文件名
	
	//数据字典的常理信息
	public static String PATENTTYPE="PATENTTYPE";//专利类型
	public static String PATENTSOURCE="PATENTSOURCE";//专利来源
	public static String TRADETYPE="TRADETYPE";//交易类型
	public static String INVALIDREASON="INVALIDREASON";//专利失效原因
	public static String VALIDYEAR="VALIDYEAR";//专利有效年限
	public static String RANK="RANK";//专利等级
	public static String PROPOSALSTATUS="PROPOSALSTATUS";//提案状态
	public static String PROPOSALRESULT="PROPOSALRESULT";//提案结果
	public static String PCTCOUNTRY="PCTCOUNTRY";//pct国家
	public static String AWARDTYPE="AWARDTYPE";//奖金类型
	public static String LAWSUITTYPE="LAWSUITTYPE";//诉讼类型
	public static String LAWSUITRESULTONE="LAWSUITRESULTONE";//诉讼结果
	public static String LAWSUITRESULTTWO="LAWSUITRESULTTWO";//诉讼结果
	public static String PACKAGEORIGIN="PACKAGEORIGIN";//专利包来源
	public static String PROPOSALDEAL="PROPOSALDEAL";//提案处理
	public static String PATENTREEXAMTYPE="PATENTREEXAMTYPE";//复议复审类型
	public static String PATENTREEXAMRESULT="PATENTREEXAMRESULT";//复议复审结果
	public static String PATENTSTATUS="PATENTSTATUS";//专利状态
	public static String DOCUMENTTYPE="DOCUMENTTYPE";//文档类型
	public static String DOCUMENTSRC="DOCUMENTSRC";//文档来源
	public static String IMPORTANTGRADE="IMPORTANTGRADE";//重要等级
	public static String PREMIUMTYPE="PREMIUMTYPE";//奖金类型
	public static String REMINDSTATUS="REMINDSTATUS";//提醒状态
	public static String SECRETDEADLINE="SECRETDEADLINE";//保密期限
	public static String CASETYPE="CASETYPE";//案件类型
	public static String CASESTATUS="CASESTATUS";//案件类型
	public static String SECRETGRADE="SECRETGRADE";//秘密等级
	public static String LAWSUITFLOWRESULT = "LAWSUITFLOWRESULT";//提案结果（诉讼、复议复审）
	
	
	//文档管理--文档类型
	public static String LW = "lw";//来文
	public static String FW = "fw";//发文
	public static String TAFJ = "tafj";//提案附件
	public static String ZLFJ = "zlfj";//专利附件
	public static String ZXGLFJ = "zxglfj";//专项管理附件
	public static String QT = "qt";//其他	
	//文档管理--文档来源
	public static String TAGL = "tagl";//提案管理
	public static String SQGL = "sqgl";//申请管理
	public static String ZLGL = "zlgl";//专利管理
	public static String FYFSGL = "fyfsgl";//复议复审管理
	public static String WXSSGL = "wxssgl";//无效诉讼管理
	public static String ZLJYGL = "zljygl";//专利交易管理
	public static String JLSBGL = "jlsbgl";//奖励申报管理
	public static String JJGL = "jjgl";//奖金管理
	public static String YSJKGL = "ysjkgl";//预算监控管理
	public static String XMGL = "xmgl";//项目管理
	public static String CPGL = "cpgl";//产品管理
	public static String QQFXGL="qqfxgl";//侵权分析管理
	
	public static String EXPENSEPAYTARGET="国家知识产权局";
}
