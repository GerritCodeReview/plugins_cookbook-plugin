gerrit_plugin(
  name = 'cookbook-plugin',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/resources/**/*']),
  manifest_entries = [
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.cookbook.HttpModule',
    'Gerrit-Module: com.googlesource.gerrit.plugins.cookbook.Module',
    'Gerrit-SshModule: com.googlesource.gerrit.plugins.cookbook.SshModule',
  ]
)
