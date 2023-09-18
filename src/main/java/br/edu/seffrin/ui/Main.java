package br.edu.seffrin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {
	public static void main(String[] args) {
		Client client = new Client(parseArgs(args));
		SwingUtilities.invokeLater(() -> new Main(client));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (client.isConnected()) {
				client.disconnect();
			}
			client.closeRabbitMQ();
		}));
	}

	public static String parseArgs(String[] args) {

		PropertiesConfiguration config = new PropertiesConfiguration();
		try {
			config.load("application.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		String HOST = config.getString("HOST");
		if (args.length < 1) {
			return HOST;
		}
		return args[0];
	}

	private final String FONT = "";
	public final static SimpleAttributeSet ATTR_PLAIN = new SimpleAttributeSet();
	public final static SimpleAttributeSet ATTR_BOLD = new SimpleAttributeSet();
	public final static SimpleAttributeSet ATTR_ITALIC = new SimpleAttributeSet();
	public final static SimpleAttributeSet ATTR_ERROR = new SimpleAttributeSet();
	public final static SimpleAttributeSet ATTR_SERVER = new SimpleAttributeSet();
	private final Client mClient;
	private BufferedImage mIcon;
	private JFrame mFrame;
	private JTextPane mChatArea;

	public Main(Client client) {
		loadTextStyles();
		setDialogs();
		createFrame();
		mClient = client;
		mClient.bindWithGUI(this);
	}

	private void loadTextStyles() {
		StyleConstants.setBold(ATTR_BOLD, true);
		StyleConstants.setFontSize(ATTR_BOLD, (int) convertFontSizeForWindows(18D));
		StyleConstants.setItalic(ATTR_ITALIC, true);
		StyleConstants.setFontSize(ATTR_ITALIC, (int) convertFontSizeForWindows(18D));
		StyleConstants.setBold(ATTR_ERROR, true);
		StyleConstants.setForeground(ATTR_ERROR, Color.red);
		StyleConstants.setFontSize(ATTR_ERROR, (int) convertFontSizeForWindows(18D));
		StyleConstants.setBold(ATTR_SERVER, true);
		StyleConstants.setFontSize(ATTR_SERVER, (int) convertFontSizeForWindows(18D));
	}

	private void setDialogs() {
		UIManager.put("OptionPane.messageFont", new Font(FONT, Font.BOLD, (int) convertFontSizeForWindows(20D)));
		UIManager.put("OptionPane.buttonFont", new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));
		UIManager.put("TextField.font", new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));
	}

	private void createFrame() {
		mFrame = new JFrame("Arpag | Teste para homologação Banrisul/Vero!");
		Container container = mFrame.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(getPainelCentral(), BorderLayout.CENTER);

		mFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (mClient.isConnected()) {
					mClient.disconnect();
				}
				System.exit(0);
			}
		});
		mFrame.setIconImage(mIcon);
		mFrame.setAlwaysOnTop(true);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setLocationRelativeTo(null);

		mFrame.pack();
		mFrame.setVisible(true);
	}

	private JPanel getPainelCentral() {
		GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.weightx = 1;
		constraints1.weighty = 1;
		constraints1.gridx = 0;
		constraints1.gridy = 0;
		constraints1.fill = GridBagConstraints.BOTH;
		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.weightx = 1;
		constraints2.weighty = 0;
		constraints2.gridx = 0;
		constraints2.gridy = 1;
		constraints2.fill = GridBagConstraints.HORIZONTAL;
		constraints2.anchor = GridBagConstraints.PAGE_END;
		GridBagConstraints constraints3 = new GridBagConstraints();
		constraints3.weightx = 1;
		constraints3.weighty = 0;
		constraints3.gridx = 0;
		constraints3.gridy = 2;
		constraints3.fill = GridBagConstraints.HORIZONTAL;
		constraints3.anchor = GridBagConstraints.PAGE_END;
		JPanel painelCentral = new JPanel(new GridBagLayout());
		painelCentral.add(getPainelMensagens(), constraints1);
		painelCentral.add(getPainelPedido(), constraints2);
		painelCentral.add(getPainelEstorno(), constraints3);

		return painelCentral;
	}

	private JPanel getPainelMensagens() {
		String serial = PropertiesManager.getProperty("SERIAL");

		mChatArea = new JTextPane();
		mChatArea.setMargin(new Insets(0, 0, 0, 0));
		mChatArea.setFont(new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));
		mChatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(mChatArea);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));

		JLabel labelSerial = new JLabel();
		labelSerial.setText("Deve ser a CNPJ(somente números): Serial, ex: 03600000104:PBG5233679630");

		PHTextField tfSerial = new PHTextField();
		tfSerial.setText(serial);
		tfSerial.setPlaceholder("Serial:CNPJ");
		tfSerial.setMargin(new Insets(10, 10, 10, 10));
		tfSerial.setFont(new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(labelSerial, BorderLayout.NORTH);
		inputPanel.add(tfSerial, BorderLayout.CENTER);

		JButton gravarButton = new JButton("Gravar");
		gravarButton.addActionListener(onGravarCfgClick(tfSerial));

		inputPanel.add(gravarButton, BorderLayout.EAST);

		panel.add(inputPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private ActionListener onGravarCfgClick(JTextField textField) {
		return e -> {
			try {
				if (textField.getText().length() == 0) {
					JOptionPane.showMessageDialog(textField, "Serial Invalido. ex:  03600477000104:4AD74FS9I", FONT,
							JOptionPane.CANCEL_OPTION);
					return;
				}
				PropertiesManager.storeProperty("SERIAL", textField.getText());

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		};
	}

	private JPanel getPainelPedido() {

		PHTextField tfpedido = new PHTextField();
		tfpedido.setPlaceholder("Pedido");
		tfpedido.setMargin(new Insets(10, 10, 10, 10));
		tfpedido.setFont(new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));

		PHTextField tfValorPedido = new PHTextField();
		tfValorPedido.setPlaceholder("Valor");
		tfValorPedido.setMargin(new Insets(10, 10, 10, 10));
		tfValorPedido.setFont(new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));

		JButton button = new JButton("Cobrar");
		JButton buttonPix = new JButton("PIX");

		button.addActionListener(onCobrarClick(tfValorPedido, tfpedido, "PAGAMENTO"));
		buttonPix.addActionListener(onCobrarClick(tfValorPedido, tfpedido, "PIX"));
		JLabel labelValor = new JLabel();
		labelValor.setText("Ex Pedido: A2304  |  Ex Valor:  2800 para R$ 28,00");

		GridBagConstraints constLabel = new GridBagConstraints();
		constLabel.weightx = 1;
		constLabel.weighty = 1;
		constLabel.gridx = 1;
		constLabel.gridy = 1;
		constLabel.fill = GridBagConstraints.HORIZONTAL;

		GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.weightx = 1;
		constraints1.weighty = 0;
		constraints1.gridx = 0;
		constraints1.gridy = 0;
		constraints1.fill = GridBagConstraints.HORIZONTAL;

		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints1.weightx = 1;
		constraints1.weighty = 0;
		constraints1.gridx = 1;
		constraints1.gridy = 0;
		constraints1.fill = GridBagConstraints.HORIZONTAL;

		GridBagConstraints constraints3 = new GridBagConstraints();
		constraints2.weightx = 1;
		constraints2.weighty = 0;
		constraints2.gridx = 2;
		constraints2.gridy = 0;
		constraints2.fill = GridBagConstraints.HORIZONTAL;

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(labelValor, constLabel);
		panel.add(tfpedido, constraints1);
		panel.add(tfValorPedido, constraints2);
		panel.add(button, constraints3);
		panel.add(buttonPix, constraints3);
		return panel;
	}

	private JPanel getPainelEstorno() {
		PHTextField tfEstornoNSU = new PHTextField();
		tfEstornoNSU.setPlaceholder("NSU do estorno");
		tfEstornoNSU.setMargin(new Insets(10, 10, 10, 10));
		tfEstornoNSU.setFont(new Font(FONT, Font.PLAIN, (int) convertFontSizeForWindows(18D)));
		tfEstornoNSU.addActionListener(onSendEstorno(tfEstornoNSU));

		JLabel labelNSU = new JLabel();
		labelNSU.setText("Exemplo NSU: 2023090400002597");

		GridBagConstraints constLabelNSU = new GridBagConstraints();
		constLabelNSU.weightx = 1;
		constLabelNSU.weighty = 1;
		constLabelNSU.gridx = 0;
		constLabelNSU.gridy = 1;
		constLabelNSU.fill = GridBagConstraints.HORIZONTAL;

		JButton button = new JButton("Estornar");

		button.addActionListener(onSendEstorno(tfEstornoNSU));
		GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.weightx = 1;
		constraints1.weighty = 0;
		constraints1.gridx = 0;
		constraints1.gridy = 0;
		constraints1.fill = GridBagConstraints.HORIZONTAL;
		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.weightx = 1;
		constraints2.weighty = 0;
		constraints2.gridx = 1;
		constraints2.gridy = 0;
		constraints2.fill = GridBagConstraints.HORIZONTAL;
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(tfEstornoNSU, constraints1);
		panel.add(button, constraints2);

		panel.add(labelNSU, constLabelNSU);

		return panel;
	}

	private ActionListener onCobrarClick(JTextField textField, JTextField tfpedido, String tipo) {

		return e -> {
			try {

				if (textField.getText().length() == 0) {
					JOptionPane.showMessageDialog(textField, "Por favor informe um valor no formato 1000 para 10reais",
							FONT, JOptionPane.CANCEL_OPTION);
					return;
				}

				if (tfpedido.getText().length() == 0) {
					JOptionPane.showMessageDialog(textField, "Por favor informe o numero do pedido", FONT,
							JOptionPane.CANCEL_OPTION);
					return;
				}

				ConnectionFactory factory = new ConnectionFactory();
				PropertiesConfiguration config = new PropertiesConfiguration();
				config.load("application.properties");
				String HOST = config.getString("HOST");
				String USER = config.getString("USER");
				String PASS = config.getString("PASS");
//				String QUEUE = config.getString("QUEUE_P2_POS");
//				String QUEUE = config.getString("QUEUE_L300_POS");
				String serial = PropertiesManager.getProperty("SERIAL");
				String QUEUE = serial + ":POS";
				Integer PORT = config.getInt("PORT");
				factory.setHost(HOST);
				factory.setUsername(USER);
				factory.setPassword(PASS);
				factory.setPort(PORT);
				try {
					factory.useSslProtocol();
				} catch (KeyManagementException | NoSuchAlgorithmException ex) {
					ex.printStackTrace();
				}
				Connection conn = factory.newConnection();
				Channel channel = conn.createChannel();
				channel.queueDeclare(QUEUE, true, false, false, null);
				String valor = textField.getText();
				String pedido = tfpedido.getText();

				String mm = String.format(
						"{ \"operacao\": \"%s\", \"pedido\": \"%s\", \"valor\": %s, \"tipo\": \"CREDITO\"}",
					tipo, pedido, valor);
				channel.basicPublish("", QUEUE, null, mm.getBytes("UTF-8"));
				System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + mm + "'");

				channel.close();
				conn.close();
			} catch (IOException | TimeoutException | ConfigurationException e2) {
			}
		};
	}
	
	
	

	private ActionListener onSendEstorno(JTextField textField) {
		return e -> {
			try {
				if (textField.getText().length() == 0) {
					JOptionPane.showMessageDialog(textField, "Por favor informe NSU valido. ex: 2023090400001591", FONT,
							JOptionPane.CANCEL_OPTION);
					return;
				}
				ConnectionFactory factory = new ConnectionFactory();
				PropertiesConfiguration config = new PropertiesConfiguration();
				config.load("application.properties");
				String HOST = config.getString("HOST");
				String USER = config.getString("USER");
				String PASS = config.getString("PASS");
				String QUEUE = config.getString("QUEUE_P2_POS");
				Integer PORT = config.getInt("PORT");
				factory.setHost(HOST);
				factory.setUsername(USER);
				factory.setPassword(PASS);
				factory.setPort(PORT);
				try {
					factory.useSslProtocol();
				} catch (KeyManagementException | NoSuchAlgorithmException ex) {
					ex.printStackTrace();
				}
				Connection conn = factory.newConnection();
				Channel channel = conn.createChannel();
				channel.queueDeclare(QUEUE, true, false, false, null);
				String valor = textField.getText();

				String mm = String.format("{\"operacao\": \"ESTORNO\", \"nsu\": %s, \"tipo\": \"CREDITO\" }", valor);
				channel.basicPublish("", QUEUE, null, mm.getBytes("UTF-8"));
				System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + mm + "'");

				channel.close();
				conn.close();
			} catch (IOException | TimeoutException | ConfigurationException e2) {
			}
		};
	}

	public void addToMessages(String message, SimpleAttributeSet attributes) {
		Document doc = mChatArea.getDocument();
		if (attributes == ATTR_ERROR || attributes == ATTR_SERVER) {
			message = "***** MENSAGEM \n" + message + "\n***** FIM MENSAGEM\n";
		} else if (attributes == ATTR_PLAIN) {
			message = message + "\n";
		}
		try {
			doc.insertString(doc.getLength(), message, attributes);
		} catch (Exception ignored) {
		}
		mChatArea.setCaretPosition(doc.getLength());
	}

	public double convertFontSizeForWindows(double fontSize) {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			double testedWidth = 1500D;
			return fontSize / ((testedWidth / screenWidth) * 2D);
		}
		return fontSize;
	}

}