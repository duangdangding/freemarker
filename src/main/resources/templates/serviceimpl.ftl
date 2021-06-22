package ${jsonParam.serviceImplPath};

import ${jsonParam.entryPath}.${tableInfo.className};
import ${jsonParam.mapperPath}.${tableInfo.className}Mapper;
import ${jsonParam.servicePath}.${tableInfo.className}Service;
import org.springframework.stereotype.Service;

/**
* ${tableInfo.className}接口实现层
*
* @author ${jsonParam.author}
* @version 1.0.0 ${today}
*/
@Service
public class ${tableInfo.className}ServiceImpl extends ServiceImpl<${tableInfo.className}Mapper, ${tableInfo.className}> implements ${tableInfo.className}Service {

}