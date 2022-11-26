package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Named
public class SendMailDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        long polisaId = (long)delegateExecution.getVariable("poilsa_id");
        //long test = 0;
        String task_html = "http://localhost:8090/polisa/send_mail";
        String json_polisaId = "{" + String.valueOf(polisaId) + "}";
        try {
            URL url = new URL(task_html);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            byte[] input = json_polisaId.getBytes("utf-8");
            os.write(input, 0, input.length);
            int status = con.getResponseCode();
            con.disconnect();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    void CompleteTask(){
        String process_id = GetWorkflowId();

        StringBuffer tasks = new StringBuffer();
        try {
            URL url = new URL("http://localhost:8080/engine-rest/task");
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                tasks.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String jsonTasks = tasks.toString();
        String task_id = null;
        JSONArray obj = null;
        try {
            obj = new JSONArray(jsonTasks);
            for (int i = 0; i < obj.length(); i++)
            {
                String post_id = obj.getJSONObject(i).getString("processInstanceId");
                if(post_id.equals(process_id)){
                    task_id = obj.getJSONObject(i).getString("id");
                    break;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String task_html = "http://localhost:8080/engine-rest/task/" + task_id + "/complete";
        try {
            URL url = new URL(task_html);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");


            int status = con.getResponseCode();
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String GetWorkflowId(){
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL("http://localhost:8080/engine-rest/process-definition/key/Process_03j5yil/start");
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String jsonString = content.toString();
        String process_id;
        try {
            JSONObject obj = new JSONObject(jsonString);
            process_id = obj.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return process_id;
    }
}
