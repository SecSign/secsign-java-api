package com.secsign.secsignid;

/*
* Class to gather all information about a so called authentication session.
* It contains the access pass, a request id, a session id and the secsign id.
*
* @version 1.0
* @author SecSign Technologies Inc.
*/
public final class AuthenticationSession {
    
    /* 
     * the secsign id the authentication session has been craeted for
     */
    private String secSignID = null;
    
    /*
     * authentication session id
     */
    private long authSessionID;

    /*
     * the name of the requesting service. this will be shown at the smartphone
     */
    private String requestingServiceName = null;
    
    /*
     * the address, a valid url, of the requesting service. this will be shown at the smartphone
     */
    private String requestingServiceAddress = null;
    
    /*
     * icon data of the so called access pass. the image data needs to be displayed otherwise the user does not know which access pass he needs to choose in order to accept the authentication session.
     * the data is png image data.
     */
    private byte[] authSessionIconData = null;
    
    
    /**
     * Constructor.
     */
    public AuthenticationSession() {
    
    }

    /**
     * @return the secSignId
     */
    public String getSecSignID() {
        return secSignID;
    }

    /**
     * @param secSignId the secSignId to set
     */
    public void setSecSignID(String secSignId) {
        this.secSignID = secSignId;
    }

    /**
     * @return the authSessionId
     */
    public long getAuthSessionID() {
        return authSessionID;
    }

    /**
     * @param authSessionId the authSessionId to set
     */
    public void setAuthSessionID(long authSessionId) {
        this.authSessionID = authSessionId;
    }

    /**
     * Gets the icon pass icon data. the data is png image data.
     * @return the authSessionIconData
     */
    public byte[] getAuthSessionIconData() {
        return authSessionIconData;
    }

    /**
     * Sets the icon pass icon data. the data is png image data.
     * @param authSessionIconData the authSessionIconData to set
     */
    public void setAuthSessionIconData(byte[] authSessionIconData) {
        this.authSessionIconData = authSessionIconData;
    }

    /**
     * Gets the the description of this service. Will be displayed in the website.
     * @return the requestingServiceName
     */
    public String getRequestingServiceName() {
        return requestingServiceName;
    }

    /**
     * Sets the the description of this service. Will be displayed in the website.
     * @param requestingServiceName the requestingServiceName to set
     */
    public void setRequestingServiceName(String requestingServiceName) {
        this.requestingServiceName = requestingServiceName;
    }

    /**
     * Gets the address of this service. Will be displayed in the SecSignApp on the smart phone.
     * @return the requestingServiceAddress
     */
    public String getRequestingServiceAddress() {
        return requestingServiceAddress;
    }

    /**
     * Sets the address of this service. Will be displayed in the SecSignApp on the smart phone.
     * @param requestingServiceAddress the requestingServiceAddress to set
     */
    public void setRequestingServiceAddress(String requestingServiceAddress) {
        this.requestingServiceAddress = requestingServiceAddress;
    }

}
