package org.daijie.swagger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.contexts.ApiSelector;

import static com.google.common.base.Predicates.and;

/**
 * 重写ApiSelectorBuilder，主要是解决官方对这个类中的属性和方法的访问权限限制
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class RewriteApiSelectorBuilder {
	  private final RewriteDocket parent;
	  private Predicate<RequestHandler> requestHandlerSelector = ApiSelector.DEFAULT.getRequestHandlerSelector();
	  private Predicate<String> pathSelector = ApiSelector.DEFAULT.getPathSelector();

	  public RewriteApiSelectorBuilder(RewriteDocket parent) {
	    this.parent = parent;
	  }

	  public RewriteApiSelectorBuilder apis(Predicate<RequestHandler> selector) {
	    requestHandlerSelector = and(requestHandlerSelector, selector);
	    return this;
	  }

	  public RewriteApiSelectorBuilder paths(Predicate<String> selector) {
	    pathSelector = and(pathSelector, selector);
	    return this;
	  }

	  public RewriteDocket build() {
	    return parent.selector(new ApiSelector(combine(requestHandlerSelector, pathSelector), pathSelector));
	  }

	  private Predicate<RequestHandler> combine(Predicate<RequestHandler> requestHandlerSelector,
	      Predicate<String> pathSelector) {
	    return and(requestHandlerSelector, transform(pathSelector));
	  }

	  private Predicate<RequestHandler> transform(final Predicate<String> pathSelector) {
	    return new Predicate<RequestHandler>() {
	      @Override
	      public boolean apply(RequestHandler input) {
	        return Iterables.any(input.getPatternsCondition().getPatterns(), pathSelector);
	      }
	    };
	  }
}
