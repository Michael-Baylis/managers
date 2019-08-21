package dev.galasa.common.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.galasa.common.http.internal.HttpManagerField;
import dev.galasa.framework.spi.ValidAnnotatedFields;

/**
 * Used to annotate annotations that are to be used for Test Class fields. To be
 * populated by the Manager.
 *
 * @author William Yates
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@HttpManagerField
@ValidAnnotatedFields({ IHttpClient.class })
public @interface HttpClient {

}