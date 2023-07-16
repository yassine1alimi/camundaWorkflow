package org.camunda.example.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;

import org.apache.ibatis.ognl.BooleanExpression;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.camunda.bpm.engine.impl.javax.el.Expression;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.UserDto;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.tasklist.Tasklist;
import org.camunda.example.ProcessConstants;
import org.camunda.example.dto.ProcessDto;
import org.camunda.example.dto.Taskdto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
@CrossOrigin("*")
@RestController
@RequestMapping("/process")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class StartProcessRestController  {
	@Autowired
    private ProcessEngine camunda;
	
	
	
	 ModelMapper modelMapper = new ModelMapper() ;
		

	 	
    @PostMapping(consumes = "application/json",path = "/start")
    public void start() {
    	ProcessDto processDto = new ProcessDto();
    	processDto.setKey(ProcessConstants.PROCESS_KEY_DAS);
        startProcess(processDto.getKey());
    }

    public ProcessInstance startProcess(String key) {
        return camunda.getRuntimeService().startProcessInstanceByKey(key);
    }
    
    @GetMapping(path="/getxml")
    @Produces("image/png")
    public InputStream getProcessXML() {
    	InputStream input ;
    	String idproc = getProcessDefinition();
    	input=  camunda.getRepositoryService().getProcessDiagram(idproc);
    	return input;
    	
    }
    @GetMapping("/getdef")
    public String getProcessDefinition () {
    	List<ProcessDefinition> list = new ArrayList<>();
		list =  camunda.getRepositoryService().createProcessDefinitionQuery().list();
		List<ProcessDto> listdto = Arrays.asList(modelMapper.map(list.get(0), ProcessDto.class));
		return listdto.get(0).getId();
		
		
    }
    @GetMapping("/getoutputVariables")
    public String getVaribale() {
    	Task task = camunda.getTaskService().createTaskQuery().processInstanceId(getProcessInstance()).singleResult();
    	String taskid = task.getId();
        ArrayList <String> list = new ArrayList<String>();

    	Object values;
    	values = camunda.getTaskService().getVariables(taskid).get("Status");
    	return values.toString();
    	
    }
    /*assign task*/
    @PostMapping("/assignetask")
    public String assignetask(@RequestBody String userId) {
    	
   	int i=0;
    	Task currentTask = camunda.getTaskService().createTaskQuery().list().get(i);
    	camunda.getTaskService().setAssignee(currentTask.getId(), userId);
    	return currentTask.getAssignee();
    	
    }
    /*complete tasks specific status returned*/
    @PostMapping("/completetask")
    public  void completeTask()   {
    	
    	String idtask = getalltasks().get(0).getId();
    	
    		camunda.getTaskService().complete(idtask);
			//return  execution.getVariable(ProcessConstants.outputvariable).toString();
    		
        	
    	}
    @DeleteMapping("deleteprocess")
    public void deleteprocess() {
    	camunda.getRuntimeService().deleteProcessInstance(getProcessInstance(), getProcessDefinition());
    }
//    @PostMapping("/completetaskSU")
//    public  void executeTask()   {
//    	Map<String,Object> variables = new HashMap<>();
//      	 variables.put("isSuperuser", true);
//    	String idtask = getalltasks().get(0).getId();
//    	
//    		camunda.getTaskService().complete(idtask,variables);
//        	
//    	}
//    @PostMapping("/completetaskU")
//    public  void executeTaskUser( ) {
//    	
//    	
//   	 Map<String,Object> variables = new HashMap<>();
//   	 variables.put("isSuperuser", false);
//
//    	String idtask = getalltasks().get(0).getId();
//    	
//    		camunda.getTaskService().complete(idtask,variables);
//        	
//    	}
    @PostMapping("/completetask_valid")
    public void executeTaskDecisionValid() {
    	Map<String,Object> variables =  new HashMap<>();
    	variables.put("isvalid",true);
    	String idtask = getalltasks().get(0).getId();
    	camunda.getTaskService().complete(idtask,variables);
		//return  execution.getVariable(ProcessConstants.outputvariable).toString();

        	
    	}
    @PostMapping("/completetask_nonvalid")
    public  void  executeTaskDecisionNonValid( ) {
    	Map<String,Object> variables =  new HashMap<>();
    	variables.put("isvalid",false);
    	String idtask = getalltasks().get(0).getId();
    	camunda.getTaskService().complete(idtask,variables);
		//return  execution.getVariable(ProcessConstants.outputvariable).toString();
	
    	}
    
    
     @PostMapping("/createuser")
     public void createuser(@RequestBody UserDto userdto) {
 	
    	User user = camunda.getIdentityService().newUser(userdto.getId());
         user.setFirstName(userdto.getFirstName());
         user.setLastName(userdto.getLastName());
        user.setPassword(((User) userdto).getPassword());
         user.setEmail(((User) userdto).getEmail());
         camunda.getIdentityService().saveUser(user);
     }
     @GetMapping("/instance")
     public  String getProcessInstance() {
    	 String instanceid = null;

    	 List<ProcessInstance> processInstance = camunda.getRuntimeService().createProcessInstanceQuery().list();
    	 for(int  i =0;i<processInstance.size();i++) {
    		 instanceid = processInstance.get(i).getId();
        	 return instanceid;

     }
    	 return instanceid;
    	 }
    
    @GetMapping("/tasks")
    public List<Taskdto> getalltasks() {
    	List<Task> list = new ArrayList<>();
    	List<Taskdto> listtask = new ArrayList<>();
    	List<Task> list1 = new ArrayList<>();

    	list1 = camunda.getTaskService().createTaskQuery().processInstanceId(getProcessInstance().toString()).list();

    	//list = camunda.getTaskService().createTaskQuery().processDefinitionKey(ProcessConstants.PROCESS_KEY_DAS).list();
    	//String id = list.get(0).getExecutionId();
    	//List<TaskDto> listtask = Arrays.asList(modelMapper.map(list.get(0), TaskDto.class));
    	for (Task task:list1) {
    		Taskdto newTask = new Taskdto();
    		newTask.setId(task.getId());
    		newTask.setExecutionId(task.getExecutionId());
    		newTask.setCreateTime(task.getCreateTime());
    		newTask.setProcessDefinitionId(task.getProcessInstanceId());
    		newTask.setName(task.getName());
    		newTask.setProcessInstanceId(task.getProcessInstanceId());
    		newTask.setAssignee(task.getAssignee());
    		newTask.setDescription(task.getDescription());

    		listtask.add(newTask);
    	}
    	return listtask;
    	
    }
    @GetMapping("/users")
	public List<User> getallusers() {
	      
	     return camunda.getIdentityService().createUserQuery().list();
	}

    @PostMapping("/demandcertificat")
    public String executedemandcertif() {
    	String idtask = getalltasks().get(0).getId();
    	camunda.getTaskService().complete(idtask);
    	return "la demande est effectuée";
    }
    @PostMapping("/levelcomment")
    public String commentandexecute(@RequestBody String progress) {
    	

    	String idtask = getalltasks().get(0).getId();
    	camunda.getTaskService().complete(idtask);
    	return progress;
    	//return  execution.getVariable(ProcessConstants.outputvariable).toString();

    }
    @GetMapping("/completedtTasks")
    public List<HistoricTaskInstance> getallcompletedtasks(){
    	
    	return camunda.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
    }
	

 
}
