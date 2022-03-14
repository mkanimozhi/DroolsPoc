package com.drools.poc.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DroolsPocRuleResponse {

	private RuleResponse ruleResponse;

	public RuleResponse getRuleResponse() {
		return ruleResponse;
	}

	public void setRuleResponse(RuleResponse ruleResponse) {
		this.ruleResponse = ruleResponse;
	}

	
}
