package org.daijie.jdbc.ognl;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.matedata.TableMateDataManage;
import org.daijie.jdbc.scripting.SqlSpelling;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基于ognl分析节点表达示
 * @author daijie
 * @since 2019/12/30
 */
public class ParseScriptExpression {

    private final Logger log = LoggerFactory.getLogger(ParseScriptExpression.class);

    /**
     * SQL脚本
     */
    private String script;

    /**
     * 脚本根节点元素
     */
    private Element element;

    /**
     * 方法参数转存到map
     */
    private Map<String, Object> map = Maps.newHashMap();

    /**
     * ognl处理的参数数据
     */
    private Map context;

    /**
     * 构建脚本表达式分析器
     * @param script SQL脚本
     * @param entity 方法参数
     * @param parameters 方法参数名
     * @throws DocumentException 脚本异常
     */
    public ParseScriptExpression(String script, Object entity, String[] parameters) throws DocumentException {
        this.script = script;
        convertMap(entity, parameters);
        checkElements();
    }

    /**
     * 构建脚本表达式分析器
     * @param entity 方法参数
     * @param element 根节点元素
     */
    public ParseScriptExpression(Object entity, Element element) {
        this.script = new String();
        convertMap(entity, new String[]{});
        this.element = element;
    }

    /**
     * 方法参数转换map
     * @param entity 方法参数
     * @param parameters 方法参数名
     */
    private void convertMap(Object entity, String[] parameters) {
        if (parameters.length > 1) {
            Object[] args = (Object[]) entity;
            for (int i = 0; i < args.length; i ++) {
                this.map.put(parameters[i], args[i]);
            }
        } else if (parameters.length == 1 && (entity instanceof Object[] || entity instanceof List || TableMateDataManage.isBaseClass(entity.getClass()))) {
            this.map.put(parameters[0], entity);
        } else if (entity instanceof Map) {
            this.map = (Map<String, Object>) entity;
        } else {
            this.map = BeanUtil.beanToMap(entity);
        }
        this.context = Ognl.createDefaultContext(this.map);
    }

    /**
     * 检查脚本并初始化XML格式脚本
     * @throws DocumentException 脚本异常
     */
    private void checkElements() throws DocumentException {
        if (StringUtils.isNotEmpty(this.script)) {
            if (this.script.contains("<") && !this.script.startsWith("<script>")) {
                this.script = "<script>" + this.script + "</script>";
            }
            if (this.script.startsWith("<script>")) {
                Document document = DocumentHelper.parseText(this.script);
                this.element = document.getRootElement();
            }
        }
    }

    /**
     * 分析脚本转换成可执行的SQL语句
     * @param params SQL占位符对应的参数，用于当前方法添加
     * @return 已分析完成后的脚本
     * @throws DocumentException 脚本异常
     * @throws OgnlException ognl表达式异常
     */
    public String parse(List<Object> params) throws DocumentException, OgnlException {
        parseExpression(this.element, null, false, params);
        parseParams("#{", "}", params);
        return this.script;
    }

    /**
     * 处理ognl表达式
     * @param element 节点元素
     * @param sql 用于节点深度遍历处理完表达式拼接的SQL
     * @param isSelectived 当使用choose节点时记录子节点已符合条件的节点
     * @param params SQL占位符对应的参数，用于当前方法添加
     * @throws DocumentException 脚本异常
     * @throws OgnlException ognl表达式异常
     */
    public void parseExpression(Element element, StringBuilder sql, boolean isSelectived, List<Object> params) throws DocumentException, OgnlException {
        if (element != null) {
            if (sql == null) {
                sql = new StringBuilder();
            }
            if (sql.length() == 0) {
                sql.append(element.getText());
            }
            List<Element> elements = element.elements();
            for (Element node : elements) {
                NodeType nodeType = NodeType.valueOf(node.getName().toUpperCase());
                if (nodeType == NodeType.CHOOSE) {
                    isSelectived = false;
                } else if (nodeType == NodeType.IF || nodeType == NodeType.WHEN) {
                    String test = node.attributeValue("test");
                    if (StringUtils.isNotEmpty(test)) {
                        Object value = getOgnlValue(test);
                        if (value instanceof Boolean && (boolean) value) {
                            sql.append(" ").append(node.getText());
                        }
                    }
                } else if (nodeType == NodeType.OTHERWISE) {
                    if (!isSelectived) {
                        sql.append(" ").append(node.getText());
                    }
                } else if (nodeType == NodeType.FOREACH) {
                    String item = node.attributeValue("item");
                    String index = node.attributeValue("index");
                    String collection = node.attributeValue("collection");
                    String open = node.attributeValue("open");
                    String close = node.attributeValue("close");

                    Object collectionObj = getOgnlValue(collection);
                    List<Object> list = null;
                    if (collectionObj instanceof Object[]) {
                        list = Arrays.asList((Object[]) collectionObj);
                    }
                    if (collectionObj instanceof List) {
                        list = (List) collectionObj;
                    }
                    List<String> items = Lists.newArrayList();
                    for (int i = 0; i < list.size(); i ++) {
                        Map<String, Object> map = Maps.newHashMap();
                        map.put(item, list.get(i));
                        map.put(index, i);
                        ParseScriptExpression parseScriptExpression = new ParseScriptExpression(map, node);
                        String text = parseScriptExpression.parse(params);
                        if (text.length() > 0) {
                            items.add(text);
                        }
                    }
                    sql.append(open).append(new SqlSpelling().collectionToDelimitedString(items, ",")).append(close);
                }
                if (node.elements().size() > 0) {
                    parseExpression(node, sql, isSelectived, params);
                }
            }
            this.script = sql.toString();
        }
    }

    /**
     * 根据ognl表达式获取表达式计算结果
     * @param name ognl表达式
     * @return 获取表达式计算结果
     * @throws OgnlException ognl表达式格式错误
     */
    private Object getOgnlValue(String name) throws OgnlException {
        Object obj = null;
        obj = Ognl.getValue(Ognl.parseExpression(name), this.context, this.map);
        if (obj == null) {
            throw new OgnlException("找不到字段：" + name);
        }
        return obj;
    }

    /**
     * 替换SQL语句中使用openToken开头和closeToken结尾的字符串变量
     * @param openToken 开头字符串
     * @param closeToken 结尾字符串
     * @param params SQL占位符对应的参数，用于当前方法添加
     * @throws OgnlException ognl表达式异常
     */
    public void parseParams(String openToken, String closeToken, List<Object> params) throws OgnlException {
        if (this.script == null || this.script.isEmpty()) {
            return;
        }
        int start = this.script.indexOf(openToken, 0);
        if (start == -1) {
            return;
        }
        char[] src = this.script.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = this.script.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = this.script.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    Object value = getOgnlValue(expression.toString());
                    params.add(value);
                    builder.append("?");
                    offset = end + closeToken.length();
                }
            }
            start = this.script.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        this.script = builder.toString();
    }
}
