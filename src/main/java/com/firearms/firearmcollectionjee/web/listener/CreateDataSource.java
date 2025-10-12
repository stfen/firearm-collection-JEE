package com.firearms.firearmcollectionjee.web.listener;

import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.firearms.firearmcollectionjee.serialization.component.CloningUtility;

@WebListener
public class CreateDataSource implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("datasource", new DataStorage(new CloningUtility()));
    }
}
