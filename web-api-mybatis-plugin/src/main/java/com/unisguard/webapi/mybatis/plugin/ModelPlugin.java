package com.unisguard.webapi.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;


/**
 * @author zemel
 * @date 2021/12/11 21:15
 */
public class ModelPlugin extends PluginAdapter {

    private TopLevelClass topLevelClass;

    /**
     * 校验属性
     *
     * @param warnings
     * @return
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 设置属性
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }


    /**
     * 字段扩展
     *
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelFieldGenerated(Field field,
                                       TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        if (ModelClassType.BASE_RECORD == modelClassType) {
            this.topLevelClass = topLevelClass;
        }
        if (ModelClassType.RECORD_WITH_BLOBS == modelClassType) {
            this.topLevelClass.getFields().add(field);
        }
        // 父类有属性，子类不生成该属性
        FullyQualifiedJavaType superClass = topLevelClass.getSuperClass().orElse(null);
        if (superClass != null) {
            try {
                Class clazz = Class.forName(superClass.getFullyQualifiedName());
                java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
                for (java.lang.reflect.Field field1 : fields) {
                    if (field1.getName().equals(introspectedColumn.getJavaProperty())) {
                        return false;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        field.getJavaDocLines().clear();
        List<IntrospectedColumn> introspectedColumnList = introspectedTable.getAllColumns();
        int index = IntStream.range(0, introspectedColumnList.size()).filter(i -> introspectedColumn.getJavaProperty().equals(introspectedColumnList.get(i).getJavaProperty())).findFirst().orElse(-1);
        if (index == -1) {
            return false;
        }
        // 获取数据库字段名称
        field.addJavaDocLine("@ApiModelProperty(value = \"" + introspectedColumn.getRemarks() + "\", position = " + (index * 10) + ")");
        // 是否为空
        if (introspectedColumn.isNullable()) {
            return true;
        }
        if ("java.lang.Long".equals(field.getType().getFullyQualifiedName()) || "java.lang.Integer".equals(field.getType().getFullyQualifiedName())) {
            field.addJavaDocLine("@NotNull(message = \"" + introspectedColumn.getRemarks() + "不能为空\", groups = {Add.class, Edit.class})");
        } else if ("java.lang.Integer".equals(field.getType().getFullyQualifiedName())) {
            field.addJavaDocLine("@NotNull(message = \"" + introspectedColumn.getRemarks() + "不能为空\", groups = {Add.class, Edit.class})");
        } else if ("java.lang.String".equals(field.getType().getFullyQualifiedName())) {
            field.addJavaDocLine("@NotBlank(message = \"" + introspectedColumn.getRemarks() + "不能为空\", groups = {Add.class, Edit.class})");
        }
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * getter方法
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * setter方法
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * model文件
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String s = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        // getter方法
        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addJavaDocLine("@Getter");
        // setter方法
        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.addJavaDocLine("@Setter");
        // @ApiModel
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addJavaDocLine("@ApiModel(value = \"" + introspectedTable.getRemarks().replace("表", "") + "\")");
        // @ApiModelProperty
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        // @NotBlank
        topLevelClass.addImportedType("org.hibernate.validator.constraints.NotBlank");
        // @NotNull
        topLevelClass.addImportedType("javax.validation.constraints.NotNull");
        return true;
    }

    /**
     * Example文件
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }
}
