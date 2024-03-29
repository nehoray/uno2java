
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


//handles the data for each individual card
class card
{
	public char m_type;
	public char m_color;
	public int m_quantity;
}

public class Deck 
{
	private card[] m_data; //data
	private int m_size; //the "visible" size of the deck
	//using m_data.length was throwing array exceptions, so we use m_length
	private int m_length; //the length of the deck
	private int m_numOfCards; //total number of cards left in the deck
	private int m_lastCard; //saves the location of the last card added to the deck
	
	public Deck()
	{
		m_data = null;
		m_size = 0;
		m_length = 0;
		m_numOfCards = 0;
	}
	//constructor that loads the deck
	public Deck(String a_filename) throws IOException
	{
		m_data = null;
		m_size = 0;
		m_length = 0;
		m_numOfCards = 0;
		//load(a_filename);
		FileReader f = new FileReader(a_filename);
		load(f);
	}
	//constructor that creates a blank deck based off the size of
	//another deck (used for creating player hands and discard pile
	public Deck(Deck other)
	{
		m_length = other.m_length;
		m_data = new card[m_length];
		for(int r = 0; r < m_length; ++r)
		{
			//create each card
			m_data[r] = new card();
			//make sure the new deck is empty
			m_data[r].m_type = '0';
			m_data[r].m_color = '0';
			m_data[r].m_quantity = 0;
		}
		m_size = m_numOfCards = m_lastCard = 0;
	}
	//load deck from a filename
	//return true if successful
	public boolean load(String a_filename)
	{
		try
		{
			FileReader f = new FileReader(a_filename);
			load(f);
		} catch (Exception e)
		{
			//ugly fail safe
			//loadMcNasty();
			return false;
		}
		return true;
	}
	
	//if for some reason, java cannot load the file, hard code the deck
	//REALLY BAD!!!!!
	public void loadMcNasty() 
	{
		m_length = m_size = 54;
		m_data = new card[m_length];
		
		for(int i = 0; i < m_length; ++i)
		{
			//create new card
			m_data[i] = new card();
			//set types
			if(i == 0 || i == 13 || i == 26 || i == 39)
			{
				m_data[i].m_type = '0';
				m_data[i].m_quantity = 1;
			}
			else if(i == 1 || i == 14 || i == 27 || i == 40)
			{
				m_data[i].m_type = '1';
				m_data[i].m_quantity = 2;
			}
			else if(i == 2 || i == 15 || i == 28 || i == 41)
			{
				m_data[i].m_type = '2';
				m_data[i].m_quantity = 2;
			}
			else if(i == 3 || i == 16 || i == 29 || i == 42)
			{
				m_data[i].m_type = '3';
				m_data[i].m_quantity = 2;
			}
			else if(i == 4 || i == 17 || i == 30 || i == 43)
			{
				m_data[i].m_type = '4';
				m_data[i].m_quantity = 2;
			}
			else if(i == 5 || i == 18 || i == 31 || i == 44)
			{
				m_data[i].m_type = '5';
				m_data[i].m_quantity = 2;
			}
			else if(i == 6 || i == 19 || i == 32 || i == 45)
			{
				m_data[i].m_type = '6';
				m_data[i].m_quantity = 2;
			}
			else if(i == 7 || i == 20 || i == 33 || i == 46)
			{
				m_data[i].m_type = '7';
				m_data[i].m_quantity = 2;
			}
			else if(i == 8 || i == 21 || i == 34 || i == 47)
			{
				m_data[i].m_type = '8';
				m_data[i].m_quantity = 2;
			}
			else if(i == 9 || i == 22 || i == 35 || i == 48)
			{
				m_data[i].m_type = '9';
				m_data[i].m_quantity = 2;
			}
			else if(i == 10 || i == 23 || i == 36 || i == 49)
			{
				m_data[i].m_type = 'D';
				m_data[i].m_quantity = 2;
			}
			else if(i == 11 || i == 24 || i == 37 || i == 50)
			{
				m_data[i].m_type = 'R';
				m_data[i].m_quantity = 2;
			}
			else if(i == 12 || i == 25 || i == 38 || i == 51)
			{
				m_data[i].m_type = 'S';
				m_data[i].m_quantity = 2;
			}
			else if(i == 52)
			{
				m_data[i].m_type = '0';
				m_data[i].m_quantity = 4;
			}
			else if(i == 53)
			{
				m_data[i].m_type = '4';
				m_data[i].m_quantity = 4;
			}
			
			//set color
			if(i < 13)
			{
				m_data[i].m_color = 'B';
			}
			else if(i < 26)
			{
				m_data[i].m_color = 'G';
			}
			else if(i < 39)
			{
				m_data[i].m_color = 'R';
			}
			else if(i < 52)
			{
				m_data[i].m_color = 'Y';
			}
			else
			{
				m_data[i].m_color = 'W';
			}
			if(i > 10)//TODO REMOVE THIS MCUGLY
			{
				//m_data[i].m_quantity = 0; 
			}
			//update total card quantity
			m_numOfCards += m_data[i].m_quantity;
		}
		
		
		
	}
	//load the deck from file
	public void load(InputStreamReader f) throws IOException
	{
		//input size and create array
		m_length = readNextInt(f);
		m_size = m_length;
		m_data = new card[m_length];

				
		for(int r = 0; r < m_length; ++r)
		{
			//create each card
			m_data[r] = new card();
			//input type, color, and quantity
			m_data[r].m_type = (char) ignoreEndline(f);
			m_data[r].m_color = (char) ignoreEndline(f);
			m_data[r].m_quantity = readNextInt(f);//ignoreEndline(f);
			
			//add the numb of cards to the total
			m_numOfCards += m_data[r].m_quantity;
			
		}
		
	}
	//returns the int value of the next character after the next endline 
	private static int ignoreEndline(InputStreamReader f) throws IOException
	{
		int c;
		boolean whitespaceRead = true;
		do
		{
			c = f.read();
			switch(c)
			{
			case '\n':
			case '\r':
				whitespaceRead = true;
				break;
			default:
				whitespaceRead = false;
			}
		}while(whitespaceRead);
		return c;
	}
	//return the value of the next token as an integer
	private static int readNextInt(InputStreamReader f) throws IOException
	{
		String s = readNextToken(f).toString();
		return Integer.parseInt(s);
	}
	//inputs a token from stream, ignores whitespace
	private static StringBuffer readNextToken(InputStreamReader f) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		int c;
		boolean whitespaceRead = false;
		do
		{
			c = f.read();
			switch(c)
			{
			//ignore whitespace when searching for tokens
			case ' ':
			case '\t':
			case '\n':
			case '\r':
			case '\b':
				whitespaceRead = true;
				break;
			default:
				sb.append((char) c);
			}
		}while (sb.length() == 0 || !whitespaceRead);
		return sb;
	}
	//load a blank deck based off the size of another deck
	public void load(Deck other)
	{
		m_length = other.m_length;
		m_data = new card[m_length];
		for(int r = 0; r < m_length; ++r)
		{
			//make sure the new deck is empty
			m_data[r].m_type = '0';
			m_data[r].m_color = '0';
			m_data[r].m_quantity = 0;
		}
		m_size = m_numOfCards = m_lastCard = 0;
	}
	//calculates the total number of cards in the deck at anytime
	public void calcNumOfCards()
	{
		int total = 0;
		for(int r = 0; r < m_length; ++r)
		{
			total += m_data[r].m_quantity;
		}
		m_numOfCards = total;
	}
	//calculates the "visible size of the deck
	public void calcSize()
	{
		int size = 0;
		for(int r = 0; r < m_length; ++r)
		{
			//if there is a card of this type & color
			if(m_data[r].m_quantity > 0)
			{
				//increase size
				++size;
			}
		}
		m_size = size;
	}
	//return the length of the deck
	public int getLength()
	{
		return m_length;
	}
	//calculate and return the current size
	public int getSize()
	{
		calcSize();
		return m_size;
	}
	//calculate and return the current number of cards in the deck
	public int getNumOfCards()
	{
		calcNumOfCards();
		return m_numOfCards;
	}
	//return the type of a specific card
	public char getTypeAt(int row)
	{
		return m_data[row].m_type;
	}
	//return the color of a specific card
	public char getColorAt(int row)
	{
		return m_data[row].m_color;
	}
	//return the quantity of a specific card
	public int getQuantityAt(int row)
	{
		return m_data[row].m_quantity;
	}
	//returns false if the deck is empty
	//randomly draws a card from the deck
	public boolean drawCard(Deck other)
	{
		//if the deck is not empty
		if(getNumOfCards() > 0)
		{
			//select a random number within the 'visible size of the deck;
			Random rand = new Random();
			int card = rand.nextInt(getSize());
			
			int check = other.getNumOfCards();
			//save the cards into another deck(i.e. pile)
			addCardToOther(card, other);
			//System.out.println("OTHER :" + other.getNumOfCards()); //TODO REMOVE FROM DRAWCARD()
			if(other.getNumOfCards() > check +1)
			{
				System.out.println("------------FAIL!!!!!!-------------");
				//Deck temp = new Deck(this);
				//temp = storeCopy();
				//clearAll();
				//addCopy(temp);
				//addCardToOther(card, other);
			}
			
			//if the quantity = 0, move this element to the end of the deck
			if(m_data[card].m_quantity == 0)
			{
				moveToEnd(card);
			}
			return true;
		}
		return false;
	}
	//add drawn card to another deck
	public void addCardToOther(int card, Deck other)
	{
		boolean isAdded = false;
		//make sure the size is updated
		other.calcSize();
		//search through the "visible" deck
		for(int r = 0; r < other.m_size; ++r)
		{
			//if the card already exists in the deck
			//update the quantity
			if(other.m_data[r].m_type == m_data[card].m_type
					&& other.m_data[r].m_color == m_data[card].m_color)
			{
				other.m_data[r].m_quantity++;
				m_data[card].m_quantity--;
				isAdded = true;
				//save location of the last card added
				other.m_lastCard = r;
			}
		}
		//if the card is not in the deck, add it to the end
		if(!isAdded)
		{
			other.m_data[other.m_size].m_type = m_data[card].m_type;
			other.m_data[other.m_size].m_color = m_data[card].m_color;
			other.m_data[other.m_size].m_quantity = 1;
			m_data[card].m_quantity--;
			//save location of last card added
			other.m_lastCard = other.m_size;			
		}
	}
	//after a card's quantity is reduced to zero
	//this function moves it to the end of the array
	public void moveToEnd(int loc)
	{
		//save current type and color
		card temp = new card();
		temp.m_type = m_data[loc].m_type;
		temp.m_color = m_data[loc].m_color;
		
		for(int r = loc; r < m_length - 1; ++r)
		{
			//move each element up one
			m_data[r] = m_data[r+1];
		}
		
		//save "empty" card to the end of the deck
		m_data[m_data.length-1].m_type = temp.m_type;
		m_data[m_data.length-1].m_color = temp.m_color;
		m_data[m_data.length-1].m_quantity = 0;
	}
	//shuffle the contents of another deck back into this deck
	public void shuffle(Deck other)
	{
		other.calcSize();
		for(int r = 0; r < other.m_size; ++r)
		{
			//while quantity is greater than 0
			//continue to add this card to the deck
			while(other.getQuantityAt(r) > 0)
			{
				addOtherToThis(r, other);
			}
			other.debug();
		}
	}
	//add another deck to this deck
	//this is used to shuffle (i.e. add the discard pile back to the draw deck)
	public void addOtherToThis(int card, Deck other)
	{
		boolean isAdded = false;
		//make sure size is updated
		calcSize();
		//search through the "visible" deck
		for(int r = 0; r < getSize(); ++r)
		{
			//if the card already exists in the deck
			//update the quantity
			if(m_data[r].m_type == other.m_data[card].m_type
					&& m_data[r].m_color == other.m_data[card].m_color)
			{
				//add to this deck
				m_data[r].m_quantity++;
				//remove from the other deck
				other.m_data[card].m_quantity--;
				isAdded = true;
			}
		}
		
		//if the card is not in the deck
		//add it to the end
		if(!isAdded)
		{
			m_data[m_size].m_type = other.m_data[card].m_type;
			m_data[m_size].m_color = other.m_data[card].m_color;
			m_data[m_size].m_quantity = 1;
			other.m_data[card].m_quantity--;
		}
	}
	//return the location of the last card added to a deck
	public int getLastCard()
	{
		return m_lastCard;
	}
	//plays a card from a players hand to the pile
	public void addFromHandToPile(int card, Deck other)
	{
		//add the card to the pile
		addCardToOther(card, other);
		//if quantity is 0, move to the end of the array
		if(getQuantityAt(card) == 0)
		{
			moveToEnd(card);
		}
	}
	//shuffles the contents of another deck (minus one card) back into this deck
	public void shuffleMinusOne(int card, Deck other)
	{
		other.calcSize();
		for(int r = 0; r < other.m_size; ++r)
		{
			if(r != card)//if not the card we want to keep
			{
				//while quantity is greater than 0
				//continue to add this card to the deck
				while(other.getQuantityAt(r) > 0)
				{
					addOtherToThis(r, other);
				}
			}
			else //if it is the card we want to keep
			{
				//while the quantity is greater than 1
				//continue to add this card to the deck
				while(other.getQuantityAt(r) > 1)
				{
					addOtherToThis(r, other);
				}
			}
		}
		//then move the saved card to the front
		other.m_data[0].m_type = other.m_data[card].m_type;
		other.m_data[0].m_color = other.m_data[card].m_color;
		other.m_data[0].m_quantity = 1;
		other.m_data[card].m_quantity = 0;
		other.m_lastCard = 0;
	}
	
	//DEBUG
	//prints info about the deck
	public void debug()
	{
		System.out.println("DEBUG: \nSize: " + getSize() + "  Card#: " + getNumOfCards() + "\n");
		for(int r = 0; r < m_size; ++r)
		{
			System.out.println("Type: " + m_data[r].m_type + " Color: " + m_data[r].m_color +
					"Quantity: " + m_data[r].m_quantity + "\n");
		}
	}
	
	//add a type and color at a specific location
	public void add(int loc, char a_type, char a_color)
	{
		m_data[loc].m_type = a_type;
		m_data[loc].m_color = a_color;
	}
	//returns the location of a card based off its color and type
	public int getLocation(char a_type, char a_color)
	{
		int val = -1;
		for(int n = 0; n < m_length; ++n)
		{
			//if the color and type match
			if(m_data[n].m_type == a_type && m_data[n].m_color == a_color)
			{
				//return the location
				val = n;
			}
		}
		return val;
	}
	//clear all quantities in the deck
	public void clearQuantity()
	{
		for(int r = 0; r < m_length; ++r)
		{
			m_data[r].m_quantity = 0;
		}
	}
	//clear everything in the deck
	public void clearAll()
	{
		m_size = 0;
		m_numOfCards = 0;
		for(int r = 0; r < m_length; ++r)
		{
			m_data[r].m_type = '0';
			m_data[r].m_color = '0';
			m_data[r].m_quantity = 0;
		}
	}

	public Deck storeCopy()
	{
		Deck temp = new Deck(this);
		for(int i = 0; i < m_length; ++i)
		{
			temp.m_data[i].m_color = m_data[i].m_color;
			temp.m_data[i].m_type = m_data[i].m_type;
			temp.m_data[i].m_quantity = m_data[i].m_quantity;
		}
		return temp;
	}
	public void addCopy(Deck other)
	{
		for(int i = 0; i < m_length; ++i)
		{
			m_data[i].m_color = other.m_data[i].m_color;
			m_data[i].m_type = other.m_data[i].m_type;
			m_data[i].m_quantity = other.m_data[i].m_quantity;
		}
	}
}




