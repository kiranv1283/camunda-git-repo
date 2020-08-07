/**
 * 
 */
package com.camunda.demo.springboot.rest;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.demo.springboot.ProcessConstants;

/**
 * @author kk
 *
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

	@Autowired
	private RuntimeService runtimeService;

	private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

	public static class WorkflowRequest {

		private String processId;

		private String orderId;

		private int amount;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public String getProcessId() {
			return processId;
		}

		public void setProcessId(String processId) {
			this.processId = processId;
		}

		@Override
		public String toString() {
			return "WorkflowRequest [processId=" + processId + ", orderId=" + orderId + ", amount=" + amount + "]";
		}
		
		
	}

	@PostMapping(path = "/trigger", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, String>> triggerWorkflow(@RequestBody WorkflowRequest request) {
		log.info("workflow request: {}", request);
		Map<String, String> response = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("param1", "value1");
		ProcessInstance execute = runtimeService.createProcessInstanceByKey(request.getProcessId()).setVariables(map)
				.execute();
		log.info("process response:{}", execute);
		response.put("status", "success");
		response.put("ProcessInstance", execute.getId());
		return ResponseEntity.accepted().body(response);
	}

	@RequestMapping(path = "/start", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Map<String, String>> startWorkflow(@RequestBody WorkflowRequest request) {
		Map<String, String> response = new HashMap<>();
		ProcessInstance execute =  runtimeService.startProcessInstanceByKey(request.getProcessId(),
				Variables.putValue(ProcessConstants.VAR_NAME_orderId, request.orderId) //
						.putValue(ProcessConstants.VAR_NAME_amount, request.amount));
		
		response.put("status", "success");
		response.put("ProcessInstance", execute.getId());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
