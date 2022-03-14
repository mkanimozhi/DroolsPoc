package com.drools.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.drools.poc.constants.DroolsPocDBConstants;
import com.drools.poc.constants.DroolsPocServiceConstants;
import com.drools.poc.model.Discount;
import com.drools.poc.model.DroolsRule;
import com.drools.poc.model.Event;
import com.drools.poc.model.OrderEvent;
import com.drools.poc.model.response.DroolsPocRuleResponse;
import com.drools.poc.model.response.RuleResponse;
import com.drools.poc.util.DroolsPocDBUtil;
import com.drools.poc.util.DroolsPocServiceUtil;

@RestController
@RequestMapping(path="/drools")
public class DroolsPocController {

	@GetMapping(path="/generate/rule")
	public @ResponseBody String generateDrl() {
		String drl = "";
		try {
			ResultSet resultSet = DroolsPocDBUtil.executeQuery(DroolsPocDBConstants.SELECT_DROOLS_RULES);
			List<Map> finalList = new ArrayList<>();
			while(resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				String data = resultSet.getString(DroolsPocDBConstants.PARAM_NAME1) + 
						"="+resultSet.getString(DroolsPocDBConstants.PARAM_COND1) +
						"'"+resultSet.getString(DroolsPocDBConstants.VALUE1)+"'";
				map.put(DroolsPocServiceConstants.POC_RULE, data);
				map.put(DroolsPocServiceConstants.POC_RES, resultSet.getString(DroolsPocDBConstants.RES_COND1));
				OrderEvent orderEvent = new OrderEvent();
				map.put(DroolsPocServiceConstants.EVENT_TYPE, orderEvent.getClass().getName());
				finalList.add(map);
			}
			ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
			drl = objectDataCompiler.compile(finalList,  getNewRulesStream());
			DroolsPocServiceUtil.writeToFile(drl);
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drl;
	}

	@PostMapping(path="/fire/rule")
	public @ResponseBody DroolsPocRuleResponse fireDrl(@RequestBody Discount discount) {
		DroolsPocRuleResponse response = new DroolsPocRuleResponse();
		RuleResponse ruleResponse = new RuleResponse();
		try {
			// Create an event that will be tested against the rule. In reality, the event would be read from some external store.
			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setDeptName(discount.getFieldName());
			Discount discount1 = evaluateDrl(orderEvent);
			ruleResponse.setStatus("200");
			ruleResponse.setMessage("");
			ruleResponse.setResponseBody(discount1);
			response.setRuleResponse(ruleResponse);
			System.out.println("discount =================>"+discount1.getRuleResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private Discount evaluateDrl(Event event) throws Exception {
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write(DroolsPocServiceConstants.DRL_FILE_PATH, DroolsPocServiceUtil.readFromFile());
		kieServices.newKieBuilder(kieFileSystem).buildAll();

		KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		KieSession newKieSession = kieContainer.getKieBase().newKieSession();
		Discount discount = new Discount();
		newKieSession.setGlobal("discount", discount);
		newKieSession.insert(event);
		newKieSession.fireAllRules();
		return discount;
	}


	@PostMapping(path="/update/rule")
	public @ResponseBody DroolsPocRuleResponse updateRule(@RequestBody DroolsRule droolsRule) {
		DroolsPocRuleResponse response = new DroolsPocRuleResponse();
		RuleResponse ruleResponse = new RuleResponse();
		boolean saveStatus = false;
		try {
			if(droolsRule != null) {
				String query = constructDroolsRulesQuery(droolsRule, true);
				saveStatus = DroolsPocDBUtil.save(query);
			} else {
				ruleResponse.setStatus("400");
				ruleResponse.setMessage("Request is Empty");
				
			}
		} catch (Exception e) {
			System.out.println("Exception Occurred"+e);
			e.printStackTrace();
		}
		if(saveStatus) {
			ruleResponse.setStatus("200");
			ruleResponse.setMessage("Drool: "+ droolsRule.getRule_desc() + " rule is saved Successfully...");
		} else {
			ruleResponse.setStatus("400");
			ruleResponse.setMessage("Drool: "+ droolsRule.getRule_desc() + " rule is failed to Persist.");
		}
		response.setRuleResponse(ruleResponse);
		return response;
	}
	
	@PostMapping(path="/add/rule")
	public @ResponseBody DroolsPocRuleResponse addRule(@RequestBody DroolsRule droolsRule) {
		DroolsPocRuleResponse response = new DroolsPocRuleResponse();
		RuleResponse ruleResponse = new RuleResponse();
		boolean saveStatus = false;
		try {
			if(droolsRule != null) {
				String query = constructDroolsRulesQuery(droolsRule, false);
				saveStatus = DroolsPocDBUtil.save(query);
			} else {
				ruleResponse.setStatus("400");
				ruleResponse.setMessage("Request is Empty");
				
			}
		} catch (Exception e) {
			System.out.println("Exception Occurred"+e);
			e.printStackTrace();
		}
		if(saveStatus) {
			ruleResponse.setStatus("200");
			ruleResponse.setMessage("Drool: "+ droolsRule.getRule_desc() + " rule is saved Successfully...");
		} else {
			ruleResponse.setStatus("400");
			ruleResponse.setMessage("Drool: "+ droolsRule.getRule_desc() + " rule is failed to Persist.");
		}
		response.setRuleResponse(ruleResponse);
		return response;
	}


	private String constructDroolsRulesQuery(DroolsRule droolsRule, boolean isUpdate) {
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder paramBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		paramBuilder.append("INSERT INTO drools_rules(");
		paramBuilder.append("rule_id,");
		valueBuilder.append(droolsRule.getRule_id() +",");
		if(droolsRule.getRule_desc() != null && StringUtils.isNotBlank(droolsRule.getRule_desc())) {
			paramBuilder.append("rule_desc,");
			valueBuilder.append(" '"+droolsRule.getRule_desc()+"',");
		}
		if(droolsRule.getParam_name1() != null && StringUtils.isNotBlank(droolsRule.getParam_name1())) {
			paramBuilder.append("param_name1,");
			valueBuilder.append(" '"+droolsRule.getParam_name1()+"',");
		}
		if(droolsRule.getParam_cond1() != null && StringUtils.isNotBlank(droolsRule.getParam_cond1())) {
			paramBuilder.append("param_cond1,");
			valueBuilder.append(" '"+droolsRule.getParam_cond1()+"',");
		}
		if(droolsRule.getValue1() != null && StringUtils.isNotBlank(droolsRule.getValue1())) {
			paramBuilder.append("value1,");
			valueBuilder.append(" '"+droolsRule.getValue1()+"',");
		}
		if(droolsRule.getRes_cond1() != null && StringUtils.isNotBlank(droolsRule.getRes_cond1())) {
			paramBuilder.append("res_cond1,");
			valueBuilder.append(" '"+droolsRule.getRes_cond1()+"',");
		}
		if(droolsRule.getOperation1() != null && StringUtils.isNotBlank(droolsRule.getOperation1())) {
			paramBuilder.append("operation1,");
			valueBuilder.append(" '"+droolsRule.getOperation1()+"',");
		}
		if(droolsRule.getParam_name2() != null && StringUtils.isNotBlank(droolsRule.getParam_name2())) {
			paramBuilder.append("param_name2,");
			valueBuilder.append(" '"+droolsRule.getParam_name2()+"',");
		}
		if(droolsRule.getParam_cond2() != null && StringUtils.isNotBlank(droolsRule.getParam_cond2())) {
			paramBuilder.append("param_cond2,");
			valueBuilder.append(" '"+droolsRule.getParam_cond2()+"',");
		}
		if(droolsRule.getValue2() != null && StringUtils.isNotBlank(droolsRule.getValue2())) {
			paramBuilder.append("value2,");
			valueBuilder.append(" '"+droolsRule.getValue2()+"',");
		}
		if(droolsRule.getRes_cond2() != null && StringUtils.isNotBlank(droolsRule.getRes_cond2())) {
			paramBuilder.append("res_cond2,");
			valueBuilder.append(" '"+droolsRule.getRes_cond2()+"',");
		}
		if(droolsRule.getOperation2() != null && StringUtils.isNotBlank(droolsRule.getOperation2())) {
			paramBuilder.append("operation2,");
			valueBuilder.append(" '"+droolsRule.getOperation2()+"',");
		}
		String paramStr = paramBuilder.toString();
		queryBuilder.append(paramStr.substring(0, paramStr.length()-1));
		queryBuilder.append(") VALUES(");
		String valueStr = valueBuilder.toString();
		queryBuilder.append(valueStr.substring(0, valueStr.length()-1));
		queryBuilder.append(")");
		return queryBuilder.toString();
	}

	private DroolsRule populateDroolsRule(ResultSet resultSet1) throws SQLException {
		DroolsRule droolsRule = new DroolsRule();
		droolsRule.setRule_id(resultSet1.getInt(DroolsPocDBConstants.RULE_ID));
		droolsRule.setRule_desc(resultSet1.getString(DroolsPocDBConstants.RULE_DESC));
		droolsRule.setParam_name1(resultSet1.getString(DroolsPocDBConstants.PARAM_NAME1));
		droolsRule.setParam_cond1(resultSet1.getString(DroolsPocDBConstants.PARAM_COND1));
		droolsRule.setValue1(resultSet1.getString(DroolsPocDBConstants.VALUE1));
		droolsRule.setRes_cond1(resultSet1.getString(DroolsPocDBConstants.RES_COND1));
		droolsRule.setOperation1(resultSet1.getString(DroolsPocDBConstants.OPERATION1));
		droolsRule.setParam_name2(resultSet1.getString(DroolsPocDBConstants.PARAM_NAME2));
		droolsRule.setParam_cond2(resultSet1.getString(DroolsPocDBConstants.PARAM_COND2));
		droolsRule.setValue2(resultSet1.getString(DroolsPocDBConstants.VALUE2));
		droolsRule.setRes_cond2(resultSet1.getString(DroolsPocDBConstants.RES_COND2));
		return droolsRule;
	}

	private static InputStream getNewRulesStream() throws FileNotFoundException {
		return new FileInputStream(DroolsPocServiceConstants.TEMPLATE_FILE_PATH);
	}

}
