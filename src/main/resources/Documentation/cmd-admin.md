cookbook-plugin admin
=====================

NAME
----
cookbook-plugin admin - Administrate the cookbook plugin

SYNOPSIS
--------
>     ssh -p <port> <host> cookbook-plugin admin

DESCRIPTION
-----------
Prints "Hello, cookbook administrator".

ACCESS
------
Any user who has configured an SSH key and has been granted
the `Administrate Cookbook` capability (provided by this plugin)
or the 'Administrate Server' capability.

SCRIPTING
---------
This command is intended to be used in scripts.

EXAMPLES
--------

Have the server say hello to the administrator.

>     $ ssh -p 29418 review.example.com cookbook-plugin admin

SEE ALSO
--------

* [Plugin Development](../../../Documentation/dev-plugins.html)

GERRIT
------
Part of [Gerrit Code Review](../../../Documentation/index.html)
