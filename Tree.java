
// Trees define a canopy which covers a square area of the landscape
public class Tree{
	
private
	volatile int xpos;	// x-coordinate of center of tree canopy
	volatile int ypos;	// y-coorindate of center of tree canopy
	volatile float ext;	// extent of canopy out in vertical and horizontal from center
	
	static float growfactor = 10.0f; // divide average sun exposure by this amount to get growth in extent
	
    public Tree(int x, int y, float e){
		xpos=x; ypos=y; ext=e;
	}
	
	// return the x-position of the tree center
	synchronized int getX() {
		return xpos;
	}
	
	// return the y-position of the tree center
	synchronized int getY() {
		return ypos;
	}
	
	// return the extent of the tree
	//
	synchronized float getExt() {
		return ext;
	}
	
	// set the extent of the tree to <e>
	synchronized void setExt(float e) {
		ext = e;
	}

	// return the average sunlight for the cells covered by the tree
	synchronized float sunexposure(Land land){
		// to do 
		//return 0.0f; // not correct
		float sum =0;
		int startX= xpos;
		int startY = ypos;
		if(ext>1){
		startX = xpos - (int)ext;
		startY = ypos - (int)ext;}
	
		int numValues=1;
		
		
		
		//get the total for sunlight values and the number of values
		if(ext>1){
		for(int outter = startX ; outter <(startX+(2*ext)-1);outter++)
			for(int inner = startY; outter <(startY+(2*ext)-1);inner++)
				//ensure that  you do not go outside the bounds of the terrain
				if((outter<land.getDimX())&&(outter>=0)&&(inner<=land.getDimY())&&(inner>=0)) {
					//if((startX+(2*ext)-1<land.getDimX())&&(startY+(2*ext)-1<land.getDimY())){
					sum+= land.getFull(outter,inner);
					numValues++;//}
				}
		}else{
			sum = land.getShade(xpos,ypos);
			}
		float avg= sum/numValues;
		System.out.println("avg = "+String.valueOf(avg));
		return avg;
	}
	
	// is the tree extent within the provided range [minr, maxr)
	synchronized boolean inrange(float minr, float maxr) {
		return (ext >= minr && ext < maxr);
	}
	
	// grow a tree according to its sun exposure
	synchronized void sungrow(Land land) {
		// to do
		ext= ext+ (sunexposure(land)/growfactor);
		System.out.println("ext = "+String.valueOf(ext));
	}
}
