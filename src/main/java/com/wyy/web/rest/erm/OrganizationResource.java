package com.wyy.web.rest.erm;

import com.codahale.metrics.annotation.Timed;
import com.wyy.domain.erm.Organization;
import com.wyy.domain.erm.User;
import com.wyy.service.erm.OrganizationService;
import com.wyy.service.erm.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);


    @Inject
    private OrganizationService organizationService;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/OrgzfindAll",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organization>> tree(Long id) {
        if(id==null){
            return new ResponseEntity(organizationService.findAll(),HttpStatus.OK) ;
        }else{
            return new ResponseEntity(organizationService.findOrgzList(id),HttpStatus.OK) ;
        }

    }

    @RequestMapping(value = "/saveOrgz",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organization> saveOrgz(Organization orgz)throws URISyntaxException {
        return new ResponseEntity(organizationService.save(orgz),HttpStatus.OK) ;
    }
    @RequestMapping(value = "/deleteOrgz",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String,String>> deleteOrgz(Long id) {
        List<User> users=userService.findByOrganizationId(id);
        Map<String,String> map=new HashMap<String,String>();
        if(users.size()>0){
            map.put("msg","error");
        }else{
            organizationService.deleteOrgz(id);
            map.put("msg","success");
        }
        return new ResponseEntity(map,HttpStatus.OK) ;
    }
}
