package com.wyy.service.erm;

import com.wyy.domain.erm.MenuAction;
import com.wyy.repository.erm.MenuActionRepository;
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
public class MenuActionService   {

    private final Logger log = LoggerFactory.getLogger(MenuAction.class);

    @Inject
    private MenuActionRepository menuActionRepository;

    public MenuAction findOne(Long id){
        return menuActionRepository.findOne(id);
    }

}
