package com.lsh.freemarker.util;

import com.lsh.freemarker.entry.TableVo;
import com.lsh.freemarker.entry.Tables;
import com.lsh.freemarker.entry.TableInfo;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FreeMarkerUtils {
    @Autowired
    private Configuration freeMarker;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 判断包路径是否存在
     */
    private void pathJudgeExist(String path){
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 输出到文件
     */
    public  void printFile(Map<String, Object> root, Template template, String filePath, String fileName) throws Exception  {
        pathJudgeExist(filePath);
        File file = new File(filePath, fileName );
        if(!file.exists()) {
            file.createNewFile();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        template.process(root,  out);
        out.close();
    }

    /**
     * 首字母大写
     */
    public  String capFirst(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
    }

    /**
     * 下划线命名转为驼峰命名
     */
    public String underlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s);
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 获取类名
     */
    public String getEntityName(String tableName){
        return underlineToHump(capFirst(tableName.toLowerCase()));
    }

    /**
     * 获取首字母小写类名
     */
    public String getEntityNameLower(String tableName){
        return underlineToHump(tableName.toLowerCase());
    }

    /**
     * 将[数据库类型]转换成[Java类型],如果遇到没有写的类型,会出现Undefine,在后面补充即可
     */
    public  String convertToJava(String columnType){
        String result;
        if ("VARCHAR".equals(columnType) || "LONGTEXT".equals(columnType) || "MEDIUMTEXT".equals(columnType)) {
            result = "String";
        } else if ("INT".equals(columnType)) {
            result = "Integer";
        } else if ("BIGINT".equals(columnType)) {
            result = "Long";
        } else if ("FLOAT".equals(columnType) || "DOUBLE".equals(columnType)) {
            result = "BigDecimal";
        } else if ("DATETIME".equals(columnType) || "TIMESTAMP".equals(columnType) || "DATE".equals(columnType)) {
            result = "Date";
        } else if ("BIT".equals(columnType)) {
            result = "Boolean";
        } else {
            result = "Undefine";
        }
        return result;
    }
    public  String convertToJava_bak(String columnType){
        String result;
        switch (columnType){
            case "VARCHAR":{
                result = "String";
                break;
            }
            case "INT":{
                result = "Integer";
                break;
            }
            case "BIGINT":{
                result = "Long";
                break;
            }
            case "FLOAT":{
                result = "Float";
                break;
            }
            case "DOUBLE":{
                result = "Double";
                break;
            }
            case "DATETIME":{
                result = "Date";
                break;
            }
            case "TIMESTAMP":{
                result = "Timestamp";
                break;
            }
            case "BIT":{
                result = "Boolean";
                break;
            }
            default:{
                result = "Undefine";
                break;
            }
        }
        return result;
    }

    /**
     * 匹配字符串中的英文字符
     */
    public String matchResult(String str) {
        String regEx2 = "[a-z||A-Z]";
        Pattern pattern = Pattern.compile(regEx2);
        StringBuilder sb = new StringBuilder();
        Matcher m = pattern.matcher(str);
        while (m.find()){
            for (int i = 0; i <= m.groupCount(); i++)
            {
                sb.append(m.group());
            }
        }
        return sb.toString();
    }

    /**
     * 获取表信息
     */
    public List<TableVo> getDataInfo(String tableName){
        // mysql查询表结构的语句,如果是其他数据库,修改此处查询语句
        String sql = "show full columns from " + tableName;
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        List<TableVo> vos = new LinkedList<>();
        maps.forEach(info -> {
            TableVo vo = new TableVo();
            String key = info.get("Key").toString();
            vo.setKey(key);
            vo.setPrimary(key.equals("PRI"));
            String field = info.get("Field").toString();
            vo.setColumn(field);
            String type = info.get("Type").toString().toUpperCase();
            String tmp_type = matchResult(type);
            vo.setColumnType(tmp_type);
            String javaType = convertToJava(tmp_type);
            vo.setAttributeType(javaType);
            vo.setAttribute(underlineToHump(field));
            Object comment = info.get("Comment");
            vo.setDescription(comment.toString() + "~" + field);
            vos.add(vo);
        });
        return vos;
    }
    public List<Map<String, String>> getDataInfo_bak(String tableName){
        // mysql查询表结构的语句,如果是其他数据库,修改此处查询语句
        String sql = "show columns from "+tableName;
        List<Map<String, Object>> sqlToMap = jdbcTemplate.queryForList(sql);

        List<Map<String, String>> columns = new LinkedList<>();
        for (Map map : sqlToMap) {
            Map<String, String> columnMap = new HashMap<>(16);
            // 字段名称
            String columnName = map.get("Field").toString();
            columnMap.put("columnName", columnName);
            // 字段类型
            String columnType = map.get("Type").toString().toUpperCase();
            columnType = matchResult(columnType).trim();
            columnType = convertToJava(columnType);
            columnMap.put("columnType", columnType);
            // 成员名称
            columnMap.put("entityColumnNo", underlineToHump(columnName));
            columns.add(columnMap);
        }
        return columns;
    }

    /**
     * 获取全部的表,表注释
     * @return
     */
    public List<Tables> getTables(String database) {
        String sql = "select table_name ,table_comment from INFORMATION_SCHEMA.TABLES t where TABLE_SCHEMA = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, database);
        List<Tables> tables = new ArrayList<>();
        maps.forEach(map -> {
            Tables table = new Tables();
            String tableName = map.get("TABLE_NAME").toString();
            table.setTable_name(tableName);
            Object comment = map.get("TABLE_COMMENT");
            table.setTable_comment(ObjectUtils.isEmpty(comment) ? "" : comment.toString());
            tables.add(table);
        });
        return tables;
    }
    
    public Tables getTable(String database,String tableName) {
        String sql = "select table_name ,table_comment from INFORMATION_SCHEMA.TABLES t where TABLE_SCHEMA = ? and table_name = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, database, tableName);

        Tables table = new Tables();
        Object table_comment = map.get("TABLE_COMMENT");
        table.setTable_name(tableName);
        table.setTable_comment(ObjectUtils.isEmpty(table_comment) ? "" : table_comment.toString());
        return table;
    }
    
    public List<String> getDatabases() {
        String sql = "show databases";
        List<String> maps = jdbcTemplate.queryForList(sql,String.class);
        return maps;
    }
    
    /**
     * 生成代码
     */
    public void generate_bak(Map<String, Object> root,String templateName,String saveUrl,String entityName) throws Exception {
        //获取模板
        Template template = freeMarker.getTemplate(templateName);
        //输出文件
        printFile(root, template, saveUrl, entityName);
    }
    public void generate(Map<String, Object> root,String templateName,String path) throws Exception {
        //获取模板
        Template template = freeMarker.getTemplate(templateName);
        //输出文件
        createFile(root, template, path);
    }

    public void createFile(Map<String, Object> root, Template template,String path) throws Exception  {
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), StandardCharsets.UTF_8));
        template.process(root,  out);
        out.close();
    }
}
