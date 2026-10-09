/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.sourcecode.lang;

import nl.obren.sokrates.sourcecode.lang.abap.AbapAnalyzer;
import nl.obren.sokrates.sourcecode.lang.adabasnatural.AdabasNaturalAnalyzer;
import nl.obren.sokrates.sourcecode.lang.cfg.CfgAnalyzer;
import nl.obren.sokrates.sourcecode.lang.clojure.ClojureLangAnalyzer;
import nl.obren.sokrates.sourcecode.lang.cpp.CStyleAnalyzer;
import nl.obren.sokrates.sourcecode.lang.cpp.CppAnalyzer;
import nl.obren.sokrates.sourcecode.lang.csharp.CSharpAnalyzer;
import nl.obren.sokrates.sourcecode.lang.css.CssAnalyzer;
import nl.obren.sokrates.sourcecode.lang.d.DAnalyzer;
import nl.obren.sokrates.sourcecode.lang.dbc.DbcAnalyzer;
import nl.obren.sokrates.sourcecode.lang.go.GoLangAnalyzer;
import nl.obren.sokrates.sourcecode.lang.gradle.GradleAnalyzer;
import nl.obren.sokrates.sourcecode.lang.groovy.GroovyAnalyzer;
import nl.obren.sokrates.sourcecode.lang.hack.HackAnalyzer;
import nl.obren.sokrates.sourcecode.lang.html.HtmlAnalyzer;
import nl.obren.sokrates.sourcecode.lang.java.JavaAnalyzer;
import nl.obren.sokrates.sourcecode.lang.js.JavaScriptAnalyzer;
import nl.obren.sokrates.sourcecode.lang.jsp.JspAnalyzer;
import nl.obren.sokrates.sourcecode.lang.julia.JuliaAnalyzer;
import nl.obren.sokrates.sourcecode.lang.kotlin.KotlinAnalyzer;
import nl.obren.sokrates.sourcecode.lang.less.LessAnalyzer;
import nl.obren.sokrates.sourcecode.lang.lua.LuaAnalyzer;
import nl.obren.sokrates.sourcecode.lang.objectpascal.ObjectPascalAnalyzer;
import nl.obren.sokrates.sourcecode.lang.perl.PerlAnalyzer;
import nl.obren.sokrates.sourcecode.lang.php.PhpAnalyzer;
import nl.obren.sokrates.sourcecode.lang.plsql.PlSqlAnalyzer;
import nl.obren.sokrates.sourcecode.lang.puppet.PuppetAnalyzer;
import nl.obren.sokrates.sourcecode.lang.python.PythonAnalyzer;
import nl.obren.sokrates.sourcecode.lang.r.RAnalyzer;
import nl.obren.sokrates.sourcecode.lang.ruby.RubyAnalyzer;
import nl.obren.sokrates.sourcecode.lang.rust.RustAnalyzer;
import nl.obren.sokrates.sourcecode.lang.sass.SassAnalyzer;
import nl.obren.sokrates.sourcecode.lang.scala.ScalaAnalyzer;
import nl.obren.sokrates.sourcecode.lang.scss.ScssAnalyzer;
import nl.obren.sokrates.sourcecode.lang.shell.ShellAnalyzer;
import nl.obren.sokrates.sourcecode.lang.sql.SqlAnalyzer;
import nl.obren.sokrates.sourcecode.lang.swift.SwiftAnalyzer;
import nl.obren.sokrates.sourcecode.lang.ts.TypeScriptAnalyzer;
import nl.obren.sokrates.sourcecode.lang.vb.VisualBasicAnalyzer;
import nl.obren.sokrates.sourcecode.lang.yaml.YamlAnalyzer;

import java.util.Map;

class LanguageAnalyzerRegistrations {
    static void register(Map<String, Class> analyzersMap) {
        registerJavaScriptFamilyAndGo(analyzersMap);
        registerCFamilyAndLua(analyzersMap);
        registerPhpAndHack(analyzersMap);
        registerSqlPythonAndMarkup(analyzersMap);
        registerRemainingLanguages(analyzersMap);
    }

    private static void registerJavaScriptFamilyAndGo(Map<String, Class> analyzersMap) {
        // java
        analyzersMap.put("java", JavaAnalyzer.class);
        analyzersMap.put("ck", JavaAnalyzer.class);
        analyzersMap.put("j", JavaAnalyzer.class);
        analyzersMap.put("uc", JavaAnalyzer.class);

        // javascript
        analyzersMap.put("js", JavaScriptAnalyzer.class);
        analyzersMap.put("cy", JavaScriptAnalyzer.class);
        analyzersMap.put("jsx", HtmlAnalyzer.class);
        analyzersMap.put("_js", JavaScriptAnalyzer.class);
        analyzersMap.put("bones", JavaScriptAnalyzer.class);
        analyzersMap.put("cjs", JavaScriptAnalyzer.class);
        analyzersMap.put("es", JavaScriptAnalyzer.class);
        analyzersMap.put("es6", JavaScriptAnalyzer.class);
        analyzersMap.put("frag", JavaScriptAnalyzer.class);
        analyzersMap.put("gs", JavaScriptAnalyzer.class);
        analyzersMap.put("jake", JavaScriptAnalyzer.class);
        analyzersMap.put("jsb", JavaScriptAnalyzer.class);
        analyzersMap.put("jscad", JavaScriptAnalyzer.class);
        analyzersMap.put("jsfl", JavaScriptAnalyzer.class);
        analyzersMap.put("jsm", JavaScriptAnalyzer.class);
        analyzersMap.put("njs", JavaScriptAnalyzer.class);
        analyzersMap.put("pac", JavaScriptAnalyzer.class);
        analyzersMap.put("sjs", JavaScriptAnalyzer.class);
        analyzersMap.put("ssjs", JavaScriptAnalyzer.class);
        analyzersMap.put("xsjs", JavaScriptAnalyzer.class);
        analyzersMap.put("xsjslib", JavaScriptAnalyzer.class);

        // typescript
        analyzersMap.put("ts", TypeScriptAnalyzer.class);
        analyzersMap.put("tsx", TypeScriptAnalyzer.class);

        analyzersMap.put("go", GoLangAnalyzer.class);
        analyzersMap.put("v", GoLangAnalyzer.class);
    }

    private static void registerCFamilyAndLua(Map<String, Class> analyzersMap) {
        // C#
        analyzersMap.put("cs", CSharpAnalyzer.class);
        analyzersMap.put("cake", CSharpAnalyzer.class);
        analyzersMap.put("csx", CSharpAnalyzer.class);

        analyzersMap.put("lua", LuaAnalyzer.class);
        analyzersMap.put("nse", LuaAnalyzer.class);
        analyzersMap.put("p8", LuaAnalyzer.class);
        analyzersMap.put("pd_lua", LuaAnalyzer.class);
        analyzersMap.put("rbxs", LuaAnalyzer.class);
        analyzersMap.put("rockspec", LuaAnalyzer.class);
        analyzersMap.put("wlua", LuaAnalyzer.class);

        analyzersMap.put("d", DAnalyzer.class);
        analyzersMap.put("di", DAnalyzer.class);

        // c
        analyzersMap.put("c", CStyleAnalyzer.class);
        analyzersMap.put("cats", CStyleAnalyzer.class);
        analyzersMap.put("idc", CStyleAnalyzer.class);

        // cpp
        analyzersMap.put("cpp", CppAnalyzer.class);
        analyzersMap.put("c++", CppAnalyzer.class);
        analyzersMap.put("cc", CppAnalyzer.class);
        analyzersMap.put("cp", CppAnalyzer.class);
        analyzersMap.put("cxx", CppAnalyzer.class);
        analyzersMap.put("h", CppAnalyzer.class);
        analyzersMap.put("h++", CppAnalyzer.class);
        analyzersMap.put("hh", CppAnalyzer.class);
        analyzersMap.put("hpp", CppAnalyzer.class);
        analyzersMap.put("hxx", CppAnalyzer.class);
        analyzersMap.put("inl", CppAnalyzer.class);
        analyzersMap.put("ino", CppAnalyzer.class);
        analyzersMap.put("ipp", CppAnalyzer.class);
        analyzersMap.put("re", CppAnalyzer.class);
        analyzersMap.put("tcc", CppAnalyzer.class);
        analyzersMap.put("tpp", CppAnalyzer.class);
        analyzersMap.put("m", CppAnalyzer.class);
        analyzersMap.put("mm", CppAnalyzer.class);
        analyzersMap.put("dart", CppAnalyzer.class);
    }

    private static void registerPhpAndHack(Map<String, Class> analyzersMap) {
        // php
        analyzersMap.put("php", PhpAnalyzer.class);
        analyzersMap.put("inc", PhpAnalyzer.class);
        analyzersMap.put("php3", PhpAnalyzer.class);
        analyzersMap.put("php4", PhpAnalyzer.class);
        analyzersMap.put("php5", PhpAnalyzer.class);
        analyzersMap.put("phps", PhpAnalyzer.class);
        analyzersMap.put("phpt", PhpAnalyzer.class);
        analyzersMap.put("ctp", PhpAnalyzer.class);
        analyzersMap.put("aw", PhpAnalyzer.class);

        // Hack
        analyzersMap.put("hack", HackAnalyzer.class);
    }

    private static void registerSqlPythonAndMarkup(Map<String, Class> analyzersMap) {
        registerPlSql(analyzersMap);
        registerPython(analyzersMap);

        // scala
        analyzersMap.put("scala", ScalaAnalyzer.class);
        analyzersMap.put("kojo", ScalaAnalyzer.class);
        analyzersMap.put("sbt", ScalaAnalyzer.class);
        analyzersMap.put("sc", ScalaAnalyzer.class);
        registerHtml(analyzersMap);

        // asp
        analyzersMap.put("asp", HtmlAnalyzer.class);
        analyzersMap.put("aspx", HtmlAnalyzer.class);
        analyzersMap.put("asax", HtmlAnalyzer.class);
        analyzersMap.put("ascx", HtmlAnalyzer.class);
        analyzersMap.put("ashx", HtmlAnalyzer.class);
        analyzersMap.put("asmx", HtmlAnalyzer.class);
        analyzersMap.put("axd", HtmlAnalyzer.class);

        MarkupAnalyzerRegistrations.registerXml(analyzersMap);

        // perl
        analyzersMap.put("pl", PerlAnalyzer.class);
        analyzersMap.put("al", PerlAnalyzer.class);
        analyzersMap.put("perl", PerlAnalyzer.class);
        analyzersMap.put("ph", PerlAnalyzer.class);
        analyzersMap.put("plx", PerlAnalyzer.class);
        analyzersMap.put("pm", PerlAnalyzer.class);
        analyzersMap.put("psgi", PerlAnalyzer.class);
        analyzersMap.put("t", PerlAnalyzer.class);

        registerRuby(analyzersMap);

        // groovy
        analyzersMap.put("groovy", GroovyAnalyzer.class);
        analyzersMap.put("grt", GroovyAnalyzer.class);
        analyzersMap.put("gtpl", GroovyAnalyzer.class);
        analyzersMap.put("gvy", GroovyAnalyzer.class);

        // gradle
        analyzersMap.put("gradle", GradleAnalyzer.class);

        analyzersMap.put("css", CssAnalyzer.class);
        analyzersMap.put("less", LessAnalyzer.class);
        analyzersMap.put("sass", SassAnalyzer.class);
        analyzersMap.put("scss", ScssAnalyzer.class);
        MarkupAnalyzerRegistrations.registerJson(analyzersMap);

        analyzersMap.put("gsp", JspAnalyzer.class);
        analyzersMap.put("jsp", JspAnalyzer.class);

        registerVisualBasic(analyzersMap);
    }

    private static void registerRemainingLanguages(Map<String, Class> analyzersMap) {
        registerClojure(analyzersMap);

        analyzersMap.put("swift", SwiftAnalyzer.class);

        // kotlin
        analyzersMap.put("kt", KotlinAnalyzer.class);
        analyzersMap.put("ktm", KotlinAnalyzer.class);
        analyzersMap.put("kts", KotlinAnalyzer.class);

        registerSql(analyzersMap);

        // shell
        analyzersMap.put("sh", ShellAnalyzer.class);
        analyzersMap.put("bash", ShellAnalyzer.class);
        analyzersMap.put("bats", ShellAnalyzer.class);
        analyzersMap.put("command", ShellAnalyzer.class);
        analyzersMap.put("ksh", ShellAnalyzer.class);
        analyzersMap.put("tmux", ShellAnalyzer.class);
        analyzersMap.put("tool", ShellAnalyzer.class);
        analyzersMap.put("zsh", ShellAnalyzer.class);

        analyzersMap.put("dbc", DbcAnalyzer.class);
        analyzersMap.put("cfg", CfgAnalyzer.class);

        registerYaml(analyzersMap);

        registerR(analyzersMap);

        analyzersMap.put("jl", JuliaAnalyzer.class);

        analyzersMap.put("rs", RustAnalyzer.class);
        analyzersMap.put("in", RustAnalyzer.class);
        analyzersMap.put("rlib", RustAnalyzer.class);

        analyzersMap.put("pas", ObjectPascalAnalyzer.class);
        analyzersMap.put("pp", ObjectPascalAnalyzer.class);
        analyzersMap.put("p", ObjectPascalAnalyzer.class);
        analyzersMap.put("dfm", ObjectPascalAnalyzer.class);
        analyzersMap.put("dpr", ObjectPascalAnalyzer.class);
        analyzersMap.put("lpr", ObjectPascalAnalyzer.class);
        analyzersMap.put("pascal", ObjectPascalAnalyzer.class);

        analyzersMap.put("nsp", AdabasNaturalAnalyzer.class);
        analyzersMap.put("nsm", AdabasNaturalAnalyzer.class);
        analyzersMap.put("nsh", AdabasNaturalAnalyzer.class);
        analyzersMap.put("nsd", AdabasNaturalAnalyzer.class);
        analyzersMap.put("nsn", AdabasNaturalAnalyzer.class);
        analyzersMap.put("nsc", AdabasNaturalAnalyzer.class);

        analyzersMap.put("abap", AbapAnalyzer.class);

        analyzersMap.put("pp", PuppetAnalyzer.class);
    }

    private static void registerRuby(Map<String, Class> analyzersMap) {
        // ruby
        analyzersMap.put("rb", RubyAnalyzer.class);
        analyzersMap.put("builder", RubyAnalyzer.class);
        analyzersMap.put("eye", RubyAnalyzer.class);
        analyzersMap.put("gemspec", RubyAnalyzer.class);
        analyzersMap.put("god", RubyAnalyzer.class);
        analyzersMap.put("jbuilder", RubyAnalyzer.class);
        analyzersMap.put("mspec", RubyAnalyzer.class);
        analyzersMap.put("podspec", RubyAnalyzer.class);
        analyzersMap.put("rabl", RubyAnalyzer.class);
        analyzersMap.put("rake", RubyAnalyzer.class);
        analyzersMap.put("rbi", RubyAnalyzer.class);
        analyzersMap.put("rbuild", RubyAnalyzer.class);
        analyzersMap.put("rbw", RubyAnalyzer.class);
        analyzersMap.put("rbx", RubyAnalyzer.class);
        analyzersMap.put("ru", RubyAnalyzer.class);
        analyzersMap.put("ruby", RubyAnalyzer.class);
        analyzersMap.put("thor", RubyAnalyzer.class);
        analyzersMap.put("watchr", RubyAnalyzer.class);
    }

    private static void registerVisualBasic(Map<String, Class> analyzersMap) {
        // vb
        analyzersMap.put("vb", VisualBasicAnalyzer.class);
        analyzersMap.put("bas", VisualBasicAnalyzer.class);
        analyzersMap.put("cls", VisualBasicAnalyzer.class);
        analyzersMap.put("ctl", VisualBasicAnalyzer.class);
        analyzersMap.put("frm", VisualBasicAnalyzer.class);
        analyzersMap.put("frx", VisualBasicAnalyzer.class);
        analyzersMap.put("vba", VisualBasicAnalyzer.class);
        analyzersMap.put("vbs", VisualBasicAnalyzer.class);
    }

    private static void registerClojure(Map<String, Class> analyzersMap) {
        analyzersMap.put("clj", ClojureLangAnalyzer.class);
        analyzersMap.put("cljs", ClojureLangAnalyzer.class);
        analyzersMap.put("cljscm", ClojureLangAnalyzer.class);
        analyzersMap.put("cljc", ClojureLangAnalyzer.class);
        analyzersMap.put("cljx", ClojureLangAnalyzer.class);
        analyzersMap.put("hl", ClojureLangAnalyzer.class);
        analyzersMap.put("hic", ClojureLangAnalyzer.class);
        analyzersMap.put("cl2", ClojureLangAnalyzer.class);
        analyzersMap.put("boot", ClojureLangAnalyzer.class);
        analyzersMap.put("edn", ClojureLangAnalyzer.class);
        analyzersMap.put("rg", ClojureLangAnalyzer.class);
        analyzersMap.put("wisp", ClojureLangAnalyzer.class);
    }

    private static void registerYaml(Map<String, Class> analyzersMap) {
        analyzersMap.put("yml", YamlAnalyzer.class);
        analyzersMap.put("yaml", YamlAnalyzer.class);
        analyzersMap.put("mir", YamlAnalyzer.class);
        analyzersMap.put("reek", YamlAnalyzer.class);
        analyzersMap.put("rviz", YamlAnalyzer.class);
        analyzersMap.put("syntax", YamlAnalyzer.class);
        analyzersMap.put("sublime-syntax", YamlAnalyzer.class);
        analyzersMap.put("yaml-tmlanguage", YamlAnalyzer.class);
        analyzersMap.put("sed", YamlAnalyzer.class);
    }

    private static void registerPython(Map<String, Class> analyzersMap) {
        analyzersMap.put("py", PythonAnalyzer.class);
        analyzersMap.put("gyp", PythonAnalyzer.class);
        analyzersMap.put("gypi", PythonAnalyzer.class);
        analyzersMap.put("lmi", PythonAnalyzer.class);
        analyzersMap.put("py3", PythonAnalyzer.class);
        analyzersMap.put("pyde", PythonAnalyzer.class);
        analyzersMap.put("pyi", PythonAnalyzer.class);
        analyzersMap.put("pyp", PythonAnalyzer.class);
        analyzersMap.put("pyt", PythonAnalyzer.class);
        analyzersMap.put("pyw", PythonAnalyzer.class);
        analyzersMap.put("rpy", PythonAnalyzer.class);
        analyzersMap.put("smk", PythonAnalyzer.class);
        analyzersMap.put("tac", PythonAnalyzer.class);
        analyzersMap.put("wsgi", PythonAnalyzer.class);
        analyzersMap.put("xpy", PythonAnalyzer.class);
        analyzersMap.put("eb", PythonAnalyzer.class);
        analyzersMap.put("gn", PythonAnalyzer.class);
        analyzersMap.put("pyx", PythonAnalyzer.class);
        analyzersMap.put("pxd", PythonAnalyzer.class);
        analyzersMap.put("pxi", PythonAnalyzer.class);
        analyzersMap.put("numpy", PythonAnalyzer.class);
        analyzersMap.put("numpyw", PythonAnalyzer.class);
        analyzersMap.put("numsc", PythonAnalyzer.class);
        analyzersMap.put("pytb", PythonAnalyzer.class);
    }

    private static void registerHtml(Map<String, Class> analyzersMap) {
        //
        analyzersMap.put("html", HtmlAnalyzer.class);
        analyzersMap.put("htm", HtmlAnalyzer.class);
        analyzersMap.put("cshtml", HtmlAnalyzer.class);
        analyzersMap.put("vbhtml", HtmlAnalyzer.class);
        analyzersMap.put("razor", HtmlAnalyzer.class);
        analyzersMap.put("soy", HtmlAnalyzer.class);
        analyzersMap.put("st", HtmlAnalyzer.class);
        analyzersMap.put("xht", HtmlAnalyzer.class);
        analyzersMap.put("xhtml", HtmlAnalyzer.class);
        analyzersMap.put("jinja", HtmlAnalyzer.class);
        analyzersMap.put("jinja2", HtmlAnalyzer.class);
        analyzersMap.put("mustache", HtmlAnalyzer.class);
        analyzersMap.put("njk", HtmlAnalyzer.class);
        analyzersMap.put("ecr", HtmlAnalyzer.class);
        analyzersMap.put("eex", HtmlAnalyzer.class);
        analyzersMap.put("erb", HtmlAnalyzer.class);
        analyzersMap.put("deface", HtmlAnalyzer.class);
        analyzersMap.put("haml", HtmlAnalyzer.class);
        analyzersMap.put("mtml", HtmlAnalyzer.class);
        analyzersMap.put("rtml", HtmlAnalyzer.class);
        analyzersMap.put("vue", HtmlAnalyzer.class);
        analyzersMap.put("phtml", HtmlAnalyzer.class);
        analyzersMap.put("hhi", HtmlAnalyzer.class);
        analyzersMap.put("hbs", HtmlAnalyzer.class);
        analyzersMap.put("handlebars", HtmlAnalyzer.class);

    }

    private static void registerR(Map<String, Class> analyzersMap) {
        analyzersMap.put("r", RAnalyzer.class);
        analyzersMap.put("rds", RAnalyzer.class);
        analyzersMap.put("rda", RAnalyzer.class);
        analyzersMap.put("rdata", RAnalyzer.class);
        analyzersMap.put("rd", RAnalyzer.class);
        analyzersMap.put("rsx", RAnalyzer.class);
    }

    private static void registerSql(Map<String, Class> analyzersMap) {
//        analyzersMap.put("pls", SqlAnalyzer.class);
        analyzersMap.put("bdy", SqlAnalyzer.class);
        analyzersMap.put("fnc", SqlAnalyzer.class);
//        analyzersMap.put("pck", SqlAnalyzer.class);
//        analyzersMap.put("pkb", SqlAnalyzer.class);
//        analyzersMap.put("pks", SqlAnalyzer.class);
//        analyzersMap.put("plb", SqlAnalyzer.class);
//        analyzersMap.put("plsql", SqlAnalyzer.class);
        analyzersMap.put("prc", SqlAnalyzer.class);
        analyzersMap.put("spc", SqlAnalyzer.class);
        analyzersMap.put("tpb", SqlAnalyzer.class);
        analyzersMap.put("tps", SqlAnalyzer.class);
        analyzersMap.put("trg", SqlAnalyzer.class);
        analyzersMap.put("vw", SqlAnalyzer.class);
        analyzersMap.put("sql", SqlAnalyzer.class);
        analyzersMap.put("cql", SqlAnalyzer.class);
        analyzersMap.put("ddl", SqlAnalyzer.class);
        analyzersMap.put("mysql", SqlAnalyzer.class);
        analyzersMap.put("tab", SqlAnalyzer.class);
        analyzersMap.put("udf", SqlAnalyzer.class);
        analyzersMap.put("viw", SqlAnalyzer.class);

    }

    private static void registerPlSql(Map<String, Class> analyzersMap) {
        analyzersMap.put("plsql", PlSqlAnalyzer.class);
        analyzersMap.put("pls", PlSqlAnalyzer.class);
        analyzersMap.put("pks", PlSqlAnalyzer.class);
        analyzersMap.put("pck", PlSqlAnalyzer.class);
        analyzersMap.put("pkb", PlSqlAnalyzer.class);
        analyzersMap.put("plb", PlSqlAnalyzer.class);

    }
}
