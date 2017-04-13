package com.wyy.service.erm;

import com.wyy.domain.erm.Role;
import com.wyy.repository.erm.RoleRepository;
import com.wyy.service.ActivitiService;
import com.wyy.service.ItenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */
@Service
@Transactional
public class RoleService implements ItenantService {

    private final Logger log = LoggerFactory.getLogger(Role.class);

    @Inject
    private RoleRepository roleRepository;
    @Inject
    private RoleService roleService;
    @Inject
    private ActivitiService activitiService;

    public Role findOne(Long id){
        return roleRepository.findOne(id);
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role save(Role role){
        if(role.getId()!=null){
            Role roleOne=roleService.findOne(role.getId());
            roleOne.setName(role.getName());
            roleOne.setDescribe(role.getDescribe());
            return  roleRepository.save(roleOne);
        }
        else{
            role=roleRepository.save(role);
            addActivitiGroup(role);
            return role;
        }
    }

    public void deleteRole(Long id){
         roleRepository.delete(id);
         deleteActivitiGroup(id);
    }

    public List<Role> findAllByName(String name){
        return roleRepository.findAllByName(name);
    }

    public List<Role> findByNameLike(String name){
        if(name==null||"".equals(name)){
            return roleRepository.findAll();
        }
        else{
            return roleRepository.findByNameLike("%"+name+"%");
        }

    }

    private void addActivitiGroup(Role role){
        //activitiService.saveGroup(role,true);
    }
    private void deleteActivitiGroup(Long roleId){
        //activitiService.deleteRole(roleId.toString());
    }

}
