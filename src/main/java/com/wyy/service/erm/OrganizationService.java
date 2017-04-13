package com.wyy.service.erm;

import com.wyy.domain.erm.Organization;
import com.wyy.repository.erm.OrganizationRepository;
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
public class OrganizationService implements ItenantService {

    private final Logger log = LoggerFactory.getLogger(Organization.class);

    @Inject
    private OrganizationRepository organizationRepository;

    public List<Organization> findAll(){
        return organizationRepository.findAll();
    }


    public List<Organization> findOrgzList(Long id){
        return organizationRepository.findAllByParentId(id);
    }

    public Organization save(Organization organization){
        if(organization.getId()!=null){
           Organization orgz= organizationRepository.findOne(organization.getId());
            orgz.setCls(organization.getCls());
            orgz.setCode(organization.getCode());
            orgz.setName(organization.getName());
            return organizationRepository.save(orgz);
        }else{
            return organizationRepository.save(organization);

        }

    }
    public void updateOrgz(String name,Long id){
        organizationRepository.updateOrgz(name,id);
    }

    public void deleteOrgz(Long id){
         organizationRepository.delete(id);
    }
}
