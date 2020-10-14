package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class IdProvider implements InitializingBean, DisposableBean, BeanPostProcessor {
    Logger logger = Logger.getLogger(IdProvider.class);

    public String provideID(Book book) {
        return this.hashCode() + "_" + book.hashCode();
    }

    private void destroyIdProvider() {
        logger.info("Provider Destroy");
    }

    private void initIdProvider() {
        logger.info("Provider Init");
    }

    private void defaultInit(){
        logger.info("Default Init in IdProvider");
    }

    private void defaultDestroy(){
        logger.info("Default Destroy in IdProvider");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("DisposableBean Destroy invoked");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Provider afterPropertiesSet invoked");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessBeforeInitialization invoked by bean " + beanName);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessAfterInitialization invoked by bean " + beanName);
        return null;
    }

    @PostConstruct
    public void postConstructIdProvider(){
        logger.info("PostConstruct annotated method called");
    }

    @PreDestroy
    public void preDestroyIdProvider(){
        logger.info("PreDestroy annotated method called");
    }
}
