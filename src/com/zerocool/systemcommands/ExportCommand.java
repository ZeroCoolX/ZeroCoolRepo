package com.zerocool.systemcommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zerocool.controllers.AutoDetect;
import com.zerocool.controllers.SystemController;
import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class ExportCommand implements Command {
	
	private SystemController controller;
	
	public ExportCommand(SystemController controller) {
		this.controller = controller;
	}
	

	/**
	 * Copies the data from the event log and writes the data to an external device (USB) if there is one connected.
	 * 
	 * @throws FileNotFoundException 
	 **/
	@Override
	public void execute(String... args) {

		AbstractEvent exportEvent = controller.getTimer().getCurrentEvent();

		if (exportEvent == null) {
			System.err.println("ERROR: No event!");
			return;
		}
			
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("logged_data");
			doc.appendChild(rootElement);

			Element eventData = doc.createElement("event_data");
			rootElement.appendChild(eventData);	


			Element timestamp = doc.createElement("timestamp");
			timestamp.appendChild(doc.createTextNode(""+controller.getSystemTime()));
			eventData.appendChild(timestamp);

			Element eventName = doc.createElement("event_name");
			eventName.appendChild(doc.createTextNode(""+exportEvent.getEventName()));
			eventData.appendChild(eventName);	

			Element eventId = doc.createElement("event_ID");
			eventId.appendChild(doc.createTextNode(""+exportEvent.getEventId()));
			eventData.appendChild(eventId);

			Element eventType = doc.createElement("event_type");
			eventType.appendChild(doc.createTextNode(""+exportEvent.getType()));
			eventData.appendChild(eventType);

			Element eventTime = doc.createElement("event_time");
			eventTime.appendChild(doc.createTextNode(""+exportEvent.getFormattedEventTime()));
			eventData.appendChild(eventTime);

			//END event_data root child element


			Element parRun = doc.createElement("participant_data");
			rootElement.appendChild(parRun);

			int i = 0;
			ArrayList<Element> elements = new ArrayList<Element>();
			for (Participant p: controller.getTimer().getCurrentEvent().getCurrentParticipants()) {
				int k = i;

				elements.add(doc.createElement("participant_event_ID_"+k));
				elements.get(i).appendChild(doc.createTextNode(""+controller.getTimer().getCurrentEvent().getEventId()));
				parRun.appendChild(elements.get(i));

				++i;

				elements.add(doc.createElement("participant_BIB_"+k));
				elements.get(i).appendChild(doc.createTextNode(""+p.getId()));
				parRun.appendChild(elements.get(i));

				++i;

				elements.add(doc.createElement("participant_time_"+k));
				elements.get(i).appendChild(doc.createTextNode(""+SystemTime.formatTime(p.getLastRecord().getElapsedTime())));
				parRun.appendChild(elements.get(i));

				++i;

			}

			//END participant_run root child element

			//END participant root element

			AutoDetect detector = controller.getAutoDetect();
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			String saveFilePath = "" + (detector.driveConnected() ? detector.getDrive() + "/" : "") + exportEvent.getEventName() + exportEvent.getEventId() + ".xml";
			if (detector.driveConnected()) {
				System.out.println("Saving file to external device: " + saveFilePath);
			} else {
				//there isn't a usb drive to export to...throw error and gtfo.
				System.err.println("NO USB DRIVE DETECTED\nSaving file locally to: " + saveFilePath);
			}
			StreamResult result = new StreamResult(new File(saveFilePath));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}	
	}
}
