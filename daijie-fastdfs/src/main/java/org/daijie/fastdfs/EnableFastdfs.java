package org.daijie.fastdfs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.github.tobato.fastdfs.FdfsClientConfig;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
	FastdfsUtil.class, 
	FdfsClientConfig.class
	})
public @interface EnableFastdfs {

}
