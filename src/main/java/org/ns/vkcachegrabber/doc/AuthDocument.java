package org.ns.vkcachegrabber.doc;

import java.awt.Color;
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
import org.ns.vkcachegrabber.vk.Credential;
import org.ns.vkcachegrabber.ui.ResultableDocument;

/**
 *
 * @author stupak
 */
class AuthDocument extends ResultableDocument {
    
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
    protected void processOpenableChanged() {
        String login = (String) openable.getParameter(AuthHandler.LOGIN);
        if ( !Strings.empty(login) ) {
            authPane.setLogin(login);
        }
        char[] password = (char[]) openable.getParameter(AuthHandler.PASSWORD);
        if ( password != null && password.length > 0 ) {
            authPane.setPassword(password);
        }
        password = null;
        boolean invalid = (boolean) openable.getParameter(AuthHandler.INVALID, false);
        String invalidMessage = (String) openable.getParameter(AuthHandler.INVALID_MESSAGE);
        authPane.setInvalid(invalid, invalidMessage);
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
        authPane.setCorrect(validLogin, validPassword);
    }
    
    @Override
    public JComponent getComponent() {
        return authPane;
    }

    private static final Icon INCORRECT_ICON = new ImageIcon(AuthDocument.class.getResource("incorrect.png"));
    private static final Icon CORRECT_ICON = new ImageIcon(AuthDocument.class.getResource("correct.png"));
    private static final int RIGHT_GAP = 5;
    private static final int LEFT_GAP = INCORRECT_ICON.getIconWidth() + RIGHT_GAP;
    
    private class AuthPane extends JPanel {

        private JLabel loginLabel;
        private JTextField loginField;
        private JLabel passwordLabel;
        private JPasswordField passwordField;
        private JCheckBox rememberMeCheckBox;
        private JButton signInButton;
        private JLabel incorrectLoginLabel;
        private JLabel incorrectPasswordLabel;
        private JLabel invalidLabel;
        
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
            incorrectLoginLabel = new JLabel(CORRECT_ICON);
            incorrectPasswordLabel = new JLabel(CORRECT_ICON);
            invalidLabel = new JLabel();
            invalidLabel.setForeground(Color.red);
            invalidLabel.setVisible(false);
        }
        
        private void initLayouts() {
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            
            layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addContainerGap(LEFT_GAP, LEFT_GAP)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
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
                                .addComponent(incorrectLoginLabel)
                                .addComponent(incorrectPasswordLabel)
                            )
                        )
                        .addComponent(invalidLabel)
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
                                .addComponent(incorrectLoginLabel)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField)
                                .addComponent(incorrectPasswordLabel)
                            )
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rememberMeCheckBox)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(signInButton)
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(invalidLabel)
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

        public void setPassword(char[] password) {
            passwordField.setText(new String(password));
        }

        @Override
        public void requestFocus() {
            loginField.requestFocus();
        }
        
        void setCorrect(boolean loginCorrect, boolean passwordCorrect) {
            incorrectLoginLabel.setIcon(loginCorrect ? CORRECT_ICON : INCORRECT_ICON);
            incorrectPasswordLabel.setIcon(passwordCorrect ? CORRECT_ICON : INCORRECT_ICON);
        }
        
        void setInvalid(boolean invalid, String invalidMessage) {
            invalidLabel.setVisible(invalid);
            if ( invalid ) {
                invalidLabel.setText(invalidMessage);
            } else {
                invalidLabel.setText(null);
            }
        }
    }
}
