package com.unisguard.webapi.mybatis.plugin;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zemel
 * @date 2021/12/11 21:15
 */
public class SqlPlugin extends PluginAdapter {
    private String id = "id";

    private String parameterType = "parameterType";

    private String resultMap = "resultMap";

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 生成sql文件
     *
     * @param sqlMap
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        sqlMap.setMergeable(false);
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement root = document.getRootElement();
        List<VisitableElement> elementList = root.getElements();
        // 生成list方法
        sqlMapListSelectiveElementGenerated(elementList, introspectedTable);
        List<XmlElement> xmlElementList = elementList.stream().map(element -> ((XmlElement) element)).collect(Collectors.toList());
        Map<String, Integer> map = new HashMap<>();
        map.put("resultMap", 1);
        map.put("list", 2);
        map.put("add", 3);
        map.put("detail", 4);
        map.put("edit", 5);
        map.put("delete", 6);
        // 排序
        xmlElementList = xmlElementList.stream()
                .sorted(Comparator.comparing(element -> element.getAttributes().stream()
                        .filter(attribute -> id.equals(attribute.getName()))
                        .mapToInt(attribute -> map.get(attribute.getValue())).findFirst().orElse(0))
                ).collect(Collectors.toList());
        elementList.clear();
        elementList.addAll(xmlElementList);
        return true;
    }

    private void sqlMapListSelectiveElementGenerated(List<VisitableElement> elementList, IntrospectedTable introspectedTable) {
        XmlElement selectElement = new XmlElement("select");
        elementList.add(selectElement);
        List<Attribute> attributeList = selectElement.getAttributes();
        attributeList.add(new Attribute(id, "list"));
        attributeList.add(new Attribute(parameterType, introspectedTable.getBaseRecordType()));
        attributeList.add(new Attribute(resultMap, resultMap));
        selectElement.addElement(new TextElement("select " + introspectedTable.getAllColumns().stream().map(IntrospectedColumn::getActualColumnName).collect(Collectors.joining(","))));
        selectElement.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        XmlElement whereElement = new XmlElement("where");
        selectElement.addElement(whereElement);
        introspectedTable.getAllColumns().forEach(column -> {
            XmlElement ifElement = new XmlElement("if");
            whereElement.addElement(ifElement);
            ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
            ifElement.addElement(new TextElement("and " + column.getActualColumnName() + " = #{" + column.getJavaProperty() + "}"));
        });
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 字段映射
        List<Attribute> list = element.getAttributes();
        list.removeIf(attribute -> id.equals(attribute.getName()));
        list.add(new Attribute(id, resultMap));
        List<String> valueList = new ArrayList<>();
        List<VisitableElement> elementList = element.getElements();
        Iterator<VisitableElement> iterator = elementList.iterator();
        String column = "column";
        while (iterator.hasNext()) {
            VisitableElement visitableElement = iterator.next();
            // 去掉所有注释
            if (visitableElement instanceof TextElement) {
                iterator.remove();
                continue;
            }
            // xml元素
            XmlElement xmlElement = (XmlElement) visitableElement;
            Iterator<Attribute> it = xmlElement.getAttributes().iterator();
            while (it.hasNext()) {
                Attribute attribute = it.next();
                // 去掉jdbcType
                if ("jdbcType".equals(attribute.getName())) {
                    it.remove();
                    continue;
                }
                // 获取字段
                if (column.equals(attribute.getName())) {
                    valueList.add(attribute.getValue());
                }
            }
        }
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            // 已经添加的字段
            if (valueList.stream().anyMatch(value -> introspectedColumn.getActualColumnName().equals(value))) {
                continue;
            }
            XmlElement xmlElement = new XmlElement("result");
            List<Attribute> attributeList = xmlElement.getAttributes();
            attributeList.add(new Attribute(column, introspectedColumn.getActualColumnName()));
            attributeList.add(new Attribute("property", introspectedColumn.getJavaProperty()));
            elementList.add(xmlElement);
        }
        return true;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 添加方法
        List<Attribute> attributeList = element.getAttributes();
        attributeList.clear();
        // 添加新的添加方法名称
        attributeList.add(new Attribute(id, "add"));
        attributeList.add(new Attribute(parameterType, introspectedTable.getBaseRecordType()));
        attributeList.add(new Attribute("useGeneratedKeys", "true"));
        attributeList.add(new Attribute("keyProperty", id));
        List<VisitableElement> elementList = element.getElements();
        elementList.removeIf(item -> item instanceof TextElement);
        elementList.add(0, new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        return true;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 详情方法
        List<Attribute> attributeList = element.getAttributes();
        // 移除方法名称
        attributeList.removeIf(attribute -> id.equals(attribute.getName()) || resultMap.equals(attribute.getName()));
        // 添加新的详情方法名称
        attributeList.add(new Attribute(id, "detail"));
        attributeList.add(new Attribute(resultMap, resultMap));
        List<VisitableElement> elementList = element.getElements();
        // 清空所有注释和语句
        elementList.clear();
        // 添加新的详情语句
        elementList.add(new TextElement("select " + introspectedTable.getAllColumns().stream().map(IntrospectedColumn::getActualColumnName).collect(Collectors.joining(","))));
        elementList.add(new TextElement("from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        elementList.add(new TextElement("where id = #{id}"));
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 编辑方法
        List<Attribute> attributeList = element.getAttributes();
        attributeList.clear();
        // 添加新的添加方法名称
        attributeList.add(new Attribute(id, "edit"));
        attributeList.add(new Attribute(parameterType, introspectedTable.getBaseRecordType()));
        List<VisitableElement> elementList = element.getElements();
        // 获取where语句
        VisitableElement visitableElement = elementList.get(elementList.size() - 1);
        // 删除所有文本
        elementList.removeIf(item -> item instanceof TextElement);
        elementList.add(0, new TextElement(" update " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        elementList.add(visitableElement);
        return true;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 删除方法
        List<Attribute> attributeList = element.getAttributes();
        // 移除方法名称
        attributeList.removeIf(attribute -> id.equals(attribute.getName()));
        // 添加新的删除方法名称
        attributeList.add(new Attribute(id, "delete"));
        List<VisitableElement> elementList = element.getElements();
        // 清空所有注释和语句
        elementList.clear();
        // 添加新的删除语句
        elementList.add(new TextElement("delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " where id = #{id}"));
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }


    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }
}
