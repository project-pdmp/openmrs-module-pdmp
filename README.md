openmrs-module-pdmp
===================

Module for integrating patient prescription data from a Prescription
Drug Monitoring Program system using Open Search to locate
prescription information and return a standardized healthcare document
format.

Overview
--------

This module runs in the OpenMRS electronic health record system.
OpenMRS's web site is [http://www.openmrs.org/](http://www.openmrs.org/).

Module documentation is
[https://wiki.openmrs.org/display/docs/Administering+Modules](https://wiki.openmrs.org/display/docs/Administering+Modules).

This module works with the "Spike" PDMP simulator which is available
at [https://github.com/project-pdmp/spike](https://github.com/project-pdmp/spike) .

Installation
------------

This module uses the Maven application to build.  Most of the time all
you need to do is run "mvn install" and the module will be built in
the omod/target directory.  It's called pdmp_query-&lt;version&gt;.omod
where &lt;version&gt; is the module version.

Once you've built the module you can upload it to OpenMRS using the
administrative interface.

License
-------

This module is distributed under the terms of the OpenMRS public
license which is in a file called "LICENSE" in this directory.  The
OpenMRS public license is based on the Mozilla Public License but with
some differences.
