package com.wyy.service.erm;

import com.wyy.domain.erm.RolePermission;
import com.wyy.repository.erm.RolePermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/8.
 */
@Service
@Transactional
public class RolePermissionService {

    private final Logger log = LoggerFactory.getLogger(RolePermission.class);

    @Inject
    private RolePermissionRepository rolePermissionRepository;


}
