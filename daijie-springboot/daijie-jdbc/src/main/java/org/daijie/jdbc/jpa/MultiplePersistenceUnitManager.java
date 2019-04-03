package org.daijie.jdbc.jpa;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Converter;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.index.CandidateComponentsIndex;
import org.springframework.context.index.CandidateComponentsIndexLoader;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

/**
 * 重写PersistenceUnitManager类
 * 创建多个PersistenceUnitInfo
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MultiplePersistenceUnitManager implements PersistenceUnitManager, ResourceLoaderAware, LoadTimeWeaverAware, InitializingBean {

	private static final String CLASS_RESOURCE_PATTERN = "/**/*.class";

	private static final String PACKAGE_INFO_SUFFIX = ".package-info";

	private static final String DEFAULT_ORM_XML_RESOURCE = "META-INF/orm.xml";

	private static final String PERSISTENCE_XML_FILENAME = "persistence.xml";

	public final static String DEFAULT_PERSISTENCE_XML_LOCATION = "classpath*:META-INF/" + PERSISTENCE_XML_FILENAME;

	public final static String ORIGINAL_DEFAULT_PERSISTENCE_UNIT_ROOT_LOCATION = "classpath:";

	public final static String ORIGINAL_DEFAULT_PERSISTENCE_UNIT_NAME = "default";


	private static final Set<AnnotationTypeFilter> entityTypeFilters;

	static {
		entityTypeFilters = new LinkedHashSet<>(4);
		entityTypeFilters.add(new AnnotationTypeFilter(Entity.class, false));
		entityTypeFilters.add(new AnnotationTypeFilter(Embeddable.class, false));
		entityTypeFilters.add(new AnnotationTypeFilter(MappedSuperclass.class, false));
		entityTypeFilters.add(new AnnotationTypeFilter(Converter.class, false));
	}


	protected final Log logger = LogFactory.getLog(getClass());

	private String[] persistenceXmlLocations = new String[] {DEFAULT_PERSISTENCE_XML_LOCATION};

	@Nullable
	private String defaultPersistenceUnitRootLocation = ORIGINAL_DEFAULT_PERSISTENCE_UNIT_ROOT_LOCATION;

	@Nullable
	private String defaultPersistenceUnitName = ORIGINAL_DEFAULT_PERSISTENCE_UNIT_NAME;

	@Nullable
	private String[] packagesToScan;

	@Nullable
	private String[] mappingResources;

	@Nullable
	private SharedCacheMode sharedCacheMode;

	@Nullable
	private ValidationMode validationMode;

	private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

	@Nullable
	private DataSource defaultDataSource;

	@Nullable
	private DataSource defaultJtaDataSource;

	@Nullable
	private PersistenceUnitPostProcessor[] persistenceUnitPostProcessors;

	@Nullable
	private LoadTimeWeaver loadTimeWeaver;

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Nullable
	private CandidateComponentsIndex componentsIndex;

	private final Set<String> persistenceUnitInfoNames = new HashSet<>();

	private final Map<String, PersistenceUnitInfo> persistenceUnitInfos = new HashMap<>();
	
	public void setPersistenceXmlLocation(String persistenceXmlLocation) {
		this.persistenceXmlLocations = new String[] {persistenceXmlLocation};
	}

	public void setPersistenceXmlLocations(String... persistenceXmlLocations) {
		this.persistenceXmlLocations = persistenceXmlLocations;
	}

	public void setDefaultPersistenceUnitRootLocation(String defaultPersistenceUnitRootLocation) {
		this.defaultPersistenceUnitRootLocation = defaultPersistenceUnitRootLocation;
	}

	public void setDefaultPersistenceUnitName(String defaultPersistenceUnitName) {
		this.defaultPersistenceUnitName = defaultPersistenceUnitName;
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void setMappingResources(String... mappingResources) {
		this.mappingResources = mappingResources;
	}

	public void setSharedCacheMode(SharedCacheMode sharedCacheMode) {
		this.sharedCacheMode = sharedCacheMode;
	}

	public void setValidationMode(ValidationMode validationMode) {
		this.validationMode = validationMode;
	}

	public void setDataSources(Map<String, DataSource> dataSources) {
		this.dataSourceLookup = new MapDataSourceLookup(dataSources);
	}

	public void setDataSourceLookup(@Nullable DataSourceLookup dataSourceLookup) {
		this.dataSourceLookup = (dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup());
	}

	@Nullable
	public DataSourceLookup getDataSourceLookup() {
		return this.dataSourceLookup;
	}

	public void setDefaultDataSource(@Nullable DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	@Nullable
	public DataSource getDefaultDataSource() {
		return this.defaultDataSource;
	}

	public void setDefaultJtaDataSource(@Nullable DataSource defaultJtaDataSource) {
		this.defaultJtaDataSource = defaultJtaDataSource;
	}

	@Nullable
	public DataSource getDefaultJtaDataSource() {
		return this.defaultJtaDataSource;
	}

	public void setPersistenceUnitPostProcessors(@Nullable PersistenceUnitPostProcessor... postProcessors) {
		this.persistenceUnitPostProcessors = postProcessors;
	}

	@Nullable
	public PersistenceUnitPostProcessor[] getPersistenceUnitPostProcessors() {
		return this.persistenceUnitPostProcessors;
	}

	@Override
	public void setLoadTimeWeaver(@Nullable LoadTimeWeaver loadTimeWeaver) {
		this.loadTimeWeaver = loadTimeWeaver;
	}

	@Nullable
	public LoadTimeWeaver getLoadTimeWeaver() {
		return this.loadTimeWeaver;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.componentsIndex = CandidateComponentsIndexLoader.loadIndex(resourceLoader.getClassLoader());
	}


	@Override
	public void afterPropertiesSet() {
		if (this.loadTimeWeaver == null && InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
			this.loadTimeWeaver = new InstrumentationLoadTimeWeaver(this.resourcePatternResolver.getClassLoader());
		}
		preparePersistenceUnitInfos();
	}

	public void preparePersistenceUnitInfos() {
		this.persistenceUnitInfoNames.clear();
		this.persistenceUnitInfos.clear();

		List<SpringPersistenceUnitInfo> puis = readPersistenceUnitInfos();
		for (SpringPersistenceUnitInfo pui : puis) {
			if (pui.getPersistenceUnitRootUrl() == null) {
				pui.setPersistenceUnitRootUrl(determineDefaultPersistenceUnitRootUrl());
			}
			if (pui.getJtaDataSource() == null && this.defaultJtaDataSource != null) {
				pui.setJtaDataSource(this.defaultJtaDataSource);
			}
			if (pui.getNonJtaDataSource() == null && this.defaultDataSource != null) {
				if (this.defaultDataSource instanceof DefaultRoutingDataSource) {
					pui.setNonJtaDataSource(((DefaultRoutingDataSource) this.defaultDataSource).getTargetDataSources()
							.get(pui.getPersistenceUnitName()));
				} else {
					pui.setNonJtaDataSource(this.defaultDataSource);
				}
			}
			if (this.sharedCacheMode != null) {
				pui.setSharedCacheMode(this.sharedCacheMode);
			}
			if (this.validationMode != null) {
				pui.setValidationMode(this.validationMode);
			}
			if (this.loadTimeWeaver != null) {
				pui.init(this.loadTimeWeaver);
			}
			else {
				pui.init(this.resourcePatternResolver.getClassLoader());
			}
			postProcessPersistenceUnitInfo(pui);
			String name = pui.getPersistenceUnitName();
			if (!this.persistenceUnitInfoNames.add(name) && !isPersistenceUnitOverrideAllowed()) {
				StringBuilder msg = new StringBuilder();
				msg.append("Conflicting persistence unit definitions for name '").append(name).append("': ");
				msg.append(pui.getPersistenceUnitRootUrl()).append(", ");
				msg.append(this.persistenceUnitInfos.get(name).getPersistenceUnitRootUrl());
				throw new IllegalStateException(msg.toString());
			}
			this.persistenceUnitInfos.put(name, pui);
		}
	}

	private List<SpringPersistenceUnitInfo> readPersistenceUnitInfos() {
		List<SpringPersistenceUnitInfo> infos = new LinkedList<>();
		boolean buildDefaultUnit = (this.packagesToScan != null || this.mappingResources != null);

		if (getDefaultDataSource() instanceof DefaultRoutingDataSource) {
			DefaultRoutingDataSource dataSource = (DefaultRoutingDataSource) getDefaultDataSource();
			Iterator<Entry<Object, DataSource>> iterator = dataSource.getTargetDataSources().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Object, DataSource> entry = iterator.next();
				
				SpringPersistenceUnitInfo scannedUnit = buildDefaultPersistenceUnitInfo();
				scannedUnit.setPersistenceUnitName(entry.getKey().toString());
				infos.add(scannedUnit);
			}
		} else if (buildDefaultUnit) {
			infos.add(buildDefaultPersistenceUnitInfo());
		}
		this.defaultPersistenceUnitName = infos.get(0).getPersistenceUnitName();
		return infos;
	}

	private SpringPersistenceUnitInfo buildDefaultPersistenceUnitInfo() {
		SpringPersistenceUnitInfo scannedUnit = new SpringPersistenceUnitInfo();
		if (this.defaultPersistenceUnitName != null) {
			scannedUnit.setPersistenceUnitName(this.defaultPersistenceUnitName);
		}
		scannedUnit.setExcludeUnlistedClasses(true);
		if (this.packagesToScan != null) {
			for (String pkg : this.packagesToScan) {
				scanPackage(scannedUnit, pkg);
			}
		}
		return scannedUnit;
	}

	private void scanPackage(SpringPersistenceUnitInfo scannedUnit, String pkg) {
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					ClassUtils.convertClassNameToResourcePath(pkg) + CLASS_RESOURCE_PATTERN;
			Resource[] resources = this.resourcePatternResolver.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader reader = readerFactory.getMetadataReader(resource);
					String className = reader.getClassMetadata().getClassName();
					if (matchesFilter(reader, readerFactory)) {
						scannedUnit.addManagedClassName(className);
						if (scannedUnit.getPersistenceUnitRootUrl() == null) {
							URL url = resource.getURL();
							if (ResourceUtils.isJarURL(url)) {
								scannedUnit.setPersistenceUnitRootUrl(ResourceUtils.extractJarFileURL(url));
							}
						}
					}
					else if (className.endsWith(PACKAGE_INFO_SUFFIX)) {
						scannedUnit.addManagedPackage(
								className.substring(0, className.length() - PACKAGE_INFO_SUFFIX.length()));
					}
				}
			}
		}
		catch (IOException ex) {
			throw new PersistenceException("Failed to scan classpath for unlisted entity classes", ex);
		}
	}

	private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
		for (TypeFilter filter : entityTypeFilters) {
			if (filter.match(reader, readerFactory)) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	private URL determineDefaultPersistenceUnitRootUrl() {
		if (this.defaultPersistenceUnitRootLocation == null) {
			return null;
		}
		try {
			URL url = this.resourcePatternResolver.getResource(this.defaultPersistenceUnitRootLocation).getURL();
			return (ResourceUtils.isJarURL(url) ? ResourceUtils.extractJarFileURL(url) : url);
		}
		catch (IOException ex) {
			throw new PersistenceException("Unable to resolve persistence unit root URL", ex);
		}
	}

	@Nullable
	private Resource getOrmXmlForDefaultPersistenceUnit() {
		Resource ormXml = this.resourcePatternResolver.getResource(
				this.defaultPersistenceUnitRootLocation + DEFAULT_ORM_XML_RESOURCE);
		if (ormXml.exists()) {
			try {
				Resource persistenceXml = ormXml.createRelative(PERSISTENCE_XML_FILENAME);
				if (!persistenceXml.exists()) {
					return ormXml;
				}
			}
			catch (IOException ex) {
				// Cannot resolve relative persistence.xml file - let's assume it's not there.
				return ormXml;
			}
		}
		return null;
	}

	@Nullable
	protected final MutablePersistenceUnitInfo getPersistenceUnitInfo(String persistenceUnitName) {
		PersistenceUnitInfo pui = this.persistenceUnitInfos.get(persistenceUnitName);
		return (MutablePersistenceUnitInfo) pui;
	}
	
	@Nullable
	protected final MutablePersistenceUnitInfo getPersistenceUnitInfo() {
		PersistenceUnitInfo pui = this.persistenceUnitInfos.get(this.defaultPersistenceUnitName);
		return (MutablePersistenceUnitInfo) pui;
	}

	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		PersistenceUnitPostProcessor[] postProcessors = getPersistenceUnitPostProcessors();
		if (postProcessors != null) {
			for (PersistenceUnitPostProcessor postProcessor : postProcessors) {
				postProcessor.postProcessPersistenceUnitInfo(pui);
			}
		}
	}

	protected boolean isPersistenceUnitOverrideAllowed() {
		return false;
	}


	@Override
	public PersistenceUnitInfo obtainDefaultPersistenceUnitInfo() {
		if (this.persistenceUnitInfoNames.isEmpty()) {
			throw new IllegalStateException("No persistence units parsed from " +
					ObjectUtils.nullSafeToString(this.persistenceXmlLocations));
		}
		if (this.persistenceUnitInfos.isEmpty()) {
			throw new IllegalStateException("All persistence units from " +
					ObjectUtils.nullSafeToString(this.persistenceXmlLocations) + " already obtained");
		}
		if (this.persistenceUnitInfos.size() > 1 && this.defaultPersistenceUnitName != null) {
			return obtainPersistenceUnitInfo(this.defaultPersistenceUnitName);
		}
		PersistenceUnitInfo pui = this.persistenceUnitInfos.values().iterator().next();
		this.persistenceUnitInfos.clear();
		return pui;
	}

	@Override
	public PersistenceUnitInfo obtainPersistenceUnitInfo(String persistenceUnitName) {
		PersistenceUnitInfo pui = this.persistenceUnitInfos.remove(persistenceUnitName);
		return pui;
	}
}
