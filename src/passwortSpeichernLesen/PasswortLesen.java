package passwortSpeichernLesen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * Dieses Programm liest den Hashwert des gespeicherten Passwortes aus und vergleicht ihn mit dem eingegebenen Passwort.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class PasswortLesen {
	
	private JFrame frame1 = new JFrame("Passwort einlesen");
	private JPasswordField passwortFeld = new JPasswordField();
	private JButton buttonBestaetigen = new JButton("Bestätigen");
	private int versuche = 3;
	
	public PasswortLesen() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(300,86));
		frame1.setMinimumSize(new Dimension(300,86));
		frame1.setMaximumSize(new Dimension(450,129));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel passwoerterPanel = new JPanel();
		passwoerterPanel.setLayout(new BorderLayout());
		JPanel labels = new JPanel();
		labels.setLayout(new GridBagLayout());
		JLabel pwLabel1 = new JLabel(" Passwort?");
		labels.add(pwLabel1,new GridBagFelder(0,0,1,1,1,0.5));
		JPanel passwortfelder = new JPanel();
		passwortfelder.setLayout(new GridBagLayout());
		passwortfelder.add(passwortFeld,new GridBagFelder(0,0,1,1,1,1));
		passwoerterPanel.add(labels,BorderLayout.WEST);
		passwoerterPanel.add(passwortfelder,BorderLayout.CENTER);
		
		buttonBestaetigen.setMargin(new Insets(2, 2, 2, 2));
		buttonBestaetigen.addActionListener(new ActionListener() { 
	    	public void actionPerformed(ActionEvent evt) {
	    		einlesen();
	    	}	
	    });
		
		frame1.add(passwoerterPanel,BorderLayout.CENTER);
		frame1.add(buttonBestaetigen,BorderLayout.SOUTH);
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode liest den Hashwert des gespeicherten Passwortes ein. Anschliessend vergleicht es diesen mit dem eingegebenen String.<br>
	 * Sollte der Hashwert nicht gleich sein, wird eine Fehlermeldung angezeigt. Bei drei gescheiterten Anmeldeversuchen wird das Programm geschlossen.
	 */
	private void einlesen() {
		try {
			char[] passwortTemp = passwortFeld.getPassword();
			String passwortEingabe = new String(passwortTemp);
			String passwortHash;
			File f = new File("./dateien/passwort.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./dateien/passwort.txt"), Charset.forName("UTF-8")));
			char[] tempChar = new char[(int)f.length()];
			br.read(tempChar);
			br.close();
			passwortHash = new String(tempChar);
			
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(passwortEingabe.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while(hashtext.length() < 32 ) {
				hashtext = "0"+hashtext;
			}
			
			if(passwortHash.equals(hashtext)) {
				JOptionPane.showMessageDialog(null, "Korrekt Genosse!"+System.getProperty("line.separator")+"Dabendorf heißt Dich willkommen!", "Willkommen", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else if(!(versuche>1)) {
				JOptionPane.showMessageDialog(null, "Du hast das Passwort zu oft versucht einzugeben."+System.getProperty("line.separator")+"Bitte höre auf damit fremde Passwörter zu knacken.", "Gesperrt", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			} else {
				versuche--;
				JOptionPane.showMessageDialog(null, "Das eingegebene Passwort ist falsch."+System.getProperty("line.separator")+"Bitte versuche es erneut!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
			}
			passwortFeld.setText("");
		} catch(NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new PasswortLesen();
	}
}