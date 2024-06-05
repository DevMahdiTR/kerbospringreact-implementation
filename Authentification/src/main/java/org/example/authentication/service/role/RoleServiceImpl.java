package org.example.authentication.service.role;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.exceptions.custom.DatabaseException;
import org.example.authentication.exceptions.custom.ResourceNotFoundException;
import org.example.authentication.model.role.Role;
import org.example.authentication.repository.RoleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Role fetchRoleByName(String name) {
        return roleRepository.fetchRoleByName(name).orElseThrow(
                () -> new ResourceNotFoundException("The Role with name %s could not be found.".formatted(name))
        );
    }

    @Transactional
    @Override
    public Role deleteRoleById(Long id) {
        Role exisitingRole = getRoleById(id);
        roleRepository.removeRoleById(id);
        log.info("%s deleted successfully.".formatted(exisitingRole));
        return exisitingRole;
    }

    @Override
    public Role updateRoleById(Long id, @NotNull Role role) {
        Role exisitingRole = getRoleById(id);
        role.setId(exisitingRole.getId());
        log.info("%s updated successfully.".formatted(role));
        return this.save(role);
    }


    @Override
    public Role save(Role role) {
        try {
            log.info("Database Request to save Role: {}", role);
            final Role savedRole = roleRepository.save(role);
            log.info("{} saved successfully.", savedRole);
            return savedRole;
        } catch (Exception e) {
            log.error("Error saving Role: {}", e.getMessage());
            throw new DatabaseException("Error executing database query.");
        }
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.fetchRoleById(id).orElseThrow(
                () -> new ResourceNotFoundException("The Role with ID : '%d' could not be found".formatted(id))
        );
    }
}
