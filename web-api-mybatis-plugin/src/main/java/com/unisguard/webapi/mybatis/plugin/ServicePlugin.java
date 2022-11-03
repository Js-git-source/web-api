package com.unisguard.webapi.mybatis.plugin;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Created by mm on 2019/4/15.
 */
public class ServicePlugin extends PluginAdapter {
    //父接口
    private String rootInterface;

    private String serviceDir;

    private String serviceImplDir;

    private String serviceImplPackage;

    private Map<String, String> importTypeMap = new LinkedHashMap<String, String>() {
        {
            put("org.springframework.stereotype.Service", "@Service");
            put("java.util.List", "");
            put("com.unisguard.webapi.common.dataobject.base.ResponseDO", "");
            put("com.github.pagehelper.Page", "");
            put("com.github.pagehelper.PageHelper", "");
        }
    };

    public ServicePlugin() {
        super();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        rootInterface = properties.getProperty("rootInterface");
        serviceDir = properties.getProperty("serviceDir");
        serviceImplDir = properties.getProperty("serviceImplDir");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
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
        // 生成service层文件
        Interface interfaces = generatedServiceFile(domainObjectName, introspectedTable.getBaseRecordType());
        // 生成service.impl层文件
        generatedServiceImplFile(interfaces, domainObjectName, introspectedTable.getBaseRecordType());
        return mapperJavaFiles;
    }

    /**
     * 生成service.impl层文件
     *
     * @param interfaces
     * @param domainObjectName
     * @param baseRecordType
     */
    private void generatedServiceImplFile(Interface interfaces, String domainObjectName, String baseRecordType) {
        // 生成文件的名称
        String serviceImpl = serviceName + "Impl";
        TopLevelClass topLevelClass = new TopLevelClass(serviceImplPackage + "." + domainObjectName + serviceImpl);
        // 设置类权限
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        importTypeMap.forEach((k, v) -> {
            topLevelClass.addImportedType(k);
            if (stringHasValue(v)) {
                topLevelClass.addJavaDocLine(v);
            }
        });
        // Entity类型
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(baseRecordType);
        // 导入Entity类型
        topLevelClass.addImportedType(modelType);
        // 实现接口
        topLevelClass.addSuperInterface(interfaces.getType());
        // 导入接口类型
        topLevelClass.addImportedType(interfaces.getType());
        // 生成manage字段
        generateFiled(domainObjectName, topLevelClass, manageName, managePackage);
        // 生成方法
        generatedMethod(domainObjectName, topLevelClass, modelType);
        // 生成文件
        generatedFile(serviceImplDir, serviceImplPackage, domainObjectName, serviceImpl, topLevelClass);
    }

    /**
     * 生成方法
     *
     * @param domainObjectName
     * @param topLevelClass
     * @param modelType
     */
    private void generatedMethod(String domainObjectName, TopLevelClass topLevelClass, FullyQualifiedJavaType modelType) {
        String name = domainObjectName + manageName;
        String field = name.substring(0, 1).toLowerCase() + name.substring(1);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(ResponseDO.class.getSimpleName());
        String object = modelType.getShortName().substring(0, 1).toLowerCase() + modelType.getShortName().substring(1);
        // list
        Method method = new Method("list");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<List<" + modelType.getShortName() + ">>"));
        method.addBodyLine("Page page = PageHelper.startPage(param.getPage(), param.getLimit());");
        method.addBodyLine("List<" + modelType.getShortName() + "> " + object + "List = " + field + ".list(param);");
        method.addBodyLine("return " + returnType.getShortName() + ".success(" + object + "List, page.getTotal());");
        // add
        method = new Method("add");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine(field + ".add(param);");
        method.addBodyLine("return " + returnType.getShortName() + ".success(param.getId());");
        // detail
        method = new Method("detail");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType(Long.class.getSimpleName()), "id"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<" + modelType.getShortName() + ">"));
        method.addBodyLine(modelType.getShortName() + " " + object + " = " + field + ".detail(id);");
        method.addBodyLine("return " + returnType.getShortName() + ".success(" + object + ");");
        // edit
        method = new Method("edit");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine(field + ".edit(param);");
        method.addBodyLine("return " + returnType.getShortName() + ".success();");
        // delete
        method = new Method("delete");
        method.addJavaDocLine("@Override");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType(Long.class.getSimpleName()), "id"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine(field + ".delete(id);");
        method.addBodyLine("return " + returnType.getShortName() + ".success();");
    }

    /**
     * 生成service层文件
     *
     * @param domainObjectName
     * @param baseRecordType
     */
    private Interface generatedServiceFile(String domainObjectName, String baseRecordType) {
        // 创建接口
        Interface interfaces = addRootInterface(rootInterface, servicePackage, domainObjectName, serviceName, baseRecordType);
        generatedMethod(interfaces, new FullyQualifiedJavaType(baseRecordType));
        // 生成文件
        generatedFile(serviceDir, servicePackage, domainObjectName, serviceName, interfaces);
        return interfaces;
    }

    /**
     * 生成方法
     *
     * @param interfaces
     * @param modelType
     */
    private void generatedMethod(Interface interfaces, FullyQualifiedJavaType modelType) {
        interfaces.addImportedType(new FullyQualifiedJavaType("com.unisguard.webapi.common.dataobject.base.ResponseDO"));
        interfaces.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaces.addImportedType(modelType);
        // list
        Method method = new Method("list");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<List<" + modelType.getShortName() + ">>"));
        // add
        method = new Method("add");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        // detail
        method = new Method("detail");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType(Long.class.getSimpleName()), "id"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<" + modelType.getShortName() + ">"));
        // edit
        method = new Method("edit");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        // delete
        method = new Method("delete");
        interfaces.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.getParameters().add(new Parameter(new FullyQualifiedJavaType(Long.class.getSimpleName()), "id"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
    }
}
