package github.july_summer.julyitems.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    public String cmd();
    public String msg();
    public int checkArgs1() default 0;
    public int checkArgs2() default 0;
    public boolean checkLength() default true;
    public boolean checkId() default true;
    public boolean existId() default true;
    public boolean isOp() default true;
    public boolean isPlayer() default false;
}
