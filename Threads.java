

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class TreeGrow implements ActionListener{//, Runnable{
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static ForestPanel fp;
	static JPanel counterPanel;
	static JPanel buttonPanel;
	static JLabel counterLabel;
	static JButton resetButton;
	static JButton pauseButton;
	static JButton playButton;
	static JButton endButton;
	static String state = "play";
	volatile static int year;
	volatile static int cutoff=1000;
	volatile static SunData sundata = new SunData();
	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Tree [] trees) {
		Dimension fsize = new Dimension(800, 800);
		// Frame init and dimensions
    	JFrame frame = new JFrame("Photosynthesis"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setPreferredSize(fsize);
    	frame.setSize(800, 800);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setPreferredSize(fsize);
 
		fp = new ForestPanel(trees);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		JScrollPane scrollFrame = new JScrollPane(fp);
		fp.setAutoscrolls(true);
		scrollFrame.setPreferredSize(fsize);
	    g.add(scrollFrame);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
        
        // inititalise and set components for gui buttons panels and labels
        
        buttonPanel = new JPanel();
        resetButton = new JButton("reset");
        resetButton.setActionCommand("reset");
        playButton = new JButton("play");
        playButton.setActionCommand("play");
        pauseButton = new JButton("pause");
        pauseButton.setActionCommand("pause");
        endButton = new JButton("end");
        endButton.setActionCommand("end");
        buttonPanel.add(resetButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(endButton);
        g.add(buttonPanel);
        
        counterPanel = new JPanel();
        counterLabel = new JLabel("Year: ");
        counterPanel.add(counterLabel);
        g.add(counterPanel);
	}
	
		
	public static void main(String[] args) {
		//SunData sundata = new SunData();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java treeGrow.java intputfilename");
			//System.exit(0);
		}
				
		// read in forest and landscape information from file supplied as argument
		//sundata.readData("src/sample_input.txt");
		sundata.readData(args[0]);
		System.out.println("Data loaded");
		
		frameX = sundata.sunmap.getDimX();
		frameY = sundata.sunmap.getDimY();
		setupGUI(frameX, frameY, sundata.trees);
		
		for(int iLoop=0;iLoop< sundata.trees.length;iLoop++){
				sundata.trees[iLoop].setExt(0.4f);
				}
		
		//sundata.sunmap.resetShade();
		// create and start simulation loop here as separate thread
		year = 0 ;
		int lo =0;
	    int hi = sundata.trees.length;
	    Tree[] arr = sundata.trees;
		while(state.equalsIgnoreCase("play") || state.equalsIgnoreCase("pause")){
			if(state.equalsIgnoreCase("play")){
				year++;
				counterLabel.setText("Year: "+String.valueOf(year));
				//oneRun(sundata.trees);
				Threads t = new Threads(arr,lo,hi,sundata);
				t.oneRun(sundata.trees);
				}
			}
			
			/*for(int iLoop=0; iLoop<sundata.trees.length ; iLoop++){
				   sundata.trees[iLoop].sungrow(sundata.sunmap);
				   sundata.sunmap.shadow(sundata.trees[iLoop]);
				   }
			*/
		
	}
	
	
	/*public void TreeGrow(Land[] land,int hi, int lo,int min, int max)
	{
		if((hi-lo)>= cutoff){
			TreeGrow(land,lo,(hi/2),min,max);//left recurion 
			TreeGrow(land,(hi/2)+1,hi);//right recursion
			}
		for(int iLoop = lo; iLoop<high;iLoop++){
			//check to see if tree is within a given extent range
			if((land.trees[iLoop].getExt()<=min)&&(land.trees[iLoop]<max)){
			land.trees[iLoop].sungrow();
			land.shadow(trees[iLoop]);}
			}
	}	*/
	
	/*int lo;
	int hi;
	Tree[] arr = sundata.trees;
	*/
	/*public TreeGrow(Tree[] a, int l, int h){
		arr = a;
		lo =l;
		hi=h;
		}
	
	public void run(){
		
		if((hi-lo)<cutoff){
			for(int i=lo;i<hi;i++){
				simulateTree(arr[i]);
				}
			}else{
				TreeGrow left = new TreeGrow(arr,lo,(hi+lo)/2);
				TreeGrow right = new TreeGrow(arr,(hi+lo)/2,hi);
				left.run();
				right.run();
				//left.join();
				//right.join();
			}
		
		}
		
	public static void oneRun(Tree[] arr){
		TreeGrow t = new TreeGrow(arr,0,arr.length);
		t.run();
		}
	
	public static void simulateTree(Tree tree){
		tree.sungrow(sundata.sunmap);
		sundata.sunmap.shadow(tree);
		}
*/
	@Override
	public void actionPerformed(ActionEvent e) {
		state = e.getActionCommand();
		
		if(state.equalsIgnoreCase("reset")){
			sundata.sunmap.resetShade();
			for(int iLoop=0;iLoop< sundata.trees.length;iLoop++){
				sundata.trees[iLoop].setExt(0.4f);
				}
			}
		
		if(state.equalsIgnoreCase("pause")){
			state = "pause";
			}
			
		if(state.equalsIgnoreCase("end")){
			System.exit(0);
			}
			
		if (state.equalsIgnoreCase("play")){
			state = "play";
			}
		
		
	}
}
