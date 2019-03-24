//to compile from command prompt c:\projects\RSI>javac -cp ../ displayRSI.java
//to execute from command prompt c:\projects\RSI>java -cp ../ RSI.displayRSI

package RSI;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class displayRSI {

   	private Frame mainFrame;
   	private Label headerLabel;
   	private Label statusLabel;
  	private Panel controlPanel;

   	int xwidth = 1100;
   	int yheight = 650;
   	int basex = 100;
   	int basey = yheight - 100;
   	int gheight = basey - 100;
   	int gwidth = xwidth - basex;
   	String gFileName = "";
   	String oldgFileName = "";
   	int numofobjsg = 0;
   	int numofobjrb = 0;
   	int numofobjcb = 0;
   	int numofobjcr = 0;

 	public displayRSI(){
      		prepareGUI();
   	}

   	public static void main(String[] args){
      		displayRSI  dga = new displayRSI();
      		dga.showFileDialogDemo();
   	}

   	private void prepareGUI(){
      		mainFrame = new Frame("Java AWT Examples");
      		mainFrame.setSize(400,400);
      		mainFrame.setLayout(new GridLayout(3, 1));
      		mainFrame.addWindowListener(new WindowAdapter() {
         		public void windowClosing(WindowEvent windowEvent){
            			System.exit(0);
         		}        
      		});    
      		headerLabel = new Label();
      		headerLabel.setAlignment(Label.CENTER);
      		statusLabel = new Label();        
      		statusLabel.setAlignment(Label.CENTER);
      		statusLabel.setSize(350,100);

      		controlPanel = new Panel();
      		controlPanel.setLayout(new FlowLayout());

      		mainFrame.add(headerLabel);
      		mainFrame.add(controlPanel);
      		mainFrame.add(statusLabel);
      		mainFrame.setVisible(true);  
   	}

   	private void showFileDialogDemo(){
      		headerLabel.setText("Control in action: FileDialog"); 

      		final FileDialog fileDialog = new FileDialog(mainFrame,"RSI Graph");
      		Button showFileDialogButton = new Button("Open File");
	      	controlPanel.add(showFileDialogButton);
		Button sg = showGraphButton();
		Button rb = showReportButton();
		Button cb = showClosePButton();
		Button cr = showClosePRSIButton();
      		showFileDialogButton.addActionListener(new ActionListener() {
         		@Override
         		public void actionPerformed(ActionEvent e) {
            			fileDialog.setVisible(true);
            			statusLabel.setText("File Selected :" + fileDialog.getDirectory() + fileDialog.getFile());
				gFileName = fileDialog.getDirectory() + fileDialog.getFile();
				enableGraphButton(gFileName,sg);
				enableReportButton(gFileName, rb);
				enableClosePButton(gFileName, cb);
				enableClosePRSIButton(gFileName, cr);
         		}
      		});

      		mainFrame.setVisible(true);

   	}

	//================================================================
   	private Button showGraphButton(){

    		Button sg = new Button("RSI Graph");

	      	controlPanel.add(sg);
		sg.setEnabled(false);

		return sg;
   	}

	//================================================================
   	private Button showReportButton(){

    		Button rb = new Button("RSI Report");

	      	controlPanel.add(rb);
		rb.setEnabled(false);

		return rb;
   	}

	//================================================================
   	private Button showClosePButton(){

    		Button cb = new Button("CloseP Graph");

	      	controlPanel.add(cb);
		cb.setEnabled(false);

		return cb;
   	}

	//================================================================
   	private Button showClosePRSIButton(){

    		Button cr = new Button("CloseP/RSI Graph");

	      	controlPanel.add(cr);
		cr.setEnabled(false);

		return cr;
   	}
	//================================================================
   	private void enableGraphButton(String fn, Button sg){

		if (numofobjsg > 0){
			for( ActionListener al : sg.getActionListeners() ) {
        			sg.removeActionListener( al );
    			}
		}
      		sg.addActionListener(new ActionListener() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
				displayGraph dg = new displayGraph(fn);
				dg.setVisible(true);
        		}
      		});

		sg.setEnabled(true);
	      	mainFrame.setVisible(true);
		numofobjsg++;

   	}

	//================================================================
   	private void enableReportButton(String fn, Button rb){

		if (numofobjrb > 0){
			for( ActionListener al : rb.getActionListeners() ) {
        			rb.removeActionListener( al );
    			}
		}
      		rb.addActionListener(new ActionListener() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
				displayReport dg = new displayReport(fn);
				dg.showTextAreaDemo();
        		}
      		});

		rb.setEnabled(true);
	      	mainFrame.setVisible(true);
		numofobjrb++;

   	}

	//================================================================
   	private void enableClosePButton(String fn, Button sg){

		if (numofobjcb > 0){
			for( ActionListener al : sg.getActionListeners() ) {
        			sg.removeActionListener( al );
    			}
		}
      		sg.addActionListener(new ActionListener() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
				displayCloseP dg = new displayCloseP(fn);
				dg.setVisible(true);
        		}
      		});

		sg.setEnabled(true);
	      	mainFrame.setVisible(true);
		numofobjcb++;

   	}

	//================================================================
   	private void enableClosePRSIButton(String fn, Button sg){

		if (numofobjcr > 0){
			for( ActionListener al : sg.getActionListeners() ) {
        			sg.removeActionListener( al );
    			}
		}
      		sg.addActionListener(new ActionListener() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
				displayClosePRSI dg = new displayClosePRSI(fn);
				dg.setVisible(true);
        		}
      		});

		sg.setEnabled(true);
	      	mainFrame.setVisible(true);
		numofobjcr++;

   	}

	//===============================================================
	public class displayReport{

		String fileName = "";

   		public displayReport(String x){
			fileName = x;
      			//prepareGUI();
   		}

   		private void showTextAreaDemo(){

			int avDays = 14;
			int i = 0;
			String txt = "";

			RSIFileTBL tbl = new RSIFileTBL();
			RSIFileVO vo = new RSIFileVO();
			displayGraph dg = new displayGraph();

			tbl.setAvDays(avDays);
			tbl.setRSIName(fileName);
			tbl = dg.readFile(tbl);
			tbl = dg.deriveRSI(tbl);

			txt = "  Date ClosePrice  Gain  Loss  Avgain  Avloss   RS   RSI\n";
			txt += " ----------------------------------------------------------------------\n";
			DecimalFormat df = new DecimalFormat("0.00");
			while (i < tbl.getRepCnt()){
				vo = tbl.getRSITBL(i);

				txt += String.format("%10s  %6.2f %6s %6s %6s %6s %6s %6s %3s\n",
					vo.getRSIDate(),vo.getRSIClosep(),
					vo.getRSIGain() == 0.00? "          " : df.format(vo.getRSIGain()),
					vo.getRSILoss() == 0.00? "          " : df.format(vo.getRSILoss()),
					vo.getRSIAvgain() == 0.00? "          " : df.format(vo.getRSIAvgain()),
					vo.getRSIAvloss() == 0.00? "          " : df.format(vo.getRSIAvloss()),
					vo.getRSIRelstr() == 0.00? "          " : df.format(vo.getRSIRelstr()),
					vo.getRSIValy() == 0.00? "          " : df.format(vo.getRSIValy()),
					i+1+"");
				i++;
			}
      			final TextArea commentTextArea = new TextArea(txt,30,60);

		      	final Frame aboutFrame = new Frame();
      			aboutFrame.setSize(500,500);;
      			aboutFrame.setTitle("Relative Strength Index Report");
      			aboutFrame.addWindowListener(new WindowAdapter() {
            			public void windowClosing(WindowEvent windowEvent){
               				aboutFrame.dispose();
         			}        
      			}); 

      			controlPanel = new Panel();
      			controlPanel.setLayout(new FlowLayout());
			Font font = new Font("Courier", Font.PLAIN, 15);
      			aboutFrame.add(controlPanel);
       			controlPanel.add(commentTextArea);        
      			aboutFrame.setVisible(true);  
   		}
	}

	//===============================================================
	public class displayClosePRSI extends Frame{

		String fileName = "";

   		public displayClosePRSI(){
			System.out.println("In dcr() - 01");
		}

   		public displayClosePRSI(String x){
			System.out.println("In dcr(fn) - 01");

			fileName = x;
     			prepareGUIcp();
   		}

   		private void prepareGUIcp(){
			System.out.println("In pg - 01");

      			setSize(xwidth,yheight);
      			setTitle("Display CloseP");
      			addWindowListener(new WindowAdapter() {
            			public void windowClosing(WindowEvent windowEvent){
               				dispose();
            			}
			});       
      		}

		//==============================================================
		public RSIFileTBL displayCloseP(RSIFileTBL tbl){

			RSIFileVO vo = new RSIFileVO();

			int i = 0;
			int maxy = 0;
			float closep = 0.0f;
			System.out.println("dcp - 01 i="+i+" repcnt="+tbl.getRepCnt());
			while (i < tbl.getRepCnt()){
				vo = tbl.getRSITBL(i);
				if (i > tbl.getAvDays()){
					vo.setRSIValy(vo.getRSIClosep());
					closep = vo.getRSIClosep();
					if (maxy < closep) maxy = Math.round(closep);
					tbl.setRSIMap(vo);
				}
				//System.out.println("dcp - 02 cp="+vo.getRSIClosep() );
				i++;
			}
			tbl.setMaxY(maxy);
			return tbl;
		}
		//===================================================================
   		@Override
   		public void paint(Graphics g) {
			System.out.println("In paint - 01");

			int avDays = 14;

			RSIFileTBL tbl = new RSIFileTBL();
			displayGraph dg = new displayGraph();
			displayCloseP dp = new displayCloseP();
			tbl.setAvDays(avDays);
			tbl.setRSIName(fileName);
			tbl = dg.readFile(tbl);
			tbl = dg.deriveRSI(tbl);
			dg.displayAxis(g,tbl);
       			g.setColor(Color.YELLOW);
			dg.displayGraph(g,tbl);

			tbl = tbl.clearTBL(tbl);
			tbl.setAvDays(avDays);
			tbl.setRSIName(fileName);
			tbl = dg.readFile(tbl);
			tbl = displayCloseP(tbl);
       			g.setColor(Color.RED);
			dg.displayGraph(g,tbl);
    		}

	}
	//===============================================================
	public class displayCloseP extends Frame{

		String fileName = "";

   		public displayCloseP(){
			System.out.println("In dg() - 01");
		}

   		public displayCloseP(String x){
			System.out.println("In dg(fn) - 01");

			fileName = x;
     			prepareGUIcp();
   		}

   		private void prepareGUIcp(){
			System.out.println("In pg - 01");

      			setSize(xwidth,yheight);
      			setTitle("Display CloseP");
      			addWindowListener(new WindowAdapter() {
            			public void windowClosing(WindowEvent windowEvent){
               				dispose();
            			}
			});       
      		}

		//==============================================================
		public RSIFileTBL deriveCloseP(RSIFileTBL tbl){

			RSIFileVO vo = new RSIFileVO();

			int i = 0;
			int maxy = 0;
			float closep = 0.0f;
			while (i < tbl.getRepCnt()){
				vo = tbl.getRSITBL(i);
				vo.setRSIValy(vo.getRSIClosep());
				closep = vo.getRSIClosep();
				if (maxy < closep) maxy = Math.round(closep);
				tbl.setRSIMap(vo);
				i++;
			}
			tbl.setMaxY(maxy);
			return tbl;
		}

		//===================================================================
   		@Override
   		public void paint(Graphics g) {
			System.out.println("In paint - 01");

			int avDays = 14;

			RSIFileTBL tbl = new RSIFileTBL();
			displayGraph dg = new displayGraph();
			tbl.setAvDays(avDays);
			tbl.setRSIName(fileName);
			tbl = dg.readFile(tbl);
			tbl = deriveCloseP(tbl);
			dg.displayAxis(g,tbl);
       			g.setColor(Color.YELLOW);
			dg.displayGraph(g,tbl);
    		}

	}

	//===============================================================
	public class displayGraph extends Frame{

		String fileName = "";

   		public displayGraph(){
			System.out.println("In dg() - 01");
		}

   		public displayGraph(String x){
			System.out.println("In dg(fn) - 01");

			RSIFileTBL tbl = new RSIFileTBL();
			fileName = x;
     			prepareGUIdg();
   		}

   		private void prepareGUIdg(){
			System.out.println("In pg - 01");

      			setSize(xwidth,yheight);
      			setTitle("Display Graph");
      			addWindowListener(new WindowAdapter() {
            			public void windowClosing(WindowEvent windowEvent){
               				dispose();
            			}
			});       
      		}

		//============================================================================================
    		public RSIFileTBL readFile(RSIFileTBL tbl){

			System.out.println("rf - start - fn="+tbl.getRSIName());

			RSIFileVO vo = new RSIFileVO();

			try
			{
				String xx = "";
				int siz = 0;
				float maxy = 0.0f;
				String[] voStr = new String[7];

				RandomAccessFile bf = new RandomAccessFile(tbl.getRSIName(), "r");
				while ((xx=bf.readLine())!=null){
					voStr = xx.split(",");
					if (!voStr[0].equals("Date")){
						vo.setRSIValx(siz);
						vo.setRSIDate(voStr[0]);
						vo.setRSIClosep(Float.parseFloat(voStr[4]));
						if (maxy < Float.parseFloat(voStr[4])) maxy = Float.parseFloat(voStr[4]);
						tbl.setRSITBL(vo);
					}
					siz++;
				}
				bf.close();
				if (siz==0){
					xx="99File not loaded";
					System.out.println("readfile - "+xx);
					tbl.setXmlError("readfile - "+xx);
				}

				tbl.setMaxX(siz);
				tbl.setMaxY(maxy);

				System.out.println("rf - end - ele="+siz+" maxy="+maxy);
				return tbl;

			}catch (IOException ioe) {
        			System.out.println ("rf - ioe - " + ioe.getMessage ());
				tbl.setXmlError("IO error rf - " + ioe.getMessage ());
				return tbl;

		 	}catch (Exception exc) {
        			System.out.println ("rf - exc - " + exc.getMessage ());
				tbl.setXmlError("rf - excep - error " + exc.getMessage ());
				return tbl;
			}
	    	}

		//==============================================================
		public RSIFileTBL deriveRSI(RSIFileTBL tbl){

			RSIFileVO vo = new RSIFileVO();

			int i = 0;
			int avDays = tbl.getAvDays();
			float lastClosep = 0.0f;
			float totgain = 0.0f;
			float totloss = 0.0f;
			float lastavgain = 0.0f;
			float lastavloss = 0.0f;
			float rsi = 0.0f;

			while (i < tbl.getRepCnt()){
				vo = tbl.getRSITBL(i);
				if (i > 0){
					if (lastClosep > vo.getRSIClosep()){
						vo.setRSIGain(0.0f);
						vo.setRSILoss(Math.abs(lastClosep - vo.getRSIClosep()));
						vo.setRSIAvgain(0.0f);
						vo.setRSIAvloss(0.0f);
						totloss += vo.getRSILoss();
					}else{
						vo.setRSIGain(vo.getRSIClosep() - lastClosep);
						vo.setRSILoss(0.0f);
						vo.setRSIAvgain(0.0f);
						vo.setRSIAvloss(0.0f);
						totgain += vo.getRSIGain();
					}
				}else{
					vo.setRSIGain(0.0f);
					vo.setRSILoss(0.0f);
					vo.setRSIAvgain(0.0f);
					vo.setRSIAvloss(0.0f);
				}
				if (i == avDays){
					if (totgain > 0.00f && totloss > 0.00f){
						vo.setRSIAvgain(totgain/avDays);
						vo.setRSIAvloss(totloss/avDays);
					}else{
						vo.setRSIAvgain(1.0f);
						vo.setRSIAvloss(1.0f);
					}
					vo.setRSIRelstr(vo.getRSIAvgain() / vo.getRSIAvloss());
					rsi = 100 - (100 / (1 + vo.getRSIRelstr()));
					vo.setRSIValy(rsi);
					tbl.setRSIMap(vo);
					lastavgain = vo.getRSIAvgain();
					lastavloss = vo.getRSIAvloss();

				}else if (i > avDays){
					vo.setRSIAvgain(((lastavgain * (avDays - 1))+vo.getRSIGain())/avDays);
					vo.setRSIAvloss(((lastavloss * (avDays - 1))+vo.getRSILoss())/avDays);
					vo.setRSIRelstr(vo.getRSIAvgain() / vo.getRSIAvloss());
					rsi = 100 - (100 / (1 + vo.getRSIRelstr()));
					vo.setRSIValy(rsi);
					tbl.setRSIMap(vo);
					lastavgain = vo.getRSIAvgain();
					lastavloss = vo.getRSIAvloss();					
				}
				lastClosep = vo.getRSIClosep();


				tbl.updRSITBL(vo,i);
				i++;

				System.out.printf("%8s %5.2f %6.2f %6.2f %6.2f %6.2f %6.2f %6.2f %3s\n",
					vo.getRSIDate(),vo.getRSIClosep(),vo.getRSIGain(),vo.getRSILoss(),
					vo.getRSIAvgain(),vo.getRSIAvloss(),vo.getRSIRelstr(),
					vo.getRSIValy(),i+"");
			}

			//tbl.setMaxX(siz);
			tbl.setMaxY(100);		//max for rsi

			return tbl;

		}

		//==============================================================
		public void displayAxis(Graphics g, RSIFileTBL tbl){

       			g.setColor(Color.GREEN);
     	 		Font font = new Font("Serif", Font.PLAIN, 15);
      			g.setFont(font);
			g.drawLine(basex,100,basex,basey);  
			g.drawLine(basex,basey,xwidth,basey);
			g.drawString("0",basex - 10,basey); 
			int maxy = Math.round(tbl.getMaxY());
			g.drawString(maxy+"",basex - 20,100); 
			System.out.println("da - my="+tbl.getMaxY());

			int xw = gwidth / tbl.getRSICnt();
			int x = basex;
  
			int ygrid = basey - (basey - 100)/10;
			int i = 1;
			while (ygrid > 99){
				if(maxy==100) g.drawString(i+"0",basex - 20,ygrid); 
				g.drawLine(basex,ygrid,basex + 10,ygrid);
				ygrid -= (basey - 100)/10; 
				i++; 
			}
			if (xw > 50){
				while(x < gwidth){
					x += xw;
					g.drawLine(x,basey,x,basey - 10);				
				}
			}
			g.drawString(tbl.getRSIDate(0),basex,basey + 15);
			g.drawString(tbl.getRSIDate(tbl.getRSICnt() - 1),(tbl.getRSICnt() - 1)*xw - 20,basey + 15);

			g.drawString(gFileName, (xwidth - gFileName.length())/2, yheight - 50);
		}

		//==============================================================
		public void displayGraph(Graphics g, RSIFileTBL tbl){

     	 		Font font = new Font("Serif", Font.PLAIN, 15);
      			g.setFont(font);

			int i = 0;
			int xw = gwidth / tbl.getRSICnt();
			int x1 = basex;
			int x2 = basex;
			int y1 = basey - (int)(gheight*(tbl.getRSIMap(i))/tbl.getMaxY());
			int y2 = 0;
			i++;
			while (i < tbl.getRSICnt()){
				x2 += xw;  
				y2 = basey - (int)Math.round(gheight*(tbl.getRSIMap(i))/tbl.getMaxY());
				//System.out.println("dg - x1="+x1+" x2="+x2+" y1="+y1+" y2="+y2+" vy="+tbl.getRSIMap(i));
				g.drawLine(x1,y1,x2,y2); 
				x1 = x2;
				y1 = y2;
				i++;
			}
		}

		//===================================================================
   		@Override
   		public void paint(Graphics g) {
			System.out.println("In paint - 01");

			int avDays = 14;

			RSIFileTBL tbl = new RSIFileTBL();
			tbl.setAvDays(avDays);
			tbl.setRSIName(fileName);
			tbl = readFile(tbl);
			tbl = deriveRSI(tbl);
			displayAxis(g,tbl);
       			g.setColor(Color.YELLOW);
			displayGraph(g,tbl);
    		}
	}

	//=============================================================
	public class RSIFileVO{

		private String rsidate;
		private int valx;
		private float valy;
		private float closep;
		private float gain;
		private float loss;
		private float avgain;
		private float avloss;
		private float relStrgth;

		public void setRSIDate(String x){this.rsidate = x;}
		public void setRSIValx(int x){this.valx = x;}
		public void setRSIValy(float x){this.valy = x;}
		public void setRSIClosep(float x){this.closep = x;}
		public void setRSIGain(float x){this.gain = x;}
		public void setRSILoss(float x){this.loss = x;}
		public void setRSIAvgain(float x){this.avgain = x;}
		public void setRSIAvloss(float x){this.avloss = x;}
		public void setRSIRelstr(float x){this.relStrgth = x;}

		public String getRSIDate(){return this.rsidate;}
		public int getRSIValx(){return this.valx;}
		public float getRSIValy(){return this.valy;}
		public float getRSIClosep(){return this.closep;}
		public float getRSIGain(){return this.gain;}
		public float getRSILoss(){return this.loss;}
		public float getRSIAvgain(){return this.avgain;}
		public float getRSIAvloss(){return this.avloss;}
		public float getRSIRelstr(){return this.relStrgth;}
	}

	//================================================================
	public class RSIFileTBL{

    		private Map<Integer,Object> newreplst = new HashMap<Integer,Object>(); 

    		private Map<Integer,Integer> valx = new HashMap<Integer,Integer>(); 
    		private Map<Integer,Float> valy = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> closep = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> gain = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> loss = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> avgain = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> avloss = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> relStrgth = new HashMap<Integer,Float>(); 
    		private Map<Integer,Float> rsi = new HashMap<Integer,Float>(); 
    		private Map<Integer,String> rsidate = new HashMap<Integer,String>(); 

   		private int repCnt;
		private String rsiName;
		private String xmlError;
		private int maxx;
		private float maxy;
		private float toty;
		private int avDays;
   		private int rsiCnt = rsi.size();

		RSIFileVO vo = new RSIFileVO();

		public RSIFileTBL clearTBL(RSIFileTBL tbl1){
			rsi.clear();
			rsidate.clear();
			closep.clear();
			valx.clear();
			valy.clear();
			gain.clear();
			loss.clear();
			avgain.clear();
			avloss.clear();
			relStrgth.clear();;
       			newreplst.clear();
			System.out.println("ct - rsisz="+rsi.size());
			return tbl1;
  		}

    		public void setRSIMap(RSIFileVO rep){
			Integer cnt = rsi.size();
			//System.out.println("srm - rsisz="+rsi.size());
			rsi.put(cnt,rep.getRSIValy());
			rsidate.put(cnt,rep.getRSIDate());
			this.rsiCnt = rsi.size();
		}

    		public float getRSIMap(int x){
			Integer cnt = (Integer) x;
			return rsi.get(cnt);
		}
    		public String getRSIDate(int x){
			Integer cnt = (Integer) x;
			return rsidate.get(cnt);
		}

    		public int getRSICnt(){
			return this.rsiCnt;
		}

    		public void setRSITBL(RSIFileVO rep){
			Integer cnt = newreplst.size();
			rsidate.put(cnt,rep.getRSIDate());
			closep.put(cnt,rep.getRSIClosep());
			valx.put(cnt,rep.getRSIValx());
			valy.put(cnt,rep.getRSIValy());
			gain.put(cnt,rep.getRSIGain());
			loss.put(cnt,rep.getRSILoss());
			avgain.put(cnt,rep.getRSIAvgain());
			avloss.put(cnt,rep.getRSIAvloss());
			relStrgth.put(cnt,rep.getRSIRelstr());
       			newreplst.put(cnt, rep);
    		}
    		public void updRSITBL(RSIFileVO rep,int cnt1){
			Integer cnt = (Integer) cnt1;
			rsidate.put(cnt,rep.getRSIDate());
			closep.put(cnt,rep.getRSIClosep());
			valx.put(cnt,rep.getRSIValx());
			valy.put(cnt,rep.getRSIValy());
			gain.put(cnt,rep.getRSIGain());
			loss.put(cnt,rep.getRSILoss());
			avgain.put(cnt,rep.getRSIAvgain());
			avloss.put(cnt,rep.getRSIAvloss());
			relStrgth.put(cnt,rep.getRSIRelstr());
       			newreplst.put(cnt, rep);
    		}

    		public RSIFileVO getRSITBL(int cnt){
			Integer cnt1 = (Integer) cnt;
			vo = (RSIFileVO)newreplst.get(cnt1);
			vo.setRSIDate(rsidate.get(cnt1));
			vo.setRSIValx(valx.get(cnt1));
			vo.setRSIValy(valy.get(cnt1));
			vo.setRSIClosep(closep.get(cnt1));
			vo.setRSIGain(gain.get(cnt1));
			vo.setRSILoss(loss.get(cnt1));
			vo.setRSIAvgain(avgain.get(cnt1));
			vo.setRSIAvloss(avloss.get(cnt1));
			vo.setRSIRelstr(relStrgth.get(cnt1));
        		return vo;
    		}

    		public int getRepCnt(){return this.newreplst.size();}

		public void setRSIName(String x){this.rsiName = x;}
		public String getRSIName(){return this.rsiName;}

		public void setXmlError(String x){this.xmlError = x;}
		public String getXmlError(){return this.xmlError;}

    		public int getMaxX(){return this.maxx;}
		public void setMaxX(int x){this.maxx = x;}

    		public int getAvDays(){return this.avDays;}
		public void setAvDays(int x){this.avDays = x;}

    		public float getMaxY(){return this.maxy;}
		public void setMaxY(float x){this.maxy = x;}

    		public float getTotY(){return this.toty;}
		public void setTotY(float x){this.toty = x;}

	}
   
}
