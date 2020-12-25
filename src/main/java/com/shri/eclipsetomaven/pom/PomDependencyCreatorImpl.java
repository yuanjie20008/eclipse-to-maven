package com.shri.eclipsetomaven.pom;

import static junit.framework.Assert.assertNotNull;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.shri.eclipsetomaven.util.ApplicationConfig;

public class PomDependencyCreatorImpl implements PomDependencyCreator {
    Document pomDoc;

    public PomDependencyCreatorImpl(Document pomDoc) {
        super();
        this.pomDoc = pomDoc;
    }

    private static String[] NUMBERS = { "-0", "-1", "-2", "-3", "-4", "-5", "-6", "-7",
            "-8", "-9" };

    @Override
	public void createPomDependencyFromClasspathEntry(
            Element dependenciesElement, String pathAttribute) {
        createPomDependencyFromClasspathEntry(dependenciesElement,
                pathAttribute, null, null,null);
    }


    @Override
	public void createPomDependencyFromClasspathEntry(
            Element dependenciesElement, String pathAttribute, String groupId,
            String artifactId,String version) {
        PomDependency pomDependency = createPomDependency(pathAttribute,
				groupId, artifactId,version);
        if (pomDependency!=null)
        	createDependencyElement(dependenciesElement, pomDependency);
    }


    PomDependency createPomDependency(String pathAttribute,
			String groupId, String artifactId,String version) {
		assertNotNull(pathAttribute);
		String jarVersion=version;
		String jarName = getJarName(pathAttribute);
		String scope=null;
		if (pathAttribute.indexOf("/lib/") ==-1){
			String mavenSetting = ApplicationConfig.INSTANCE.getValue(jarName);
			if (mavenSetting!=null&&mavenSetting.equalsIgnoreCase("NONE"))return null;
			if (mavenSetting!=null) {
				String splits[] = mavenSetting.split(";");
				if (splits.length>2) {
					groupId=splits[0];
					artifactId=splits[1];
					jarVersion=splits[2];
				}
				if (splits.length>3)
					scope=splits[3];
			}
		}
        if (artifactId == null) {
            artifactId = getArtifactId(jarName);
        }
        if (artifactId!=null&&artifactId.equalsIgnoreCase("EE_LIB"))return null;
        if (groupId == null) {
            groupId = artifactId;
        }
        if(jarVersion==null)
        	jarVersion = getJarVersion(jarName);
        PomDependency pomDependency = new PomDependency(groupId, artifactId, jarVersion,scope);
		return pomDependency;
	}

    String getArtifactId(String jarName) {
        int indexOfAny = StringUtils.indexOfAny(jarName, NUMBERS);
        if (indexOfAny != -1) {
            return jarName.substring(0, indexOfAny);
        } else {
        	int dotIndex = jarName.indexOf(".");
        	if(dotIndex != -1){
        		return jarName.substring(0, dotIndex);
        	}
			return jarName;
        }
    }

    String getJarName(String pathAttribute) {
        return pathAttribute.substring(pathAttribute.lastIndexOf('/') + 1);
    }

    String getJarVersion(String jarName) {
        int indexOfAny = StringUtils.indexOfAny(jarName, NUMBERS);
        int jarIndex = jarName.indexOf(".jar");
        if (jarIndex == -1) {
            jarIndex = jarName.indexOf(".zip");
        }
        if (indexOfAny != -1 && jarIndex != -1) {

            return jarName.substring(indexOfAny+1, jarIndex);
        }
        return "";
    }

    private void createDependencyElement(Element dependenciesElement,
    		PomDependency pomDependency) {
        Element dependencyElement = pomDoc.createElement("dependency");
        dependenciesElement.appendChild(dependencyElement);
        appendElement(dependencyElement, "groupId", pomDependency.getGroupId());
        appendElement(dependencyElement, "artifactId", pomDependency.getArtifactId());
        appendElement(dependencyElement, "version", pomDependency.getJarVersion());
        if (pomDependency.getScope() !=null)
        	appendElement(dependencyElement, "scope", pomDependency.getScope());
    }

    private void appendElement(Element dependencyElement, String tagName,
            String tagValue) {
        Element appendedElement = pomDoc.createElement(tagName);
        appendedElement.appendChild(pomDoc.createTextNode(tagValue));
        dependencyElement.appendChild(appendedElement);
    }
}
