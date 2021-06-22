package com.lsh.freemarker.util;

import com.lsh.freemarker.entry.TableVo;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * ftl模板中引入java类或者方法
 */
public class FtlUtils {

    private final static BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
    private final static TemplateHashModel staticModels = wrapper.getStaticModels();

    /**
     * 注册静态类
     * @param packname
     * @return
     */
    public static TemplateHashModel useStaticPacker(String packname) {
        TemplateHashModel fileStatics = null;
        try {
            fileStatics = (TemplateHashModel) staticModels.get(packname);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return fileStatics;
    };

    /**
     * 判断是否存在 type
     * @param vos
     * @param type
     * @return
     */
    public static boolean fieldTypeExisted(List<TableVo> vos,String type) {
        for (TableVo vo : vos) {
            if (vo.getAttributeType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
