package com.camunda.demo.springboot.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.demo.springboot.ProcessConstants;

@RestController
@RequestMapping("/order")
public class OrderRestController {
  
  @Autowired
  private ProcessEngine camunda;
  
  public static class OrderDetails {
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
  }

  @RequestMapping(method=RequestMethod.POST)
  //public void placeOrderPOST(String orderId, int amount) {
  public void placeOrderPOST(@RequestBody OrderDetails request) {
    //placeOrder(orderId, amount);
	  placeOrder(request.orderId, request.amount);
  }

  /**
   * we need a method returning the {@link ProcessInstance} to allow for easier tests,
   * that's why I separated the REST method (without return) from the actual implementaion (with return value)
   */
  public ProcessInstance placeOrder(String orderId, int amount) {
    return camunda.getRuntimeService().startProcessInstanceByKey(//
        ProcessConstants.PROCESS_KEY_order, //
        Variables //
          .putValue(ProcessConstants.VAR_NAME_orderId, orderId) //
          .putValue(ProcessConstants.VAR_NAME_amount, amount));
  }
}
