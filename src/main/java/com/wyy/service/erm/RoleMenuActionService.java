package com.wyy.service.erm;

import com.wyy.domain.erm.RoleMenuAction;
import com.wyy.repository.erm.RoleMenuActionRepository;
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
public class RoleMenuActionService {
    private final Logger log = LoggerFactory.getLogger(RoleMenuAction.class);

    @Inject
    private RoleMenuActionRepository roleMenuActionRepository;


}
