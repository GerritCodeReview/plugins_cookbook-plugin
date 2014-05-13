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
  provided_deps = [
    '//lib/jgit:jgit',
    '//lib/commons:lang',
  ],
)
