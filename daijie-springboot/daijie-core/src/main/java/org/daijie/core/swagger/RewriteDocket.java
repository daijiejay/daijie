package org.daijie.core.swagger;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.builders.BuilderDefaults.defaultIfAbsent;
import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;
import static springfox.documentation.schema.AlternateTypeRules.DIRECT_SUBSTITUTION_RULE_ORDER;
import static springfox.documentation.schema.AlternateTypeRules.GENERIC_SUBSTITUTION_RULE_ORDER;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.PathProvider;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.CodeGenGenericTypeNamingStrategy;
import springfox.documentation.schema.DefaultGenericTypeNamingStrategy;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spi.service.contexts.SecurityContext;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Ordering;

/**
 * 重写Docket，主要是解决官方对这个类中的属性和方法的访问权限限制
 * @author daijie_jay
 * @since 2018年1月2日
 */
@SuppressWarnings("rawtypes")
public class RewriteDocket implements DocumentationPlugin {

	  public static final String DEFAULT_GROUP_NAME = "default";

	  private final DocumentationType documentationType;
	  private final List<SecurityContext> securityContexts = newArrayList();
	  private final Map<RequestMethod, List<ResponseMessage>> responseMessages = newHashMap();
	  private final List<Parameter> globalOperationParameters = newArrayList();
	  private final List<Function<TypeResolver, AlternateTypeRule>> ruleBuilders = newArrayList();
	  private final Set<Class> ignorableParameterTypes = newHashSet();
	  private final Set<String> protocols = newHashSet();
	  private final Set<String> produces = newHashSet();
	  private final Set<String> consumes = newHashSet();
	  private final Set<ResolvedType> additionalModels = newHashSet();
	  private final Set<Tag> tags = newHashSet();

	  private PathProvider pathProvider;
	  private List<? extends SecurityScheme> securitySchemes;
	  private Ordering<ApiListingReference> apiListingReferenceOrdering;
	  private Ordering<ApiDescription> apiDescriptionOrdering;
	  private Ordering<Operation> operationOrdering;

	  private ApiInfo apiInfo = ApiInfo.DEFAULT;
	  private String groupName = DEFAULT_GROUP_NAME;
	  private boolean enabled = true;
	  private GenericTypeNamingStrategy genericsNamingStrategy = new DefaultGenericTypeNamingStrategy();
	  private boolean applyDefaultResponseMessages = true;
	  private String host = "";
	  private Optional<String> pathMapping = Optional.absent();
	  private ApiSelector apiSelector = ApiSelector.DEFAULT;
	  private boolean enableUrlTemplating = false;
	  private List<VendorExtension> vendorExtensions = newArrayList();


	  public RewriteDocket(DocumentationType documentationType) {
	    this.documentationType = documentationType;
	  }

	  public RewriteDocket extensions(List<VendorExtension> vendorExtensions) {
	    this.vendorExtensions.addAll(vendorExtensions);
	    return this;
	  }

	  public RewriteDocket apiInfo(ApiInfo apiInfo) {
	    this.apiInfo = defaultIfAbsent(apiInfo, apiInfo);
	    return this;
	  }

	  public RewriteDocket securitySchemes(List<? extends SecurityScheme> securitySchemes) {
	    this.securitySchemes = securitySchemes;
	    return this;
	  }

	  public RewriteDocket securityContexts(List<SecurityContext> securityContexts) {
	    this.securityContexts.addAll(securityContexts);
	    return this;
	  }

	  public RewriteDocket groupName(String groupName) {
	    this.groupName = defaultIfAbsent(groupName, this.groupName);
	    return this;
	  }

	  public RewriteDocket pathProvider(PathProvider pathProvider) {
	    this.pathProvider = pathProvider;
	    return this;
	  }

	  public RewriteDocket globalResponseMessage(RequestMethod requestMethod,
	                                      List<ResponseMessage> responseMessages) {

	    this.responseMessages.put(requestMethod, responseMessages);
	    return this;
	  }

	  public RewriteDocket globalOperationParameters(List<Parameter> operationParameters) {
	    this.globalOperationParameters.addAll(nullToEmptyList(operationParameters));
	    return this;
	  }

	  public RewriteDocket ignoredParameterTypes(Class... classes) {
	    this.ignorableParameterTypes.addAll(Arrays.asList(classes));
	    return this;
	  }

	  public RewriteDocket produces(Set<String> produces) {
	    this.produces.addAll(produces);
	    return this;
	  }

	  public RewriteDocket consumes(Set<String> consumes) {
	    this.consumes.addAll(consumes);
	    return this;
	  }

	  @Incubating("2.3")
	  public RewriteDocket host(String host) {
	    this.host = defaultIfAbsent(host, this.host);
	    return this;
	  }

	  public RewriteDocket protocols(Set<String> protocols) {
	    this.protocols.addAll(protocols);
	    return this;
	  }

	  public RewriteDocket alternateTypeRules(AlternateTypeRule... alternateTypeRules) {
	    this.ruleBuilders.addAll(from(newArrayList(alternateTypeRules)).transform(identityRuleBuilder()).toList());
	    return this;
	  }

	  public RewriteDocket operationOrdering(Ordering<Operation> operationOrdering) {
	    this.operationOrdering = operationOrdering;
	    return this;
	  }

	public RewriteDocket directModelSubstitute(final Class clazz, final Class with) {
	    this.ruleBuilders.add(newSubstitutionFunction(clazz, with));
	    return this;
	  }

	  public RewriteDocket genericModelSubstitutes(Class... genericClasses) {
	    for (Class clz : genericClasses) {
	      this.ruleBuilders.add(newGenericSubstitutionFunction(clz));
	    }
	    return this;
	  }

	  public RewriteDocket useDefaultResponseMessages(boolean apply) {
	    this.applyDefaultResponseMessages = apply;
	    return this;
	  }

	  public RewriteDocket apiListingReferenceOrdering(Ordering<ApiListingReference> apiListingReferenceOrdering) {
	    this.apiListingReferenceOrdering = apiListingReferenceOrdering;
	    return this;
	  }

	  public RewriteDocket apiDescriptionOrdering(Ordering<ApiDescription> apiDescriptionOrdering) {
	    this.apiDescriptionOrdering = apiDescriptionOrdering;
	    return this;
	  }

	  public RewriteDocket enable(boolean externallyConfiguredFlag) {
	    this.enabled = externallyConfiguredFlag;
	    return this;
	  }

	  public RewriteDocket forCodeGeneration(boolean forCodeGen) {
	    if (forCodeGen) {
	      genericsNamingStrategy = new CodeGenGenericTypeNamingStrategy();
	    }
	    return this;
	  }

	  public RewriteDocket pathMapping(String path) {
	    this.pathMapping = Optional.fromNullable(path);
	    return this;
	  }

	  @Incubating("2.1.0")
	  public RewriteDocket enableUrlTemplating(boolean enabled) {
	    this.enableUrlTemplating = enabled;
	    return this;
	  }

	  public RewriteDocket additionalModels(ResolvedType first, ResolvedType... remaining) {
	    additionalModels.add(first);
	    additionalModels.addAll(newHashSet(remaining));
	    return this;
	  }

	  public RewriteDocket tags(Tag first, Tag... remaining) {
	    tags.add(first);
	    tags.addAll(newHashSet(remaining));
	    return this;
	  }

	  public RewriteApiSelectorBuilder select() {
	    return new RewriteApiSelectorBuilder(this);
	  }

	  public DocumentationContext configure(DocumentationContextBuilder builder) {
	    return builder
	        .apiInfo(apiInfo)
	        .selector(apiSelector)
	        .applyDefaultResponseMessages(applyDefaultResponseMessages)
	        .additionalResponseMessages(responseMessages)
	        .additionalOperationParameters(globalOperationParameters)
	        .additionalIgnorableTypes(ignorableParameterTypes)
	        .ruleBuilders(ruleBuilders)
	        .groupName(groupName)
	        .pathProvider(pathProvider)
	        .securityContexts(securityContexts)
	        .securitySchemes(securitySchemes)
	        .apiListingReferenceOrdering(apiListingReferenceOrdering)
	        .apiDescriptionOrdering(apiDescriptionOrdering)
	        .operationOrdering(operationOrdering)
	        .produces(produces)
	        .consumes(consumes)
	        .host(host)
	        .protocols(protocols)
	        .genericsNaming(genericsNamingStrategy)
	        .pathMapping(pathMapping)
	        .enableUrlTemplating(enableUrlTemplating)
	        .additionalModels(additionalModels)
	        .tags(tags)
	        .vendorExtentions(vendorExtensions)
	        .build();
	  }

	  @Override
	  public String getGroupName() {
	    return groupName;
	  }

	  public boolean isEnabled() {
	    return enabled;
	  }

	  @Override
	  public DocumentationType getDocumentationType() {
	    return documentationType;
	  }

	  @Override
	  public boolean supports(DocumentationType delimiter) {
	    return documentationType.equals(delimiter);
	  }

	  private Function<AlternateTypeRule, Function<TypeResolver, AlternateTypeRule>> identityRuleBuilder() {
	    return new Function<AlternateTypeRule, Function<TypeResolver, AlternateTypeRule>>() {
	      @Override
	      public Function<TypeResolver, AlternateTypeRule> apply(AlternateTypeRule rule) {
	        return identityFunction(rule);
	      }
	    };
	  }

	  private Function<TypeResolver, AlternateTypeRule> identityFunction(final AlternateTypeRule rule) {
	    return new Function<TypeResolver, AlternateTypeRule>() {
	      @Override
	      public AlternateTypeRule apply(TypeResolver typeResolver) {
	        return rule;
	      }
	    };
	  }

	  RewriteDocket selector(ApiSelector apiSelector) {
	    this.apiSelector = apiSelector;
	    return this;
	  }

	  private Function<TypeResolver, AlternateTypeRule> newSubstitutionFunction(final Class clazz, final Class with) {
	    return new Function<TypeResolver, AlternateTypeRule>() {

	      @Override
	      public AlternateTypeRule apply(TypeResolver typeResolver) {
	        return newRule(
	            typeResolver.resolve(clazz),
	            typeResolver.resolve(with),
	            DIRECT_SUBSTITUTION_RULE_ORDER);
	      }
	    };
	  }

	  private Function<TypeResolver, AlternateTypeRule> newGenericSubstitutionFunction(final Class clz) {
	    return new Function<TypeResolver, AlternateTypeRule>() {
	      @Override
	      public AlternateTypeRule apply(TypeResolver typeResolver) {
	        return newRule(
	            typeResolver.resolve(clz, WildcardType.class),
	            typeResolver.resolve(WildcardType.class),
	            GENERIC_SUBSTITUTION_RULE_ORDER);
	      }
	    };
	  }
}
