package com.lsh.freemarker.service.impl;

import com.lsh.freemarker.service.TableService;
import com.lsh.freemarker.util.FreeMarkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableServiceImpl implements TableService {
    
    @Autowired
    private FreeMarkerUtils freeMarkerUtils;
    
    
}
