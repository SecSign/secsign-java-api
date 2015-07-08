
package com.secsign.secsignid;

import java.util.logging.Level;
import java.util.logging.Logger;

import seccommerce.mobile.bo.ReqCancelAuthSession;
import seccommerce.mobile.bo.ReqGetAuthSessionState;
import seccommerce.mobile.bo.ReqReleaseAuthSession;
import seccommerce.mobile.bo.ReqRequestAuthSession;
import seccommerce.mobile.bo.ResGetAuthSessionState;
import seccommerce.mobile.bo.ResRequestAuthSession;
import seccommerce.secpki.api.SecPKIApi;
import seccommerce.secpki.api.SecPKIApiException;

/**
 * The class for the SecSign ID Java Api. The class holds information about the ID-Server and takes care about the communication with the ID Server.
 * 
 * The class holds four methods to request a so called authentication session, to check the state of the session and to cancel and release the session.
 * 
 * @version 1.0
 * @author SecSign Technology Inc.
 */
public final class SecSignIDApi {

    // a fix string representing the api version. it should match the version number if class comment
    private static final String ApiVersion = "1.0";
    
    /**
     * Public logger. The LoggerWrapper class can be overwritten so several logging mechanisms could be used
     * e.g: Apache Log4J
     */
    public static Logger log;
    
    /**
     * the connection to the SecSignID server
     */
    private SecPKIApi secPkiApi = null;
    private AuthenticationSession _authSession = null;
    
    private String serviceType = "SecSignIDApi_Java";
    private String pluginName = "SecSignIDApi_Java";
    
    
    /**
     * Constructor of the SecSign ID Api bean
     * @throws SecSignIDException 
     */
    public SecSignIDApi() throws SecSignIDException {
        // create the SecPKI-API instance which can send requests to the SecSignID server
        try {
            secPkiApi = new SecPKIApi();
        } catch (Exception ex) {
            String errorMessage =  "Cannot instantiate secpkiapi for communicattion with the SecSign ID server: " + ex.getMessage(); 
            if(log != null){
                log.log(Level.SEVERE, errorMessage, ex);
            }
            
            throw new SecSignIDException(errorMessage, ex);
        }
    }
    
    /**
     * Requests an authentication session for given SecSign ID. The service info is displayed at the push notification on the smart phone.
     * @param secSignID
     * @param serviceInfo The description of this service. Will be displayed in the web site and in the push notification.
     * @param serviceAddress The address of this service. Will be displayed in the SecSignApp on the smart phone.
     * @param browserIpAddr the IP address of the browser (user) requesting a authentication session
     * @return the complete authentication session
     * @throws SecSignIDException 
     */
    public AuthenticationSession requestAuthenticationSession(String secSignID, String serviceInfo, String serviceAddress, String browserIpAddr) throws SecSignIDException
    {
        // build the request to get an authentication session for the SecSignID user
        ReqRequestAuthSession reqRequestAuthSession = new ReqRequestAuthSession();
        reqRequestAuthSession.setUserID(secSignID);
        reqRequestAuthSession.setServiceName(serviceInfo);
        reqRequestAuthSession.setServiceAddress(serviceAddress);
        reqRequestAuthSession.setServiceType(serviceType);
        reqRequestAuthSession.setPluginName(pluginName);
        reqRequestAuthSession.setBrowserIpAddr(browserIpAddr);
        
        // send the request to the SecSign ID server
        try
        {
            // end request and wait for answer.
            ResRequestAuthSession resRequestAuthSession = secPkiApi.requestAuthSession(reqRequestAuthSession);
            
            // create an instance of Authentication Session object
            AuthenticationSession authSession = new AuthenticationSession();
            authSession.setSecSignID(secSignID);
            authSession.setRequestingServiceName(serviceInfo);
            authSession.setRequestingServiceAddress(serviceAddress);
            
            authSession.setAuthSessionID(resRequestAuthSession.getAuthSessionID());
            authSession.setAuthSessionIconData(resRequestAuthSession.getPassIconData());
            
            if(log != null){
                log.log(Level.INFO, "Received the authentication session with ID '" + authSession.getAuthSessionID() + "' for SecSignID user ID '" + secSignID + "'.");
            }
            
            this._authSession = authSession;
            
            return authSession; // go to the page showing the access pass and update the displayed URL in the browser
        }
        catch(Exception ex)
        {
            int errorStatus = (ex instanceof SecPKIApiException) ? ((SecPKIApiException)ex).getStatus() : -1;
            String errorMessage =  "Error when requesting an authentication session for SecSignID user ID \"" + secSignID + "\": " + errorStatus + ": " + ex.getMessage();
            
            if(log != null){
                log.log(Level.SEVERE, errorMessage, ex);
            }
            
            throw new SecSignIDException(errorMessage, ex);
        }
    }
    
    /**
     * Gets the authentication session state for a certain secsign id whether the authentication session is still pending or it was accepted or denied.
     * @return the authentication session state.
     * @throws SecSignIDException if the state could not be queried.
     */
    public AuthenticationSessionState getAuthenticationSessionState() throws SecSignIDException 
    {
        if(_authSession == null){
            throw new SecSignIDException("Cannot check state of an authentication session. Session is null.");
        }
        
        // build the request to query the state of the authentication session at the SecSignID server
        ReqGetAuthSessionState reqGetAuthSessionState = new ReqGetAuthSessionState();
        reqGetAuthSessionState.setAuthSessionID(_authSession.getAuthSessionID());
        
        // send the request to the SecSignID server
        try
        {
            ResGetAuthSessionState resGetAuthSessionState = secPkiApi.getAuthSessionState(reqGetAuthSessionState);
            AuthenticationSessionState state = new AuthenticationSessionState();
            state.setAuthSessionState(resGetAuthSessionState.getAuthSessionState());
            state.setAuthenticatedSecSignId(resGetAuthSessionState.getSecSignIdUserName());
            return state;
        }
        catch(Exception ex)
        {
            int errorStatus = (ex instanceof SecPKIApiException) ? ((SecPKIApiException)ex).getStatus() : -1;
            String errorMessage =  "Error when querying the state of the authentication session with ID \"" + _authSession.getAuthSessionID() + 
                    "\" for SecSignID user ID \"" + _authSession.getSecSignID() + "\": " + errorStatus + ": " + ex.getMessage();
            
            if(log != null){
                log.log(Level.SEVERE, errorMessage, ex);
            }
            
            throw new SecSignIDException(errorMessage, ex);
        }
    }
    
    /**
     * Disposes the specified authentication session which is not needed any more.
     * A call of any method after this will throw an exception until a new authentication was requested
     * @throws SecSignIDException
     */
    public void releaseAuthenticationSession() throws SecSignIDException
    {
        if(_authSession == null){
            throw new SecSignIDException("Cannot nbot release authentication session. Session is null.");
        }
        
        // build the request to dispose a used session at the SecSignID server
        ReqReleaseAuthSession reqReleaseAuthSession = new ReqReleaseAuthSession();
        reqReleaseAuthSession.setAuthSessionID(_authSession.getAuthSessionID());
        
        // send the request to the SecSignID server
        try
        {
            secPkiApi.releaseAuthSession(reqReleaseAuthSession);
            if(log != null){
                log.log(Level.INFO, "Disposed the used authentication session with ID \"" + _authSession.getAuthSessionID() + "\".");
            }
            
            // everything was canceled. no need to keep authentication instance
            _authSession = null;
        }
        catch(Exception ex)
        {
            int errorStatus = (ex instanceof SecPKIApiException) ? ((SecPKIApiException)ex).getStatus() : -1;
            String errorMessage =  "Error when releasing the authentication session with ID \"" + _authSession.getAuthSessionID() + "\": " +
                    errorStatus + " : " + ex.getMessage();
            
            if(log != null){
                log.log(Level.SEVERE, errorMessage, ex);
            }
            
            throw new SecSignIDException(errorMessage, ex);
        }
    }
    
    /**
     * Cancels an authentication session at the SecSignID server.
     * A call of any method after this will throw an exception until a new authentication was requested
     * @throws SecSignIDException
     */
    public void cancelAuthenticationSession() throws SecSignIDException
    {
        if(_authSession == null){
            throw new SecSignIDException("Cannot not cancel authentication session. Session is null.");
        }
        
        // build the request to withdraw the authentication session
        ReqCancelAuthSession reqCancelAuthSession = new ReqCancelAuthSession();
        reqCancelAuthSession.setAuthSessionID(_authSession.getAuthSessionID());
        
        // send the request to the SecSignID server
        try
        {
            secPkiApi.cancelAuthSession(reqCancelAuthSession);
            if(log != null){
                log.log(Level.INFO, "Authentication session with ID \"" + _authSession.getAuthSessionID() + "\" for SecSignID user ID \"" + _authSession.getSecSignID() + "\" withdrawn.");
            }
            
            // everything was canceled. no need to keep authentication instance
            _authSession = null;
        }
        catch(Exception ex)
        {
            int errorStatus = (ex instanceof SecPKIApiException) ? ((SecPKIApiException)ex).getStatus() : -1;
            String errorMessage =  "Error when canceling the authentication session with ID \"" + _authSession.getAuthSessionID() + "\": " +
                    errorStatus + " : " + ex.getMessage();
            
            if(log != null){
                log.log(Level.SEVERE, errorMessage, ex);
            }
            
            throw new SecSignIDException(errorMessage, ex);
        }
    }
}
