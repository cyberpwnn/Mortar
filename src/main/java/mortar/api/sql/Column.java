package mortar.api.sql;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Column
{
	String name();

	String type();

	String placeholder();

	boolean primary() default false;
}
