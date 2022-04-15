package malo.bloc.tree.service;

import malo.bloc.tree.persistence.entity.Role;
import malo.bloc.tree.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> getAllRoles(){
       return new HashSet<>(roleRepository.findAll());
    }

    public HashMap<String,Role> getAllRolesMapByName() {
        LinkedHashMap <String,Role>  rolesMap = new LinkedHashMap<>();
        getAllRoles().forEach(role -> rolesMap.put(role.getName(),role));
        return rolesMap;
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Optional<Role> delete(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if(role.isPresent()) {
            roleRepository.deleteById(role.get().getId());
            roleRepository.flush();
            Optional<Role> shadowRole= roleRepository.findByName(name);
            return shadowRole.isPresent()? Optional.empty() : role;
        }
        throw new EntityNotFoundException("role with name = "+name+" does not found for delete");
    }

}
