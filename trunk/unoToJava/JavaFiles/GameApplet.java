import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextField;
import java.io.IOException;




public class GameApplet extends Applet implements Runnable

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Game m_game;
	Button player[];
	Button wildSelect[];
	Button cards[];
	Button action[];
	boolean cardsSet = false;
	Button label1;
	Button label2;
	Button playerName;
	Button hotSeat;

	Label inputLabel;
	TextField nameBlock;

	static final int buttonCount = 9;
	int numOfPlayers;

	Graphics g;

	String PlayerNames[];
	int playerCounter = 0;
	boolean runOnce = true;

	boolean playerSet = false;
	boolean Setupdone = false;
	

	public void init()
	{
		this.setSize(new Dimension(700, 480));
		this.setBackground(new Color(225, 128, 225));
		this.setFont(new Font("Arial", 0, 18));
		try {
			
			m_game = new Game(5, 
					"../unoDeck.txt");
					//f.getAbsolutePath()+"./unoDeck.txt");
					//"C:/Eclipse/workspace/uno2Java/unoDeck.txt");
		} catch (IOException e) {
			//fail-safe
			//ideally we want to read from the file, because thats the way the 
			//program was originally designed in DOS, but if for some reason, java can't
			//locate the text file, we have the deck hard-coded in
			m_game = new Game(5);
		}
		playerNumButtons();
		addKeyListener(m_game.getUI());
		addMouseListener(m_game.getUI());


		Thread t = new Thread(this);
		t.start();


	}
	
	
	public void paint(Graphics g)
	{


		validate();
		//m_game.draw(g);	
		if(Setupdone)
		{
			drawTopCard(g);
		}
		
		//repaint();
	}
	
	
	public void playerNumButtons()
	{
		Label output_label;
		output_label = new Label("Select the number of Players:");
		add(output_label);
		player = new Button[buttonCount];
		for(int i = 0; i < buttonCount; ++i)
		{
			player[i] = new Button(" " + (i+2));
			add(player[i]);
		}
	}
	
	int actionCount = 4;
	static final int ACTION_DRAW = 0;
	static final int ACTION_END = 1;
	static final int ACTION_UNO = 2;
	static final int ACTION_FAIL = 3;
	
	public void playerNameLabel()
	{
		playerName = new Button("CURRENT PLAYER: " + m_game.getCurrentPlayer().getName());
		playerName.setPreferredSize(new Dimension(700, 20));
		add(playerName);
	}
	
	public void actionButtons()
	{
		//Current player
		playerNameLabel();
		
		action = new Button[actionCount];
		action[ACTION_DRAW] = new Button("Draw Card");
		action[ACTION_END] = new Button("End Turn");
		action[ACTION_UNO] = new Button("Call Uno");
		action[ACTION_FAIL] = new Button("Failure to Call Uno");
		for(int i = 0; i < actionCount; ++i)
		{
			action[i].setPreferredSize(new Dimension(160, 20));
			add(action[i]);
		}
	}
	
	public void playerNames()
	{
		
		inputLabel = new Label("Enter Name for Player " + (playerCounter+1) + ":");
		nameBlock = new TextField(10);
		add(inputLabel);
		add(nameBlock);
		repaint();
		
	}
	
	static final int WILD_COUNT = 4;
	static final int WILD_BLUE = 0;
	static final int WILD_RED = 1;
	static final int WILD_GREEN = 2;
	static final int WILD_YELLOW = 3;
	public void wildSelection()
	{
		//remove all buttons
		removeAll();
		//Current player
		playerNameLabel();
		//label button
		label1 = new Button("SELECT A COLOR:");
		label1.setPreferredSize(new Dimension(700, 20));
		add(label1);
		//create buttons
		wildSelect = new Button[WILD_COUNT];
		wildSelect[WILD_BLUE] = new Button("BLUE");
		wildSelect[WILD_BLUE].setBackground(Color.blue);
		wildSelect[WILD_RED] = new Button("RED");
		wildSelect[WILD_RED].setBackground(Color.red);
		wildSelect[WILD_GREEN] = new Button("GREEN");
		wildSelect[WILD_GREEN].setBackground(Color.green);
		wildSelect[WILD_YELLOW] = new Button("YELLOW");
		wildSelect[WILD_YELLOW].setBackground(Color.yellow);
		for(int i = 0; i < actionCount; ++i)
		{
			wildSelect[i].setPreferredSize(new Dimension(160, 20));
			add(wildSelect[i]);
		}
		
		label2 = new Button("YOUR CURRENT HAND:");
		label2.setPreferredSize(new Dimension(700, 20));
		add(label2);
		
		drawHand();
		
	}
	
	public void loadHotSeat()
	{
		removeAll();
		playerNameLabel();
		hotSeat = new Button("HOT SEAT");
		hotSeat.setFont(new Font("Arial", Font.BOLD, 72));
		hotSeat.setBackground(Color.black);
		hotSeat.setForeground(Color.white);
		hotSeat.setPreferredSize(new Dimension(700, 160));
		add(hotSeat);
		
	}
	
	public boolean action (Event e, Object args)
	  { 
		
		actionPlayerNum(e, args);
		
		if(e.target == hotSeat)
		{
			if(m_game.isGameInWildState())
			{
				wildSelection();
			}
			else
			{
				m_game.setGamePlay();
				removeAll();
				actionButtons();
				drawHand();
			}
		}
		if(cardsSet)
		{
			if(m_game.isGameInWildState())
			{
				actionWild(e, args);
			}
			else //game in play
			{
				actionActionButtons(e, args);
				actionPlayCard(e, args);
			}
		}
			
		if (e.target instanceof TextField)
		{
			actionPlayerName(e, args);
		}
		
	    return true;    // Yes, we do need this!
	  }


	public void actionPlayerNum(Event e, Object args)
	{
		for(int i = 0; i < buttonCount; ++i)
		{
			if (e.target == player[i] && !playerSet)
		     {  // user has clicked this button
				numOfPlayers = i +2;
				//System.out.println("WORKED "+ numOfPlayers);
				
				removeAll();
				m_game.setup(numOfPlayers);
				PlayerNames = new String[numOfPlayers];
				playerNames();
				
				playerSet = true;
				

		     }
			
		}
	}
	public void actionWild(Event e, Object args)
	{
		for(int r = 0; r < WILD_COUNT; ++r)
		{
			if(e.target == wildSelect[r])
			{
				switch(r)
				{
				case WILD_BLUE: m_game.setWildColor('B');
					break;
				case WILD_RED: m_game.setWildColor('R');
					break;
				case WILD_GREEN: m_game.setWildColor('G');
					break;
				case WILD_YELLOW: m_game.setWildColor('Y');
					break;
				}
				m_game.setGamePlay();
				removeAll();
				actionButtons();
				drawHand();
				
			}
		}
	}
	public void actionActionButtons(Event e, Object args)
	{
		for(int n = 0; n < actionCount; ++n)
		{
			if(e.target == action[n])
			{
				switch(n)
				{
				case ACTION_DRAW:
					if(!m_game.getHasDrawn() && !m_game.getCardPlayed()&& m_game.okayToAddCard(m_game.getCurrentPlayerLoc()))
					{

						m_game.getDrawDeck().drawCard(m_game.getCurrentPlayer().getHand());
						m_game.setHasDrawn(true);
						m_game.setCursorLock(true);
						//clear all buttons
						removeAll();
						actionButtons();
						drawLastCard();	//only let the user select the last card drawn
						
					}
					break;
				case ACTION_END:
					//if the user has either played a card
					//or they have drawn, but cannot play their last card
					if(m_game.getCardPlayed() || (m_game.getHasDrawn() && !m_game.isCardLegal(m_game.getCurrentPlayer().getHand().getLastCard())))
					{
						m_game.endTurn();
						//removeAll();
						//actionButtons();
						//drawHand();
						loadHotSeat();
					}
					break;
				case ACTION_UNO:
					if(!m_game.getUnoCalled())
					{
						m_game.callUno();
						removeAll();
						actionButtons();
						drawHand();
					}
					break;
				case ACTION_FAIL:
					if(!m_game.getUnoFailed())
					{
						m_game.failureToCallUno();
						removeAll();
						actionButtons();
						drawHand();
					}
					break;
				}
			}
		}
	}
	public void actionPlayCard(Event e, Object args)
	{
		int q = 0;
		char t = ' ', c = ' ';
		//checks to see if the card is legal
		for(int i=0; i<m_game.getCurrentPlayer().getHand().getNumOfCards(); ++i)//m_game.getCurrentPlayer().getHand().getColorAt(i) != lastColor || m_game.getCurrentPlayer().getHand().getTypeAt(i) != lastType; ++i)
		{
			q = m_game.getCurrentPlayer().getHand().getQuantityAt(i);
			
			while(q > 0)
			{
				if(e.target == cards[i])
				{
					//if the cursor is locked, set the look at to the last card drawn
					if(m_game.getCursorLock())
					{
						i = m_game.getCurrentPlayer().getHand().getLastCard();
					}
					if(m_game.isCardLegal(i) && !m_game.getCardPlayed())
					{
						//play card function here
						System.out.print("Yes, you can play the card at location ");
						//first re-save type and color directly from deck
						t = m_game.getCurrentPlayer().getHand().getTypeAt(i);
						c = m_game.getCurrentPlayer().getHand().getColorAt(i);
						//then add the card to the pile
						m_game.getCurrentPlayer().getHand().addFromHandToPile(i, m_game.getDiscardPile());
						System.out.println(m_game.getDiscardPile().getColorAt(m_game.getDiscardPile().getLastCard()));
						removeAll(); //clear all
						actionButtons();
						drawHand(); //redraw hand
						if(!m_game.calcWin())
						{
							//clear the wild color
							m_game.setWildColor('0');
							//set card effect
							m_game.setCardEffect(t, c);
							//if a wild card was played
							if(m_game.isGameInWildState())
							{
								wildSelection();
							}
							//set flag for endTurn()
							m_game.setCardPlayed(true);
						}
						else //if win, set end effect
						{m_game.setEndEffect(t, c);}
						{}
					}
					else
					{
						System.out.print("No, you can't play the card at location ");
					}
					
					System.out.print(i);
					System.out.print("\n");
					
					i=m_game.getCurrentPlayer().getHand().getNumOfCards();//checks to see if the card is legal
				}
			--q;
			}
		}
	}
	public void actionPlayerName(Event e, Object args)
	{
		if (e.target == nameBlock && runOnce == true)
        {
    		PlayerNames[playerCounter] = nameBlock.getText(); 
    		nameBlock.setText("");
    		playerCounter++;
    		runOnce = false;
    		//m_game.setPlayerNames(PlayerNames);
    		removeAll();//clean
    		playerNames();//print new label and text field
        	
		}
        if(playerCounter == numOfPlayers && Setupdone == false)
        {
        	m_game.setPlayerNames(PlayerNames);
        	removeAll();
        	//TODO first game drawing start here
        	loadHotSeat();	
        	//actionButtons();
        	//drawHand();
			Setupdone = true;
        }   
	}
	
	public void drawHand()
	{
		

		int current = m_game.getDiscardPile().getLastCard();
		System.out.print("\n\n\n");
		System.out.print(m_game.getDiscardPile().getColorAt(current));
		System.out.print(" ");
		System.out.print(m_game.getDiscardPile().getTypeAt(current));
		System.out.print("\n");
		
		/******************/
		
		
		//buttons = new JPanel();
		Player player = m_game.getCurrentPlayer();
		cards = new Button[player.getHand().getNumOfCards()];
		int q = 0;
		int j = 0;
		int offset = 0;
		
		String color, type;
		/**
		 * sets the color for each card and creates the buttons
		 */
		for(int i=0; i<player.getHand().getNumOfCards() - offset; ++i)
		{
			q = player.getHand().getQuantityAt(i);
			color = getColor(player, i);
			type = getType(player, i);
			//print all duplicates
			while(q > 0)
			{
				cards[j] = new Button("" + color 
						+ " " + type);
				switch(player.getHand().getColorAt(i))
				{
				case 'B':	cards[j].setBackground(new Color(0, 0, 255));	
							cards[j].setForeground(new Color(0, 0, 0));	break;
				case 'R':	cards[j].setBackground(new Color(255, 0, 0));	
							cards[j].setForeground(new Color(0, 0, 0));	break;
				case 'G':	cards[j].setBackground(new Color(0, 255, 0));	
							cards[j].setForeground(new Color(0, 0, 0));	break;
				case 'Y':	cards[j].setBackground(new Color(255, 255, 0));	
							cards[j].setForeground(new Color(0, 0, 0));	break;
				case 'W':	cards[j].setBackground(new Color(0, 0, 0));	
							cards[j].setForeground(new Color(255, 255, 255));	break;
				}
				cards[j].setPreferredSize(new Dimension(150, 20));
				//buttons.add(cards[i]);
				add(cards[j]);
				if(q > 1)
				{
					offset += 1;
				}
				--q;
				j++;
			}
		}
		/*****************/
		
		cardsSet = true;
	}

	
	public void drawLastCard()
	{
		
		
		
		//buttons = new JPanel();
		Player player = m_game.getCurrentPlayer();
		cards = new Button[1];

		
		String color, type;
		/**
		 * sets the color for each card and creates the buttons
		 */
		
			
			color = getColor(player, player.getHand().getLastCard());
			type = getType(player, player.getHand().getLastCard());
			//print all duplicates

			cards[0] = new Button("" + color 
					+ " " + type);
			switch(player.getHand().getColorAt(player.getHand().getLastCard()))
			{
			case 'B':	cards[0].setBackground(new Color(0, 0, 255));	
						cards[0].setForeground(new Color(0, 0, 0));	break;
			case 'R':	cards[0].setBackground(new Color(255, 0, 0));	
						cards[0].setForeground(new Color(0, 0, 0));	break;
			case 'G':	cards[0].setBackground(new Color(0, 255, 0));	
						cards[0].setForeground(new Color(0, 0, 0));	break;
			case 'Y':	cards[0].setBackground(new Color(255, 255, 0));	
						cards[0].setForeground(new Color(0, 0, 0));	break;
			case 'W':	cards[0].setBackground(new Color(0, 0, 0));	
						cards[0].setForeground(new Color(255, 255, 255));	break;
			}
			cards[0].setPreferredSize(new Dimension(150, 20));
			//buttons.add(cards[i]);
			add(cards[0]);
		
		/*****************/
		
		cardsSet = true;
	}
	
	
	//@Override
	public void run() {
		while(true)
		{
//			System.out.println("repaint?");
			repaint();
			runOnce = true;
			try{
				Thread.sleep(100);
			}catch(Exception e){}
			
			
		}
		
	}
	
	public String getColor(Player p, int loc)
	{
		String temp = " ";
		switch(p.getHand().getColorAt(loc))
		{
		case 'B':	temp = "Blue";	break;
		case 'R':	temp = "Red";	break;
		case 'G':	temp = "Green";	break;
		case 'Y':	temp = "Yellow"; break;
		case 'W':	temp = "Wild"; break;
		}
		return temp;
	}
	
	public String getType(Player p, int loc)
	{
		String temp = " ";
		switch(p.getHand().getTypeAt(loc))
		{
		case '0':	
			if(p.getHand().getColorAt(loc) == 'W')
			{temp = "";}
			else
			{temp = "0";}
			break;
		case '1':	temp = "1";	break;
		case '2':	temp = "2";	break;
		case '3':	temp = "3"; break;
		case '4':	
			if(p.getHand().getColorAt(loc) == 'W')
			{temp = "+4";}
			else
			{temp = "4";}
			break;
		case '5':	temp = "5";	break;
		case '6':	temp = "6";	break;
		case '7':	temp = "7";	break;
		case '8':	temp = "8"; break;
		case '9':	temp = "9"; break;
		case 'R':	temp = "Reverse";	break;
		case 'S':	temp = "Skip";	break;
		case 'D':	temp = "Draw"; break;
		}
		return temp;
	}

	int topCardX = 300; int topCardBX = 290;//loc
	int topCardY = 280; int topCardBY = 270;
	int topCardW = 125; int topCardBW = 145;//width
	int topCardH = 175; int topCardBH = 195;//height
	int topCardWA = 20;//arc width
	int topCardHA = 20;//arc height
	Font topCardFont = new Font("Arial",Font.BOLD,30);
	Font topCardLabel = new Font("Arial",Font.ITALIC,22);
	int topCardFontX = 305;
	int topCardFontY = 375;
	int topCardFontX_Offset = 0;
	int topCardLabelX = 335;
	int topCardLabelY = 265;
	
	public void	drawTopCard(Graphics g)
	{
		//card border
		g.setColor(Color.black);
		g.drawRoundRect(topCardBX, topCardBY,topCardBW, topCardBH, topCardWA, topCardHA);
		//label
		g.setFont(topCardLabel);
		g.drawString("PILE:", topCardLabelX, topCardLabelY);
		g.setColor(Color.white);
		g.fillRoundRect(topCardBX+1, topCardBY+1,topCardBW-1, topCardBH-1, topCardWA+2, topCardHA+2);
		
		//color card
		char color = m_game.getDiscardPile().getColorAt(m_game.getDiscardPile().getLastCard());
		char type = m_game.getDiscardPile().getTypeAt(m_game.getDiscardPile().getLastCard());
		switch(color)
		{
		case 'B':	g.setColor(Color.blue);	break;
		case 'R':	g.setColor(Color.red);	break;
		case 'G':	g.setColor(Color.green);	break;
		case 'Y':	g.setColor(Color.yellow); break;
		case 'W':	g.setColor(Color.black); break;
		}
		g.fillRoundRect(topCardX, topCardY,topCardW, topCardH, topCardWA, topCardHA);
		//draw type
		String temp = " ";
		switch(type)
		{
		case '0':	
			if(color == 'W')
			{temp = " WILD  ";topCardFontX_Offset = 11;}
			else
			{temp = "   0   "; topCardFontX_Offset = 25;break;}
			break;
		case '1':	temp = "   1   ";	topCardFontX_Offset = 25;break;
		case '2':	temp = "   2   ";	topCardFontX_Offset = 25;break;
		case '3':	temp = "   3   "; topCardFontX_Offset = 25;break;
		case '4':	
			if(color == 'W')
			{temp = "WILD +4";topCardFontX_Offset = -2;}
			else
			{temp = "   4   "; topCardFontX_Offset = 25;break;}
			break;
		case '5':	temp = "   5   ";	topCardFontX_Offset = 25;break;
		case '6':	temp = "   6   ";	topCardFontX_Offset = 25;break;
		case '7':	temp = "   7   ";	topCardFontX_Offset = 25;break;
		case '8':	temp = "   8   "; topCardFontX_Offset = 25;break;
		case '9':	temp = "   9   "; topCardFontX_Offset = 25;break;
		case 'R':	temp = "Reverse";	topCardFontX_Offset = -2;break;
		case 'S':	temp = " Skip  ";	topCardFontX_Offset = 17;break;
		case 'D':	temp = " Draw  "; topCardFontX_Offset = 15;break;
		}
		//set color for font
		if(color == 'W')
			g.setColor(Color.white);
		else
			g.setColor(Color.black);
		
		g.setFont(topCardFont);
		g.drawString(temp, topCardFontX + topCardFontX_Offset, topCardFontY);
		
	}

	public void drawHUD(Graphics g)
	{
		
	}
}