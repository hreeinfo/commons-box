package commons.box.util;

import commons.box.app.AppError;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/7/2 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class XMLs {
    private static final Map<String, String> EMPTY_HEADERS = Maps.immmap(null);
    private static final DocumentBuilderFactory XML_DOCUMENT_BUILDER_FACT = DocumentBuilderFactory.newInstance();

    private static ConcurrentMap<String, JAXBContext> JAXB_CONTEXT = new ConcurrentHashMap<>();

    private XMLs() {
    }


    public static Document fromStringToDocument(String xml) {
        try {
            return fromInputStreamToDocument(new ByteArrayInputStream(xml.getBytes("utf-8")), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document fromInputStreamToDocument(InputStream in, String encoding) {
        DocumentBuilder builder = null;
        Document ret = null;

        try {
            builder = XML_DOCUMENT_BUILDER_FACT.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        try {
            InputSource is = new InputSource(in);
            is.setEncoding(encoding);
            ret = builder.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    /**
     * 选择所有的节点 返回的列表按照文档顺序来组织
     *
     * @param path
     * @param node
     * @param namespaces 命名空间
     * @return
     */
    /*
    @SuppressWarnings("unchecked")
    public static List<Node> selectNodes(String path, Object node, Map<String, String> namespaces) {
        try {
            return getDOMXPath(path, namespaces).selectNodes(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */

    /**
     * 列出所有的节点 返回的列表按照文档顺序来组织
     *
     * @param path
     * @param node
     * @return
     */
    /*
    public static List<Node> selectNodes(String path, Object node) {
        return selectNodes(path, node, null);
    }

    public static Node selectNode(String path, Object node, Map<String, String> namespaces) {
        try {
            List<Node> nodes = selectNodes(path, node, namespaces);
            if (nodes.size() == 0) {
                return null;
            }
            return nodes.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Node selectNode(String path, Object node) {
        return selectNode(path, node, null);
    }
    */

    /**
     * 返回节点的内容或者属性的值
     *
     * @param path 解析路径
     * @param node 可以是节点node或者其他内容对象
     */
    /*
    public static String selectText(String path, Object node, Map<String, String> namespaces) {
        try {
            Node rnode = (Node) getDOMXPath(path, namespaces).selectSingleNode(node);
            if (rnode == null) {
                return null;
            }
            if (!(rnode instanceof Text)) {
                rnode = rnode.getFirstChild();
            }
            if (!(rnode instanceof Text)) {
                return null;
            }
            return ((Text) rnode).getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */

    /**
     * 返回节点的内容或者属性的值
     *
     * @param path 解析路径
     * @param node 可以是节点node或者其他内容对象
     */
    /*
    public static String selectText(String path, Object node) {
        return selectText(path, node, null);
    }

    private static DOMXPath getDOMXPath(String path, Map<String, String> namespaces) throws Exception {
        DOMXPath xpath = new DOMXPath(path);
        if (namespaces != null) {
            for (String prefix : namespaces.keySet()) {
                xpath.addNamespace(prefix, namespaces.get(prefix));
            }
        }
        return xpath;
    }
    */

    /**
     * 值对象转换为XML
     *
     * @param root
     * @return
     */
    public static String toXml(Object root) {
        Class<?> clazz = getUserClass(root);
        return toXml(root, clazz, null);
    }

    /**
     * 值对象转换为XML 根据对象判断所属类
     *
     * @param root
     * @param encoding
     * @return
     */
    public static String toXml(Object root, String encoding) {
        Class<?> clazz = getUserClass(root);
        return toXml(root, clazz, encoding);
    }

    /**
     * 值对象转换为XML
     *
     * @param root
     * @param clazz
     * @param encoding
     * @return
     */
    public static String toXml(Object root, Class<?> clazz, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw unchecked(e);
        }
    }

    /**
     * 集合转换为XML
     *
     * @param root
     * @param rootName
     * @param clazz
     * @return
     */
    public static String toXml(Collection<?> root, String rootName, Class<?> clazz) {
        return toXml(root, rootName, clazz, null);
    }

    /**
     * 集合转换为XML
     *
     * @param root
     * @param rootName
     * @param clazz
     * @param encoding
     * @return
     */
    public static String toXml(Collection<?> root, String rootName, Class<?> clazz, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
                    CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw unchecked(e);
        }
    }

    /**
     * Xml转换为Java 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xml, Class<T> clazz) {
        try {
            StringReader reader = new StringReader(xml);
            return (T) createUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw unchecked(e);
        }
    }

    /**
     * 创建Marshaller并设定encoding(可为null).
     * 线程不安全，需要每次创建或pooling。
     */
    public static Marshaller createMarshaller(Class<?> clazz, String encoding) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (Strs.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }

            return marshaller;
        } catch (JAXBException e) {
            throw unchecked(e);
        }
    }

    /**
     * 创建UnMarshaller.
     * 线程不安全，需要每次创建或pooling。
     */
    public static Unmarshaller createUnmarshaller(Class<?> clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw unchecked(e);
        }
    }

    protected static JAXBContext getJaxbContext(Class<?> clazz) {
        if (clazz == null) clazz = Object.class;
        String cname = clazz.getName();

        JAXBContext jaxbContext = JAXB_CONTEXT.get(cname);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
                JAXB_CONTEXT.putIfAbsent(cname, jaxbContext);
            } catch (JAXBException ex) {
                throw AppError.error("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {
        @XmlAnyElement
        protected Collection<?> collection;
    }

    private static RuntimeException unchecked(Exception e) {
        return new RuntimeException(e);
    }

    public static Class<?> getUserClass(Object instance) {
        Class<?> clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains("$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) return superClass;
        }
        return clazz;
    }
}
