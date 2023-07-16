package org.camunda.example;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
ProcessEngine engine = BpmPlatform.getDefaultProcessEngine();
		
        createDefaultUser(engine);
        deployprocess(engine);
	}
	 private static void deployprocess(ProcessEngine engine) {
	    	engine.getRepositoryService().createDeployment().addClasspathResource("process_v1.bpmn").deploy();
	    }
	public static void createDefaultUser(ProcessEngine engine) {
        // and add default user to Camunda to be ready-to-go
		//addDefaultUser(engine);
        // create default "all tasks" filter
        createDefaultFilter(engine);
        //createCompletedTasksFilter(engine, execution);
    }
	  private static void addDefaultUser(ProcessEngine engine) {
	        if (engine.getIdentityService().createUserQuery().userId(ProcessConstants.DEFAULT_USER).count() == 0) {
	            User user = engine.getIdentityService().newUser(ProcessConstants.DEFAULT_USER);
	            user.setFirstName(ProcessConstants.DEFAULT_USER);
	            user.setLastName(ProcessConstants.DEFAULT_USER);
	            user.setPassword(ProcessConstants.DEFAULT_USER);
	            user.setEmail(ProcessConstants.DEFAULT_USER_EMAIL);
	            engine.getIdentityService().saveUser(user);

	            Group group = engine.getIdentityService().newGroup(Groups.CAMUNDA_ADMIN);
	            group.setName("Administrators");
	            group.setType(Groups.GROUP_TYPE_SYSTEM);
	            engine.getIdentityService().saveGroup(group);

	            for (Resource resource : Resources.values()) {
	                Authorization auth = engine.getAuthorizationService().createNewAuthorization(AUTH_TYPE_GRANT);
	                auth.setGroupId(Groups.CAMUNDA_ADMIN);
	                auth.addPermission(ALL);
	                auth.setResourceId(ANY);
	                auth.setResource(resource);
	                engine.getAuthorizationService().saveAuthorization(auth);
	            }

	            engine.getIdentityService().createMembership(ProcessConstants.DEFAULT_USER, Groups.CAMUNDA_ADMIN);
	        }
	    }

	    private static void createDefaultFilter(ProcessEngine engine) {
	        if (engine.getFilterService().createFilterQuery().filterName("All").count() == 0) {

	            Map<String, Object> filterProperties = new HashMap<String, Object>();
	            filterProperties.put("description", "All tasks");
	            filterProperties.put("priority", 10);

	            Filter filter = engine.getFilterService().newTaskFilter() //
	                    .setName("All") //
	                    .setProperties(filterProperties)//
	                    .setOwner(ProcessConstants.DEFAULT_USER)//
	                    .setQuery(engine.getTaskService().createTaskQuery());
	            engine.getFilterService().saveFilter(filter);

	            // and authorize spark user for it
	            authorizeUser(engine, filter);

	        }
	    }
	    private static void createCompletedTasksFilter(ProcessEngine engine, ExecutionEntity execution) {
	        if (engine.getFilterService().createFilterQuery().filterName("CompletedTasks").count() == 0) {

	            Map<String, Object> filterProperties = new HashMap<String, Object>();
	            filterProperties.put("description", "Completedtasks");
	            filterProperties.put("priority", 10);

	            Filter filter = engine.getFilterService().newTaskFilter() //
	                    .setName("CompletedTasks") //
	                    .setProperties(filterProperties)//
	                    .setOwner(ProcessConstants.DEFAULT_USER)//
	                    .setQuery(execution.getProcessEngineServices().getHistoryService().createHistoricTaskInstanceQuery().finished());
	            engine.getFilterService().saveFilter(filter);

	            // and authorize spark user for it
	            authorizeUser(engine, filter);

	        }
	    }
	    private static void authorizeUser(ProcessEngine engine, Filter filter) {
	        if (engine.getAuthorizationService().createAuthorizationQuery().resourceType(FILTER).resourceId(filter.getId()) //
	                .userIdIn(ProcessConstants.DEFAULT_USER).count() == 0) {
	            Authorization managementGroupFilterRead = engine.getAuthorizationService().createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
	            managementGroupFilterRead.setResource(FILTER);
	            managementGroupFilterRead.setResourceId(filter.getId());
	            managementGroupFilterRead.addPermission(ALL);
	            managementGroupFilterRead.setUserId(ProcessConstants.DEFAULT_USER);
	            engine.getAuthorizationService().saveAuthorization(managementGroupFilterRead);
	        }
	    }

	   
}
