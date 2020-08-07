/**
 * 
 */
package com.sboot.controllers;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sboot.model.WorkflowRequest;

/**
 * @author kk
 *
 */
@RestController
@RequestMapping(path = "/workflow/service")
public class WorkflowController {

	@Autowired
	private RuntimeService runtimeService;

	private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

	@PostMapping(path = "/trigger", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ResponseEntity<Map<String, String>> triggerWorkflow(@RequestBody WorkflowRequest request) {
		log.info("workflow request: {}", request);
		Map<String, String> response = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("param1", "value1");
		ProcessInstance execute = runtimeService.createProcessInstanceByKey(request.getProcessId()).setVariables(map)
				.execute();
		log.info("process response:{}", execute);
		response.put("status", "success");
		return ResponseEntity.accepted().body(response);
	}

}
