package it.unicam.cs.ids.filieraagricola.system;

import java.util.Arrays;
import java.util.List;
/*
* DECORATOR + STRATEGY
* Wraps another SystemAction and, before executing it,
* checks if the user has all required permissions
 */

public class AuthorizeAction implements SystemAction {


    private SystemAction systemAction;
    private String[] permissions;

    public AuthorizeAction(SystemAction systemAction, String... permissions) {
        this.permissions = permissions;
        this.systemAction = systemAction;
    }

    @Override
    public void handleRequest(FormatRequest request) {
        SystemManager system = SystemManager.getInstance();
        String[] userPermissionsArray = system.getPermissions();
        List<String> userPermissionsList = Arrays.asList(userPermissionsArray);
        boolean isAuthorized = true;
        for (String requiredPermission : this.permissions) {
            if (!userPermissionsList.contains(requiredPermission)) {
                isAuthorized = false;
            }
        }
        if (isAuthorized) {
            this.systemAction.handleRequest(request);
        } else {
            return;
        }
    }
}
