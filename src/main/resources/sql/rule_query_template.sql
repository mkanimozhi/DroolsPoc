create database kani;
show databases;
use kani;
create user 'kani'@'%' identified by 'kani';
grant all on kani.* to 'kani'@'%';
show tables;
select * from user;

create database restservice;
show databases;
use restservice;
create user 'restservice'@'%' identified by 'restservice';
grant all on restservice.* to 'restservice'@'%';
show tables;
desc user;
select * from user;
select * from product;
select * from purchase;
select * from age_rules;
drop table age_rules;
CREATE TABLE age_rules(id INTEGER, min_age INTEGER, max_age INTEGER, status VARCHAR(256),cond1 VARCHAR(256),oper VARCHAR(256),cond2 VARCHAR(256));
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(1, 0, 2,'Infant','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(2, 2, 6,'Baby','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(3, 6, 13,'Young Age','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(4, 13,18,'Juvenile','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(5, 18,41,'Youth','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(6, 41,61,'Middle Aged','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(7, 61,81,'Senior Citizen','>=','&&','<');
INSERT INTO age_rules(id, min_age, max_age, status,cond1,oper,cond2) VALUES(8, 81, 100,'Old Aged','>=','&&','<');
commit;
show tables;
select * from rules;
CREATE TABLE rules (
          rule_id INT,
          rule_desc VARCHAR(256),
          PRIMARY KEY (rule_id)
     ) ;
INSERT INTO rules(rule_id, rule_desc) VALUES(1, 'TDS Rule');     
INSERT INTO rules(rule_id, rule_desc) VALUES(2, 'Incentive Rule');
INSERT INTO rules(rule_id, rule_desc) VALUES(3, 'Commission Rule');
select * from rules_param_ids;     
CREATE TABLE rules_param_ids (
          param_id INT,
          rule_id INT,
          param_name VARCHAR(256),
          PRIMARY KEY (param_id, rule_id)
     ) ;     
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(1, 1, 'DeptName');     
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(2, 1, 'Designation');       
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(1, 2, 'DeptName');    
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(2, 2, 'Designation');    
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(3, 2, 'Rating');    
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(1, 3, 'DeptName');    
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(2, 3, 'Designation');    
INSERT INTO rules_param_ids(param_id, rule_id, param_name) VALUES(3, 3, 'Net Sales');    
select * from rules_param_values;
desc rules_param_values;
drop table rules_param_values;
CREATE TABLE rules_param_values (
		  rule_id INT,
          param_id INT,
          param_value_id INT,
          param_value VARCHAR(256),  
          param_cond VARCHAR(256),
          PRIMARY KEY (rule_id, param_id, param_value_id)
     ) ;     
INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(1,
 1, 1, 'IT', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(1,
 1, 2, 'Sales', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(2,
 2, 3, 'Manager', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(2,
 2, 4, 'Regional Manager', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(2,
 3, 5, 'Strong', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(3,
 3, 6, 'Solid', '=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(3,
 3, 1, '$1M', '<=');
 INSERT INTO rules_param_values(rule_id, param_id, param_value_id, param_value, param_cond) VALUES(3,
 3, 2, '$2M', '<=');
 select * from rules_condition;
 drop table rules_condition;
 commit;
CREATE TABLE rules_condition (
		  rule_id INT,
          condition_id INT,
          param_id_1 VARCHAR(256),
          param_id_2 VARCHAR(256),
          operation VARCHAR(256),
          condition_result_id VARCHAR(256),  
          condition_result VARCHAR(256),
          PRIMARY KEY (condition_id)
     ) ;     
 INSERT INTO rules_condition(rule_id, condition_id, param_id_1, param_id_2, operation, condition_result_id, 
 condition_result) VALUES(1, 1, '1', '2', '&&', 'Final', '20%'); 
 INSERT INTO rules_condition(rule_id, condition_id, param_id_1, param_id_2, operation, condition_result_id, 
 condition_result) VALUES(2, 2, '1', '2', '&&', 'R1', 'TRUE/FALSE'); 
 INSERT INTO rules_condition(rule_id, condition_id, param_id_1, param_id_2, operation, condition_result_id, 
 condition_result) VALUES(2, 3, 'R1', '3', '&&', 'Final', '120%'); 
 INSERT INTO rules_condition(rule_id, condition_id, param_id_1, param_id_2, operation, condition_result_id, 
 condition_result) VALUES(3, 4, '1', '2', '&&', 'R1', 'TRUE/FALSE'); 
 INSERT INTO rules_condition(rule_id, condition_id, param_id_1, param_id_2, operation, condition_result_id, 
 condition_result) VALUES(3, 5, 'R1', '3', '&&', 'Final', '50%'); 
commit;     
show tables;
select * from rules;
select * from rules_param_ids;
select * from rules_param_values;
select * from rules_condition;
select rule1.rule_id,rule1.rule_desc,
ruleparam1.param_id,ruleparam1.param_name,
ruleparamval1.param_value_id, ruleparamval1.param_value,ruleparamval1.param_cond,
rules_condition1.condition_id,rules_condition1.param_id_1,rules_condition1.param_id_2,rules_condition1.operation,
rules_condition1.condition_result_id,rules_condition1.condition_result
from rules rule1 
inner join rules_param_ids ruleparam1 on rule1.rule_id = ruleparam1.rule_id 
inner join rules_param_values ruleparamval1 on ruleparam1.rule_id = ruleparamval1.rule_id 
inner join rules_condition rules_condition1 on ruleparamval1.rule_id = rules_condition1.rule_id
where rule1.rule_id = 1;

------------

select rule1.rule_id,rule1.rule_desc,
ruleparam1.param_id,ruleparam1.param_name,
ruleparamval1.param_value_id, ruleparamval1.param_value,ruleparamval1.param_cond
from rules rule1 
inner join rules_param_ids ruleparam1 on rule1.rule_id = ruleparam1.rule_id 
inner join rules_param_values ruleparamval1 on ruleparam1.rule_id = ruleparamval1.rule_id 
where rule1.rule_id = 1;
-------
select rule1.rule_id,rule1.rule_desc,
ruleparam1.param_id,ruleparam1.param_name
from rules rule1 
inner join rules_param_ids ruleparam1 on rule1.rule_id = ruleparam1.rule_id 
where rule1.rule_id = 1;

SELECT id, min_age, max_age, status,cond1,oper,cond2 FROM age_rules;
select * from drools_rules;
drop table drools_rules;
CREATE TABLE drools_rules (
		  rule_id INT,
          rule_desc VARCHAR(256),
          param_name1 VARCHAR(256),
          param_cond1 VARCHAR(256),
          value1 VARCHAR(256),
          res_cond1 VARCHAR(256),
          operation1 VARCHAR(256),
          param_name2 VARCHAR(256),
          param_cond2 VARCHAR(256),
          value2 VARCHAR(256),
          res_cond2 VARCHAR(256),
          operation2 VARCHAR(256),
          PRIMARY KEY (rule_id)
     ) ;  
     commit;
     show tables;
     select * from rules;
     drop table purchase;
     select * from employee;
select * from drools_rules;     
delete from drools_rules where rule_id=7;
update drools_rules set rule_desc ='rating', param_name1 ='designation' where rule_id = 9;
INSERT INTO drools_rules(rule_id, rule_desc) VALUES(6, :rule_desc);
INSERT INTO drools_rules(rule_id, rule_desc, param_name1, param_cond1, value1, res_cond1, operation1,
 param_name2, param_cond2, value2, res_cond2, operation2) VALUES(1, 
 'TDS Rule', 'DeptName', '=', 'IT', '', '&&', 'Designation', '=','Sales','20%','');
 INSERT INTO drools_rules(rule_id, rule_desc, param_name1, param_cond1, value1, res_cond1, operation1,
 param_name2, param_cond2, value2, res_cond2, operation2) VALUES(2, 
 'Incentive Rule', 'DeptName', '=', 'Manager', '', '&&', 'Designation', '=','Regional Manager','TRUE/FALSE',''); 
commit;     
select * from employee where empId = 1 and rule_id = 1;
select * from employee;
drop table employee;
CREATE TABLE employee (
          empId INT,
          empName VARCHAR(256),
          deptName VARCHAR(256),
          designation VARCHAR(256),
          rule_id INT,
          rule_desc VARCHAR(256),
          salary VARCHAR(256),
          bonus VARCHAR(256),
          PRIMARY KEY (empId)
     ) ;