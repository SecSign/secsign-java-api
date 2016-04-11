package com.secsign.secsignid.plugins.java.faces;

import seccommerce.mobile.bo.ReqCancelAuthSession;
import seccommerce.mobile.bo.ReqGetAuthSessionState;
import seccommerce.mobile.bo.ReqReleaseAuthSession;
import seccommerce.mobile.bo.ReqRequestAuthSession;
import seccommerce.mobile.bo.ResGetAuthSessionState;
import seccommerce.mobile.bo.ResRequestAuthSession;
import seccommerce.mobile.types.MobileAuthTypes;
import seccommerce.secpki.api.SecPKIApi;
import seccommerce.secpki.api.SecPKIApiException;
import sun.misc.BASE64Encoder;

/**
 * The SecSignID bean for the Java Server Faces example login page.
 * This bean holds the SecSignID user ID entered by the user, the session ID
 * sent by the SecSignID server and finally the authenticated SecSignID user ID.
 *
 * With Java 1.6 you can replace the following block in faces-config.xml:
 * <managed-bean>
 *     <managed-bean-name>secsignid</managed-bean-name>
 *     <managed-bean-class>com.secsign.secsignid.plugins.java.faces.SecSignIDBean</managed-bean-class>
 *     <managed-bean-scope>session</managed-bean-scope>
 * </managed-bean>
 * with these annotations:
 * @ManagedBean(name="secsignid")
 * @SessionScoped
 *
 * @version $Id: SecSignIDBean.java,v 1.2 2014/12/02 15:35:43 jwollner Exp $
 * @author SecSign Technology Inc.
 */
public class SecSignIDBean
{
    /**
     * the unchecked SecSignID user ID entered by the user
     */
    private String userId;
    
    /**
     * the authenticated SecSignID user ID
     */
    private String authenticatedUserId;
    
    /**
     * the sessionID for the user
     */
    private long sessionID;
    
    /**
     * the most recent error message
     */
    private String errMsg;
    
    /**
     * The description of this service. Will be displayed in the SecSignApp on the smart phone.
     */
    private String serviceInfo;
    
    /**
     * The address of this service.
     */
    private String serviceAddress;
    
    /**
     * The service type which is the pseudonym of the used interface or plugin
     */
    private String serviceType;
    
    /**
     * The plugin name is the name which uses this interface
     */
    private String pluginName;
    
    /**
     * The iconData of the access pass
     */
    private String iconData;
    
    /**
     * the connection to the SecSignID server
     */
    private SecPKIApi secPkiApi = null;
    
    /**
     * Constructor.
     */
    public SecSignIDBean() throws SecPKIApiException
    {
        // The description of this service. Will be displayed in the website.
        serviceInfo = "SecSignID Java Server Faces integration example";
        // The address of this service. Will be displayed in the SecSignApp on the smart phone.
        serviceAddress = "www.example.com";
        // the pseudonym of used plugin or interface
        serviceType = "SecSignIDApi_JavaFaces";
        // the plugin name
        pluginName = "SecSignID-JavaBean";
        
        // create the SecPKI-API instance which can send requests to the SecSignID server
        secPkiApi = new SecPKIApi();
    }
    
    /**
     * Sets the SecSignID userID. Will be called by the JSF server if the user enters the SecSignID in index.xhtml.
     * @param userId the SecSignID userID entered by the user
     */
    public void setUserid(String userId)
    {
        // The user name of the SecSignID user can only be set once. It
        // cannot be overwritten while the authentication is in progress.
        if (null == this.userId)
        {
            this.userId = userId;
        }
    }
    
    /**
     * Gets the SecSignID userID.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the SecSignID userID entered by the user
     */
    public String getUserid()
    {
        return userId;
    }
    
    /**
     * Gets the SecSignID access pass as base64 PNG data.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the access pass
     */
    public String getIconData()
    {
        return iconData;
    }
    
    /**
     * Gets the session ID generated by the SecSignID server.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the authentication session ID
     */
    public long getSessionID()
    {
        return sessionID;
    }
    
    /**
     * Gets the most recent error message.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the most recent error message
     */
    public String getErrmsg()
    {
        return errMsg;
    }
    
    /**
     * Gets the description of this service.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the description of this service
     */
    public String getServiceinfo()
    {
        return serviceInfo;
    }
    
    /**
     * Gets the authenticated SecSignID user ID.
     * Will be called by the JSF server to fill the HTML files generated from the XHTML files.
     * @return the authenticated SecSignID user ID
     */
    public String getauthenticateduserid()
    {
        return authenticatedUserId;
    }
    
    /**
     * Requests a session for the SecSignID user ID entered by the user.
     * Will be called by the JSF server if the user clicks "Login" in index.xhtml.
     * @return the name of the next page to display
     */
    public String requestsession()
    {
        // build the request to get an authentication session for the SecSignID user ID
        ReqRequestAuthSession reqRequestAuthSession = new ReqRequestAuthSession();
        reqRequestAuthSession.setUserID(userId);
        reqRequestAuthSession.setServiceName(serviceInfo);
        reqRequestAuthSession.setServiceAddress(serviceAddress);
        reqRequestAuthSession.setServiceType(serviceType);
        reqRequestAuthSession.setPluginName(pluginName);
        reqRequestAuthSession.setBrowserIpAddr("127.0.0.1");
        
        // send the request to the SecSignID server
        errMsg = null;
        try
        {
            ResRequestAuthSession resRequestAuthSession = secPkiApi.requestAuthSession(reqRequestAuthSession);
            sessionID = resRequestAuthSession.getAuthSessionID();
            iconData = new BASE64Encoder().encode(resRequestAuthSession.getPassIconData());
            System.out.println("Received the authentication session with ID \"" + sessionID + "\" for SecSignID user ID \"" + userId + "\".");
            
            return "accesspass?faces-redirect=true"; // go to the page showing the access pass and update the displayed URL in the browser
        }
        catch(SecPKIApiException secPkiApiEx)
        {
            errMsg = secPkiApiEx.getMessage();
            System.out.println("Error when requesting an authentication session for SecSignID user ID \"" + userId + "\": " +
                               secPkiApiEx.getStatus() + ": " + errMsg);
        }
        
        userId = null; // interrupt the login of this user
        return "index"; // stay on the login page if there was an error
    }
    
    /**
     * Cancels an authentication session at the SecSignID server.
     * Will be called by the JSF server if the user clicks "Cancel" in accesspass.xhtml.
     * @return the name of the next page to display
     */
    public String cancelsession()
    {
        // build the request to withdraw the authentication session
        ReqCancelAuthSession reqCancelAuthSession = new ReqCancelAuthSession();
        reqCancelAuthSession.setAuthSessionID(sessionID);
        
        // send the request to the SecSignID server
        errMsg = null;
        try
        {
            secPkiApi.cancelAuthSession(reqCancelAuthSession);
            System.out.println("Authentication session with ID \"" + sessionID + "\" for SecSignID user ID \"" + userId + "\" withdrawn.");
        }
        catch(SecPKIApiException secPkiApiEx)
        {
            errMsg = secPkiApiEx.getMessage();
            // see seccommerce.secpki.types.SecPKIStatus
            System.out.println("Error when withdrawing the authentication session with ID \"" + sessionID + "\" for SecSignID user ID \"" + userId + "\": " +
                               secPkiApiEx.getStatus() + ": " + errMsg);
        }
        
        userId = null; // interrupt the login of this user
        sessionID = 0;
        return "index?faces-redirect=true"; // go back to the login start page and update the displayed URL in the browser
    }
    
    /**
     * Checks whether the user has already accepted an authentication session at his smart phone.
     * Will be called by the JSF server if the user clicks "OK" in accesspass.xhtml.
     * @return the name of the next page to display
     */
    public String checksessionstate()
    {
        // build the request to query the state of the authentication session at the SecSignID server
        ReqGetAuthSessionState reqGetAuthSessionState = new ReqGetAuthSessionState();
        reqGetAuthSessionState.setAuthSessionID(sessionID);
        
        // send the request to the SecSignID server
        errMsg = null;
        int sessionState = -1;
        try
        {
            ResGetAuthSessionState resGetAuthSessionState = secPkiApi.getAuthSessionState(reqGetAuthSessionState);
            sessionState = resGetAuthSessionState.getAuthSessionState();
        }
        catch(SecPKIApiException secPkiApiEx)
        {
            errMsg = secPkiApiEx.getMessage();
            System.out.println("Error when querying the state of the authentication session with ID \"" + sessionID +
                               "\" for SecSignID user ID \"" + userId + "\": " + secPkiApiEx.getStatus() + ": " + errMsg);
            userId = null; // interrupt the login of this user
            return "index?faces-redirect=true"; // go back to the login start page and update the displayed URL in the browser
        }
        
        String nextPage;
        switch(sessionState)
        {
        	case MobileAuthTypes.SESSION_STATE_FETCHED:
        	case MobileAuthTypes.SESSION_STATE_PENDING:
            {
                errMsg = "The session is still pending. Please accept the session in the SecSignApp on your smart phone.";
                nextPage = "accesspass"; // stay on the page showing the access pass to accept
                break;
            }
            case MobileAuthTypes.SESSION_STATE_DENIED:
            {
                errMsg = "The session has been denied on the smart phone.";
               	disposeHandledSession(sessionID); // dispose the session
                nextPage = "index"; // go back to the login start page
                userId = null; // interrupt the login of this user
                sessionID = 0;
                break;
            }
            case MobileAuthTypes.SESSION_STATE_AUTHENTICATED:
            {
               	disposeHandledSession(sessionID); // dispose the session
                nextPage = "menu"; // go to the menu page for authenticated users
                System.out.println ("SecSignID user ID '" + userId + "' has logged in using session '" + sessionID + "'.");
                authenticatedUserId = userId;
                userId = null; // the login has ended
                sessionID = 0;
                break;
            }
            case MobileAuthTypes.SESSION_STATE_EXPIRED:
            {
                errMsg = "The session has expired.";
                nextPage = "index"; // go back to the login start page
                userId = null; // interrupt the login of this user
                sessionID = 0;
                break;
            }
            case MobileAuthTypes.SESSION_STATE_CANCELED:
            {
                // This state occurs if the user clicks on cancel. But in that case we will not go into this method anyway.
                errMsg = "The session has been withdrawn by the service that had started the login.";
                nextPage = "index"; // go back to the login start page
                userId = null; // interrupt the login of this user
                sessionID = 0;
                break;
            }
            case MobileAuthTypes.SESSION_STATE_INVALID:
            case MobileAuthTypes.SESSION_STATE_SUSPENDED:
            {
                // for example if a second session for the same user has been requested in between
                errMsg = "The SecSignID server has retracted the session for security reasons.";
                nextPage = "index"; // go back to the login start page
                userId = null; // interrupt the login of this user
                sessionID = 0;
                break;
            }
            default:
            {
                // should not happen
                errMsg = "The state of the session is unknown.";
                nextPage = "index"; // go back to the login start page
                userId = null; // interrupt the login of this user
                sessionID = 0;
                break;
            }
        }
        
        if (null != errMsg)
        {
            System.err.println (errMsg);
        }
        
        return nextPage + "?faces-redirect=true"; // redirect to the specified page and update the displayed URL in the browser
    }
    
    /**
     * Disposes the specified authentication session which is not needed any more.
     * @param sessionIdToDispose the ID of the session to dispose.
     */
    private void disposeHandledSession(long sessionIdToDispose)
    {
        // build the request to dispose a used session at the SecSignID server
        ReqReleaseAuthSession reqReleaseAuthSession = new ReqReleaseAuthSession();
        reqReleaseAuthSession.setAuthSessionID(sessionIdToDispose);
        
        // send the request to the SecSignID server
        try
        {
            secPkiApi.releaseAuthSession(reqReleaseAuthSession);
            System.out.println("Disposed the used authentication session with ID \"" + sessionIdToDispose + "\".");
        }
        catch(SecPKIApiException secPkiApiEx)
        {
            String errorMsg = secPkiApiEx.getMessage();
            System.out.println("Error when disposing the authentication session with ID \"" + sessionIdToDispose + "\": " +
                               secPkiApiEx.getStatus() + ": " + errorMsg);
        }
    }
    
    /**
     * Ends the session of the logged in user.
     * Will be called by the JSF server if the user clicks "Logout" in menu.xhtml.
     * @return the name of the next page to display
     */
    public String logout()
    {
        authenticatedUserId = null;
        userId = null; // interrupt the login of this user (should be null already anyway)
        sessionID = 0;
        return "index?faces-redirect=true"; // go back to the login start page and update the displayed URL in the browser
    }
}