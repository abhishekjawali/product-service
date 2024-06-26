package com.productservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.ProductApplication;
import com.productservice.repository.ProductRepository;
import com.productservice.service.ProductServiceImpl;

import software.amazon.awssdk.services.eventbridge.model.ResourceNotFoundException;

public class ProductServiceGetRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static ProductServiceImpl productService = new ProductServiceImpl(new ProductRepository());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceGetRequestHandler.class);

    public ProductServiceGetRequestHandler() {

    }

    public ProductServiceGetRequestHandler(ProductRepository productRepository) {
        productService = new ProductServiceImpl(productRepository);
    }

    /**
     * Handle GET        ∫√√√√√√√√√√√√√√√requests to get a unicorn.
     * Expects {id} as a path parameter
     * @param APIGatewayProxyRequestEvent
     * @param Context
     * @return API Gateway response with the retrieved unicorn data
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, final Context context)  {
        try {
			var productId = event.getPathParameters().get("id");
			var product = productService.getProduct(Integer.parseInt(productId));
			var response = objectMapper.writeValueAsString(product);
            
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(response);
        } catch (ResourceNotFoundException e) {
        	logger.error("ResourceNotFoundException: ", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Error: the provided unicorn does not exist");
        } catch (Exception e) {
            logger.error("Error handling request: ", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error while handling the Request");
        }
    }


}
