package com.wyy.service;

import com.wyy.domain.enums.SystemEnum;
import com.wyy.repository.SystemEnumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */
@Service
@Transactional
public class SystemEnumService  {

    private final Logger log = LoggerFactory.getLogger(SystemEnum.class);

    @Inject
    private SystemEnumRepository systemEnumRepository;

    @Transactional(readOnly = true)
    public List<SystemEnum> findAll() {
        List<SystemEnum> result = systemEnumRepository.findAll();
        return result;
    }

    @Transactional(readOnly = true)
    public List<SystemEnum> findAllByType(String type){
        return  systemEnumRepository.findAllByType(type);
    }

    @Transactional(readOnly = true)
    public List<SystemEnum> findAllByParent(String parent){
        return  systemEnumRepository.findAllByParent(parent);
    }


    @Transactional(readOnly = true)
    public SystemEnum findOneByParentAndValue(String parent,String value){
       return  systemEnumRepository.findOneByParentAndValue(parent, value);
   }

    @Transactional(readOnly = true)
    public List<SystemEnum>  findAllByParentAndParam(String parent,String param){
        return  systemEnumRepository.findAllByParentAndParam(parent, param);
    }

    public SystemEnum save(SystemEnum systemEnum) {
        return systemEnumRepository.save(systemEnum);
    }


    public void deleteById(Long id){
        systemEnumRepository.deleteById(id);
    }

}
