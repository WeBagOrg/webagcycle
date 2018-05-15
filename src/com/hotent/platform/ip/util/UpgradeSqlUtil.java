package com.hotent.platform.ip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UpgradeSqlUtil {
	/**
	 * sql文件名：必须是以该表为文件，即一张表对应一个文件
	 * @param sqlinput
	 * @param foreignKeySql
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void SQLFileInput(File file,List<String> sqls,List<String> foreignKeySql) throws FileNotFoundException,IOException {
		if(!file.exists()) return ;
        String tablename=file.getName();
        tablename=tablename.substring(0,tablename.indexOf("."));
        InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader br = new BufferedReader(reader);
        String thisLine;
        StringBuffer sb=new StringBuffer();
        //remove sql file comment
        boolean commentFlag=false;// /*(backSlantStart) or --(doubleBar)
        final String backSlantStart="/*";
        final String doubleBar="--";
        final String startBackSlant="*/";
        while ((thisLine = br.readLine()) != null)
        {
           if("".equals(thisLine)) continue;
           
           if(thisLine.startsWith(backSlantStart)&&thisLine.indexOf(startBackSlant)!=-1){
               continue;
           }
           else if(thisLine.startsWith(backSlantStart)){
               commentFlag=true;
               continue;
           }
           else if(thisLine.startsWith(startBackSlant)){
               commentFlag=false;
               continue;
           }
           else if(thisLine.startsWith(doubleBar)){
               continue;
           }
           
           if(!commentFlag){
               if(!getForeignKeySql(thisLine,tablename,foreignKeySql)){
                   sb.append(thisLine);
                   if(sb.toString().endsWith(";")){
                       sqls.add(sb.toString().replaceAll(",\\s*\\)", ")"));
                       sb=new StringBuffer();
                   }
               }
           }
        }
        br.close();
    }
     /**
      * create foreign key sql
      * @param foreignSql
      */
    private static boolean getForeignKeySql(String foreignSqlParsed,String tablename,List<String> foreignKeySql){
        StringBuffer foreignSql=new StringBuffer("ALTER TABLE ");
        foreignSql.append(tablename).append(" ADD ");
        boolean flag=false;
        foreignSqlParsed=foreignSqlParsed.trim();
        if(!"".equals(foreignSqlParsed)&&foreignSqlParsed.indexOf("FOREIGN KEY")!=-1){
            if(",".equals(foreignSqlParsed.substring(foreignSqlParsed.length()-1)))
                //CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
                foreignSqlParsed=foreignSqlParsed.replace(",", "");
            foreignSql.append(foreignSqlParsed).append(";");
            foreignKeySql.add(foreignSql.toString());
            flag=true;
        }
        return flag;
    }
}
