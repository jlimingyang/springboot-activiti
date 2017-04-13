package com.wyy.web.rest.erm;

import com.codahale.metrics.annotation.Timed;
import com.wyy.domain.erm.*;
import com.wyy.repository.erm.RoleMenuActionRepository;
import com.wyy.repository.erm.RoleMenuRepository;
import com.wyy.repository.erm.RolePermissionRepository;
import com.wyy.service.erm.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 授权管理
 */
@RestController
@RequestMapping("/api/auth")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @Inject
    private AuthorService authorService;
    @Inject
    private RoleService roleService;
    @Inject
    private MenuService menuService;
    @Inject
    private MenuActionService menuActionService;
    @Inject
    private RoleMenuRepository roleMenuRepository;
    @Inject
    private RoleMenuActionRepository roleMenuActionRepository;
    @Inject
    private RolePermissionRepository rolePermissionRepository;
    @Inject
    private UserService userService;

    /**
     * 查询角色列表
     */
    @RequestMapping(value = "/findAllRole",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Role>> findAllRole(String name) {
        List<Role> list=new ArrayList<>();
        if(StringUtils.isNotBlank(name)){
            list=roleService.findAllByName(name);
        }else{
            list=roleService.findAll();
        }
        return new ResponseEntity(list,HttpStatus.OK);
    }

    /**
     * 查询角色菜单及动作
     */
    @RequestMapping(value = "/findAllRoleMenu",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findAllRoleMenu(@RequestParam Long roleId) {
    //{id:11,name:"角色管理",status:false,parent:{id:1,name:"系统管理",status:false},
    // oprs:[{id:111,name:"新增",flag:"add",status:false},{id:222,name:"删除",flag:"del",status:false}]}
        List listAll=new ArrayList<>();

//        List<Menu> listMenu=menuService.findAllMenuItems();
        List<Menu> listMenu=menuService.findAllItems();
        List<RoleMenu> list=authorService.findAllRoleMenu(roleId);
        for (Menu menu:listMenu){
            Map<String,Object> map=new HashMap<>();
            Map<String,Object> pmap=new HashMap<>();
            pmap.put("id",menu.getParent().getId());
            pmap.put("name",menu.getParent().getName());
            pmap.put("status",false);

            map.put("id",menu.getId());
            map.put("name",menu.getName());
            map.put("status",false);

            List<Map<String,Object>> listAction=new ArrayList<>();
            for (MenuAction ma: menu.getActions()){
                Map<String,Object> lmap=new HashMap<>();
                lmap.put("id",ma.getId());
                lmap.put("name",ma.getText());
                lmap.put("status",false);
                listAction.add(lmap);
            }

            for (RoleMenu rm : list){
                if(rm.getMenu().getId().equals(menu.getId())){
                    pmap.put("status",true);
                    map.put("status",true);
                    for (RoleMenuAction rma : rm.getActions()){
                        for(Map<String,Object> m : listAction){
                            if(m.get("id").toString().equals(rma.getMenuAction().getId().toString())){
                                m.put("status",true);
                            }
                        }
                    }
                }
            }

            map.put("parent",pmap);
            map.put("actions",listAction);
            map.put("roleId",roleId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * 查询角色菜单及权限
     */
    @RequestMapping(value = "/findAllRolePermission",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findAllRolePermission(@RequestParam Long roleId) {
        List listAll=new ArrayList<>();

        List<Menu> listMenu=menuService.findAllMenuItems();
        List<RoleMenu> list=authorService.findAllRoleMenu(roleId);
        for (Menu menu:listMenu){
            Map<String,Object> map=new HashMap<>();
            map.put("id",menu.getId());
            map.put("name",menu.getName());
            map.put("parentName",menu.getParent().getName());
            map.put("status",false);
            for (RoleMenu rm : list){
                if(rm.getMenu().getId().equals(menu.getId())){
                    if(rm.getIsConfig()!=null && rm.getIsConfig()){
                        map.put("status",true);
                    }
                }
            }
            map.put("roleId",roleId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * 查询组织下的用户
     */
    @RequestMapping(value = "/findPermissionUser",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> findPermissionUser(Long orgId) throws URISyntaxException {
        log.debug("REST request to save Menu : {}");
//        {
//            "id" : 2,
//            "code" : "1",
//            "name" : "公司领导",
//            "cls" : "iconfont icon-msnui-org",
//            "fullName" : null,
//            "orgzType" : 2,
//            "isParent" : false,
//            "orgzUrl" : "擎木科技/",
//            "parentId" : 1
//        }
        List listAll=new ArrayList<>();
        List<User> list = userService.findAll(orgId);
        for (User user : list){
            Map<String,Object> map=new HashMap<>();
            map.put("id","user_"+user.getId());
            map.put("name",user.getName());
            map.put("cls","iconfont icon-lingdao");
            map.put("parentId",orgId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * 查询角色下菜单权限集合
     */
    @RequestMapping(value = "/findRolePermissions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findRolePermissions(Long roleId,Long menuId) throws URISyntaxException {
        log.debug("REST request to save Menu : {}");
        List listAll=new ArrayList<>();

        RoleMenu rm=roleMenuRepository.findOneByRoleIdAndMenuId(roleId,menuId);
        for(RolePermission permission:rm.getPermissions()){
            listAll.add("user_" + permission.getUser().getId());
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * 保存角色菜单动作
     */
    @RequestMapping(value = "/saveRoleMenu",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> saveRoleMenu(String jsondata) throws Exception{
        //[{id:11,name:"角色管理",status:false,parent:{id:1,name:"系统管理",status:false},
        // actions:[{id:111,name:"新增",flag:"add",status:false},{id:222,name:"删除",flag:"del",status:false}]}

        JSONArray array=new JSONArray(jsondata);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj=array.getJSONObject(i);
            doSave(obj);
        }
        return new ResponseEntity(HttpStatus.OK) ;
    }

    private void doSave(JSONObject jsonObject) throws Exception{
        Long roleId=jsonObject.getLong("roleId");
        Long id=jsonObject.getLong("id");
        boolean status=jsonObject.getBoolean("status");

//        JSONObject parentObject=jsonObject.getJSONObject("parent");
//        Long parentId=parentObject.getLong("id");
//        boolean parentStatus=parentObject.getBoolean("status");

        List<Map<String,Object>> listActions=new ArrayList<>();
        JSONArray actionsArray=jsonObject.getJSONArray("actions");
        for (int i = 0; i < actionsArray.length(); i++) {
            Map<String,Object> map=new HashMap<>();
            JSONObject action=actionsArray.getJSONObject(i);
            Long cId=action.getLong("id");
            boolean cStatus=action.getBoolean("status");
            map.put("id",cId);
            map.put("status",cStatus);
            listActions.add(map);
        }

        RoleMenu roleMenu=authorService.findOneByRoleIdAndMenuId(roleId,id);
        if(roleMenu==null){
            if(status){
                roleMenu=new RoleMenu();
                roleMenu.setRole(roleService.findOne(roleId));
                roleMenu.setMenu(menuService.findOne(id));
                for (Map<String,Object> map : listActions){
                    if(map.get("status").toString().equals("true")){
                        MenuAction ma=menuActionService.findOne(Long.parseLong(map.get("id").toString()));
                        RoleMenuAction rma=new RoleMenuAction();
                        rma.setRoleMenu(roleMenu);
                        rma.setMenuAction(ma);
                        roleMenu.getActions().add(rma);
                    }
                }

                roleMenuRepository.save(roleMenu);
            }
        }else{
            if(status){
                //先清空再添加
                roleMenuActionRepository.deleteInBatch(roleMenu.getActions());

                Set<RoleMenuAction> setRma=new HashSet<>();
                for (Map<String,Object> map : listActions){
                    if(map.get("status").toString().equals("true")){
                        MenuAction ma=menuActionService.findOne(Long.parseLong(map.get("id").toString()));
                        RoleMenuAction rma=new RoleMenuAction();
                        rma.setRoleMenu(roleMenu);
                        rma.setMenuAction(ma);
                        setRma.add(rma);
                    }
                }
                roleMenu.setActions(setRma);

                roleMenuRepository.save(roleMenu);
            }else{
                //清除菜单所有动作
                roleMenuRepository.delete(roleMenu.getId());

            }
        }
    }

    /**
     * 保存角色菜单权限
     */
    @RequestMapping(value = "/saveRolePermission",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> saveRolePermission(Long roleId,Long menuId,String jsondata,String deldata) throws Exception{
        //{id:11,name:"角色管理",status:false,parent:{id:1,name:"系统管理",status:false},
        // oprs:[{id:111,name:"新增",flag:"add",status:false},{id:222,name:"删除",flag:"del",status:false}]}
        List<Long> listUser=new ArrayList<>();//新增的用户
        List<Long> listDelUser=new ArrayList<>();//移除的用户

        JSONArray array=new JSONArray(jsondata);
        for (int i = 0; i < array.length(); i++) {
            listUser.add(array.getLong(i));
        }

        JSONArray array2=new JSONArray(deldata);
        for (int j = 0; j < array2.length(); j++) {
            listDelUser.add(array.getLong(j));
        }

        Map<Long,Long> map=new HashMap<>();
        RoleMenu rm=roleMenuRepository.findOneByRoleIdAndMenuId(roleId, menuId);
        if(rm==null){
            rm=new RoleMenu();
            rm.setRole(roleService.findOne(roleId));
            rm.setMenu(menuService.findOne(menuId));
        }
        rm.setIsConfig(true);
        roleMenuRepository.save(rm);

        for(RolePermission permission:rm.getPermissions()){
            Long userId=permission.getUser().getId();
            map.put(userId,permission.getId());
        }

        //保存新用户
        for (Long id : listUser){
            if(!map.containsKey(id)){
                RolePermission rp=new RolePermission();
                rp.setRoleMenu(rm);
                rp.setUser(userService.getUserWithAuthorities(id));
                rolePermissionRepository.save(rp);
            }
        }
        //移除用户
        for (Long id : listDelUser){
            if(map.containsKey(id)){
                Long rpId=map.get(id);
                rolePermissionRepository.deleteById(rpId);
            }
        }

        return new ResponseEntity(HttpStatus.OK) ;
    }


}
