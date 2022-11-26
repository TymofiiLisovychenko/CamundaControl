package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class RecordDataDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        long polisaId = (long)delegateExecution.getVariable("poilsa_id");
        long test = 0;

    }
}