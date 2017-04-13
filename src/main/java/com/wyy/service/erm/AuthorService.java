package com.wyy.service.erm;

import com.wyy.domain.erm.RoleMenu;
import com.wyy.repository.erm.RoleMenuActionRepository;
import com.wyy.repository.erm.RoleMenuRepository;
import com.wyy.repository.erm.RolePermissionRepository;
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
public class AuthorService implements ItenantService{

    private final Logger log = LoggerFactory.getLogger(AuthorService.class);

    @Inject
    private RoleMenuRepository roleMenuRepository;
    @Inject
    private RoleMenuActionRepository roleMenuActionRepository;
    @Inject
    private RolePermissionRepository rolePermissionRepository;

    @Transactional(readOnly = true)
    public List<RoleMenu> findAllRoleMenu(Long roleId) {
        log.debug("Request to get all RoleMenu");
        List<RoleMenu> list = roleMenuRepository.findAllByRoleId(roleId);
        return list;
    }

    @Transactional(readOnly = true)
    public RoleMenu findOneByRoleIdAndMenuId(Long roleId,Long menuId){
        RoleMenu roleMenu = roleMenuRepository.findOneByRoleIdAndMenuId(roleId, menuId);
        return roleMenu;
    }

}
