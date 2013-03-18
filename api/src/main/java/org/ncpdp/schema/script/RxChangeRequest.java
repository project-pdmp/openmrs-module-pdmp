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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Request" type="{http://www.ncpdp.org/schema/SCRIPT}RequestType" minOccurs="0"/>
 *         &lt;element name="Pharmacy" type="{http://www.ncpdp.org/schema/SCRIPT}MandatoryPharmacyType"/>
 *         &lt;element name="Prescriber" type="{http://www.ncpdp.org/schema/SCRIPT}PrescriberType"/>
 *         &lt;element name="Supervisor" type="{http://www.ncpdp.org/schema/SCRIPT}SupervisorType" minOccurs="0"/>
 *         &lt;element name="Facility" type="{http://www.ncpdp.org/schema/SCRIPT}MandatoryFaciltyType" minOccurs="0"/>
 *         &lt;element name="Patient" type="{http://www.ncpdp.org/schema/SCRIPT}PatientType"/>
 *         &lt;element name="MedicationPrescribed" type="{http://www.ncpdp.org/schema/SCRIPT}RxChangePrescribedMedicationType"/>
 *         &lt;element name="MedicationRequested" type="{http://www.ncpdp.org/schema/SCRIPT}RxChangeDispensedMedicationType" maxOccurs="9" minOccurs="0"/>
 *         &lt;element ref="{http://www.ncpdp.org/schema/SCRIPT}Observation" minOccurs="0"/>
 *         &lt;element name="BenefitsCoordination" type="{http://www.ncpdp.org/schema/SCRIPT}BenefitsCoordinationType" maxOccurs="3" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "request",
    "pharmacy",
    "prescriber",
    "supervisor",
    "facility",
    "patient",
    "medicationPrescribed",
    "medicationRequested",
    "observation",
    "benefitsCoordination"
})
@XmlRootElement(name = "RxChangeRequest")
public class RxChangeRequest {

    @XmlElement(name = "Request")
    protected RequestType request;
    @XmlElement(name = "Pharmacy", required = true)
    protected MandatoryPharmacyType pharmacy;
    @XmlElement(name = "Prescriber", required = true)
    protected PrescriberType prescriber;
    @XmlElement(name = "Supervisor")
    protected SupervisorType supervisor;
    @XmlElement(name = "Facility")
    protected MandatoryFaciltyType facility;
    @XmlElement(name = "Patient", required = true)
    protected PatientType patient;
    @XmlElement(name = "MedicationPrescribed", required = true)
    protected RxChangePrescribedMedicationType medicationPrescribed;
    @XmlElement(name = "MedicationRequested")
    protected List<RxChangeDispensedMedicationType> medicationRequested;
    @XmlElement(name = "Observation")
    protected ObservationType observation;
    @XmlElement(name = "BenefitsCoordination")
    protected List<BenefitsCoordinationType> benefitsCoordination;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType }
     *     
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType }
     *     
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the pharmacy property.
     * 
     * @return
     *     possible object is
     *     {@link MandatoryPharmacyType }
     *     
     */
    public MandatoryPharmacyType getPharmacy() {
        return pharmacy;
    }

    /**
     * Sets the value of the pharmacy property.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatoryPharmacyType }
     *     
     */
    public void setPharmacy(MandatoryPharmacyType value) {
        this.pharmacy = value;
    }

    /**
     * Gets the value of the prescriber property.
     * 
     * @return
     *     possible object is
     *     {@link PrescriberType }
     *     
     */
    public PrescriberType getPrescriber() {
        return prescriber;
    }

    /**
     * Sets the value of the prescriber property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrescriberType }
     *     
     */
    public void setPrescriber(PrescriberType value) {
        this.prescriber = value;
    }

    /**
     * Gets the value of the supervisor property.
     * 
     * @return
     *     possible object is
     *     {@link SupervisorType }
     *     
     */
    public SupervisorType getSupervisor() {
        return supervisor;
    }

    /**
     * Sets the value of the supervisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupervisorType }
     *     
     */
    public void setSupervisor(SupervisorType value) {
        this.supervisor = value;
    }

    /**
     * Gets the value of the facility property.
     * 
     * @return
     *     possible object is
     *     {@link MandatoryFaciltyType }
     *     
     */
    public MandatoryFaciltyType getFacility() {
        return facility;
    }

    /**
     * Sets the value of the facility property.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatoryFaciltyType }
     *     
     */
    public void setFacility(MandatoryFaciltyType value) {
        this.facility = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link PatientType }
     *     
     */
    public PatientType getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientType }
     *     
     */
    public void setPatient(PatientType value) {
        this.patient = value;
    }

    /**
     * Gets the value of the medicationPrescribed property.
     * 
     * @return
     *     possible object is
     *     {@link RxChangePrescribedMedicationType }
     *     
     */
    public RxChangePrescribedMedicationType getMedicationPrescribed() {
        return medicationPrescribed;
    }

    /**
     * Sets the value of the medicationPrescribed property.
     * 
     * @param value
     *     allowed object is
     *     {@link RxChangePrescribedMedicationType }
     *     
     */
    public void setMedicationPrescribed(RxChangePrescribedMedicationType value) {
        this.medicationPrescribed = value;
    }

    /**
     * Gets the value of the medicationRequested property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the medicationRequested property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMedicationRequested().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RxChangeDispensedMedicationType }
     * 
     * 
     */
    public List<RxChangeDispensedMedicationType> getMedicationRequested() {
        if (medicationRequested == null) {
            medicationRequested = new ArrayList<RxChangeDispensedMedicationType>();
        }
        return this.medicationRequested;
    }

    /**
     * Gets the value of the observation property.
     * 
     * @return
     *     possible object is
     *     {@link ObservationType }
     *     
     */
    public ObservationType getObservation() {
        return observation;
    }

    /**
     * Sets the value of the observation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObservationType }
     *     
     */
    public void setObservation(ObservationType value) {
        this.observation = value;
    }

    /**
     * Gets the value of the benefitsCoordination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the benefitsCoordination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBenefitsCoordination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BenefitsCoordinationType }
     * 
     * 
     */
    public List<BenefitsCoordinationType> getBenefitsCoordination() {
        if (benefitsCoordination == null) {
            benefitsCoordination = new ArrayList<BenefitsCoordinationType>();
        }
        return this.benefitsCoordination;
    }

}
