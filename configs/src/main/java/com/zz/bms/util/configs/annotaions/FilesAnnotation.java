package com.zz.bms.util.configs.annotaions;


import java.lang.annotation.*;


/**
 * 文件属性注解类
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilesAnnotation {
}
