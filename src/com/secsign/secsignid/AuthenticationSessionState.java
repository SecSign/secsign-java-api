package com.secsign.secsignid;

/*
* Class to gather all information about the state of a so called authentication session.
* It contains the session state and if the user was successfully authenticated a SecSign ID.
*
* @version 1.0
* @author SecSign Technologies Inc.
*/
public class AuthenticationSessionState
{
    //
    //
    // states of an authentication session
    //
    //

    /**
     * No State: Used when the session state is undefined.
     */
    public static final int NOSTATE = 0;

    /**
     * Pending: The session is still pending for authentication.
     */
    public static final int PENDING = 1;

    /**
     * Expired: The authentication timeout has been exceeded.
     */
    public static final int EXPIRED = 2;

    /**
     * Accepted: The user was successfully authenticated and accepted the session.
     */
    public static final int AUTHENTICATED = 3;

    /**
     * Denied: The user has denied the session.
     */
    public static final int DENIED = 4;

    /**
     * Suspended: The server suspended this session, because another authentication request was received while this session was still pending.
     */
    public static final int SUSPENDED = 5;

    /**
     * Canceled: The service has canceled this session.
     */
    public static final int CANCELED = 6;

    /**
     * Fetched: The device has already fetched the session, but the session hasn't been authenticated or denied yet.
     */
    public static final int FETCHED = 7;

    /**
     * Invalid: This session has become invalid.
     */
    public static final int INVALID = 8;
    
    /**
     * the state of the session
     */
    private int authSessionState;
    
    /**
     * the authenticated secsign id (is only set if authSessionState is AUTHENTICATED).
     */
    private String authenticatedSecSignId;

    /**
     * Gets the State of the Authentication Session (see constants)
     * 
     * @return the authSessionState
     */
    public int getAuthSessionState() {
        return authSessionState;
    }

    /**
     * @param authSessionState the authSessionState to set
     */
    public void setAuthSessionState(int authSessionState) {
        this.authSessionState = authSessionState;
    }

    /**
     * The SecSignID after the use has successfully authenticated himself.
     * (this can different to the String that was entered by the user because he might have entered a priority code) 
     * @return the authenticatedSecSignId
     */
    public String getAuthenticatedSecSignId() {
        return authenticatedSecSignId;
    }

    /**
     * @param authenticatedSecSignId the authenticatedSecSignId to set
     */
    public void setAuthenticatedSecSignId(String authenticatedSecSignId) {
        this.authenticatedSecSignId = authenticatedSecSignId;
    }
}
