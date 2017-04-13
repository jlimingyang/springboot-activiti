package com.wyy.service.erm;

import com.wyy.domain.erm.RoleMenu;
import com.wyy.repository.erm.RoleMenuRepository;
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
public class RoleMenuService {

    private final Logger log = LoggerFactory.getLogger(RoleMenu.class);

    @Inject
    private RoleMenuRepository roleMenuRepository;
}
