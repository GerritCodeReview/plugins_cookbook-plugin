load('//tools/bzl:junit.bzl', 'junit_tests')
load('//tools/bzl:plugin.bzl', 'gerrit_plugin')

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
  ],
)

# For the reasons, how the plugin tests are implemented
# they cannot be ran hermetically. 
junit_tests(
  name = 'cookbook_tests',
  srcs = glob(['src/test/java/**/*IT.java']),
  tags = ['cookbook-plugin', 'local'],
  deps = [
    ':cookbook-plugin__plugin',
    '//gerrit-acceptance-framework:lib',
    '//gerrit-plugin-api:lib',
  ],
  visibility = ['//visibility:public'],
)
