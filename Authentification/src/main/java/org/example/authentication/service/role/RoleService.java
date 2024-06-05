package org.example.authentication.service.role;

import org.example.authentication.model.role.Role;

public interface RoleService {

    Role fetchRoleByName(final String name);

    Role deleteRoleById(final Long id);

    Role updateRoleById(final Long id, final Role role);

    Role save(final Role role);

    Role getRoleById(final Long id);
}
