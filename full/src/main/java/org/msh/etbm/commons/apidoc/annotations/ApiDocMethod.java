package org.msh.etbm.commons.apidoc.annotations;

import java.lang.annotation.*;

/**
 * Created by rmemoria on 28/4/15.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDocMethod {
    String description();
    ApiDocReturn[] returnCodes() default {};
}
