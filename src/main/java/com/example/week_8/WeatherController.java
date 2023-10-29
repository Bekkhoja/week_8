package com.example.week_8;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.net.URLConnection;


import org.json.JSONObject;


public class WeatherController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Button;

    @FXML
    private TextField cityInput;

    @FXML
    private Label feelsLikeLabel;

    @FXML
    private Label maxTemperatureLabel;

    @FXML
    private Label minTemperatureLabel;

    @FXML
    private Label pressureLabel;

    @FXML
    private Label temperatureLabel;


    @FXML
    void initialize() {
        Button.setOnAction(event -> {
            String city = cityInput.getText();
            String urlAddressXML = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&mode=xml&appid=fc1117fb68a1aac8ed3fc1e76d0d5851";

            StringBuffer content = content(urlAddressXML);
            System.out.println(content);

            JSONObject jsonObjectXML = AdapterXML(content);
            System.out.println(jsonObjectXML);

            String urlAddressJSON = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=fc1117fb68a1aac8ed3fc1e76d0d5851";
            StringBuffer contentJSON = content(urlAddressJSON);
            JSONObject JSONObject = new JSONObject(contentJSON.toString());
            System.out.println(contentJSON);
            System.out.println("Temperature : " + JSONObject.getJSONObject("main").getDouble("temp"));
            System.out.println("Feels like : " + JSONObject.getJSONObject("main").getDouble("feels_like"));
            System.out.println("Min temperature : " + JSONObject.getJSONObject("main").getDouble("temp_min"));
            System.out.println("Max temperature : " + JSONObject.getJSONObject("main").getDouble("temp_max"));

            if(jsonObjectXML != null) {
                GeneralValue(jsonObjectXML, JSONObject);
            }
        });
    }
    private void GeneralValue(JSONObject jsonObjectXML,JSONObject JSONObject){
        feelsLikeLabel.setText(Math.round(((JSONObject.getJSONObject("main").getDouble("feels_like") +
                jsonObjectXML.getJSONObject("main").getDouble("feels_like")) / 2) - 273.15) + "°C");
        maxTemperatureLabel.setText(Math.round(((JSONObject.getJSONObject("main").getDouble("temp_max") +
                jsonObjectXML.getJSONObject("main").getDouble("temp_max")) / 2) - 273.15) + "°C");
        minTemperatureLabel.setText(Math.round(((JSONObject.getJSONObject("main").getDouble("temp_min") +
                jsonObjectXML.getJSONObject("main").getDouble("temp_min")) / 2) - 273.15) + "°C");
        pressureLabel.setText(Math.round((JSONObject.getJSONObject("main").getDouble("pressure") +
                jsonObjectXML.getJSONObject("main").getDouble("pressure")) / 2) + " Pa");
        temperatureLabel.setText(Math.round(((JSONObject.getJSONObject("main").getDouble("temp") +
                jsonObjectXML.getJSONObject("main").getDouble("temp")) / 2) - 273.15) + "°C");
    }

    private StringBuffer content(String urlAddress) {
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }
    private JSONObject AdapterXML(StringBuffer content) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(content.toString())));

            Element currentElement = doc.getDocumentElement();

            JSONObject mainObject = new JSONObject();
            JSONObject mainDataObject = new JSONObject();

            NodeList temperatureNodeList = currentElement.getElementsByTagName("temperature");
            if (temperatureNodeList.getLength() > 0) {
                Element temperatureElement = (Element) temperatureNodeList.item(0);
                double tempValue = Double.parseDouble(temperatureElement.getAttribute("value"));
                double tempMin = Double.parseDouble(temperatureElement.getAttribute("min"));
                double tempMax = Double.parseDouble(temperatureElement.getAttribute("max"));
                mainDataObject.put("temp", tempValue);
                mainDataObject.put("temp_min", tempMin);
                mainDataObject.put("temp_max", tempMax);
            }

            NodeList feelsLikeNodeList = currentElement.getElementsByTagName("feels_like");
            if (feelsLikeNodeList.getLength() > 0) {
                Element feelsLikeElement = (Element) feelsLikeNodeList.item(0);
                double feelsLikeValue = Double.parseDouble(feelsLikeElement.getAttribute("value"));
                mainDataObject.put("feels_like", feelsLikeValue);
            }

            NodeList humidityNodeList = currentElement.getElementsByTagName("humidity");
            if (humidityNodeList.getLength() > 0) {
                Element humidityElement = (Element) humidityNodeList.item(0);
                int humidityValue = Integer.parseInt(humidityElement.getAttribute("value"));
                mainDataObject.put("humidity", humidityValue);
            }

            NodeList pressureNodeList = currentElement.getElementsByTagName("pressure");
            if (pressureNodeList.getLength() > 0) {
                Element pressureElement = (Element) pressureNodeList.item(0);
                int pressureValue = Integer.parseInt(pressureElement.getAttribute("value"));
                mainDataObject.put("pressure", pressureValue);
            }

            mainObject.put("main", mainDataObject);
            return mainObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}



















