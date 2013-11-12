MODULE = 'com.googlesource.gerrit.plugins.cookbook.HelloForm'

gerrit_plugin(
  name = 'cookbook-plugin',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/**/*']),
  gwt_module = MODULE,
  manifest_entries = [
    'Gerrit-PluginName: cookbook-plugin',
    'Gerrit-Module: com.googlesource.gerrit.plugins.cookbook.Module',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.cookbook.HttpModule',
    'Gerrit-SshModule: com.googlesource.gerrit.plugins.cookbook.SshModule',
  ]
)
