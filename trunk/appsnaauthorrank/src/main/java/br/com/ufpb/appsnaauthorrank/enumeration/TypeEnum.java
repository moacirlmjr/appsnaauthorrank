package br.com.ufpb.appsnaauthorrank.enumeration;

public enum TypeEnum {
	
	STRING_TYPE("String"),
	DOUBLE_TYPE("double"),
	INT_TYPE("int"),
	CHAR_TYPE("char"),
	BOOLEAN_TYPE("boolean");
	
	private final String type;
	
	
	private TypeEnum(String type) {
		this.type=type;
		
	}

	public String getType() {
		return type;
	}
	
	
	
	
	

}
