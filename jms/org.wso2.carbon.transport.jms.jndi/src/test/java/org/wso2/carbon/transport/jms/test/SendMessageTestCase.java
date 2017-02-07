/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.transport.jms.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.carbon.messaging.MessageProcessorException;
import org.wso2.carbon.transport.jms.jndi.sender.JMSSender;
import org.wso2.carbon.transport.jms.jndi.utils.JMSConstants;
import org.wso2.carbon.transport.jms.test.util.JMSServer;
import org.wso2.carbon.transport.jms.test.util.JMSTestConstants;

import javax.jms.JMSException;

/**
 * A test class for testing queue listening
 */
public class SendMessageTestCase {
    private JMSServer jmsServer;
    private CarbonMessage carbonMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageTestCase.class);

    @BeforeClass(groups = "queueSending", description = "Setting up the server and carbon message to be sent")
    public void setUp() {
        carbonMessage = new DefaultCarbonMessage();
        carbonMessage.setProperty(JMSConstants.DESTINATION_PARAM_NAME, JMSTestConstants.QUEUE_NAME_1);
        carbonMessage.setProperty(JMSConstants.CONNECTION_FACTORY_JNDI_PARAM_NAME,
                                  JMSTestConstants.QUEUE_CONNECTION_FACTORY);
        carbonMessage.setProperty(JMSConstants.NAMING_FACTORY_INITIAL_PARAM_NAME,
                                  JMSTestConstants.ACTIVEMQ_FACTORY_INITIAL);
        carbonMessage.setProperty(JMSConstants.PROVIDER_URL_PARAM_NAME,
                                  JMSTestConstants.ACTIVEMQ_PROVIDER_URL);
        carbonMessage.setProperty(JMSConstants.CONNECTION_FACTORY_TYPE_PARAM_NAME,
                                  JMSConstants.DESTINATION_TYPE_QUEUE);
        carbonMessage.setProperty(JMSConstants.TEXT_DATA, "Hello World");
        carbonMessage.setProperty(JMSConstants.JMS_MESSAGE_TYPE, JMSConstants.TEXT_MESSAGE_TYPE);
        jmsServer = new JMSServer();
        jmsServer.startServer();
    }

    @Test(groups = "queueSending", description =
            "Testing whether queue sending is working correctly without any " +
            "exceptions") public void queueListeningTestCase()
            throws InterruptedException, JMSException, MessageProcessorException {
        LOGGER.info("JMS Transport Sender is sending a message to the queue " +
                    JMSTestConstants.QUEUE_NAME_1);
        JMSSender sender = new JMSSender();
        jmsServer.receiveMessagesFromQueue();
        sender.send(carbonMessage, null);
    }
}
