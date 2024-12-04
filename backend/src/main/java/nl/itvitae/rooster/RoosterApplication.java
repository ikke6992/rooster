package nl.itvitae.rooster;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class RoosterApplication implements CommandLineRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(RoosterApplication.class).headless(false).run(args);
	}


	@Override
	public void run(String... args) throws Exception {
		JFrame frame = new JFrame("Rooster");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);

		JPanel panel = new JPanel(new BorderLayout());
		JTextField text = new JTextField("Running...");
		panel.add(text, BorderLayout.CENTER);
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
}
