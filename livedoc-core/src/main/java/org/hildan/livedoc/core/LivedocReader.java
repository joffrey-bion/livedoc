package org.hildan.livedoc.core;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.meta.LivedocMetaDataReader;
import org.hildan.livedoc.core.model.GlobalTemplateData;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.readers.GlobalDocReader;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.TypeScanner;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

/**
 * Builds a {@link Livedoc} object representing an API documentation by reading java classes and their annotations.
 */
public class LivedocReader {

    private final LivedocConfiguration configuration;

    private final MasterApiDocReader masterApiDocReader;

    private final MasterTypeDocReader masterTypeDocReader;

    private final GlobalDocReader globalDocReader;

    private final TypeScanner typeScanner;

    private final TemplateProvider templateProvider;

    private final TypeReferenceProvider typeReferenceProvider;

    /**
     * Creates a new {@link LivedocReader} with the given configuration. Using this constructor directly is not
     * recommended. Please consider using a {@link LivedocReaderBuilder} instead.
     *
     * @param configuration
     *         the configuration for the doc generation (including the packages to scan)
     * @param docReader
     *         the {@link DocReader} to use to generate the documentation for API operations
     * @param typeDocReader
     *         the {@link TypeDocReader} to use to generate the documentation for the types used in the API
     * @param globalDocReader
     *         the {@link GlobalDocReader} to use to generate the global documentation of a project (general info,
     *         flows, migrations)
     * @param typeScanner
     *         the {@link TypeScanner} to use to retrieve all the types that should be documented, starting from the
     *         types that are directly referenced in the API (as request or response body, mainly)
     * @param propertyScanner
     *         defines the properties of a type
     * @param templateProvider
     *         the {@link TemplateProvider} to use to create example objects for the types used in the API
     * @param typeReferenceProvider
     *         the {@link TypeReferenceProvider} to use to build {@link LivedocType}s for documentation elements
     *
     * @see LivedocReaderBuilder
     */
    public LivedocReader(LivedocConfiguration configuration, DocReader docReader, TypeDocReader typeDocReader,
            GlobalDocReader globalDocReader, TypeScanner typeScanner, PropertyScanner propertyScanner,
            TemplateProvider templateProvider, TypeReferenceProvider typeReferenceProvider) {
        this.configuration = configuration;
        this.masterApiDocReader = new MasterApiDocReader(docReader);
        this.masterTypeDocReader = new MasterTypeDocReader(typeDocReader, propertyScanner);
        this.globalDocReader = globalDocReader;
        this.typeScanner = typeScanner;
        this.templateProvider = templateProvider;
        this.typeReferenceProvider = typeReferenceProvider;
    }

    /**
     * Creates a new {@link LivedocReader} based on the given {@link LivedocConfiguration}.
     *
     * @param configuration
     *         the configuration for the doc generation (including the packages to scan)
     *
     * @return the newly created {@link LivedocReader}
     */
    public static LivedocReader basicAnnotationReader(LivedocConfiguration configuration) {
        return new LivedocReaderBuilder(configuration).build();
    }

    /**
     * Reads a {@link Livedoc} object by inspecting classes as configured.
     *
     * @param apiInfo
     *         the current version of the API
     *
     * @return a new {@link Livedoc} object representing the API
     */
    public Livedoc read(ApiMetaData apiInfo) {
        LivedocMetaData livedocInfo = LivedocMetaDataReader.read();

        Collection<ApiDoc> apiDocs = masterApiDocReader.readApiDocs(typeReferenceProvider, templateProvider);
        Set<Class<?>> types = getClassesToDocument();
        List<ApiTypeDoc> typeDocs = masterTypeDocReader.readApiTypeDocs(types, typeReferenceProvider, templateProvider);
        Set<ApiFlowDoc> flowDocs = globalDocReader.getApiFlowDocs(getAllApiOperationDocsById(apiDocs));

        GlobalTemplateData globalTemplateData = new GlobalTemplateData(apiInfo, livedocInfo, configuration);
        ApiGlobalDoc globalDoc = globalDocReader.getApiGlobalDoc(configuration, globalTemplateData);

        Livedoc livedoc = new Livedoc(livedocInfo, apiInfo);
        livedoc.setPlaygroundEnabled(configuration.isPlaygroundEnabled());
        livedoc.setDisplayMethodAs(configuration.getDisplayMethodAs());
        livedoc.setApis(Group.groupAndSort(apiDocs));
        livedoc.setTypes(Group.groupAndSort(typeDocs));
        livedoc.setFlows(Group.groupAndSort(flowDocs));
        livedoc.setGlobal(globalDoc);
        return livedoc;
    }

    private Map<String, ApiOperationDoc> getAllApiOperationDocsById(Collection<ApiDoc> apiDocs) {
        return apiDocs.stream()
                      .flatMap(api -> api.getOperations().stream())
                      .filter(m -> m.getId() != null && !m.getId().isEmpty())
                      .collect(Collectors.toMap(ApiOperationDoc::getId, x -> x));
    }

    private Set<Class<?>> getClassesToDocument() {
        Set<Type> rootTypes = typeReferenceProvider.getProvidedReferences();
        return typeScanner.findTypesToDocument(rootTypes);
    }

    // for testing purposes only
    public Optional<ApiDoc> readApiDoc(Class<?> controller) {
        return masterApiDocReader.readApiDoc(controller, typeReferenceProvider, templateProvider);
    }
}
