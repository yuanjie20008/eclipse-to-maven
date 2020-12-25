package com.shri.eclipsetomaven.pom;

public class PomDependency {
	private final String groupId;
	private final String artifactId;
	private final String jarVersion;
	private final String scope;
	
	
	public PomDependency(String groupId, String artifactId, String jarVersion,String scope) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.jarVersion = jarVersion;
		this.scope=scope;
	}


	public String getGroupId() {
		return groupId;
	}


	public String getArtifactId() {
		return artifactId;
	}


	public String getJarVersion() {
		return jarVersion;
	}
	
	public String getScope() {
		return scope;
	}
}
