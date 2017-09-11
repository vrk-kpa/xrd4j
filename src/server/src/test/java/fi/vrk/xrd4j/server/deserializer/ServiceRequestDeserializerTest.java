/**
 * The MIT License
 * Copyright © 2017 Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xrd4j.server.deserializer;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import fi.vrk.xrd4j.common.exception.XRd4JException;
import fi.vrk.xrd4j.common.member.ObjectType;
import fi.vrk.xrd4j.common.message.ServiceRequest;
import fi.vrk.xrd4j.common.util.ESOAPHelper;
import junit.framework.TestCase;

/**
 * Test cases for ServiceRequestDeserializer class.
 *
 * @author Petteri Kivimäki
 */
public class ServiceRequestDeserializerTest extends TestCase {

  private static final ESOAPHelper SOAP_HELPER = ESOAPHelper.INSTANCE;

    /**
     * Request from subsystem to service with service version included.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test1() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("4.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from member to service with service version included.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test2() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"MEMBER\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER1</id:memberCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.1</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("COM", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals(null, request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.MEMBER, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("4.1", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to central service.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test3() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"CENTRALSERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:serviceCode>getRandom</id:serviceCode></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>6.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals(null, request.getProducer().getMemberClass());
        assertEquals(null, request.getProducer().getMemberCode());
        assertEquals(null, request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals(null, request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("6.0", request.getProtocolVersion());
        assertEquals(ObjectType.CENTRALSERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from member to central service.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test4() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"MEMBER\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode></xrd:client><xrd:service id:objectType=\"CENTRALSERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:serviceCode>getRandom</id:serviceCode></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>7.5</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals(null, request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.MEMBER, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals(null, request.getProducer().getMemberClass());
        assertEquals(null, request.getProducer().getMemberCode());
        assertEquals(null, request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals(null, request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("7.5", request.getProtocolVersion());
        assertEquals(ObjectType.CENTRALSERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service without subsystem and service version.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test5() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:serviceCode>getRandom</id:serviceCode></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>10.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals(null, request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals(null, request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("10.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service without subsystem.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test6() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>11.5</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals(null, request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("11.5", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service without service version.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test7() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals(null, request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("4.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service with issue included.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test8() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.0</xrd:protocolVersion><xrd:issue>issue</xrd:issue></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("issue", request.getIssue());
        assertEquals("4.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service, only mandatory elements.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test9() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("ID11234", request.getId());
        assertEquals(null, request.getUserId());
        assertEquals(null, request.getIssue());
        assertEquals("4.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service with service version included.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void test10() throws XRd4JException, SOAPException {
        String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service><xrd:securityServer><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:serverCode>server1</id:serverCode></xrd:securityServer><xrd:userId>EE1234567890</xrd:userId><xrd:id>ID11234</xrd:id><xrd:protocolVersion>4.0</xrd:protocolVersion></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://producer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);

        ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
        ServiceRequest<String> request = deserializer.deserialize(msg);

        assertEquals("FI", request.getConsumer().getXRoadInstance());
        assertEquals("GOV", request.getConsumer().getMemberClass());
        assertEquals("MEMBER1", request.getConsumer().getMemberCode());
        assertEquals("subsystem", request.getConsumer().getSubsystemCode());
        assertEquals(ObjectType.SUBSYSTEM, request.getConsumer().getObjectType());

        assertEquals("FI", request.getProducer().getXRoadInstance());
        assertEquals("COM", request.getProducer().getMemberClass());
        assertEquals("MEMBER2", request.getProducer().getMemberCode());
        assertEquals("subsystem", request.getProducer().getSubsystemCode());
        assertEquals("getRandom", request.getProducer().getServiceCode());
        assertEquals("v1", request.getProducer().getServiceVersion());
        assertEquals("FI", request.getSecurityServer().getXRoadInstance());
        assertEquals("GOV", request.getSecurityServer().getMemberClass());
        assertEquals("MEMBER1", request.getSecurityServer().getMemberCode());
        assertEquals("server1", request.getSecurityServer().getServerCode());
        assertEquals("ID11234", request.getId());
        assertEquals("EE1234567890", request.getUserId());
        assertEquals("4.0", request.getProtocolVersion());
        assertEquals(ObjectType.SERVICE, request.getProducer().getObjectType());
        assertEquals(true, request.getSoapMessage() != null);
    }

    /**
     * Request from subsystem to service, withoud id.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void testException1() throws SOAPException, XRd4JException {
        ServiceRequest<String> request = null;
        try {
            String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://consumer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
      SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);
            ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
            request = deserializer.deserialize(msg);
            fail("Should not reach this");
        } catch (XRd4JException ex) {
            // OK
        }
    }

    /**
     * Request from subsystem to service, wrong "
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void testException2() throws SOAPException, XRd4JException {
        ServiceRequest<String> request = null;
        try {
            String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI-FAIL</id:xRoadInstance><id:memberClass>GOV</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://consumer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
      SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);
            ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
            request = deserializer.deserialize(msg);
            fail("Should not reach this");
        } catch (XRd4JException ex) {
            // OK
        }
    }

    /**
     * Request from subsystem to service, wrong MemberClass.
     *
     * @throws XRd4JException
     * @throws SOAPException
     */
    public void testException3() throws SOAPException, XRd4JException {
        ServiceRequest<String> request = null;
        try {
            String soapString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:id=\"http://x-road.eu/xsd/identifiers\" xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"><SOAP-ENV:Header><xrd:client id:objectType=\"SUBSYSTEM\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>TEST</id:memberClass><id:memberCode>MEMBER1</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode></xrd:client><xrd:service id:objectType=\"SERVICE\"><id:xRoadInstance>FI</id:xRoadInstance><id:memberClass>COM</id:memberClass><id:memberCode>MEMBER2</id:memberCode><id:subsystemCode>subsystem</id:subsystemCode><id:serviceCode>getRandom</id:serviceCode><id:serviceVersion>v1</id:serviceVersion></xrd:service></SOAP-ENV:Header><SOAP-ENV:Body><ns1:getRandom xmlns:ns1=\"http://consumer.x-road.ee\"><request><data>1234567890</data></request></ns1:getRandom></SOAP-ENV:Body></SOAP-ENV:Envelope>";
      SOAPMessage msg = SOAP_HELPER.toSOAP(soapString);
            ServiceRequestDeserializer deserializer = new ServiceRequestDeserializerImpl();
            request = deserializer.deserialize(msg);
            fail("Should not reach this");
        } catch (XRd4JException ex) {
            // OK
        }
    }
}
