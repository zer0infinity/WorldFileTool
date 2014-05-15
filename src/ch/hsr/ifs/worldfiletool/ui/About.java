package ch.hsr.ifs.worldfiletool.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * The Class About.
 */
public class About extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new about.
	 */
	public About() {
		setTitle("WorldFileTool - About");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setSize(400, 250);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 400) / 2, (dim.height - 350) / 2);

		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon(getClass().getResource(
						"/ch/hsr/ifs/worldfiletool/ui/images/about.jpg"));
				g.drawImage(img.getImage(), 0, -30, null);
			}
		};
		panel.setOpaque(false);
		panel.setToolTipText("david.tran@hsr.ch");

		// Button: Close
		JButton button_close = new JButton();
		button_close.setText("Close");
		button_close.setBounds(280, 190, 100, 20);
		button_close.setFont(new Font("Arial", Font.PLAIN, 11));
		button_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// TextField
		JTextField textField = new JTextField("www.ifs.hsr.ch");
		textField.setActionCommand("url");
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setLocation(10, 190);
		textField.setSize(265, 20);
		textField.setEditable(false);
		textField.setOpaque(false);
		textField.setToolTipText("Institute for Software");

		// PopupMenu on Rightclick
		JPopupMenu popupMenu = popupMenu_textField(textField);
		textField.setComponentPopupMenu(popupMenu);

		// Escape = Close Window
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		JRootPane rootPane = getRootPane();
		rootPane.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		panel.setLayout(null);
		panel.add(button_close);
		panel.add(textField);

		add(panel);
		setVisible(true);
	}

	/**
	 * PopupMenu TextField.
	 * 
	 * @param textField
	 * 
	 * @return JPopupMenu
	 */
	private JPopupMenu popupMenu_textField(final JTextField textField) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.setFont(new Font("Arial", Font.PLAIN, 11));
		MouseListener copy_event = new MouseAdapter() {
			public void mouseReleased(MouseEvent mouseEvent) {
				if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
					textField.copy();
				}
			}
		};
		copyMenuItem.addMouseListener(copy_event);
		popupMenu.add(copyMenuItem);
		return popupMenu;
	}
}