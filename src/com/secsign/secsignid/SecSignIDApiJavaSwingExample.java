package com.secsign.secsignid;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import seccommerce.secpki.api.SecPKIApiException;

/**
 * Example for the SecSign ID API using a Swing Window
 * 
 * @version 1.0
 * @author SecSign Technology Inc.
 */
public class SecSignIDApiJavaSwingExample implements ActionListener{

    /**
     * The label to show the logo and access pass
     */
    private JLabel image;

    /**
     * the text field to input the secsign id
     */
    private JTextField secSignIdTextField;
    
    /**
     * the button to start the login
     */
    private JButton loginButton;

    /**
     * the connection to the SecSignID server
     */
    private SecSignIDApi secSignIdApi;

    /**
     * Runnable to check the server for the state of the authentication session
     */
    private Runnable checkState = new Runnable()
    {
        @Override
        public void run() {
            boolean checkForState = true;
            //loop until the user accepts or discards the session
            while(checkForState)
            {
                //Get the State
                AuthenticationSessionState state = null;
                try
                {
                    state = secSignIdApi.getAuthenticationSessionState();
                } catch (SecSignIDException e)
                {
                    e.printStackTrace();
                }

                String msg = "";

                switch (state.getAuthSessionState()) {
                case AuthenticationSessionState.FETCHED:
                case AuthenticationSessionState.PENDING:
                {
                    msg = "The session is still pending. Please accept the session in the SecSignApp on your smart phone.";
                    break;
                }
                case AuthenticationSessionState.DENIED:
                {
                    msg = "The session has been denied on the smart phone.";
                    checkForState = false;
                    break;
                }
                case AuthenticationSessionState.AUTHENTICATED:
                {
                    try
                    {
                        secSignIdApi.releaseAuthenticationSession();
                    } catch (SecSignIDException e)
                    {
                        e.printStackTrace();
                    }
                    msg = "Successfully Authenticated SecSign ID \""+state.getAuthenticatedSecSignId()+"\"";
                    checkForState = false;
                    break;
                }
                case AuthenticationSessionState.EXPIRED:
                {
                    msg = "The session has expired.";
                    checkForState = false;
                    break;
                }
                case AuthenticationSessionState.CANCELED:
                {
                    msg = "The session has been withdrawn by the service that had started the login.";
                    checkForState = false;
                    break;
                }
                case AuthenticationSessionState.INVALID:
                case AuthenticationSessionState.SUSPENDED:
                {
                    // for example if a second session for the same user has been requested in between
                    msg = "The SecSignID server has retracted the session for security reasons.";
                    checkForState = false;
                    break;
                }
                default:
                {
                    // should not happen
                    msg = "The state of the session is unknown.";
                    checkForState = false;
                    break;
                }
                }

                if (!checkForState)
                {
                    //Authentication finished. Reset GUI and show Msg
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            image.setIcon(new ImageIcon("secsign_logo.png"));
                            loginButton.setEnabled(true);
                        }
                    });
                    JOptionPane.showMessageDialog(null, msg);
                }

                System.out.println(msg);

                //Sleep 1 sec
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }


        }
    };

    /**
     * Main method to start the window
     * @throws SecPKIApiException 
     */
    public static void main(String[] args) throws SecSignIDException {
        new SecSignIDApiJavaSwingExample();
    }

    /**
     * Constructor.
     * @throws SecSignIDException 
     */
    public SecSignIDApiJavaSwingExample() throws SecSignIDException
    {
        // create the SecSignID-API instance which can send requests to the SecSignID server
        secSignIdApi = new SecSignIDApi();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * creates the swing GUI
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SecSign ID Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400,200));

        Container pane = frame.getContentPane();

        //Image
        image = new JLabel(new ImageIcon("secsign_logo.png"));
        pane.add(image, BorderLayout.CENTER);

        //TextField
        secSignIdTextField = new JTextField();
        secSignIdTextField.setPreferredSize(new Dimension(300, 25));

        //Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setActionCommand("login");

        //Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(secSignIdTextField);
        bottomPanel.add(loginButton);
        pane.add(bottomPanel, BorderLayout.PAGE_END);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * called when the user clicks on the login button
     */
    public void actionPerformed(ActionEvent e) {
        if ("login".equals(e.getActionCommand())) {

            //Request a Session from the Server
            AuthenticationSession session = null;
            try
            {
                session = secSignIdApi.requestAuthenticationSession(secSignIdTextField.getText(), "SecSignID Java integration example", "www.example.com", "127.0.0.1");
            } catch (SecSignIDException e2)
            {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(null, e2.getLocalizedMessage());
            }

            if (session != null)
            {
                //Show the Access Pass
                final byte[] passIcon = session.getAuthSessionIconData();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        image.setIcon(new ImageIcon(passIcon));
                        loginButton.setEnabled(false);
                    }
                });

                //Start Thread to ask the Server for the State of the Authentication Session
                new Thread(checkState).start();

            }

        }
    } 

}
