package com.unisguard.webapi.mybatis.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author zemel
 * @date 2021/12/11 21:15
 */
public class PluginAdapter extends org.mybatis.generator.api.PluginAdapter {
    // mapper包
    String mapperPackage;
    // mapper后缀名
    String mapperName = "Mapper";
    // manage包
    String managePackage;
    // manage后缀名
    String manageName = "Manage";
    // service包
    String servicePackage;
    // service后缀名
    String serviceName = "Service";
    // 格式化java代码
    private JavaFormatter javaFormatter;
    // 生成的Java文件的编码
    private String encoding;
    // Slf4j注解
    String slf4jAnnotations = "lombok.extern.slf4j.Slf4j";

    private ShellCallback shellCallback = null;
    // 生成的文件集合
    List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();

    PluginAdapter() {
        this.shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        mapperPackage = properties.getProperty("mapperPackage");
        managePackage = properties.getProperty("managePackage");
        servicePackage = properties.getProperty("servicePackage");
        javaFormatter = context.getJavaFormatter();
        encoding = context.getProperty("javaFileEncoding");
    }

    /**
     * 类导入注解
     *
     * @param name
     * @param fullName
     * @param topLevelClass
     */
    protected void addAnnotations(String name, String fullName, TopLevelClass topLevelClass) {
        topLevelClass.addJavaDocLine(name);
        topLevelClass.addImportedType(fullName);
    }

    /**
     * 类导入注解
     *
     * @param name
     * @param fullName
     * @param topLevelClass
     */
    protected void addAnnotations(String name, String fullName, TopLevelClass topLevelClass, Field field) {
        field.addJavaDocLine(name);
        topLevelClass.addImportedType(fullName);
    }

    /**
     * 生成字段
     *
     * @param domainObjectName 实体类
     * @param topLevelClass    对象
     * @param layer            层的后缀名
     * @param packages         包
     */
    void generateFiled(String domainObjectName, TopLevelClass topLevelClass, String layer, String packages) {
        // 类名
        String name = domainObjectName + layer;
        // 包 + 类名
        String fullName = packages + "." + name;
        // 字段
        Field field = new Field(name.substring(0, 1).toLowerCase() + name.substring(1), new FullyQualifiedJavaType(fullName));
        // 字段可见性
        field.setVisibility(JavaVisibility.PRIVATE);
        // Resource注解
        addAnnotations("@Resource", "javax.annotation.Resource", topLevelClass, field);
        // 添加字段
        topLevelClass.addField(field);
        // 导入字段类型
        topLevelClass.addImportedType(fullName);
    }

    /**
     * 生成文件
     *
     * @param dir              目录
     * @param packages         包
     * @param domainObjectName 实体类
     * @param layer            层的后缀名
     * @param compilationUnit  对象
     */
    void generatedFile(String dir, String packages, String domainObjectName, String layer, CompilationUnit compilationUnit) {
        try {
            // 获取目录
            File directory = shellCallback.getDirectory(dir, packages);
            // 打开文件
            File file = new File(directory, String.format("%s%s.java", domainObjectName, layer));
            // 文件不存在
            if (!file.exists()) {
                mapperJavaFiles.add(new GeneratedJavaFile(compilationUnit, dir, encoding, javaFormatter));
            }
        } catch (ShellException e) {
            throw new RuntimeException(e);
        }
    }

    Interface addRootInterface(String rootInterface, String packages, String domainObjectName, String name, String baseRecordType) {
        // 生成文件的名称
        Interface interfaces = new Interface(packages + "." + domainObjectName + name);
        // 设置类权限
        interfaces.setVisibility(JavaVisibility.PUBLIC);
        if (stringHasValue(rootInterface)) {
            // 包+接口全称（父）
            FullyQualifiedJavaType superPackageInterfaceType = new FullyQualifiedJavaType(rootInterface);
            // 接口名称（父）
            String superInterfaceShortName = superPackageInterfaceType.getShortName();
            // 接口类型（父）
            FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(superInterfaceShortName);
            // 继承父接口
            interfaces.addSuperInterface(superInterfaceType);
            // 导入父接口
            interfaces.addImportedType(superPackageInterfaceType);
            // Entity类型
            FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(baseRecordType);
            // 导入Entity类型
            interfaces.addImportedType(modelType);
            // 父接口添加泛型
            superInterfaceType.addTypeArgument(modelType);
        }
        return interfaces;
    }
}
