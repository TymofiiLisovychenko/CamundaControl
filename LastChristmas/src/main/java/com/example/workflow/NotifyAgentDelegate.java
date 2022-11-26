package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class NotifyAgentDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        //String money = "0.0";
        //String ticketType = "Coach";

        //money = (String) delegateExecution.getVariable("money");
        long polisaId = (long)delegateExecution.getVariable("poilsa_id");
        //double moneyDouble = Double.parseDouble(money);
        long test = 0;
        //delegateExecution.setVariable("ticketType", ticketType);
        //send post to localhost 8080 with agent_id as parameter or something


    }
}