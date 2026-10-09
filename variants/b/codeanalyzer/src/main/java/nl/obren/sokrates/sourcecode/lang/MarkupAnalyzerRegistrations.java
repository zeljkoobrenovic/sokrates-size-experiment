/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang;

import nl.obren.sokrates.sourcecode.lang.json.JsonAnalyzer;
import nl.obren.sokrates.sourcecode.lang.thrift.ThriftAnalyzer;
import nl.obren.sokrates.sourcecode.lang.xml.XmlAnalyzer;
import nl.obren.sokrates.sourcecode.lang.yaml.YamlAnalyzer;

import java.util.Map;

class MarkupAnalyzerRegistrations {
    static void registerJson(Map<String, Class> analyzersMap) {
        // json
        analyzersMap.put("json", JsonAnalyzer.class);
        analyzersMap.put("json5", JsonAnalyzer.class);
        analyzersMap.put("jsonld", JsonAnalyzer.class);
        analyzersMap.put("jsoniq", JsonAnalyzer.class);
        analyzersMap.put("avsc", JsonAnalyzer.class);
        analyzersMap.put("geojson", JsonAnalyzer.class);
        analyzersMap.put("gltf", JsonAnalyzer.class);
        analyzersMap.put("har", JsonAnalyzer.class);
        analyzersMap.put("ice", JsonAnalyzer.class);
        analyzersMap.put("JSON-tmLanguage", JsonAnalyzer.class);
        analyzersMap.put("jsonl", JsonAnalyzer.class);
        analyzersMap.put("mcmeta", JsonAnalyzer.class);
        analyzersMap.put("tf", YamlAnalyzer.class);
        analyzersMap.put("tfstate", JsonAnalyzer.class);
        analyzersMap.put("tfstate.backup", JsonAnalyzer.class);
        analyzersMap.put("topojson", JsonAnalyzer.class);
        analyzersMap.put("webapp", JsonAnalyzer.class);
        analyzersMap.put("webmanifest", JsonAnalyzer.class);
        analyzersMap.put("yy", JsonAnalyzer.class);
        analyzersMap.put("yyp", JsonAnalyzer.class);
        analyzersMap.put("jsonc", JsonAnalyzer.class);
        analyzersMap.put("sublime-build", JsonAnalyzer.class);
        analyzersMap.put("sublime-commands", JsonAnalyzer.class);
        analyzersMap.put("sublime-completions", JsonAnalyzer.class);
        analyzersMap.put("sublime-keymap", JsonAnalyzer.class);
        analyzersMap.put("sublime-macro", JsonAnalyzer.class);
        analyzersMap.put("sublime-menu", JsonAnalyzer.class);
        analyzersMap.put("sublime-mousemap", JsonAnalyzer.class);
        analyzersMap.put("sublime-project", JsonAnalyzer.class);
        analyzersMap.put("sublime-settings", JsonAnalyzer.class);
        analyzersMap.put("sublime-theme", JsonAnalyzer.class);
        analyzersMap.put("sublime-workspace", JsonAnalyzer.class);
        analyzersMap.put("sublime_metrics", JsonAnalyzer.class);
        analyzersMap.put("sublime_session", JsonAnalyzer.class);
    }

    static void registerXml(Map<String, Class> analyzersMap) {
        registerXmlPart1(analyzersMap);
        registerXmlPart2(analyzersMap);
        registerXmlPart3(analyzersMap);
    }

    private static void registerXmlPart1(Map<String, Class> analyzersMap) {
        analyzersMap.put("xml", XmlAnalyzer.class);
        analyzersMap.put("xaml", XmlAnalyzer.class);
        analyzersMap.put("owl", XmlAnalyzer.class);
        analyzersMap.put("adml", XmlAnalyzer.class);
        analyzersMap.put("admx", XmlAnalyzer.class);
        analyzersMap.put("ant", XmlAnalyzer.class);
        analyzersMap.put("axml", XmlAnalyzer.class);
        analyzersMap.put("builds", XmlAnalyzer.class);
        analyzersMap.put("ccproj", XmlAnalyzer.class);
        analyzersMap.put("ccxml", XmlAnalyzer.class);
        analyzersMap.put("clixml", XmlAnalyzer.class);
        analyzersMap.put("cproject", XmlAnalyzer.class);
        analyzersMap.put("cscfg", XmlAnalyzer.class);
        analyzersMap.put("csdef", XmlAnalyzer.class);
        analyzersMap.put("csl", XmlAnalyzer.class);
        analyzersMap.put("csproj", XmlAnalyzer.class);
        analyzersMap.put("ct", XmlAnalyzer.class);
        analyzersMap.put("depproj", XmlAnalyzer.class);
        analyzersMap.put("dita", XmlAnalyzer.class);
        analyzersMap.put("ditamap", XmlAnalyzer.class);
        analyzersMap.put("ditaval", XmlAnalyzer.class);
        analyzersMap.put("dll.config", XmlAnalyzer.class);
        analyzersMap.put("dotsettings", XmlAnalyzer.class);
        analyzersMap.put("filters", XmlAnalyzer.class);
        analyzersMap.put("fsproj", XmlAnalyzer.class);
        analyzersMap.put("fxml", XmlAnalyzer.class);
        analyzersMap.put("glade", XmlAnalyzer.class);
        analyzersMap.put("gml", XmlAnalyzer.class);
        analyzersMap.put("gmx", XmlAnalyzer.class);
        analyzersMap.put("grxml", XmlAnalyzer.class);
        analyzersMap.put("iml", XmlAnalyzer.class);
        analyzersMap.put("ivy", XmlAnalyzer.class);
        analyzersMap.put("jelly", XmlAnalyzer.class);
        analyzersMap.put("jsproj", XmlAnalyzer.class);
        analyzersMap.put("kml", XmlAnalyzer.class);
        analyzersMap.put("launch", XmlAnalyzer.class);
        analyzersMap.put("mdpolicy", XmlAnalyzer.class);
        analyzersMap.put("mjml", XmlAnalyzer.class);
    }

    private static void registerXmlPart2(Map<String, Class> analyzersMap) {
        analyzersMap.put("mod", XmlAnalyzer.class);
        analyzersMap.put("mxml", XmlAnalyzer.class);
        analyzersMap.put("natvis", XmlAnalyzer.class);
        analyzersMap.put("ncl", XmlAnalyzer.class);
        analyzersMap.put("ndproj", XmlAnalyzer.class);
        analyzersMap.put("nproj", XmlAnalyzer.class);
        analyzersMap.put("nuspec", XmlAnalyzer.class);
        analyzersMap.put("odd", XmlAnalyzer.class);
        analyzersMap.put("osm", XmlAnalyzer.class);
        analyzersMap.put("pkgproj", XmlAnalyzer.class);
        analyzersMap.put("pluginspec", XmlAnalyzer.class);
        analyzersMap.put("proj", XmlAnalyzer.class);
        analyzersMap.put("props", XmlAnalyzer.class);
        analyzersMap.put("ps1xml", XmlAnalyzer.class);
        analyzersMap.put("psc1", XmlAnalyzer.class);
        analyzersMap.put("pt", XmlAnalyzer.class);
        analyzersMap.put("rdf", XmlAnalyzer.class);
        analyzersMap.put("resx", XmlAnalyzer.class);
        analyzersMap.put("rss", XmlAnalyzer.class);
        analyzersMap.put("sch", XmlAnalyzer.class);
        analyzersMap.put("scxml", XmlAnalyzer.class);
        analyzersMap.put("sfproj", XmlAnalyzer.class);
        analyzersMap.put("shproj", XmlAnalyzer.class);
        analyzersMap.put("srdf", XmlAnalyzer.class);
        analyzersMap.put("storyboard", XmlAnalyzer.class);
        analyzersMap.put("sublime-snippet", XmlAnalyzer.class);
        analyzersMap.put("targets", XmlAnalyzer.class);
        analyzersMap.put("tml", XmlAnalyzer.class);
        analyzersMap.put("ui", XmlAnalyzer.class);
        analyzersMap.put("urdf", XmlAnalyzer.class);
        analyzersMap.put("ux", XmlAnalyzer.class);
        analyzersMap.put("vbproj", XmlAnalyzer.class);
        analyzersMap.put("vcxproj", XmlAnalyzer.class);
        analyzersMap.put("vsixmanifest", XmlAnalyzer.class);
        analyzersMap.put("vssettings", XmlAnalyzer.class);
        analyzersMap.put("vstemplate", XmlAnalyzer.class);
        analyzersMap.put("vxml", XmlAnalyzer.class);
        analyzersMap.put("wixproj", XmlAnalyzer.class);
    }

    private static void registerXmlPart3(Map<String, Class> analyzersMap) {
        analyzersMap.put("workflow", XmlAnalyzer.class);
        analyzersMap.put("wsdl", XmlAnalyzer.class);
        analyzersMap.put("wsf", XmlAnalyzer.class);
        analyzersMap.put("wxi", XmlAnalyzer.class);
        analyzersMap.put("wxl", XmlAnalyzer.class);
        analyzersMap.put("wxs", XmlAnalyzer.class);
        analyzersMap.put("x3d", XmlAnalyzer.class);
        analyzersMap.put("xacro", XmlAnalyzer.class);
        analyzersMap.put("xib", XmlAnalyzer.class);
        analyzersMap.put("xlf", XmlAnalyzer.class);
        analyzersMap.put("xliff", XmlAnalyzer.class);
        analyzersMap.put("xmi", XmlAnalyzer.class);
        analyzersMap.put("xml.dist", XmlAnalyzer.class);
        analyzersMap.put("xproj", XmlAnalyzer.class);
        analyzersMap.put("xsd", XmlAnalyzer.class);
        analyzersMap.put("xspec", XmlAnalyzer.class);
        analyzersMap.put("xul", XmlAnalyzer.class);
        analyzersMap.put("zcml", XmlAnalyzer.class);
        analyzersMap.put("plist", XmlAnalyzer.class);
        analyzersMap.put("stTheme", XmlAnalyzer.class);
        analyzersMap.put("tmCommand", XmlAnalyzer.class);
        analyzersMap.put("tmLanguage", XmlAnalyzer.class);
        analyzersMap.put("tmPreferences", XmlAnalyzer.class);
        analyzersMap.put("tmSnippet", XmlAnalyzer.class);
        analyzersMap.put("tmTheme", XmlAnalyzer.class);
        analyzersMap.put("xsp-config", XmlAnalyzer.class);
        analyzersMap.put("xpl", XmlAnalyzer.class);
        analyzersMap.put("xproc", XmlAnalyzer.class);
        analyzersMap.put("xquery", XmlAnalyzer.class);
        analyzersMap.put("xq", XmlAnalyzer.class);
        analyzersMap.put("xql", XmlAnalyzer.class);
        analyzersMap.put("xqm", XmlAnalyzer.class);
        analyzersMap.put("xqy", XmlAnalyzer.class);
        analyzersMap.put("xsl", XmlAnalyzer.class);
        analyzersMap.put("xslt", XmlAnalyzer.class);
        analyzersMap.put("thrift", ThriftAnalyzer.class);
    }
}
