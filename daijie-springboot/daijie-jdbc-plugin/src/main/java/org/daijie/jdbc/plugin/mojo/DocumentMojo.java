package org.daijie.jdbc.plugin.mojo;

import java.sql.SQLException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.daijie.jdbc.plugin.document.ExportDatabaseHtml;

@Mojo( name = "doc")
public class DocumentMojo extends AbstractMojo {
	
	@Parameter
	private String url;
	
	@Parameter
	private String path;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		System.out.println("------------------------url:"+url);
		System.out.println("------------------------path:"+path);
		ExportDatabaseHtml edh = new ExportDatabaseHtml();
		try {
			edh.getHtml(url, path);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
