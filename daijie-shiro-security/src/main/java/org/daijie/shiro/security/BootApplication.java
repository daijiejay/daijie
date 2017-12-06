package org.daijie.shiro.security;

import org.daijie.shiro.annotation.EnableShiroSecurityServer;
import org.daijie.shiro.configure.ShiroConfigure;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableShiroSecurityServer(ShiroConfigure.class)
@SpringBootApplication
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
