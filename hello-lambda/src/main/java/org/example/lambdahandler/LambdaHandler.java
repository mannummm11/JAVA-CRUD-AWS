package org.example.lambdahandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        EmployeeService employeeService = EmployeeService.getInstance();
        switch (apiGatewayRequest.getHttpMethod()) {
            case "POST": {
                return employeeService.saveEmployee(apiGatewayRequest, context);
            }
            case "GET" : {
                context.getLogger().log("Get Invoked");
                if(apiGatewayRequest.getPathParameters() != null) {

                } else {
                    employeeService.getAllEmployees(apiGatewayRequest, context);
                }
                break;
            }
            case "DELETE" : {
                if(apiGatewayRequest.getPathParameters() != null) {

                }
                break;
            }
            default: {

            }
            return null;
        }
        return null;
    }
}
