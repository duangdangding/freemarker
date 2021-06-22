package com.lsh.freemarker.entry;

import lombok.Data;

@Data
public class TableVo {
    
    private String column;
    
    private String key;
    
//    true 是主键 
    private Boolean primary;
    
    private String columnType;
    
    private String attribute;
    
    private String attributeType;
    
    private String description;
    
}
