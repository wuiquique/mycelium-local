package com.mycelium.local;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.naming.conventions.PropertyConvention;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.web.router.naming.HyphenatedUriNamingStrategy;
import jakarta.inject.Singleton;

@Singleton
@Replaces(HyphenatedUriNamingStrategy.class)
class ConfigurableUriNamingStrategy extends HyphenatedUriNamingStrategy {

    @Value("${micronaut.context-path}")
    String contextPath = null;

    @SuppressWarnings("rawtypes")
    @Override
    public String resolveUri(Class type) {
        return (contextPath == null ? "" : contextPath) + super.resolveUri(type);
    }

    @Override
    public String resolveUri(BeanDefinition<?> beanDefinition) {
        return (contextPath == null ? "" : contextPath) + super.resolveUri(beanDefinition);
    }

    @Override
    public String resolveUri(String property) {
        return (contextPath == null ? "" : contextPath) + super.resolveUri(property);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String resolveUri(Class type, PropertyConvention id) {
        return (contextPath == null ? "" : contextPath) + super.resolveUri(type, id);
    }
}
