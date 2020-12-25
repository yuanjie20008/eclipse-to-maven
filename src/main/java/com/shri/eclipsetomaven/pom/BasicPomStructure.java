package com.shri.eclipsetomaven.pom;

import static com.shri.eclipsetomaven.ApplicationPropertyConstants.MAVEN_MODULE_GROUP_ID;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.shri.eclipsetomaven.classpathentry.ClasspathConstants;
import com.shri.eclipsetomaven.util.ApplicationConfig;
import com.svashishtha.dom.DomEditor;

public class BasicPomStructure {
    
    private final Document pomDoc;
    private final File classpathRoot;
    private String srcPath;
    public BasicPomStructure(Document pomDoc, File classpathRoot,String srcPath) {
        super();
        this.pomDoc = pomDoc;
        this.classpathRoot = classpathRoot;
        this.srcPath=srcPath;
    }


    
    public Element createBasicPomStructure() {
        Element projectElement = pomDoc.createElement("project");
        pomDoc.appendChild(projectElement);
        addAttributesToProjectElement(projectElement);
        addDefaultElementsToProjectElement(projectElement);
        return projectElement;
    }

    private void addAttributesToProjectElement(Element projectElement) {
        DomEditor.addAttributeToElement(pomDoc, projectElement, PomConstants.ATT_XMLNS,
                PomConstants.ATT_VALUE_XMLNS);
        DomEditor.addAttributeToElement(pomDoc, projectElement, PomConstants.ATT_XMLNS_XSI,
                PomConstants.ATT_VALUE_XMLNS_XSI);
        DomEditor.addAttributeToElement(pomDoc, projectElement,
                PomConstants.ATT_XSI_SCHEMA_LOCATION,
                PomConstants.ATT_VALUE_XSI_SCHEMA_LOCATION);
    }

    private void addDefaultElementsToProjectElement(Element projectElement) {
        addModelVersion(projectElement);
        addGroupId(projectElement);
        addArtifactId(projectElement);
        addVersion(projectElement);
        boolean isWar = addPackaging(projectElement);
        addName(projectElement);
        addBuild(projectElement,isWar);
    }


    private void addGroupId(Element projectElement) {
        Element groupIdElement = pomDoc.createElement(PomConstants.GROUP_ID);
        projectElement.appendChild(groupIdElement);
        groupIdElement.appendChild(pomDoc.createTextNode(ApplicationConfig.INSTANCE.getValue(MAVEN_MODULE_GROUP_ID)));
    }



    private void addName(Element projectElement) {
        Element nameElement = pomDoc.createElement(PomConstants.ELT_NAME);
        projectElement.appendChild(nameElement);
        nameElement.appendChild(pomDoc.createTextNode(classpathRoot.getName()));
    }


    private boolean addPackaging(Element projectElement) {
        String packaging = PomConstants.PACKAGING_JAR;
        File j2eeFile = new File(classpathRoot, ClasspathConstants.FILE_J2EE);
        File webContent = new File(classpathRoot, ClasspathConstants.DIR_WEB_CONTENT);
        if(j2eeFile.exists() && webContent.exists()){
            packaging = PomConstants.PACKAGING_WAR;
        }
        if (classpathRoot.getAbsolutePath().endsWith("EximBillWeb")||
        	classpathRoot.getAbsolutePath().endsWith("BIRT")||
        	classpathRoot.getAbsolutePath().endsWith("CRReportWeb")||
        	classpathRoot.getAbsolutePath().endsWith("EximBillSSWeb"))
        	packaging = PomConstants.PACKAGING_WAR;
        Element packagingElement = pomDoc.createElement(PomConstants.ELT_PACKAGING);
        projectElement.appendChild(packagingElement);
        packagingElement.appendChild(pomDoc.createTextNode(packaging));
        return PomConstants.PACKAGING_WAR.equals(packaging);
    }


    private void addArtifactId(Element projectElement) {
        Element nameElement = pomDoc.createElement(PomConstants.ARTIFACT_ID);
        projectElement.appendChild(nameElement);
        String directoryName = classpathRoot.getName();
        String artifactId = StringUtils.remove(directoryName, ' ');
        nameElement.appendChild(pomDoc.createTextNode(artifactId));
    }

    private void addModelVersion(Element projectElement) {
        Element modelVersionElement = pomDoc.createElement(PomConstants.ELT_MODEL_VERSION);
        projectElement.appendChild(modelVersionElement);
        modelVersionElement.appendChild(pomDoc.createTextNode(PomConstants.MODEL_VERSION_VALUE));        
    }
    private void addVersion(Element projectElement) {
        Element versionElement = pomDoc.createElement(PomConstants.VERSION);
        projectElement.appendChild(versionElement);
        versionElement.appendChild(pomDoc.createTextNode("1.0"));        
    }
    
    private void addBuild(Element projectElement,boolean isWar) {
    	Element buildElement = pomDoc.createElement("build");
        projectElement.appendChild(buildElement);
        Element sourceDirectoryElement = pomDoc.createElement("sourceDirectory");
        buildElement.appendChild(sourceDirectoryElement);
        sourceDirectoryElement.appendChild(pomDoc.createTextNode(srcPath));
        Element pluginsElement = pomDoc.createElement("plugins");
        buildElement.appendChild(pluginsElement);
        Element pluginElement = pomDoc.createElement("plugin");
        pluginsElement.appendChild(pluginElement);
        Element groupIdElement = pomDoc.createElement("groupId");
        pluginElement.appendChild(groupIdElement);
        groupIdElement.appendChild(pomDoc.createTextNode("org.apache.maven.plugins"));   
        Element artifactIdElement = pomDoc.createElement("artifactId");
        pluginElement.appendChild(artifactIdElement);
        artifactIdElement.appendChild(pomDoc.createTextNode("maven-compiler-plugin"));   
        Element versionElement = pomDoc.createElement("version");
        pluginElement.appendChild(versionElement);
        versionElement.appendChild(pomDoc.createTextNode("3.8.0"));   
        Element configurationElement = pomDoc.createElement("configuration");
        pluginElement.appendChild(configurationElement);
        Element sourceElement = pomDoc.createElement("source");
        configurationElement.appendChild(sourceElement);
        sourceElement.appendChild(pomDoc.createTextNode("1.7"));   
        Element targetElement = pomDoc.createElement("target");
        configurationElement.appendChild(targetElement);
        targetElement.appendChild(pomDoc.createTextNode("1.7"));   
        Element encodingElement = pomDoc.createElement("encoding");
        configurationElement.appendChild(encodingElement);
        encodingElement.appendChild(pomDoc.createTextNode("UTF-8"));   
        if (isWar){
        
        	pluginElement = pomDoc.createElement("plugin");
            pluginsElement.appendChild(pluginElement);
            artifactIdElement = pomDoc.createElement("artifactId");
            pluginElement.appendChild(artifactIdElement);
            artifactIdElement.appendChild(pomDoc.createTextNode("maven-war-plugin"));   
            versionElement = pomDoc.createElement("version");
            pluginElement.appendChild(versionElement);
            versionElement.appendChild(pomDoc.createTextNode("3.2.1"));   
            configurationElement = pomDoc.createElement("configuration");
            pluginElement.appendChild(configurationElement);
            Element warSourceElement = pomDoc.createElement("warSourceDirectory");
            configurationElement.appendChild(warSourceElement);
            warSourceElement.appendChild(pomDoc.createTextNode("WebContent"));   
            Element finalNameElement = pomDoc.createElement("finalName");
            configurationElement.appendChild(finalNameElement);
            String finalame = classpathRoot.getName();
            finalame = StringUtils.remove(finalame, ' ');
            finalNameElement.appendChild(pomDoc.createTextNode(finalame));
        }      
    }
}