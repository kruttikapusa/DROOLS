package com.myspace.bedqd;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class MeasureDataQualityObject implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	@org.kie.api.definition.type.Label(value = "mdqPriorStartDate")
	private java.util.Date mdqPriorStartDate;
	@org.kie.api.definition.type.Label(value = "mdqPriorEndDate")
	private java.util.Date mdqPriorEndDate;
	@org.kie.api.definition.type.Label(value = "mdqCurrentStartDate")
	private java.util.Date mdqCurrentStartDate;
	@org.kie.api.definition.type.Label(value = "mdqCurrentEndDate")
	private java.util.Date mdqCurrentEndDate;
	@org.kie.api.definition.type.Label(value = "mdqRuleName")
	private java.lang.String mdqRuleName;

	public MeasureDataQualityObject() {
	}

	public java.util.Date getMdqPriorStartDate() {
		return this.mdqPriorStartDate;
	}

	public void setMdqPriorStartDate(java.util.Date mdqPriorStartDate) {
		this.mdqPriorStartDate = mdqPriorStartDate;
	}

	public java.util.Date getMdqPriorEndDate() {
		return this.mdqPriorEndDate;
	}

	public void setMdqPriorEndDate(java.util.Date mdqPriorEndDate) {
		this.mdqPriorEndDate = mdqPriorEndDate;
	}

	public java.util.Date getMdqCurrentStartDate() {
		return this.mdqCurrentStartDate;
	}

	public void setMdqCurrentStartDate(java.util.Date mdqCurrentStartDate) {
		this.mdqCurrentStartDate = mdqCurrentStartDate;
	}

	public java.util.Date getMdqCurrentEndDate() {
		return this.mdqCurrentEndDate;
	}

	public void setMdqCurrentEndDate(java.util.Date mdqCurrentEndDate) {
		this.mdqCurrentEndDate = mdqCurrentEndDate;
	}

	public java.lang.String getMdqRuleName() {
		return this.mdqRuleName;
	}

	public void setMdqRuleName(java.lang.String mdqRuleName) {
		this.mdqRuleName = mdqRuleName;
	}

	public MeasureDataQualityObject(java.util.Date mdqPriorStartDate,
			java.util.Date mdqPriorEndDate, java.util.Date mdqCurrentStartDate,
			java.util.Date mdqCurrentEndDate, java.lang.String mdqRuleName) {
		this.mdqPriorStartDate = mdqPriorStartDate;
		this.mdqPriorEndDate = mdqPriorEndDate;
		this.mdqCurrentStartDate = mdqCurrentStartDate;
		this.mdqCurrentEndDate = mdqCurrentEndDate;
		this.mdqRuleName = mdqRuleName;
	}

}