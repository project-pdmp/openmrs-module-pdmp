//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.15 at 08:39:59 AM EDT 
//


package org.ncpdp.schema.script;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObservationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Measurement" type="{http://www.ncpdp.org/schema/SCRIPT}MeasurementType" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="ObservationNotes" type="{http://www.ncpdp.org/schema/SCRIPT}an..140" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObservationType", propOrder = {
    "measurement",
    "observationNotes"
})
public class ObservationType {

    @XmlElement(name = "Measurement")
    protected List<MeasurementType> measurement;
    @XmlElement(name = "ObservationNotes")
    protected String observationNotes;

    /**
     * Gets the value of the measurement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the measurement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasurement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MeasurementType }
     * 
     * 
     */
    public List<MeasurementType> getMeasurement() {
        if (measurement == null) {
            measurement = new ArrayList<MeasurementType>();
        }
        return this.measurement;
    }

    /**
     * Gets the value of the observationNotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservationNotes() {
        return observationNotes;
    }

    /**
     * Sets the value of the observationNotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservationNotes(String value) {
        this.observationNotes = value;
    }

}
