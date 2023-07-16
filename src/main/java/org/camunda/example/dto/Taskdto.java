package org.camunda.example.dto;

import java.util.Date;

public class Taskdto {
private String id ;
private String executionId;
private String processInstanceId ; 
private String processDefinitionId;
private String name;
private String taskDefinitionKey ;
private String assignee;
private String delegation;
private String description;
private int priority;
private Date createTime;
private Date dueDate;
private boolean isSuspended;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getExecutionId() {
	return executionId;
}
public void setExecutionId(String executionId) {
	this.executionId = executionId;
}
public String getProcessInstanceId() {
	return processInstanceId;
}
public void setProcessInstanceId(String processInstanceId) {
	this.processInstanceId = processInstanceId;
}
public String getProcessDefinitionId() {
	return processDefinitionId;
}
public void setProcessDefinitionId(String processDefinitionId) {
	this.processDefinitionId = processDefinitionId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getTaskDefinitionKey() {
	return taskDefinitionKey;
}
public void setTaskDefinitionKey(String taskDefinitionKey) {
	this.taskDefinitionKey = taskDefinitionKey;
}
public String getAssignee() {
	return assignee;
}
public void setAssignee(String assignee) {
	this.assignee = assignee;
}
public String getDelegation() {
	return delegation;
}
public void setDelegation(String delegation) {
	this.delegation = delegation;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getPriority() {
	return priority;
}
public void setPriority(int priority) {
	this.priority = priority;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(java.util.Date date) {
	this.createTime = date;
}
public Date getDueDate() {
	return dueDate;
}
public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
}
public boolean isSuspended() {
	return isSuspended;
}
public void setSuspended(boolean isSuspended) {
	this.isSuspended = isSuspended;
}


}