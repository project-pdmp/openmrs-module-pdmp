package org.openmrs.module.pdmp_query.extension.html;

import java.util.Arrays;
import java.util.List;


/**
 * OpenMRS extension to hook our CSS into the page header.
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
