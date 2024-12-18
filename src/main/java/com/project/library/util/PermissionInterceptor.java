package com.project.library.util;

import com.project.library.entity.PermissionEntity;
import com.project.library.entity.RoleEntity;
import com.project.library.entity.UserEntity;
import com.project.library.service.SecurityService;
import com.project.library.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();

        if (isPublic(httpMethod, path)) {
            return true;
        }

        String email = SecurityService.getCurrentUserLogin()
                .orElseThrow(() -> new UnauthorizedException("No user is logged in"));
        if (email != null && !email.isEmpty()) {
            UserEntity user = userService.getUserByEmail(email);
            if (user != null) {
                RoleEntity role = user.getRole();
                if (role != null) {
                    List<PermissionEntity> permissions = role.getPermissions();
                    boolean hasPermission = permissions.stream().anyMatch(permission ->
                            permission.getApiPath().equals(path) && permission.getMethod().equals(httpMethod));
                    if (!hasPermission) {
                        throw new ForbiddenException("You don't have permission to access this resource");
                    }
                }
            }
        }
        return true;
    }

    private boolean isPublic(String httpMethod, String path) {
        if (path.matches("/upload/.*")) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/login".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/logout".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/register".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/auth/refresh".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/auth".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/activate".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/reset-password".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/auth/resend-mail".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/authors".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/categories".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/publishers".equals(path)) return true;
        if ("GET".equals(httpMethod) && "/api/v1/books".equals(path)) return true;
        if ("POST".equals(httpMethod) && "/api/v1/users/change-password".equals(path)) return true;
        return false;
    }
}
