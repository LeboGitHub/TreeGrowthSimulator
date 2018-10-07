
public class Land{
	
	// to do
	// sun exposure data here
	volatile static int xDimension;
	volatile static int yDimension;
	volatile static private float[][] OGgrid;
	volatile static private float[][] grid;

	static float shadefraction = 0.1f; // only this fraction of light is transmitted by a tree

	Land(int dx, int dy) {
		// to do
		xDimension = dx;
		yDimension = dy;
		OGgrid = new float[xDimension][yDimension];
		grid = new float[xDimension][yDimension];
	}

	// return the number of landscape cells in the x dimension
	synchronized int getDimX() {
		// to do
		//return 0; // incorrect value
		return xDimension;
	}
	
	// return the number of landscape cells in the y dimension
	synchronized int getDimY() {
		// to do
		//return 0; // incorrect value
		return yDimension;
	}
	
	// Reset the shaded landscape to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	synchronized void resetShade() {
		// to do
		for(int x =0;x<xDimension;x++){
			for(int y=0;y<yDimension;y++){
				grid[x][y]= OGgrid[x][y];}}
	}
	
	// return the sun exposure of the initial unshaded landscape at position <x,y?
	synchronized float getFull(int x, int y) {
		// to do
		//return 0.0f; // incorrect value
		float temp =0;
		if((x<xDimension)&&(y<yDimension))
		    temp= grid[x][y];
		//return grid[x][y];
		return temp;
	}
	
	// set the sun exposure of the initial unshaded landscape at position <x,y> to <val>
	synchronized void setFull(int x, int y, float val) {
		// to do 
		//grid[x][y] = val;
		OGgrid[x][y] = val;
	}
	
	// return the current sun exposure of the shaded landscape at position <x,y>
	synchronized float getShade(int x, int y) {
		// to do 
		//return 0.0f; // incorrect value
		float temp=0;
		if((x<xDimension)&&(y<yDimension))
		    temp = grid[x][y];
		return temp;
		//return grid[x][y];
	}
	
	// set the sun exposure of the shaded landscape at position <x,y> to <val>
	synchronized void setShade(int x, int y, float val){
		// to do
		grid[x][y] = val;
	}
	
	
	// reduce the sun exposure of the shaded landscape to 10% of the original
	// within the extent of <tree>
	synchronized void shadow(Tree tree){
		// to do
		int startX = (tree.getX()- (int)tree.getExt());
		int startY = (tree.getY() - (int)tree.getExt());
		
		for(int outter = startX; outter<=(tree.getX()+(2*tree.getExt())-1);outter++)
			for(int inner = startY; inner <=(tree.getY()+(2*tree.getExt())-1);inner++)
				//ensure that you do not go outside the bounds of the terrain
				if((outter<xDimension)&&(outter>=0)&&(inner<yDimension)&&(inner>=0)) {
					float temp = getShade(outter,inner);
					temp -= 0.1*temp;
					setShade(outter,inner,temp);
				}
	}
}
