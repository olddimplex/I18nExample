package domain;

public enum EDisplayMessageType {
	
    SUCCESS("Success!"), ERROR("Error!");

    private final String label;

    private EDisplayMessageType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public EDisplayMessageType findByName(final String name) {
    	for(final EDisplayMessageType displayMessageType : values()) {
    		if(displayMessageType.name().equalsIgnoreCase(name)) {
    			return displayMessageType;
    		}
    	}
    	return SUCCESS;
    }
}
