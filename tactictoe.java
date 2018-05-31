package project;

	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;


	public class tactictoe extends JFrame {
	  
	   public static final int ROWS = 3;  
	   public static final int COLLUMS = 3;
	 
	  
	   public static final int BOXSIZE = 100; 
	   public static final int BOXW = BOXSIZE * COLLUMS;  
	   public static final int BOXH = BOXSIZE * ROWS;
	   public static final int PLAYAREAWIDTH = 8;                
	   public static final int PLAYAREAWIDHTHALF = PLAYAREAWIDTH / 2; 
	   
	   public static final int BOXOUTLINE = BOXSIZE / 6;
	   public static final int MARKS = BOXSIZE - BOXOUTLINE * 2;
	   public static final int MARKSTROKES = 8; 
	 
	 
	   public enum GameState {
	      PLAYING, TIES, XWINS, OWINS
	   }
	   private GameState currentState; 
	 
	
	   public enum Seed {
	      NOTHING, XTURN, OTURN
	   }
	   private Seed PLAYER1;  
	 
	   private Seed[][] GAMEAREA  ; 
	   private DrawCanvas CANVAS; 
	   private JLabel STATUSBAR;  
	 
	   public tactictoe() {
	      CANVAS = new DrawCanvas(); 
	      CANVAS.setPreferredSize(new Dimension(BOXW, BOXH));
	 
	 
	      CANVAS.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) { 
	            int mouseX = e.getX();
	            int mouseY = e.getY();
	           
	            int rowSelected = mouseY / BOXSIZE;
	            int colSelected = mouseX / BOXSIZE;
	 
	            if (currentState == GameState.PLAYING) {
	               if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
	                     && colSelected < COLLUMS && GAMEAREA[rowSelected][colSelected] == Seed.NOTHING) {
	                  GAMEAREA[rowSelected][colSelected] = PLAYER1; 
	                  updateGame(PLAYER1, rowSelected, colSelected);
	                 
	                  PLAYER1 = (PLAYER1 == Seed.XTURN) ? Seed.OTURN : Seed.XTURN;
	               }
	            } else {       
	            	STARTGame(); 
	            }

	            repaint();  
	         }
	      });
	 
	     
	      STATUSBAR = new JLabel("  ");
	      STATUSBAR.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
	      STATUSBAR.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
	 
	      Container cp = getContentPane();
	      cp.setLayout(new BorderLayout());
	      cp.add(CANVAS, BorderLayout.CENTER);
	      cp.add(STATUSBAR, BorderLayout.PAGE_END);
	 
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      pack();  
	      setTitle("TACTICTOE");
	      setVisible(true);  
	 
	      GAMEAREA = new Seed[ROWS][COLLUMS]; 
	      STARTGame(); 
	   }
	 
	   public void STARTGame() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLLUMS; ++col) {
	            GAMEAREA[row][col] = Seed.NOTHING;
	         }
	      }
	      currentState = GameState.PLAYING;
	      PLAYER1 = Seed.XTURN;      
	   }
	 
	  
	   public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
	      if (winner(theSeed, rowSelected, colSelected)) { 
	         currentState = (theSeed == Seed.XTURN) ? GameState.XWINS : GameState.OWINS;
	      } else if (istie()) { 
	         currentState = GameState.TIES;
	      }
	     
	   }
	 
	   
	   public boolean istie() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLLUMS; ++col) {
	            if (GAMEAREA[row][col] == Seed.NOTHING) {
	               return false; 
	            }
	         }
	      }
	      return true; 
	   }
	 

	   public boolean winner(Seed theSeed, int rowSelected, int colSelected) {
	      return (GAMEAREA[rowSelected][0] == theSeed  // 3-in-the-row
	            && GAMEAREA[rowSelected][1] == theSeed
	            && GAMEAREA[rowSelected][2] == theSeed
	       || GAMEAREA[0][colSelected] == theSeed 
	            && GAMEAREA[1][colSelected] == theSeed
	            && GAMEAREA[2][colSelected] == theSeed
	       || rowSelected == colSelected          
	            && GAMEAREA[0][0] == theSeed
	            && GAMEAREA[1][1] == theSeed
	            && GAMEAREA[2][2] == theSeed
	       || rowSelected + colSelected == 2  
	            && GAMEAREA[0][2] == theSeed
	            && GAMEAREA[1][1] == theSeed
	            && GAMEAREA[2][0] == theSeed);
	   }
	 
	 
	   class DrawCanvas extends JPanel {
	      @Override
	      public void paintComponent(Graphics g) { 
	         super.paintComponent(g);   
	         setBackground(Color.WHITE); 
	 
	       
	         g.setColor(Color.GRAY);
	         for (int row = 1; row < ROWS; ++row) {
	            g.fillRoundRect(0, BOXSIZE * row - PLAYAREAWIDHTHALF,
	                  BOXW-1, PLAYAREAWIDTH, PLAYAREAWIDTH, PLAYAREAWIDTH);
	         }
	         for (int col = 1; col < COLLUMS; ++col) {
	            g.fillRoundRect(BOXSIZE * col - PLAYAREAWIDHTHALF, 0,
	                  PLAYAREAWIDTH, BOXH-1, PLAYAREAWIDTH, PLAYAREAWIDTH);
	         }
	 
	         Graphics2D g2d = (Graphics2D)g;
	         g2d.setStroke(new BasicStroke(MARKSTROKES, BasicStroke.CAP_ROUND,
	               BasicStroke.JOIN_ROUND)); 
	         for (int row = 0; row < ROWS; ++row) {
	            for (int col = 0; col < COLLUMS; ++col) {
	               int x1 = col * BOXSIZE + BOXOUTLINE;
	               int y1 = row * BOXSIZE + BOXOUTLINE;
	               if (GAMEAREA[row][col] == Seed.XTURN) {
	                  g2d.setColor(Color.MAGENTA);
	                  int x2 = (col + 1) * BOXSIZE - BOXOUTLINE;
	                  int y2 = (row + 1) * BOXSIZE - BOXOUTLINE;
	                  g2d.drawLine(x1, y1, x2, y2);
	                  g2d.drawLine(x2, y1, x1, y2);
	               } else if (GAMEAREA[row][col] == Seed.OTURN) {
	                  g2d.setColor(Color.YELLOW);
	                  g2d.drawOval(x1, y1, MARKS, MARKS);
	               }
	            }
	         }
	 
	      
	         if (currentState == GameState.PLAYING) {
	            STATUSBAR.setForeground(Color.BLACK);
	            if (PLAYER1 == Seed.XTURN) {
	               STATUSBAR.setText("X's Turn");
	            } else {
	               STATUSBAR.setText("O's Turn");
	            }
	         } else if (currentState == GameState.TIES) {
	            STATUSBAR.setForeground(Color.CYAN);
	            STATUSBAR.setText("It's a TIE CLICK AGAIN LOSERS");
	         } else if (currentState == GameState.XWINS) {
	            STATUSBAR.setForeground(Color.MAGENTA);
	            STATUSBAR.setText("X MY BOY WITH THE DUB CLICK AGAIN TO BEAT O");
	         } else if (currentState == GameState.OWINS) {
	            STATUSBAR.setForeground(Color.YELLOW);
	            STATUSBAR.setText("O MY BOY WITH THE DUB CLICK AGAIN TO BEAT X");
	         }
	      }
	   }
	 
	  
	   public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new tactictoe();
	         }
	      });
	   }
	}

