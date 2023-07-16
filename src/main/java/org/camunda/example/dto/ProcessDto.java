package org.camunda.example.dto;
public class ProcessDto {
	private String key;
	private String id;
	private String Processinstanceid;

    public ProcessDto() {
    }
    
    public String getProcessinstanceid() {
		return Processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		Processinstanceid = processinstanceid;
	}

	public ProcessDto(String key, String id) {
        this.key = key;
        this.id = id;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
		this.key= key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
}
