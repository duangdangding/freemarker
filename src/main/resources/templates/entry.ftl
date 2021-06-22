<#-- 用于生成Lombok数据模型的自定义模板 -->
package ${jsonParam.entryPath};

<#if FtlUtils.fieldTypeExisted(tableInfo.fieldInfos, "Date")>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if FtlUtils.fieldTypeExisted(tableInfo.fieldInfos, "BigDecimal")>
import java.math.BigDecimal;
</#if>
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
* <#if StringUtils.isNotBlank(tableInfo.description)>${tableInfo.description}(${tableInfo.tableName})<#else>${tableInfo.tableName}</#if>
*
* @author ${jsonParam.author}
* @version 1.0.0 ${today}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "${tableInfo.description!tableInfo.tableName}")
<#--public class ${tableInfo.upperCamelCase}<#if StringUtils.isNotBlank(tableInfo.extendsClass)> extends ${tableInfo.extendsClass}</#if><#if StringUtils.isNotBlank(tableInfo.implementsClass)> implements ${tableInfo.implementsClass}</#if> {-->
public class ${tableInfo.className} {
<#--/** 版本号 */-->
<#--private static final long serialVersionUID = ${tableInfo.serialVersionUID!'1'}L;-->
<#if tableInfo.fieldInfos?has_content>
    <#--<#if paramConfig.fileUpdateMode == 0 || paramConfig.fileUpdateMode == 1>

        /* ${String.format(paramConfig.mergeFileMarkBegin, 1)} */
    </#if>-->
    <#list tableInfo.fieldInfos as fieldInfo>
        <#if fieldInfo.primary>
    @TableId
        </#if>
    @ApiModelProperty(value = "${fieldInfo.description}")
        <#if fieldInfo.attributeType == "Date">
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        </#if>
    private ${fieldInfo.attributeType} ${fieldInfo.attribute};
        
    </#list>
    <#--<#if paramConfig.fileUpdateMode == 0 || paramConfig.fileUpdateMode == 1>

        /* ${String.format(paramConfig.mergeFileMarkEnd, 1)} */
    </#if>-->
</#if>
}