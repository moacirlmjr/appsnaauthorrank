package br.com.ufpb.appsnaauthorrank.beans.to;

import br.com.ufpb.appsnaauthorrank.enumeration.TypeEnum;

public class XmlTO {
	
	private String attrId;
	private boolean isForNode;
	private String attrName;
	private TypeEnum attrType;
	
	public XmlTO(){
		
	}
	
	public XmlTO(String attrId, boolean isForNode, String attrName,
			TypeEnum attrType) {
		super();
		this.attrId = attrId;
		this.isForNode = isForNode;
		this.attrName = attrName;
		this.attrType = attrType;
	}


	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public boolean isForNode() {
		return isForNode;
	}

	public void setForNode(boolean isForNode) {
		this.isForNode = isForNode;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public TypeEnum getAttrType() {
		return attrType;
	}

	public void setAttrType(TypeEnum attrType) {
		this.attrType = attrType;
	}

	
	
	
	
}
