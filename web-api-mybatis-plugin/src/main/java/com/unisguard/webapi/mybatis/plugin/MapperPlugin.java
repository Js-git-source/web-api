package com.unisguard.webapi.mybatis.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author zemel
 * @date 2021/12/11 21:15
 */
public class MapperPlugin extends PluginAdapter {
    //父接口
    private String rootInterface;

    private String mapperDir;

    public MapperPlugin() {
        super();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        rootInterface = properties.getProperty("rootInterface");
        mapperDir = properties.getProperty("mapperDir");
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * mapper文件
     *
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 生成dao层文件
     *
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        // 生成文件的名称
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName().replace("DO", "");
        // 创建接口
        Interface interfaces = addRootInterface(rootInterface, mapperPackage, domainObjectName, mapperName, introspectedTable.getBaseRecordType());
        // 生成方法
        generateMethod(interfaces, introspectedTable.getBaseRecordType());
        // 生成文件
        generatedFile(mapperDir, mapperPackage, domainObjectName, mapperName, interfaces);
        return mapperJavaFiles;
    }

    private void generateMethod(Interface interfaces, String baseRecordType) {
        interfaces.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        // Entity类型
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(baseRecordType);
        // 导入Entity类型
        interfaces.addImportedType(modelType);
        // list
        Method method = new Method("list");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType("List<" + baseRecordType + ">"));
        // add
        method = new Method("add");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        // detail
        method = new Method("detail");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType("Long"), "id"));
        method.setReturnType(new FullyQualifiedJavaType(baseRecordType));
        // edit
        method = new Method("edit");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        // delete
        method = new Method("delete");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType("Long"), "id"));
    }

}
