package forecast.util;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.hl7.fhir.CodeableConcept;
import org.hl7.fhir.ImmunizationRecommendationDateCriterion;
import org.hl7.fhir.ImmunizationRecommendationRecommendation;
import org.hl7.fhir.Patient;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.ConnectFactory;
import org.tch.fc.ConnectorInterface;
import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.Event;
import org.tch.fc.model.EventType;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;

public class ForecastUtilTest {

	private static Logger log = LoggerFactory.getLogger(ForecastUtilTest.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String serviceUrl = "http://tchforecasttester.org/fv/forecast";

	// @Test
	public void testConvertStringForecastActualTestCase() {
		fail("Not yet implemented");
	}

	// @Test
	public void testConvertDateString() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvert() {
		Software software = new Software();
		software.setServiceUrl(serviceUrl);
		Service service = Service.getService("TCH");
		software.setService(service);
		TestCase testCase = createTestCase();
		ConnectorInterface connector;
		try {
			connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
			java.util.List<ForecastActual> forecastActualList = connector.queryForForecast(testCase,
					new SoftwareResult());
			ForecastActual forecastActual = forecastActualList.get(0);
			log.trace(ForecastUtil.forecastToString(forecastActual));
			log.trace(ForecastUtil.testEventsToString(testCase.getTestEventList()));
                        
                        System.out.println(ForecastUtil.forecastToString(forecastActual));
                        
//			ForecastPatient patient = (ForecastPatient) ForecastUtil.createForecastPatient(testCase);
//			Reference patientRef = ForecastUtil.createReference(patient.getIdentifier().get(0));
//			ForecastImmunizationRecommendation recommendation = ForecastUtil.createForecastImmunizationRecommendation(forecastActual, patientRef, testCase.getTestEventList());
//			assertNotNull(recommendation);
//			assertNotNull(recommendation.getIdentifier());
//			assertEquals(1, recommendation.getIdentifier().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// @Test
	public void testCreateMeta() {
		fail("Not yet implemented");
	}

	// @Test
	public void testConvertGender() {
		fail("Not yet implemented");
	}

	public static 	TestCase createTestCase() {
		TestCase testCase = new TestCase();
		try {
			testCase.setEvalDate(sdf.parse("2011-03-11"));
			testCase.setPatientSex("M");
			testCase.setPatientDob(sdf.parse("2012-01-01"));
			List<TestEvent> events = new ArrayList<TestEvent>();
                        TestEvent testEvent1 = new TestEvent(1, sdf.parse("2011-01-02"));
                        Event event1 = new Event();
                        event1.setEventType(EventType.VACCINATION);
                        event1.setVaccineCvx("115");
                        testEvent1.setEvent(event1);
                        events.add(testEvent1);
 
                        TestEvent testEvent2 = new TestEvent(2,sdf.parse("2011-02-02"));
                        Event event2 = new Event();
                        event2.setEventType(EventType.VACCINATION);
                        event2.setVaccineCvx("110");
                        testEvent2.setEvent(event2);
                        events.add(testEvent2);
                        
                        
//			events.add(new TestEvent(200, sdf.parse("2011-01-02")));
			//events.add(new TestEvent(300, sdf.parse("2011-01-03")));
			testCase.setTestEventList(events);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return testCase;
	}

        public static void main(String[] args) {
            
            Software software = new Software();
		software.setServiceUrl(serviceUrl);
		Service service = Service.getService("TCH");
		software.setService(service);
		TestCase testCase = createTestCase();
		ConnectorInterface connector;
		try {
			connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
			java.util.List<ForecastActual> forecastActualList = connector.queryForForecast(testCase,
					new SoftwareResult());
                        System.out.println("SIZE == " + forecastActualList.size());
			ForecastActual forecastActual = forecastActualList.get(0);
               
			log.trace(ForecastUtil.forecastToString(forecastActual));
//			log.trace(ForecastUtil.testEventsToString(testCase.getTestEventList()));
                        
                   //     System.out.println(ForecastUtil.forecastToString(forecastActual));
                        

                        
                        ImmunizationRecommendationRecommendation irr = ForecastUtil.createImmunizationRecommendationRecommendation(forecastActual);
            
                        EList<ImmunizationRecommendationDateCriterion> list = irr.getDateCriterion();
                        Iterator<ImmunizationRecommendationDateCriterion> it = list.iterator();
                        while(it.hasNext()) {
                            ImmunizationRecommendationDateCriterion irdc = it.next();
                            
                            System.out.println(irdc.getCode().getCoding().get(0).getCode().getValue() + " " + irdc.getValue().getValue().toString());
                        System.out.println(forecastActual.getVaccineGroup().getVaccineCvx());
                            
                        }
                        for(int i = 0; i < forecastActualList.size();i++) {
                            System.out.println(forecastActualList.get(i).getVaccineGroup().getVaccineCvx());
                            
                        }
                        
                        CodeableConcept cc = irr.getVaccineCode();
                        System.out.println(cc.getText().getValue() + "HERE!!!!!!!!!"); 
                 //       System.out.println(cc.getText().getValue());
                        
//			ForecastPatient patient = (ForecastPatient) ForecastUtil.createForecastPatient(testCase);
//			Reference patientRef = ForecastUtil.createReference(patient.getIdentifier().get(0));
//			ForecastImmunizationRecommendation recommendation = ForecastUtil.createForecastImmunizationRecommendation(forecastActual, patientRef, testCase.getTestEventList());
//			assertNotNull(recommendation);
//			assertNotNull(recommendation.getIdentifier());
//			assertEquals(1, recommendation.getIdentifier().size());


                        System.out.println(testCase.getTestEventList().get(0).getEvaluationActualList().get(0));
                        
                        System.out.println("test event size = " + testCase.getTestEventList().size());
            
            System.out.println("size of first test event actual list = " + testCase.getTestEventList().get(0).getEvaluationActualList().size());
                    
		} catch (Exception e) {
			e.printStackTrace();
		}
            
        }
        
        
}
