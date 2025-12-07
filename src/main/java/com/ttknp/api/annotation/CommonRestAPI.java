package com.ttknp.api.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// *** my custom annotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping
@CrossOrigin
public @interface CommonRestAPI {

    @AliasFor(annotation = RequestMapping.class,attribute = "path") // have to specify alias for annotation because value more than one annotation
    String[] configPath() default {""};

    @AliasFor(annotation = CrossOrigin.class,attribute = "origins") // have to specify alias for annotation because value more than one annotation
    String[] configOrigins() default {""};

}