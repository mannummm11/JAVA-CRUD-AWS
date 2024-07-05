package org.example.lambdahandler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.example.Utility;
import org.example.entity.Employee;

import java.util.List;
import java.util.Map;

public class EmployeeService {

    private static EmployeeService employeeService;

    public static EmployeeService getInstance() {
        if(employeeService == null) {
            employeeService = new EmployeeService();
        }
        return employeeService;
    }
    private EmployeeService() {
        initDynamoDb();
    }

    private DynamoDBMapper dynamoDBMapper;

    private void initDynamoDb() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

    public APIGatewayProxyResponseEvent saveEmployee(APIGatewayProxyRequestEvent request, Context context) {
        context.getLogger().log("incoming data "+request.getBody().toString());
        Employee employee = Utility.convertStringToObj(request.getBody(), context);
        context.getLogger().log("outgoing data "+employee.getEmpid()+" "+employee.getName()+" "+employee.getEmpid());
        dynamoDBMapper.save(employee);
        context.getLogger().log("data inserted successfully!!");
        employee.setResponse("Entry Submitted Successfully.");
        String json = Utility.convertObjToString(employee, context);
        return createResponse(json, 200, Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getAllEmployees(APIGatewayProxyRequestEvent request, Context context) {
        context.getLogger().log("Get employees data is beling fetch");
        List<Employee> list= dynamoDBMapper.scan(Employee.class, new DynamoDBScanExpression());
        String json = Utility.convertListOfObjToString(list, context);
        context.getLogger().log("output json"+ json);
        return createResponse(json, 200, Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent createResponse(String body, int statusCde, Map<String, String> map) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody(body);
        response.setStatusCode(statusCde);
        response.setHeaders(map);
        return response;
    }
}
