package fhir.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Serialize extends AbstractSerializeDeserialize {

    private final Logger log = LoggerFactory.getLogger(Serialize.class);

    public Serialize() {
        super();
    }

    public String it(EObject eObject, String uriString) {

        resource = resourceSet.createResource(URI.createURI(uriString));        
        resource.getContents().add(eObject);
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

  //      try {
            HashMap map = new HashMap();
            //map.put(XMLResource.OPTION_RECORD_ANY_TYPE_NAMESPACE_DECLARATIONS, true);                                    
            //resource.save(stream, Collections.EMPTY_MAP);
            //resource.save(stream, map);
            Map options = new HashMap();
            options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
            Document doc=((XMLResource)resource).save(null, options, null);
           // doc.getDocumentElement().setAttributeNS("http://hl7.org/fhir", "name", "value");
   
            String xml = xmlToString(doc);
            
            int namespace = xml.indexOf("xmlns:fhir=\"http://hl7.org/fhir\"");
            CharSequence orig = "xmlns:fhir=\"http://hl7.org/fhir\"";
            CharSequence replace = "xmlns:fhir=\"http://hl7.org/fhir\"" + " xmlns=\"http://hl7.org/fhir\"";
            xml = xml.replace(orig, replace);
            
            // TODO: For some reason the element names do not match the schema.
            // This workaround is here until the model is fixed.
            
            CharSequence imBad = "immunizationRecommendation";
            CharSequence imGood = "ImmunizationRecommendation";
            
            CharSequence iBad = "immunization";
            CharSequence iGood = "Immunization";
            
            CharSequence patBad = "patient";
            CharSequence patGood = "Patient";
            
            xml = xml.replace(imBad, imGood);
            xml = xml.replace(iBad, iGood);
            xml = xml.replace(patBad, patGood);
            
            
            return xml;
  //      } catch (IOException e) {
  //          e.printStackTrace();
  //      }
    //    return stream.toString();
    }

    public static String xmlToString(Node inputNode) {
        try {
            Source source = new DOMSource(inputNode);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document createDomDocument() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        return doc;
    }

    public String xmlFromParameter(Parameters input) throws ParserConfigurationException {
        
        Document doc = createDomDocument();
        Element root = doc.createElementNS("http://hl7.org/fhir","Parameters");
        doc.appendChild(root);
        EList<ParametersParameter> parametersList = input.getParameter();
        Iterator<ParametersParameter> it = parametersList.iterator();
        while(it.hasNext()) {
            ParametersParameter parameterOrig = it.next();
            Element parameter = doc.createElementNS("http://hl7.org/fhir", "parameter");
            Element parameterName = doc.createElementNS("http://hl7.org/fhir", "name");
            //TODO: Error check
            parameterName.setNodeValue(parameterOrig.getName().getValue());
            parameter.appendChild(parameterName);
            
            
            
        }
        
        
        
        return xmlToString(doc);
    }
    
    
    public OutputStream it(EObject eObject, String uriString, OutputStream stream) {

        try {
            resource = resourceSet.createResource(URI.createURI(uriString));
            resource.getContents().add(eObject);
            resource.save(stream, Collections.EMPTY_MAP);
        } catch (IOException e) {
            log.error("", e);
        } catch (NullPointerException e) {
            log.error(String.format("uriString=%s", uriString));
            log.error(String.format("is not null: resource=%b, uriString=%b, eObject=%b", resource, uriString, eObject), e);
        }
        return stream;
    }
}
