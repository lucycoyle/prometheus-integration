import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SimpleTimer extends JFrame
{
    private JLabel label;
    private JLabel label2;
    private Timer timer;
    private Timer timer2;
    private int counter = 10; // the duration
    private int delay = 1000; // every 1 second
    private static final long serialVersionUID = 1L;

    public SimpleTimer()
    {
        super("Simple Timer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        label = new JLabel("Wait for " + counter + " sec", JLabel.CENTER);
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(label, BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pack();

        label2 = new JLabel("Wait for " + counter + " sec", JLabel.CENTER);
        JPanel contentPane2 = (JPanel) getContentPane();
        contentPane2.add(label, BorderLayout.CENTER);
        contentPane2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pack();
        ActionListener action = new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(counter == 0)
                {
                    timer.stop();
                    label.setText("The time is up!");
                    label2.setText("no more time");
                }
                else
                {
                    label.setText("Wait for " + counter + " sec");
                    label2.setText("Wait for " + counter + " sec");
                    counter--;
                }
            }
        };

        timer = new Timer(delay, action);
        timer.setInitialDelay(0);
        timer.start();
       // timer2 = new Timer(delay, action);
        //timer2.setInitialDelay(2);
        //timer2.start();

        setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new SimpleTimer();
               // new SimpleTimer();
            }
        });
    }
}