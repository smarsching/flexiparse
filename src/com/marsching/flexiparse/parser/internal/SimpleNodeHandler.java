package com.marsching.flexiparse.parser.internal;

import com.marsching.flexiparse.configuration.HandlerConfiguration;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.NodeHandler;
import com.marsching.flexiparse.parser.ParsingHandler;
import com.marsching.flexiparse.parser.exception.ParserException;

public class SimpleNodeHandler implements NodeHandler {
	private ParsingHandler handler;
	private HandlerConfiguration configuration;
	
	public SimpleNodeHandler(ParsingHandler handler, HandlerConfiguration configuration) {
		this.handler = handler;
		this.configuration = configuration;
	}
	
	public void handleNode(HandlerContext context) throws ParserException {
		handler.handleNode(context);
	}

	public HandlerConfiguration getConfiguration() {
		return configuration;
	}
	
}
