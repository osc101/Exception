package com.ldd600.exception.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ldd600.exception.base.BaseAppRuntimeException;
import com.ldd600.exception.base.handler.ExceptionHandler;
import com.ldd600.exception.context.ExceptionContext;
import com.ldd600.exception.context.CoreContextFactory;
import com.ldd600.exception.core.AnnotationBeanDefinitionReader;
import com.ldd600.exception.core.BeanReader;
import com.ldd600.exception.core.BeanReaderFactory;
import com.ldd600.exception.core.SimpleAnnotationBeanReader;
import com.ldd600.exception.util.ClassLoaderUtils;

public class ExceptionBeanDefinitionParser implements BeanDefinitionParser {
    @SuppressWarnings("unchecked")
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        try {
            removeWhitespaceNodes(element);
            NodeList nodes = element.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                final Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    final String localName = node.getLocalName();
                    if ("exceptions".equals(localName)) {
                        final ExceptionContext expCtx = CoreContextFactory.getInstance().getExceptionContext();
                        NodeList childs = node.getChildNodes();
                        for (int j = 0; j < childs.getLength(); j++) {
                            Node exceptionNode = childs.item(j);
                            if (exceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                                final String exceptionLocalName = exceptionNode.getLocalName();
                                if ("exception".equals(exceptionLocalName)) {
                                    NamedNodeMap expttrs = exceptionNode.getAttributes();
                                    Node errorCodeNode = expttrs.getNamedItem("errorCode");
                                    Node classNode = expttrs.getNamedItem("class");
                                    String errorCode = errorCodeNode.getNodeValue();
                                    System.out.println(errorCode);
                                    String expClassName = classNode.getNodeValue();
                                    System.out.println(expClassName);
                                    final Class<? extends BaseAppRuntimeException> expClazz = (Class<? extends BaseAppRuntimeException>) ClassLoaderUtils
                                            .getClassLoader().loadClass(expClassName);
                                    final ExceptionDefinition expDefinition = new ExceptionDefinition(errorCode);
                                    expCtx.setExceptionDefinition(expClazz, expDefinition);
                                    NodeList expchilds = exceptionNode.getChildNodes();
                                    for (int m = 0; m < expchilds.getLength(); m++) {
                                        Node expChild = expchilds.item(m);
                                        if (expChild.getNodeType() == Node.ELEMENT_NODE) {
                                            final String expChildLocalName = expChild.getLocalName();
                                            if ("handler".equals(expChildLocalName)) {
                                                System.out.println(expChild.getTextContent());
                                                String handlerClassName = expChild.getTextContent().trim();
                                                final Class<ExceptionHandler> handlerClazz = (Class<ExceptionHandler>) ClassLoaderUtils
                                                        .getClassLoader().loadClass(handlerClassName);
                                                expCtx.addExceptionHander(expClazz, handlerClazz);
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    } else if ("annotationAutoLoad".equals(localName)) {
                        List<String> includePackages = null;
                        List<String> excludePackages = null;
                        AnnotationBeanDefinitionReader annotationReader = new AnnotationBeanDefinitionReader();
                        boolean scanDirs = false;
                        NodeList childs = node.getChildNodes();
                        NamedNodeMap autoLoadAttrs = node.getAttributes();
                        Node scanDirsNode = autoLoadAttrs.getNamedItem("scanDirs");
                        String scanDirStr = scanDirsNode.getNodeValue();
                        System.out.println(scanDirStr);
                        scanDirs = Boolean.valueOf(scanDirStr).booleanValue();
                        Node jarMarkerFileNode = autoLoadAttrs.getNamedItem("jarMarkerFile");
                        String jarMarkerFileStr = jarMarkerFileNode.getNodeValue();
                        System.out.println(jarMarkerFileStr);
                        for (int j = 0; j < childs.getLength(); j++) {
                            Node autoLoadChild = childs.item(j);
                            if (autoLoadChild.getNodeType() == Node.ELEMENT_NODE) {
                                final String autoLoadChildLocalName = autoLoadChild.getLocalName();
                                if ("includePackage".equals(autoLoadChildLocalName)) {
                                    System.out.println(autoLoadChild.getTextContent());
                                    if (includePackages == null) {
                                        includePackages = new ArrayList<String>();
                                    }
                                    includePackages.add(autoLoadChild.getTextContent().trim());
                                } else if ("excludePackage".equals(autoLoadChildLocalName)) {
                                    if (excludePackages == null) {
                                        excludePackages = new ArrayList<String>();
                                    }
                                    excludePackages.add(autoLoadChild.getTextContent().trim());
                                } else if ("alternateAnnotation".equals(autoLoadChildLocalName)) {
                                    NamedNodeMap annotationAttrs = autoLoadChild.getAttributes();
                                    Node annotation = annotationAttrs.getNamedItem("annotation");
                                    System.out.println(annotation.getNodeValue());
                                    String annotationName = annotation.getNodeValue();
                                    BeanReader reader = null;
                                    NodeList beanReaders = autoLoadChild.getChildNodes();
                                    for (int m = 0; m < beanReaders.getLength(); m++) {
                                        Node beanReader = beanReaders.item(m);
                                        if (beanReader.getNodeType() == Node.ELEMENT_NODE) {
                                            final String beanReaderLocalName = beanReader.getLocalName();
                                            if ("beanReader".equals(beanReaderLocalName)) {
                                                NamedNodeMap readerAttrs = beanReader.getAttributes();
                                                Node autoWireNode = readerAttrs.getNamedItem("autoWire");
                                                System.out.println(autoWireNode.getNodeValue());
                                                Node lazyNode = readerAttrs.getNamedItem("lazy");
                                                System.out.println(lazyNode.getNodeValue());
                                                Node scopeNode = readerAttrs.getNamedItem("scope");
                                                System.out.println(scopeNode.getNodeValue());
                                                reader = BeanReaderFactory.getSimpleAnnotationBeanReader();
                                                SimpleAnnotationBeanReader simpleReader = (SimpleAnnotationBeanReader) reader;
                                                simpleReader.setAutoWireMode(autoWireNode.getNodeValue());
                                                simpleReader.setScope(scopeNode.getNodeValue());
                                                simpleReader.setLazy(Boolean.valueOf(lazyNode.getNodeValue()));
                                            }
                                        }
                                    }
                                    annotationReader.register(annotationName, reader);
                                }
                            }
                        }
                        annotationReader.loadBeansWithAnnotation(parserContext.getRegistry(),
                                jarMarkerFileStr, scanDirs, includePackages, excludePackages);
                    } 

                }
            }

        } catch (Exception ex) {
           
        }
        return null;
    }

    public void removeWhitespaceNodes(Element e) {
        NodeList children = e.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
                e.removeChild(child);
            } else if (child instanceof Element) {
                removeWhitespaceNodes((Element) child);
            }
        }
    }

}