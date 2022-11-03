package com.unisguard.webapi.mybatis.plugin;

import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by mm on 2019/4/15.
 */
public class ControllerPlugin extends PluginAdapter {
    // controller目录
    private String controllerDir;
    // controller包
    private String controllerPackage;

    private List<String> importTypeList = new ArrayList<String>() {
        {
            add("org.springframework.web.bind.annotation.*");
            add("io.swagger.annotations.Api");
            add("io.swagger.annotations.ApiOperation");
            add("com.github.xiaoymin.knife4j.annotations.ApiOperationSupport");
            add("com.unisguard.webapi.common.dataobject.base.ResponseDO");
            add("org.springframework.validation.annotation.Validated");
            add("java.util.List");
        }
    };

    public ControllerPlugin() {
        super();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        controllerDir = properties.getProperty("controllerDir");
        controllerPackage = properties.getProperty("controllerPackage");
    }


    /**
     * 生成controller层文件
     *
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        // 生成文件的名称
        StringBuilder daoFileName = new StringBuilder();
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName().replace("DO", "");
        daoFileName.append(controllerPackage).append(".").append(domainObjectName).append("Controller");
        // 创建接口
        TopLevelClass topLevelClass = new TopLevelClass(daoFileName.toString());
        // 设置类权限
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 导入类型
        importTypeList.forEach(topLevelClass::addImportedType);
        // api注解
        topLevelClass.addJavaDocLine("@Api(tags = \"" + introspectedTable.getRemarks().replace("表", "管理") + "\")");
        // RestController注解
        topLevelClass.addJavaDocLine("@RestController");
        // RequestMapping注解
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        if (tableName.startsWith("t_")) {
            tableName = tableName.replace("t_", "/").replaceAll("_", "/");
        } else {
            tableName = "/" + tableName.replaceAll("_", "/");
        }
        topLevelClass.addJavaDocLine("@RequestMapping(value = \"" + tableName + "\")");
        // 生成service字段
        generateFiled(domainObjectName, topLevelClass, serviceName, servicePackage);
        // 生成方法
        generateMethod(topLevelClass, domainObjectName, introspectedTable.getBaseRecordType());
        // 生成文件
        generatedFile(controllerDir, controllerPackage, domainObjectName, "Controller", topLevelClass);
        return mapperJavaFiles;
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
        String name = domainObjectName + serviceName;
        String field = name.substring(0, 1).toLowerCase() + name.substring(1);
        // list
        Method method = new Method("list");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("@ApiOperation(value = \"列表\")");
        method.addJavaDocLine("@ApiOperationSupport(order = 10)");
        method.addJavaDocLine("@GetMapping(value = \"/list\")");
        method.getParameters().add(new Parameter(modelType, "param"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<List<" + modelType.getShortName() + ">>"));
        method.addBodyLine("return " + field + ".list(param);");
        // add
        method = new Method("add");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("@ApiOperation(value = \"添加\")");
        method.addJavaDocLine("@ApiOperationSupport(order = 20)");
        method.addJavaDocLine("@PostMapping(value = \"/add\")");
        method.getParameters().add(new Parameter(modelType, "param", "@RequestBody @Validated(value = " + modelType.getShortName() + ".Add.class)"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine("return " + field + ".add(param);");
        // detail
        method = new Method("detail");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("@ApiOperation(value = \"详情\")");
        method.addJavaDocLine("@ApiOperationSupport(order = 30, includeParameters = {\"id\"})");
        method.addJavaDocLine("@GetMapping(value = \"/detail\")");
        method.getParameters().add(new Parameter(modelType, "param", "@Validated(value = " + modelType.getShortName() + ".ID.class)"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<" + modelType.getShortName() + ">"));
        method.addBodyLine("return " + field + ".detail(param.getId());");
        // edit
        method = new Method("edit");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("@ApiOperation(value = \"编辑\")");
        method.addJavaDocLine("@ApiOperationSupport(order = 40)");
        method.addJavaDocLine("@PutMapping(value = \"/edit\")");
        method.getParameters().add(new Parameter(modelType, "param", "@RequestBody @Validated(" + modelType.getShortName() + ".Edit.class)"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine("return " + field + ".edit(param);");
        // delete
        method = new Method("delete");
        topLevelClass.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("@ApiOperation(value = \"删除\")");
        method.addJavaDocLine("@ApiOperationSupport(order = 50, includeParameters = {\"id\"})");
        method.addJavaDocLine("@DeleteMapping(value = \"/delete\")");
        method.getParameters().add(new Parameter(modelType, "param", "@Validated(value = " + modelType.getShortName() + ".ID.class)"));
        method.setReturnType(new FullyQualifiedJavaType(ResponseDO.class.getSimpleName() + "<Long>"));
        method.addBodyLine("return " + field + ".delete(param.getId());");
    }
}
