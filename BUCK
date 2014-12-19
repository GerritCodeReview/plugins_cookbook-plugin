include_defs('//bucklets/gerrit_plugin.bucklet')

MODULE = 'com.googlesource.gerrit.plugins.cookbook.HelloForm'

gerrit_plugin(
  name = 'cookbook-plugin',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/**/*']),
  gwt_module = MODULE,
  manifest_entries = [
    'Gerrit-PluginName: cookbook',
    'Gerrit-Module: com.googlesource.gerrit.plugins.cookbook.Module',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.cookbook.HttpModule',
    'Gerrit-SshModule: com.googlesource.gerrit.plugins.cookbook.SshModule',
    'Implementation-Title: Cookbook plugin',
    'Implementation-URL: https://gerrit-review.googlesource.com/#/admin/projects/plugins/cookbook-plugin',
  ]
)

java_test(
  name = 'cookbook_tests',
  srcs = glob(['src/test/java/**/*.java']),
  deps = [
    ':cookbook-plugin__plugin',
    '//lib:junit',
  ],
)

# TODO(davido): is this needed?
# requires for bucklets/tools/eclipse/project.py to work
# not sure, if this does something useful in standalone context
java_library(
  name = 'classpath',
  deps = [':cookbook-plugin__plugin'],
)
