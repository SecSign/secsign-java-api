package com.secsign.secsignid.plugins.java.faces;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Redirects users who are not logged in yet to the login page.
 *
 * @version $Id: LoginRedirector.java,v 1.2 2014/12/02 15:35:43 jwollner Exp $
 * @author SecSign Technology Inc.
 */
public class LoginRedirector implements PhaseListener
{
    /**
     * Returns the ID of the Java Server Faces phase during which this listener shall be called.
     * @return the phase ID
     */
    public PhaseId getPhaseId()
    {
        // the first phase
        return PhaseId.RESTORE_VIEW;
    }
    
    /**
     * Does something before this phase.
     * @param event the event
     */
    public void beforePhase(PhaseEvent event)
    {
        // nothing to do
    }
    
    /**
     * Does something after this phase.
     * @param event the event
     */
    public void afterPhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
        
        // get the name of the requested page
        String viewId = facesContext.getViewRoot().getViewId();
        
        // requests for the login page (index.html) are always allowed
        if (!viewId.equals("/index.xhtml"))
        {
            boolean redirectToStartPage = true;
            
            HttpServletRequest req = (HttpServletRequest) (facesContext.getExternalContext().getRequest());
            
            // get SecSignID bean knowing the authenticated user
            SecSignIDBean secSignIDBean = (SecSignIDBean)req.getSession().getAttribute("secsignid");
            if (null != secSignIDBean)
            {
                if (viewId.equals("/accesspass.xhtml"))
                {
                    // This page may only be displayed if there is a ticket to accept.
                    redirectToStartPage = secSignIDBean.getSessionID() == 0;
                }
                else
                {
                    // This page may only be displayed after the successful login.
                    redirectToStartPage = secSignIDBean.getauthenticateduserid() == null;
                }
            }
            
            if (redirectToStartPage)
            {
                // redirect to the login page
                navigationHandler.handleNavigation(facesContext, null, "index");
            }
        }
    }
}
