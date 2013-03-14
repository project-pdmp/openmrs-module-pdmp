package org.openmrs.module.pdmp_query.extension.html;

import java.util.Arrays;
import java.util.List;


/**
 * The header include extension allows a module developer to include extra files (css, javascript)
 * within the header of every page. This could be used to include a new javascript library, CSS
 * styles, etc. In order to include headers, a module developer should implement the
 * getHeaderFiles() method and return a collection of file names ("myjavascript.js", "mycss.css")
 * that reside in the modules resources directory (i.e. "web/module/resources").
 */
public class HeaderIncludeExt extends org.openmrs.module.web.extension.HeaderIncludeExt {

    /**
     * Returns references to header files in the module resource directory.
     *
     * @return a collection of header files
     */
    public List<String> getHeaderFiles() {
        return Arrays.asList("/moduleResources/pdmp_query/pdmp_style.css");
    };

}
