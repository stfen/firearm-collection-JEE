package com.firearms.firearmcollectionjee.web.listener;

import com.firearms.firearmcollectionjee.repository.impl.UserRepository;
import com.firearms.firearmcollectionjee.service.UserService;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class CreateServices implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataStorage dataSource = (DataStorage) event.getServletContext().getAttribute("datasource");
        UserRepository userRepository = new UserRepository(dataSource);
        String avatarsPath = event.getServletContext().getInitParameter("avatars.path");
        event.getServletContext().setAttribute("userService", new UserService(userRepository, avatarsPath));
    }
}
