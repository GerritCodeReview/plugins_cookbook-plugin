@PLUGIN@ chef-group
=====================

NAME
----
@PLUGIN@ chef-group - Project-dependent group of 'Chefs'

DESCRIPTION
-----------

Example to illustrate implementing a project-dependent group backend.

A group backend is added that provides a project-depdent group named '@PLUGIN@:chefs'.
Members of this groups are experienced contributers ('chefs'). A user
is regarded a 'chef' with respect to a project if he/she has contributed
at least 50 changes to this projects.

The '@PLUGIN@:chefs' group could be used to uniformly configure access rights
in a parent project based on the project-dependent experience level in the
child project.

SEE ALSO
--------

* [Plugin Development](../../../Documentation/dev-plugins.html)

GERRIT
------
Part of [Gerrit Code Review](../../../Documentation/index.html)
