package com.lsh.freemarker;

import com.lsh.freemarker.entry.FtlEntry;
import com.lsh.freemarker.entry.TableInfo;
import com.lsh.freemarker.entry.TableVo;
import com.lsh.freemarker.entry.Tables;
import com.lsh.freemarker.util.FreeMarkerUtils;
import com.lsh.freemarker.util.FtlUtils;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FreemarkerApplicationTests {
    
    @Autowired
    private FreeMarkerUtils freeMarkerUtils;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 单张表添加
     */
    @Test
    void createOne() {
        Map<String,Object> all = new HashMap<>();
        initParam(all);
        Tables table = freeMarkerUtils.getTable("boke", "parse_urls");
        oneTable(table,all);
    }

    /**
     * 生成全部的表
     */
    @Test
    void getTableInfo() {
        List<Tables> tables = freeMarkerUtils.getTables("boke");
        Map<String,Object> all = new HashMap<>();
        initParam(all);
        for (Tables table : tables) {
            oneTable(table,all);
        }
    }

    /**
     * 初始化数据
     * @param all
     */
    public void initParam(Map<String,Object> all) {
        FtlEntry ftlEntry = new FtlEntry();
        String rootPackage = "com.lll.boke.";
        ftlEntry.setEntryPath(rootPackage + "entry");
        ftlEntry.setMapperPath(rootPackage + "mapper");
        ftlEntry.setServicePath(rootPackage + "service");
        ftlEntry.setServiceImplPath(rootPackage + "service.impl");
        ftlEntry.setControllerPath(rootPackage + "ctr");
//        这里可以修改
        ftlEntry.setAuthor("lushao");
        all.put("jsonParam",ftlEntry);
        all.put("today",simpleDateFormat.format(new Date()));
        all.put("FtlUtils",new FtlUtils());
        all.put("StringUtils",FtlUtils.useStaticPacker("org.junit.platform.commons.util.StringUtils"));
    }

    /**
     * 生成的文件在本项目的freemark文件夹下面
     * @param table
     * @param all
     */
    public void oneTable(Tables table,Map<String,Object> all) {
        TableInfo info = new TableInfo();
//        表名
        String tableName = table.getTable_name();
//        类名
        String className = freeMarkerUtils.getEntityName(tableName);
        info.setTableName(tableName);
//        表的注释
        info.setDescription(table.getTable_comment());
        info.setClassName(className);
        List<TableVo> tableInfo = freeMarkerUtils.getDataInfo(tableName);
        info.setFieldInfos(tableInfo);
        info.setDescription(table.getTable_comment());
        all.put("tableInfo", info);
        try {
            freeMarkerUtils.generate_bak(all,"entry.ftl","freemark/entry/",  className + ".java");
            freeMarkerUtils.generate_bak(all,"mapper.ftl","freemark/mapper/",  className + "Mapper.java");
            freeMarkerUtils.generate_bak(all,"service.ftl","freemark/service/",  className + "Service.java");
            freeMarkerUtils.generate_bak(all,"serviceimpl.ftl","freemark/serviceimpl/",  className + "ServiceImpl.java");
            freeMarkerUtils.generate_bak(all,"controller.ftl","freemark/controller/",  className + "Ctr.java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void getTables() {
        List<Tables> boke = freeMarkerUtils.getTables("boke");
        boke.forEach(System.out::println);
    }
    @Test
    void getDatabases() {
        List<String> databases = freeMarkerUtils.getDatabases();
        databases.forEach(System.out::println);
    }
    

}
