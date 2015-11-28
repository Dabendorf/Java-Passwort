package passwortSpeichernLesen;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * Dieses Programm speichert ein eingegebenes Passwort in einer Textdatei ab.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class PasswortSpeichern {
	
	private JFrame frame1 = new JFrame("Passwort speichern");
	private JPasswordField passwortFeld = new JPasswordField();
	private JPasswordField passwortFeldBestaetigung = new JPasswordField();
	private JButton buttonSpeichern = new JButton("Speichern");
	
	public PasswortSpeichern() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(300,120));
		frame1.setMinimumSize(new Dimension(300,120));
		frame1.setMaximumSize(new Dimension(450,180));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel passwoerterPanel = new JPanel();
		passwoerterPanel.setLayout(new BorderLayout());
		JPanel labels = new JPanel();
		labels.setLayout(new GridBagLayout());
		JLabel pwLabel1 = new JLabel(" Passwort");
		JLabel pwLabel2 = new JLabel(" Passwort bestätigen");
		labels.add(pwLabel1,new GridBagFelder(0,0,1,1,1,0.5));
		labels.add(pwLabel2,new GridBagFelder(0,1,1,1,1,0.5));
		JPanel passwortfelder = new JPanel();
		passwortfelder.setLayout(new GridBagLayout());
		passwortfelder.add(passwortFeld,new GridBagFelder(0,0,1,1,1,0.5));
		passwortfelder.add(passwortFeldBestaetigung,new GridBagFelder(0,1,1,1,1,0.5));
		passwoerterPanel.add(labels,BorderLayout.WEST);
		passwoerterPanel.add(passwortfelder,BorderLayout.CENTER);
		
		buttonSpeichern.setMargin(new Insets(2, 2, 2, 2));
		buttonSpeichern.addActionListener(new ActionListener() { 
	    	public void actionPerformed(ActionEvent evt) {
	    		speichern();
	    	}	
	    });
		
		frame1.add(passwoerterPanel,BorderLayout.CENTER);
		frame1.add(buttonSpeichern,BorderLayout.SOUTH);
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Dieses Programm liest die Strings aus den beiden Passwortfeldern aus.<br>
	 * Sollten beide gleich sein, wird das Kennwort in einer Textdatei abgespeichert.<br>
	 * Wenn das Kennwort zu kurz ist, wird es nicht angenommen.
	 */
	private void speichern() {
		try {
			char[] passwortTemp = passwortFeld.getPassword();
			char[] passwortBestTemp = passwortFeldBestaetigung.getPassword();
			String passwort = new String(passwortTemp);
			String passwortBest = new String(passwortBestTemp);
			if(passwort.equals(passwortBest) && passwort.length()>=8) {
				MessageDigest m = MessageDigest.getInstance("MD5");
				m.reset();
				m.update(passwort.getBytes());
				byte[] digest = m.digest();
				BigInteger bigInt = new BigInteger(1,digest);
				String hashtext = bigInt.toString(16);
				while(hashtext.length() < 32 ) {
					hashtext = "0"+hashtext;
				}
				
				try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./dateien/passwort.txt"), Charset.forName("UTF-8")));
					bw.write(hashtext);
					bw.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Dein Passwort wurde gespeichert!", "Vorgang abgeschlossen", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			} else if(passwort.length()<8) {
				JOptionPane.showMessageDialog(null, "Dein Passwort ist viel zu kurz."+System.getProperty("line.separator")+"Ist Dir Sicherheit so wenig wert?", "Zu kurz", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Die Passwörter stimmen nicht überein."+System.getProperty("line.separator")+"Bitte versuche es erneut!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
			}
			passwortFeld.setText("");
			passwortFeldBestaetigung.setText("");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new PasswortSpeichern();
	}
}