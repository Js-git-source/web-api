package com.unisguard.webapi.mybatis.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.*;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Created by mm on 2019/4/15.
 */
public class ManagePlugin extends PluginAdapter {
    //父接口
    private String rootInterface;

    private String manageDir;

    private String manageImplDir;

    private String manageImplPackage;

    private Map<String, String> importTypeMap = new LinkedHashMap<String, String>() {
        {
            put("com.unisguard.webapi.common.annotation.Manage", "@Manage");
            put("java.util.List", "");
        }
    };

    public ManagePlugin() {
        super();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        rootInterface = properties.getProperty("rootInterface");
        manageDir = properties.getProperty("manageDir");
        manageImplDir = properties.getProperty("manageImplDir");
        manageImplPackage = properties.getProperty("manageImplPackage");
    }

    /**
     * 生成service层文件
     *
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName().replace("DO", "");
        // 生成manage层文件
        Interface interfaces = generateManageFile(domainObjectName, introspectedTable.getBaseRecordType());
        // 生成manage.impl层文件
        generatedManageImplFile(interfaces, domainObjectName, introspectedTable.getBaseRecordType());
        return mapperJavaFiles;
    }

    /**
     * 生成manage.impl层文件
     *
     * @param interfaces
     * @param domainObjectName
     * @param baseRecordType
     */
    private void generatedManageImplFile(Interface interfaces, String domainObjectName, String baseRecordType) {
        // 生成文件的名称
        String manageImpl = manageName + "Impl";
        TopLevelClass topLevelClass = new TopLevelClass(manageImplPackage + "." + domainObjectName + manageImpl);
        // 设置类权限
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        importTypeMap.forEach((k, v) -> {
            topLevelClass.addImportedType(k);
            if (stringHasValue(v)) {
                topLevelClass.addJavaDocLine(v);
            }
        });
        // 实现接口
        topLevelClass.addSuperInterface(interfaces.getType());
        // 导入接口类型
        topLevelClass.addImportedType(interfaces.getType());
        // 生成mapper字段
        generateFiled(domainObjectName, topLevelClass, mapperName, mapperPackage);
        // 生成方法
        generateMethod(topLevelClass, domainObjectName, baseRecordType);
        // 生成文件
        generatedFile(manageImplDir, manageImplPackage, domainObjectName, manageImpl, topLevelClass);
    }

    /**
     * 生成方法
     *
     * @param topLevelClass
     * @param domainObjectName
     * @param baseRecordType
     */
    private void generateMethod(TopLevelClass topLevelClass, String domainObjectName, String baseRecordType) {
        // Entity类型
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(baseRecordType);
        // 导入Entity类型
        topLevelClass.addImportedType(modelType);
        String name = domainObjectName + mapperName;
        String field = name.substring(0, 1).toLowerCase() + name.substring(1);
        // list
        Method method = new Method("list");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType("List<" + baseRecordType + ">"));
        method.addBodyLine("return " + field + ".list(param);");
        // add
        method = new Method("add");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.addBodyLine(field + ".add(param);");
        // detail
        method = new Method("detail");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType("Long"), "id"));
        method.setReturnType(new FullyQualifiedJavaType(baseRecordType));
        method.addBodyLine("return " + field + ".detail(id);");
        // edit
        method = new Method("edit");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.addBodyLine(field + ".edit(param);");
        // delete
        method = new Method("delete");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType("Long"), "id"));
        method.addBodyLine(field + ".delete(id);");
    }

    /**
     * 生成manage层文件
     *
     * @param domainObjectName
     */
    private Interface generateManageFile(String domainObjectName, String baseRecordType) {
        // 创建接口
        Interface interfaces = addRootInterface(rootInterface, managePackage, domainObjectName, manageName, baseRecordType);
        // 生成方法
        generateMethod(interfaces, baseRecordType);
        // 生成文件
        generatedFile(manageDir, managePackage, domainObjectName, manageName, interfaces);
        return interfaces;
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
