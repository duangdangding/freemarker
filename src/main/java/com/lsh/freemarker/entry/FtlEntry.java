package com.lsh.freemarker.entry;

import lombok.Data;

@Data
public class FtlEntry {
    
    private String entryPath;
    
    private String mapperPath;
    
    private String servicePath;
    
    private String serviceImplPath;
    
    private String controllerPath;
    
    private String author;
    
}
