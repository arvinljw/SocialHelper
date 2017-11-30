package net.arvin.socialhelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by arvinljw on 17/11/29 10:08
 * Function：
 * Desc：带有这个注解的参数表示必传
 */
@Target(ElementType.PARAMETER)
public @interface ParamsRequired {
}
