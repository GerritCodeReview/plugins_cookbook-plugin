load("//tools/bzl:junit.bzl", "junit_tests")
load("//tools/bzl:plugin.bzl", "gerrit_plugin")

MODULE = "com.googlesource.gerrit.plugins.cookbook.HelloForm"

gerrit_plugin(
    name = "cookbook-plugin",
    srcs = glob(["src/main/java/**/*.java"]),
    gwt_module = MODULE,
    manifest_entries = [
        "Gerrit-PluginName: cookbook",
        "Gerrit-Module: com.googlesource.gerrit.plugins.cookbook.Module",
        "Gerrit-HttpModule: com.googlesource.gerrit.plugins.cookbook.HttpModule",
        "Gerrit-SshModule: com.googlesource.gerrit.plugins.cookbook.SshModule",
        "Implementation-Title: Cookbook plugin",
        "Implementation-URL: https://gerrit-review.googlesource.com/#/admin/projects/plugins/cookbook-plugin",
    ],
    resources = glob(["src/main/**/*"]),
)

junit_tests(
    name = "cookbook_tests",
    size = "small",
    srcs = glob(["src/test/java/**/*IT.java"]),
    tags = [
        "cookbook-plugin",
    ],
    visibility = ["//visibility:public"],
    deps = [
        ":cookbook-plugin__plugin",
        "//gerrit-acceptance-framework:lib",
        "//gerrit-plugin-api:lib",
    ],
)
