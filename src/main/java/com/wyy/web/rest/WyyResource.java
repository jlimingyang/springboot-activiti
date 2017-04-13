package com.wyy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wyy.service.erm.UserService;
import com.wyy.utils.JumpActivityCmd;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 流程管理控制器
 */
@RestController
@RequestMapping("/api/workflow")
public class WyyResource {
    @Inject
    protected UserService userService;
    @Inject
    protected RepositoryService repositoryService;
    @Inject
    protected IdentityService identityService;
    @Inject
    protected RuntimeService runtimeService;
    @Inject
    protected TaskService taskService;
    @Inject
    protected HistoryService historyService;
    @Inject
    protected ManagementService managementService;

    /**
     * 代办任务
     *
     * @return
     */
    @RequestMapping(value = "/testput/{modelId}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> testput(@PathVariable String modelId,@RequestBody MultiValueMap<String, String> values) throws URISyntaxException {

        System.out.println("modelId:"+modelId);

        Iterator<String> iter=values.keySet().iterator();
        while (iter.hasNext()){
            String text=iter.next();
            System.out.println("key:"+text);
            List<String> l=values.get(text);
            for (String s : l){
                System.out.println("value:"+s);
            }
            System.out.println("---------------");
        }

//        ModelSaveRestResource

        Map map=new HashMap<>();

        map.put("success",true);
        return ResponseEntity.ok(map);
    }


    /**
     * 代办任务
     *
     * @return
     */
    @RequestMapping(value = "/mywork",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> mywork() throws URISyntaxException {
        List list=new ArrayList<>();

        String userId=userService.getUserWithAuthorities().getId().toString();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        for (Task task : taskList) {
            Map map=new HashMap<>();

            map.put("id",task.getId());
            map.put("name",task.getName());
            map.put("definitionId",task.getProcessDefinitionId());
            map.put("instanceId",task.getProcessInstanceId());
            map.put("definitionKey",task.getTaskDefinitionKey());
            map.put("assignee",task.getAssignee());
            map.put("createTime",task.getCreateTime());

            list.add(map);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 处理代办任务
     *
     * @return
     */
    @RequestMapping(value = "/complete",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> doneWork(String taskId,String[] keys,String[] values) throws URISyntaxException {
        Map map=new HashMap<>();
        if (keys!=null && keys.length>0){
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i],values[i]);
            }
        }
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        if (StringUtils.isBlank(task.getAssignee())){
            taskService.claim(taskId,userService.getUserWithAuthorities().getId().toString());
        }
        taskService.complete(taskId,map);

        return ResponseEntity.ok().build();
    }

    /**
     * 已完成任务
     *
     * @return
     */
    @RequestMapping(value = "/done",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> doneWork() throws URISyntaxException {
        List list=new ArrayList<>();


        List<HistoricProcessInstance> hpiList=historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : hpiList){
            Map map=new HashMap<>();

            map.put("id",hpi.getId());
            map.put("name",hpi.getName());
            map.put("definitionId",hpi.getProcessDefinitionId());
            map.put("startUserId",hpi.getStartUserId());
            map.put("startTime",hpi.getStartTime());
            map.put("endTime",hpi.getEndTime());

            list.add(map);
        }

        return ResponseEntity.ok(list);
    }

    /**
     * 跳转到指定节点
     *
     * @return
     */
    @RequestMapping(value = "/jump",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> jump(String processInstanceId,String activityId) throws URISyntaxException {

        managementService.executeCommand(new JumpActivityCmd(processInstanceId,activityId));

        Map map=new HashMap<>();
        map.put("success",true);

        return ResponseEntity.ok(map);
    }

}
