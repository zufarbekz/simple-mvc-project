package org.example;

import org.apache.log4j.Logger;
import org.example.web.config.WebContextConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

//web.xml
public class WebAppInitializer implements WebApplicationInitializer {

    private Logger logger = Logger.getLogger(WebAppInitializer.class);
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        logger.info("Loading app-config");
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:app-config.xml");
        servletContext.addListener(new ContextLoaderListener(appContext));

        logger.info("Loading web-config");
        //with web-config.xml

       // XmlWebApplicationContext webContext = new XmlWebApplicationContext();
       // webContext.setConfigLocation("classpath:web-config.xml");

        //without web-config.xml
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebContextConfig.class);


        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        logger.info("Dispatcher Servlet is Ready");

    }
}
