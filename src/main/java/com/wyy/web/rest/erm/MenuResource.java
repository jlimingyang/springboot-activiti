package com.wyy.web.rest.erm;

import com.codahale.metrics.annotation.Timed;
import com.wyy.domain.enums.MenuEnum;
import com.wyy.domain.enums.SystemEnum;
import com.wyy.domain.erm.Menu;
import com.wyy.domain.erm.MenuAction;
import com.wyy.repository.SystemEnumRepository;
import com.wyy.service.erm.MenuService;
import com.wyy.web.rest.util.HeaderUtil;
import com.wyy.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2016/12/8.
 */
@RestController
@RequestMapping("/api/erm")
public class MenuResource {

    private final Logger log = LoggerFactory.getLogger(MenuResource.class);

    @Inject
    private MenuService menuService;

    @Inject
    private SystemEnumRepository systemEnumRepository;


    @RequestMapping(value = "/menu",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to save Menu : {}", menu);
        if (menu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menu", "idexists", "A new menu cannot already have an ID")).body(null);
        }
        Menu result = menuService.save(menu);



        return ResponseEntity.created(new URI("/api/erm/menu/" + menu.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("menu", menu.getId().toString()))
                .body(result);
    }


    @RequestMapping(value = "/menu/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> updateMenu(@RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to update Menu : {}", menu);
        if (menu.getId() == null) {
            return createMenu(menu);
        }
        Menu result = menuService.save(menu);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("menu", menu.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/menu",
        method = RequestMethod.GET,
       params = {"parentId"})
    @Timed
    public ResponseEntity<List<Menu>> getAllMenusByParentId(@RequestParam(value = "parentId") Long parentId,Pageable pageable)
        throws URISyntaxException {
        Page<Menu>  page;
        if(parentId.compareTo(new Long(0))>0){
            page = menuService.findAllByParentId(pageable, parentId);
        }else {
            page = menuService.findAllByParentId(pageable, null);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/menu");
        return new ResponseEntity<List<Menu>>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/menu/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> getMenu(@PathVariable Long id) {
        log.debug("REST request to get UserAccount : {}", id);
        Menu menu = menuService.findOne(id);
        return Optional.ofNullable(menu)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/menu/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        log.debug("REST request to delete Menu : {}", id);
        menuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menu", id.toString())).build();
    }


    /*编辑菜单调用*/
    @RequestMapping(value = "/findMenuById",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> findMenuById(Long id) {
        Menu menu = menuService.findOne(id);
        return Optional.ofNullable(menu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /*更加上下级获取菜单（菜单管理导航树）*/
    @RequestMapping(value = "/findAllMenu",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Menu>> findAllMenu(Long id) {
        return new ResponseEntity(menuService.findAllByParentId(id),HttpStatus.OK) ;
    }

    /*获取菜单下的动作点*/
    @RequestMapping(value = "/findAllByMenuId",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MenuAction>> findAllMenuActionByMenu(Long id) {
        return new ResponseEntity(menuService.findAllByMenuId(id), HttpStatus.OK) ;
    }

    /*获取系统动作点*/
    @RequestMapping(value = "/findMenuActions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SystemEnum>> findMenuActions() {
        List<SystemEnum> list = systemEnumRepository.findAllByParent(MenuEnum.class.getSimpleName());
        return new ResponseEntity(list,HttpStatus.OK) ;
    }



    /**************************已下涉及菜单权限*************************************/
    /*（菜单导航加载调用）*/
    /*获取用户拥有的菜单*/
    @RequestMapping(value = "/findUserMenus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Menu>> findUserMenus() {
        List<Menu> list = menuService.findUserMenus();
        return new ResponseEntity(list,HttpStatus.OK) ;
    }

    /*获取当前用户所在菜单 角色的动作点*/
    @RequestMapping(value = "/findRoleActionsByMenu",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MenuAction>> findRoleActionsByMenu(String menulink) {
        List<MenuAction> list =menuService.findUserActionsByMenuName(menulink);
        return new ResponseEntity(list,HttpStatus.OK) ;
    }



}
