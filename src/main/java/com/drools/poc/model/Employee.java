package com.drools.poc.model;

public class Employee {

	private int empId;
	private String empName;
	private String deptName;
	private String designation;
	private int rule_id;
	private String rule_desc;
	private String salary;
	private String bonus;

	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public int getRule_id() {
		return rule_id;
	}
	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}
	public String getRule_desc() {
		return rule_desc;
	}
	public void setRule_desc(String rule_desc) {
		this.rule_desc = rule_desc;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	@Override
	public String toString() {
		return "Employee [empName=" + empName + ", salary=" + salary + "]";
	}
	
	
}
