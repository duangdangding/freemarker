package com.lsh.freemarker.entry;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
    
    private String className;
    
    private String tableName;
    
    private String description;
    
    private List<TableVo> fieldInfos;
    
//    private String[] fieldInfos;
    
}
