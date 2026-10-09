/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang;

import nl.obren.sokrates.sourcecode.ExtensionGroupExtractor;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.SourceFileFilter;
import nl.obren.sokrates.sourcecode.analysis.AnalyzerOverride;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageAnalyzerFactory {
    private static final Log LOG = LogFactory.getLog(LanguageAnalyzerFactory.class);

    private static LanguageAnalyzerFactory instance = new LanguageAnalyzerFactory();
    private Map<String, Class> analyzersMap = new HashMap<>();
    private List<AnalyzerOverride> overrides = new ArrayList<>();

    private LanguageAnalyzerFactory() {
        LanguageAnalyzerRegistrations.register(analyzersMap);
    }

    public static LanguageAnalyzerFactory getInstance() {
        return instance;
    }

    public Map<String, Class> getAnalyzersMap() {
        return analyzersMap;
    }

    public List<AnalyzerOverride> getOverrides() {
        return overrides;
    }

    public void setOverrides(List<AnalyzerOverride> overrides) {
        this.overrides = overrides;
    }

    public LanguageAnalyzer getLanguageAnalyzerByExtension(String extension) {
        try {
            Class aClass = analyzersMap.get(extension);
            if (aClass != null) {
                return (LanguageAnalyzer) aClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error");
        }

        return new DefaultLanguageAnalyzer();
    }

    public LanguageAnalyzer getLanguageAnalyzer(SourceFile sourceFile) {
        return getLanguageAnalyzerByExtension(getAnalyzerKey(sourceFile));
    }

    private String getAnalyzerKey(SourceFile sourceFile) {
        for (AnalyzerOverride override : overrides) {
            boolean overridden = false;
            for (SourceFileFilter sourceFileFilter : override.getFilters()) {
                if (sourceFileFilter.matches(sourceFile) && !sourceFileFilter.getException()) {
                    overridden = true;
                } else if (sourceFileFilter.matches(sourceFile) && sourceFileFilter.getException()) {
                    overridden = false;
                    break;
                }
            }
            if (overridden) {
                return override.getAnalyzer();
            }
        }
        return ExtensionGroupExtractor.getExtension(sourceFile.getFile().getPath()).toLowerCase();
    }

}
