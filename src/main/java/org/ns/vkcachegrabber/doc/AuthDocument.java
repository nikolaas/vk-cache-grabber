package org.ns.vkcachegrabber.doc;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.api.Openable;
import org.ns.vkcachegrabber.vk.Credential;
import org.ns.vkcachegrabber.ui.ResultableDocument;

/**
 *
 * @author stupak
 */
class AuthDocument extends ResultableDocument {
    
    private Openable openable;
    private final AuthPane authPane;
    private final Action signInAction = new AbstractAction("Sign in") {

        @Override
        public void actionPerformed(ActionEvent e) {
            signIn();
        }
    };

    public AuthDocument() {
        this.authPane = new AuthPane();
    }
    
    @Override
    public Openable getOpenable() {
        return openable;
    }

    @Override
    public void setOpenable(Openable openable) {
        this.openable = openable;
        String login = (String) openable.getParameter(AuthHandler.LOGIN);
        if ( !Strings.empty(login) ) {
            authPane.setLogin(login);
        }
    }
    
    private void signIn() {
        String login = authPane.getLogin();
        char[] password = authPane.getPassword();
        boolean validLogin = !Strings.empty(login);
        boolean validPassword = password.length > 0;
        if ( validLogin && validPassword ) {
            Credential credential = new Credential(login, password);
            setResult(credential, OK_RESULT);
        }
        authPane.setValid(validLogin, validPassword);
    }
    
    @Override
    public JComponent getComponent() {
        return authPane;
    }

    @Override
    public void close() {
    }
    
    private static final Icon INVALID_ICON = new ImageIcon(AuthDocument.class.getResource("invalid.png"));
    private static final Icon VALID_ICON = new ImageIcon(AuthDocument.class.getResource("valid.png"));
    private static final int RIGHT_GAP = 5;
    private static final int LEFT_GAP = INVALID_ICON.getIconWidth() + RIGHT_GAP;
    
    private class AuthPane extends JPanel {

        private JLabel loginLabel;
        private JTextField loginField;
        private JLabel passwordLabel;
        private JPasswordField passwordField;
        private JCheckBox rememberMeCheckBox;
        private JButton signInButton;
        private JLabel invalidLoginLabel;
        private JLabel invalidPasswordLabel;
        
        public AuthPane() {
            super();
            initComponents();
            initLayouts();
        }
        
        private void initComponents() {
            loginLabel = new JLabel("Email");
            loginField = new JTextField();
            loginField.addActionListener(signInAction);
            passwordLabel = new JLabel("Password");
            passwordField = new JPasswordField();
            passwordField.addActionListener(signInAction);
            rememberMeCheckBox = new JCheckBox("Remember me");
            signInButton = new JButton(signInAction);
            invalidLoginLabel = new JLabel(VALID_ICON);
            invalidPasswordLabel = new JLabel(VALID_ICON);
        }
        
        private void initLayouts() {
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            
            layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addContainerGap(LEFT_GAP, LEFT_GAP)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginLabel)
                                .addComponent(passwordLabel)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginField)
                                .addComponent(passwordField)
                                .addComponent(rememberMeCheckBox)
                            )
                        )
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(signInButton)
                        )
                    )
                    //.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(invalidLoginLabel)
                        .addComponent(invalidPasswordLabel)
                    )
                    .addContainerGap(RIGHT_GAP, RIGHT_GAP)
            );
            
            layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginLabel)
                                .addComponent(loginField)
                                .addComponent(invalidLoginLabel)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField)
                                .addComponent(invalidPasswordLabel)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rememberMeCheckBox)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(signInButton)
                        )
                    )
                    .addContainerGap()
            );
        }
        
        public String getLogin() {
            return loginField.getText();
        }
        
        public void setLogin(String login) {
            loginField.setText(login);
        }
        
        public char[] getPassword() {
            return passwordField.getPassword();
        }

        @Override
        public void requestFocus() {
            loginField.requestFocus();
        }
        
        void setValid(boolean loginValid, boolean passwordValid) {
            invalidLoginLabel.setIcon(loginValid ? VALID_ICON : INVALID_ICON);
            invalidPasswordLabel.setIcon(passwordValid ? VALID_ICON : INVALID_ICON);
        }
    }
}
