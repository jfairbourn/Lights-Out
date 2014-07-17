import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
* This class implements the game Lights Out, where
* the user tries to turn off all of the lights(yellow buttons)
* by selecting various buttons.  When selecting a button,
* the adjacent buttons(top, right, left, bottom) will change
* to the opposite color.  The game keeps count of how many times
* the user has won the game, and notifies them when they have won.
* There is also a "Randomize" and "Enter Manual Setup" button, which
* will randomly place lights on the board and the manual setup allows the
* user to individually select which buttons they want turned on.
* 
* @author James Fairbourn
* @version u0407375
* @version Program #9
* @version CS1410-001
* @version Fall 2011
* @date 22 Nov. 2011
*/
public class LightsOut extends JFrame implements ActionListener
{

	public static void main(String[] args) 
	{
		LightsOut light = new LightsOut();		//Creates a new LightsOut object, which is a new game.
		light.setVisible(true);					//sets the LightsOut object to visible.

	}
	
	//private GameBoard boardLayout;
	private JButton[][] gameButtons;					//this two dimensional array will hold a reference the all of the buttons on the board.
	private JButton manual;								//a button for the "Enter Manual Setup".
	private JLabel wins;								//a label used to tell the user how many wins they currently have.
	private int winCount;								//a count variable to hold the current number of wins.
	public LightsOut()
	{
		winCount = 0;									//sets the winCount variable to 0.
		//Terminates the program when the user closes the JFrame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Sets title and size of the JFrame.
		setTitle("Lights Out");
		setSize(500, 500);
		
		JPanel mainPanel = new JPanel();				//The current panel that everything is placed in.
		
		mainPanel.setLayout(new BorderLayout());		//sets the layout of the mainPanel.
		
		//Buttons
		JPanel buttonPanel = new JPanel();				//a new panel to hold all of the buttons on the game board.
		gameButtons = new JButton[5][5];				//creates a new two dimensional array that will hold 25 buttons.
		buttonPanel.setLayout(new GridLayout(5,5));		//sets the layout for the buttons.					
		
		for(int i = 0; i<5; i++)						//these two loops will add each button into the two dimensional array.
		{
			for(int j = 0; j<5; j++)
			{
				int random = (int)(Math.random()*3);	//sets up a variable for a random number, so the lights(buttons) will randomly be turned on.
				JButton button = new JButton();			//creats a new JButton object.
				gameButtons[i][j] = button;				//adds the button to the array.
				button.setName(""+i+j);					//sets up the name of each button accordingly where they appear on the game board.
				button.setBackground(Color.BLACK);		//sets each button to black, turned off.
				if(random == 2)							//uses the random variable to change the color of the button to yellow, turned on.
				{
					backgroundColor(button);
				}
				button.addActionListener(this);			//adds an actionlistener to each button.
				buttonPanel.add(button);				//adds the button to the buttonPanel.
			
			}
		}
		
		mainPanel.add(buttonPanel, "Center");			//adds the buttonPanel to the mainPanel.
		
		//Creates the buttons used for different controls.
		JButton random = new JButton("Randomize");		//Creates a new button that will be used for random lights to turn either on or off.
		random.setName("Random");						//sets the name of the Randomize button.
		random.addActionListener(this);					//adds an actionlistener.
		manual = new JButton("Enter Manual Setup");		//Creates a new button that will be used for the user to manually change the board.
		manual.setName("Manual");						//sets the name of the manual setup button.
		manual.addActionListener(this);					//adds an actionlistener.
		
		
		JPanel controls = new JPanel();			  		//Creates a new JPanel that will hold the different controls buttons.
		controls.setLayout(new GridLayout(1,2));		//sets the layout of the controls JPanel.
		controls.add(random,"South");					//adds the randomize button to the controls JPanel.
		controls.add(manual,"South");					//adds the manual button to the controls Jpanel.
		
		
		mainPanel.add(controls,"South");				//adds the controls JPanel to the mainPanel.
		
		//Labels
		JPanel label = new JPanel();					//creates a new JPanel for labels to be added to the game board.
		label.setLayout(new GridLayout(1,1));			//sets the layout of the labels JPanel.
		label.add(wins = new JLabel());					//adds the wins label to the label JPanel.
		wins.setText("Wins: " + winCount);				//sets the text of the wins label.
		mainPanel.add(label, "North");					//the label JPanel is added to the mainPanel.
		
		setContentPane(mainPanel);						//sets the content pane of the frame.
	}
	/**
	 * This method is used whenever
	 * a button is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		JButton button = (JButton)e.getSource();		//gets the current button that was clicked.
		String location = button.getName();				//gets the name of the current button
		if(location.equals("Random"))					//if the "Randomize" button is clicked,
		{												//then the random method is called.
			randomSetting();
			return;
		}
		if(location.equals("Manual Exit"))				//if the "Exit Manual Mode" button is clicked
		{												//the name and text is changed and the game
			manual.setText("Enter Manual Setup");		//returns to a normal state.
			manual.setName("Manual");
			return;
		}
		if(location.equals("Manual"))					//if the "Enter Manual Setup" button is clicked
		{												//then the name and text are changed.
			manual.setText("Exit Manual Mode");
			manual.setName("Manual Exit");
			return;
		}
		if(manual.getName().equals("Manual Exit"))		//while the name of the manual button is set
		{												//as "Manual Exit", the manual setting will be 
			manualSetting(button);						//called on the current button.
			return;
		}
		char colChar = location.charAt(0);				//gets the char character a position 0 of the button name.
		char rowChar = location.charAt(1);				//gets the char character a position 1 of the button name.
		int col = Character.getNumericValue(colChar);	//this int converted from the char from pos 0 will be used a column indicator.
		int row = Character.getNumericValue(rowChar);	//this int converted from the char from pos 0 will be used a row indicator.
		
		//temporary buttons for the adjacent locations 
		//next to the selected button.
		JButton tempSelected = new JButton();			//a temp button for the selected button
		JButton tempTop = new JButton();				//a temp button for the button above the selected button
		JButton tempLeft = new JButton();				//a temp button for the button left to the selected button
		JButton tempRight = new JButton();				//a temp button for the button right to the selected button
		JButton tempBottom = new JButton();				//a temp button for the button below the selected button
		
		tempSelected = gameButtons[col][row];			//gets the current button selected and stores it in the temp.
		backgroundColor(tempSelected);					//calls the backgroundColor method to change the color of the button.
		
		//each button is attempted, but if it exceeds the Array, then it is caught and nothing is done.
		try
		{
			tempTop = gameButtons[col-1][row];			//get the button that is above the selected button and stores it in temp.
			backgroundColor(tempTop);					//calls the backgroundColor method to change the color of the button.
		}
		catch(ArrayIndexOutOfBoundsException i)
		{
			
		}
		
		try
		{
			tempLeft = gameButtons[col][row-1];			//get the button that is left to the selected button and stores it in temp.
			backgroundColor(tempLeft);					//calls the backgroundColor method to change the color of the button.
		}
		catch(ArrayIndexOutOfBoundsException i)
		{
			
		}
		try
		{
			tempRight = gameButtons[col][row+1];		//get the button that is right to the selected button and stores it in temp.
			backgroundColor(tempRight);					//calls the backgroundColor method to change the color of the button.
		}
		catch(ArrayIndexOutOfBoundsException i)
		{
			
		}
		try
		{
			tempBottom = gameButtons[col+1][row];		//get the button that is below the selected button and stores it in temp.
			backgroundColor(tempBottom);				//calls the backgroundColor method to change the color of the button.
		}
		catch(ArrayIndexOutOfBoundsException i)
		{
			
		}
	
		isWon();										//the isWon method is called to see whether or not the game has been won,
														//in other words if all of the lights have been turned off.
	}
	

	/**
	 * Changes the color of the button sent in
	 * as a parameter to either yellow or black, 
	 * depending on what the current color of the
	 * parameter is.
	 * @param b JButton object.
	 */
	private void backgroundColor(JButton b)
	{
		if(b.getBackground()==Color.BLACK)			//the button b is black, then it is changed to yellow, otherwise it is
		{											//changed to black.
			b.setBackground(Color.YELLOW);
		}
		else
		{
			b.setBackground(Color.BLACK);
		}
	}
	/**
	 * This method generates a random number
	 * for each button in a two dimensional array
	 * and will change the color of the button 
	 * if the randomly generated integer equals 
	 * 2.
	 */
	private void randomSetting()
	{
		for(JButton b[]: gameButtons)					//moves through each JButton in the two dimensional array.
		{
			for(JButton c : b)
			{
				int random = (int)(Math.random()*6);	//generates a random number between 0 and 5.
				if(random == 2)							//if the number equals 2, the color of button is changed.
				{
					backgroundColor(c);
				}
				
			}
		}
	}
	/**
	 * This method utilizes the backgroundColor
	 * method to change the color of the JButton
	 * sent in as a parameter.
	 * @param b JButton object.
	 */
	private void manualSetting(JButton b)
	{
		JButton temp = new JButton();		//creates a temporary JButton.
		temp = b;							//sets the parameter as the temp.
		backgroundColor(temp);				//calls the backgroundColor method 
	}										//to change the color of the current button.
	/**
	 * This method is used to determine
	 * if the game board is in a winning
	 * state.
	 */
	private void isWon()
	{
		int count = 0;						//a count variable.
		for(JButton b[]: gameButtons)		//moves through the two dimensional array for each
		{									//JButton.
			for(JButton c: b)
			{
				if(c.getBackground()==Color.BLACK)		//if the current JButton color is black, then the count is incremented.
				{
					count++;
				}
			}
		}
		if(count == 25)									//if the count equals 25 then the game board is in a winning state.
		{
			JOptionPane.showMessageDialog(this, "Congratulations, you have won!");		//user notified the game has been won.
			winCount++;																	//winCount variable incremented by 1.
			wins.setText("Wins: " + winCount);											//sets status of current wins to the user.
			restart();																	//resets the current game board after the game has been won.
		}	
	}
	/**
	 * This method is used to
	 * restart the current game board and
	 * utilizes the backgroundColor method.
	 * 
	 */
	private void restart()
	{
		for(JButton b[]: gameButtons)					//moves through the two dimensional array for each JButton.
		{
			for(JButton c : b)
			{
				int random = (int)(Math.random()*4);	//randomly generates a number between 0 and 3.
				if(random == 2)							//if the number is two, the current JButton color is changed.
				{
					backgroundColor(c);
				}
				
			}
		}
	}
}


