package com.firearms.firearmcollectionjee.controller.servlet;

import com.firearms.firearmcollectionjee.controller.impl.FirearmController;
import com.firearms.firearmcollectionjee.controller.impl.UserController;
import com.firearms.firearmcollectionjee.controller.impl.WeaponFamilyController;
import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import com.firearms.firearmcollectionjee.dto.user.PatchUserRequest;
import com.firearms.firearmcollectionjee.dto.user.PutUserRequest;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class ApiServlet extends HttpServlet {
    
    @Inject
    private UserController userController;
    
    @Inject
    private FirearmController firearmController;
    
    @Inject
    private WeaponFamilyController weaponFamilyController;
    public static final class Paths {
        public static final String API = "/api";
    }

    public static final class Patterns {
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        public static final Pattern USERS = Pattern.compile("/users/?");
        public static final Pattern USER = Pattern.compile("/users/(%s)".formatted(UUID.pattern()));
        public static final Pattern USER_AVATAR = Pattern.compile("/users/(%s)/avatar".formatted(UUID.pattern()));
        public static final Pattern USER_BY_LOGIN = Pattern.compile("/users/login/([A-Za-z0-9._-]+)");

        // WeaponFamily patterns
        public static final Pattern WEAPON_FAMILIES = Pattern.compile("/weapon-families/?");

        // Firearm patterns
        public static final Pattern FIREARMS = Pattern.compile("/firearms/?");
        public static final Pattern FIREARM = Pattern.compile("/firearms/(%s)".formatted(UUID.pattern()));
        public static final Pattern FIREARMS_BY_USER = Pattern.compile("/users/(%s)/firearms".formatted(UUID.pattern()));
        public static final Pattern FIREARMS_BY_WEAPON_FAMILY = Pattern.compile("/weapon-families/(%s)/firearms".formatted(UUID.pattern()));
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("PATCH")) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }



    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            // User endpoints
            if (path.matches(Patterns.USERS.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(userController.getAllUsers()));
                return;
            } else if (path.matches(Patterns.USER.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.USER, path);
                response.getWriter().write(jsonb.toJson(userController.getUserById(uuid)));
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                response.setContentType("image/png");
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                byte[] avatar = userController.getUserAvatar(uuid);
                if (avatar.length == 0) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                response.setContentLength(avatar.length);
                response.getOutputStream().write(avatar);
                return;
            } else if (path.matches(Patterns.USER_BY_LOGIN.pattern())) {
                response.setContentType("application/json");
                String login = extractSingleGroup(Patterns.USER_BY_LOGIN, path);
                response.getWriter().write(jsonb.toJson(userController.getUserByLogin(login)));
                return;
            } 
            // WeaponFamily endpoints
            else if (path.matches(Patterns.WEAPON_FAMILIES.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(weaponFamilyController.getWeaponFamilies()));
                return;
            } 
            // Firearm endpoints
            else if (path.matches(Patterns.FIREARMS.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(firearmController.getFirearms()));
                return;
            } else if (path.matches(Patterns.FIREARM.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.FIREARM, path);
                response.getWriter().write(jsonb.toJson(firearmController.getFirearm(uuid)));
                return;
            } else if (path.matches(Patterns.FIREARMS_BY_USER.pattern())) {
                response.setContentType("application/json");
                UUID userId = extractUuid(Patterns.FIREARMS_BY_USER, path);
                response.getWriter().write(jsonb.toJson(firearmController.getUserFirearms(userId)));
                return;
            } else if (path.matches(Patterns.FIREARMS_BY_WEAPON_FAMILY.pattern())) {
                response.setContentType("application/json");
                UUID weaponFamilyId = extractUuid(Patterns.FIREARMS_BY_WEAPON_FAMILY, path);
                response.getWriter().write(jsonb.toJson(firearmController.getWeaponFamilyFirearms(weaponFamilyId)));
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            // User endpoints
            if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.putUser(uuid, jsonb.fromJson(request.getReader(), PutUserRequest.class));
                response.addHeader("Location", createUrl(request, Paths.API, "users", uuid.toString()));
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                userController.putUserAvatar(uuid, request.getPart("avatar").getInputStream());
                return;
            } 
            // Firearm endpoints
            else if (path.matches(Patterns.FIREARM.pattern())) {
                UUID uuid = extractUuid(Patterns.FIREARM, path);
                firearmController.putFirearm(uuid, jsonb.fromJson(request.getReader(), PutFirearmRequest.class));
                response.addHeader("Location", createUrl(request, Paths.API, "firearms", uuid.toString()));
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            // User endpoints
            if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.deleteUser(uuid);
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                userController.deleteUserAvatar(uuid);
                return;
            } 
            // Firearm endpoints
            else if (path.matches(Patterns.FIREARM.pattern())) {
                UUID uuid = extractUuid(Patterns.FIREARM, path);
                firearmController.deleteFirearm(uuid);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @SuppressWarnings("RedundantThrows")
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            // User endpoints
            if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.patchUser(uuid, jsonb.fromJson(request.getReader(), PatchUserRequest.class));
                return;
            } 
            // Firearm endpoints
            else if (path.matches(Patterns.FIREARM.pattern())) {
                UUID uuid = extractUuid(Patterns.FIREARM, path);
                firearmController.patchFirearm(uuid, jsonb.fromJson(request.getReader(), PatchFirearmRequest.class));
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("No UUID in path.");
    }

    private String parseRequestPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        path = path != null ? path : "";
        return path;
    }

    public static String createUrl(HttpServletRequest request, String... paths) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());
        for (String path : paths) {
            builder.append("/")
                    .append(path, path.startsWith("/") ? 1 : 0, path.endsWith("/") ? path.length() - 1 : path.length());
        }
        return builder.toString();
    }

    private static String extractSingleGroup(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Pattern did not match: " + pattern.pattern());
    }
}
