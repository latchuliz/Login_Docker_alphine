package com.neopharma.datavault.permissions;

import java.util.List;

public interface PermissionsListener {

    void onExplanationNeeded(List<String> permissionsToExplain);

    void onPermissionResult(boolean granted);
}
