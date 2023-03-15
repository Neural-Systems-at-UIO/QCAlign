//!!gridspacing
package qcontrol;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import data.Palette;
import data.SegLabel;
import data.Series;
import data.Slice;
import data.TreeLabel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import nonlin.Triangle;
import parsers.ITKLabel;
import parsers.JSON;
import slicer.Int32Slices;
import static data.Marker.marker;

public class QCController implements ChangeListener<Number>, EventHandler<TreeModificationEvent<String>> {
    @FXML
    private VBox vbox;

    @FXML
    private Pane pane;

    @FXML
    private Canvas imgcnv;

    @FXML
    private Canvas ovlycnv;

    @FXML
    private Canvas lblcnv;
    
    @FXML
    private Canvas gridcnv;

    @FXML
    private Slider opacitySlider;

    @FXML
    private ColorPicker outColor;
    
    @FXML
    private ChoiceBox<String> gridType;

    @FXML
    private ColorPicker gridColor;

    @FXML
    private Slider gridAlpha;
    
    @FXML
    private Spinner<Integer> spn;
    
    @FXML
    private IntegerSpinnerValueFactory spnVal;
    
    @FXML
    private RadioMenuItem lockHier;
    
    @FXML
    private RadioMenuItem overview;
    
    @FXML
    private void overview(ActionEvent event) {
    	drawGrid();
    }
    
//    @FXML
//    private RadioMenuItem keepMarkers;
    
//    @FXML
//    private Spinner<Integer> nmSpn;
//    
//    @FXML
//    private IntegerSpinnerValueFactory nmSpnVal;
//
//    @FXML
//    private Spinner<Integer> umSpn;
//    
//    @FXML
//    private IntegerSpinnerValueFactory umSpnVal;
    
    @FXML
    private Spinner<Integer> voxSpn;
    
    @FXML
    private IntegerSpinnerValueFactory voxSpnVal;
    
    @FXML
    private TreeView<String> tree;
    
    @FXML
    private Label prg;

    @FXML
    void onClick(MouseEvent event) {
        System.out.println(event);
    }
    
    void updateProgress() {
    	if(series==null || series.slices.isEmpty()) {
    		prg.setText("");
    		return;
    	}
    	int cnt=0;
    	for(Slice s:series.slices)
    		if(!s.clear)
    			cnt++;
    	prg.setText(cnt+" of "+series.slices.size()+", "+(slice.clear?"Clear":"Changed"));
    }

    int pick = -1;
    List<Double> picked;
    double pickx;
    double picky;

    void updatePick() {
        if(slice==null)return;
        List<ArrayList<Double>> markers=slice.markers;
        pick = -1;
        picked = null;
        pickx = (mouseX - imgx) * slice.width / imgw;
        picky = (mouseY - imgy) * slice.height / imgh;
        double margin = slice.width * 10 / imgw;
        for (int i = markers.size() - 1; i >= 0; i--) {
            List<Double> m = markers.get(i);
            if (Math.abs(pickx - m.get(2)) < margin && Math.abs(picky - m.get(3)) < margin) {
                pick = i;
                picked = m;
                break;
            }
        }
    }

    double mouseX;
    double mouseY;
    boolean popbottom=true;
//    int pickx,picky;

    @FXML
    void mouseMoved(MouseEvent event) {
        ovlycnv.requestFocus();
        mouseX = event.getX();
        mouseY = event.getY();
        drawPop();
        
        if(slice!=null) {
            pickx = (mouseX - imgx) * slice.width / imgw;
            pickx = Math.round((pickx - slice.gridx)/slice.xspacing);
            picky = (mouseY - imgy) * slice.height / imgh;
            picky = Math.round((picky - slice.gridy)/slice.yspacing);
        }
    }
    
    private void drawPop() {
        int h=(int)lblcnv.getHeight();
        int w=(int)lblcnv.getWidth();
        if(popbottom && mouseY>h*0.8)
            popbottom=false;
        else if(!popbottom && mouseY<h*0.2)
            popbottom=true;
        GraphicsContext gc=lblcnv.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        int mx = (int)mouseX - imgx;
        int my = (int)mouseY - imgy;
        
        if(mx>=0 && my>=0 && mx<imgw && my<imgh) {
            SegLabel l=null;
            List<Triangle> triangles=slice.triangles;
            double fx = mx * slice.width / imgw;
            double fy = my * slice.height / imgh;
            for (int i = 0; i < triangles.size(); i++) {
                double t[] = triangles.get(i).transform(fx, fy);
                if (t != null) {
                    int xx = (int) (t[0] * overlay_width / slice.width);
                    int yy = (int) (t[1] * overlay_height / slice.height);
                    if(xx>=0 && yy>=0 && xx<overlay_width && yy<overlay_height)
                        l=palette.fullmap.get(raw_overlay[yy][xx]);//!!
                }
            }
//            gc.strokeText(""+nlovly[mx+my*imgw], 100, 100);
            if(l!=null && l.index>0) {
                gc.setFill(Color.rgb(l.red, l.green, l.blue));
                gc.fillRect(0, popbottom?h-50:0, w, 50);
                gc.setFill(Color.BLACK);
                gc.setFont(Font.font("System",FontWeight.BOLD,30));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.fillText(l.name, w/2, popbottom?h-25:25);
            }
        }
    }

//    double basex, basey;

    @FXML
    void mousePressed(MouseEvent event) {
    	if(noGrid())
    		return;
//        updatePick();
//        if (picked != null) {
//            basex = picked.get(2);
//            basey = picked.get(3);
//        }
//xx        int gridspacing=(int)series.gridspacing;
        int xp=(int)(slice.width-slice.gridx+slice.xspacing-1)/slice.xspacing;
        int yp=(int)(slice.height-slice.gridy+slice.yspacing-1)/slice.yspacing;
        if(pickx>=0 && pickx<xp && picky>=0 && picky<yp) {
        	slice.clear=false;
        	lockHier.setSelected(true);
        	lockHier(null);
        	updateProgress();
            int i=(int)(pickx+picky*xp);
            slice.grid.set(i, (slice.grid.get(i)+1)%statuses.length);
        }
        drawGrid();
    }

    @FXML
    void mouseDragged(MouseEvent event) {
//        mouseMoved(event);
//        if (picked != null) {
//            picked.set(2, basex + (mouseX - imgx) * slice.width / imgw - pickx);
//            picked.set(3, basey + (mouseY - imgy) * slice.height / imgh - picky);
//            slice.triangulate();
//            reDraw();
//        }
    }

    @FXML
	void keyPressed(KeyEvent event) {
		if (slice == null)
			return;
		double code=0;
		switch (event.getCode()) {
		case SPACE:
			overview.setSelected(!overview.isSelected());
			drawGrid();
			break;
//		case LEFT:
//			spnVal.decrement(1);
//			break;
//		case RIGHT:
//			spnVal.increment(1);
//			break;
		case DIGIT5:
			code++;
		case DIGIT4:
			code++;
		case DIGIT3:
			code++;
		case DIGIT2:
			code++;
		case DIGIT1:{
	        int xp=(int)(slice.width-slice.gridx+slice.xspacing-1)/slice.xspacing;
	        int yp=(int)(slice.height-slice.gridy+slice.yspacing-1)/slice.yspacing;
	        if(pickx>=0 && pickx<xp && picky>=0 && picky<yp) {
	        	slice.clear=false;
	        	lockHier.setSelected(true);
	        	lockHier(null);
	        	updateProgress();
	            int i=(int)(pickx+picky*xp);
	            slice.grid.set(i, code);
	        }
	        drawGrid();
			break;
		}
		default:
		}
//        List<ArrayList<Double>> markers=slice.markers;
//        List<Triangle> triangles=slice.triangles;
//        updatePick();
//        switch(event.getCode()) {
//            case LEFT:
//                spnVal.decrement(1);
//                break;
//            case RIGHT:
//                spnVal.increment(1);
//                break;
//            case SPACE:
//                if (pick >= 0) return;
//                double mx = (mouseX - imgx) * slice.width / imgw;
//                double my = (mouseY - imgy) * slice.height / imgh;
//                for (int i = 0; i < triangles.size(); i++) {
//                    double xy[] = triangles.get(i).transform(mx, my);
//                    if (xy != null) {
//                        markers.add(marker(xy[0], xy[1], mx, my));
//                        break;
//                    }
//                }
//                break;
//            case BACK_SPACE:
//            case DELETE:
//                if (pick >= 0)
//                    markers.remove(pick);
//                break;
//            default:
//        }
//        slice.triangulate();
//        reDraw();
    }

    @FXML
    void initialize() throws Exception {
        spn.getStyleClass().clear(); //!!
//        nmSpn.getStyleClass().clear(); //!!
//        umSpn.getStyleClass().clear(); //!!
        voxSpn.getStyleClass().clear(); //!!
//        spn.getStyleableNode().setStyle("-fx-text-alignment: center;");
        
//        markers.add(new Marker(0, 0));
//        markers.add(new Marker(slice.width, 0));
//        markers.add(new Marker(0, slice.height));
//        markers.add(new Marker(slice.width, slice.height));
//        triangulate();

//        pane.setFocusTraversable(true);
//        imgcnv.setFocusTraversable(true);
//        ovlycnv.setFocusTraversable(true);
        pane.widthProperty().addListener(this);
        pane.heightProperty().addListener(this);
        gridColor.setValue(Color.BLACK);
        outColor.setValue(Color.DARKBLUE);
/*        debColor.setValue(Color.GREY);*/
//        palette = new ArrayList<SegLabel>();
//        try (FileReader fr = new FileReader("Segmentation.json")) {
//            JSON.mapList(JSON.parse(fr), palette, SegLabel.class, null);
//        }
//        fastpalette=new int[palette.size()];
//        for(int i=0;i<fastpalette.length;i++) {
//            SegLabel l=palette.get(i);
//            fastpalette[i]=((int)l.red << 16)+((int)l.green << 8)+(int)l.blue;
//        }
//        try (DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream("R601_s172_BDA_NR_10x-Segmentation.flat")))){
//            byte bpp=dis.readByte();
//            if(bpp!=1)throw new Exception("BPP: "+bpp);
//            int ovlyw=dis.readInt();
//            int ovlyh=dis.readInt();
//            overlay=new int[ovlyh][ovlyw];
//            for(int y=0;y<ovlyh;y++)
//                for(int x=0;x<ovlyw;x++)
//                    overlay[y][x]=dis.readByte();
//        }
//        image = new Image("file:R601_s172_BDA_NR_10x.png");
//        
        opacitySlider.valueProperty().addListener(this);
        gridAlpha.valueProperty().addListener(this);
        spnVal.valueProperty().addListener(this);
    }

    Series series;
    Slice slice;
//    int xspacing;
//    int yspacing;
    void setSlice(Slice slice) {
    	this.slice=slice;
    	if(slice!=null) {
    		updateProgress();
    		slice.updateSpacing(series.gridspacing);
    	}
//        	if(slice.anchoring.size()!=9)System.out.println("non-9 in calcSpacing");
//        	xspacing=xSpacing(slice);
//        	yspacing=ySpacing(slice);
//    	} else xspacing=yspacing=0;
    }
    
//    int xSpacing(Slice slice) {
//    	if(slice.anchoring.size()!=9)System.out.println("non-9 in xSpacing");
//    	double ow=0;
//    	for(int i=0;i<3;i++) {
//    		ow+=slice.anchoring.get(i+3)*slice.anchoring.get(i+3);
//    	}
//    	ow=Math.sqrt(ow);
//    	return (int)(slice.width*series.gridspacing/ow);
//    }
//    int ySpacing(Slice slice) {
//    	if(slice.anchoring.size()!=9)System.out.println("non-9 in ySpacing");
//    	double oh=0;
//    	for(int i=0;i<3;i++) {
//    		oh+=slice.anchoring.get(i+6)*slice.anchoring.get(i+6);
//    	}
//    	oh=Math.sqrt(oh);
//    	return (int)(slice.height*series.gridspacing/oh);
//    }
//    boolean checkGrid(Slice slice,int xspacing,int yspacing) {
//    	return slice.grid.size()==((int)(slice.width-slice.gridx+xspacing-1)/xspacing)*((int)(slice.height-slice.gridy+yspacing-1)/yspacing);
//    }
    
//    void removeThisCalcSpacing() {
//    	if(slice.anchoring.size()!=9)System.out.println("non-9 in calcSpacing");
//    	xspacing=xSpacing(slice);
//    	yspacing=ySpacing(slice);
//    	
////    	if(!checkGrid(slice, xspacing, yspacing))
////    		makeGrid(slice, xspacing, yspacing);
//    }
    
    @Override
    public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
        if(arg0==spnVal.valueProperty()) {
            loadView();
            return;
        }
        if(arg0==gridAlpha.valueProperty()) {
            drawGrid();
            return;
        }
        if (arg0 == pane.widthProperty() || arg0 == pane.heightProperty())
            drawImage();
        reDraw();
    }
    
//    @FXML
//    public void reGridTest(ObservableValue<Number> arg0, Number arg1, Number arg2) {
//        System.out.println("reGridTest!");
//        System.out.printf("Before: %d, after: %d\n",arg1+" "+arg2);
//    }
    
    boolean skiphack=false;
    @FXML
//    public void gridSpacing(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
    public void gridSpacing() {
        if(series==null || skiphack)
            return;
        series.gridspacing=voxSpnVal.getValue();
    	resetSeries();
    }

	private void resetSeries() {
		for(var slice:series.slices)
    		resetSection(slice);
    	updateProgress();
        reDraw();
	}
    
    private Image image;
    int imgx, imgy, imgw, imgh;
    int rgb[];
    int nlovly[];

    int raw_overlay[][];
    int overlay_width;
    int overlay_height;
    int fastoverlay[];
    
    public void drawImage() {
        GraphicsContext ctx = imgcnv.getGraphicsContext2D();
        int cw = (int) imgcnv.getWidth();
        int ch = (int) imgcnv.getHeight();
        ctx.clearRect(0, 0, cw, ch);
        
        if (slice == null)
            return; // !!
        
        int iw = (int) image.getWidth();
        int ih = (int) image.getHeight();

        imgx = imgy = 0;
        if (cw * ih < ch * iw) {
            imgh = ih * cw / iw;
            imgw = cw;
            imgy = (ch - imgh) / 2;
        } else {
            imgw = iw * ch / ih;
            imgh = ch;
            imgx = (cw - imgw) / 2;
        }
        
        rgb=new int[imgw*imgh];
        nlovly=new int[imgw*imgh];

        ctx.drawImage(image, imgx, imgy, imgw, imgh);
    }

    /*
     * Screen-space x: 0...imgx-1 y: 0...imgy-1
     */
    public int sample(int x, int y) {
        List<Triangle> triangles=slice.triangles;
        double fx = x * slice.width / imgw;
        double fy = y * slice.height / imgh;
        for (int i = 0; i < triangles.size(); i++) {
            double t[] = triangles.get(i).transform(fx, fy);
            if (t != null) {
                int xx = (int) (t[0] * overlay_width / slice.width);
                int yy = (int) (t[1] * overlay_height / slice.height);
                if(xx<0 || yy<0 || xx>=overlay_width || yy>=overlay_height)
                    return 0;
                //return overlay[yy][xx];
                return fastoverlay[xx+yy*overlay_width];
            }
        }
        return 0;// 0x80000000;
    }

    public void reDraw() {
        if(slice==null)return;//!!
        drawOvly();
//        if (debug.isSelected())
//            drawDebug();
        drawGrid();
        drawPop();
    }

    public void drawOvly() {
        Arrays.fill(rgb, 0);
        IntStream.range(0, imgh).parallel().forEach(y -> {
            for (int x = 0; x < imgw; x++)
                nlovly[x+y*imgw]= sample(x, y);
        });
        
        int op = (int) opacitySlider.getValue() * 255 / 100;
        if (op < 255) {
            int a = op << 24;
            IntStream.range(0, imgh).parallel().forEach(y -> {
                for (int x = 0; x < imgw; x++) {
                    int o = nlovly[x+y*imgw];
                    if(o!=0)
                        rgb[x+y*imgw]=a+palette.fastcolors[o];
                }
            });
        } else {
            Color c = outColor.getValue();
            int a = 0xFF000000 + ((int) (c.getRed() * 255) << 16) + ((int) (c.getGreen() * 255) << 8)
                    + (int) (c.getBlue() * 255);
//            IntStream.range(1, imgh-1).parallel().forEach(y -> {
            for(int y=1; y<imgh-1;y++)
                for (int x = 1; x < imgw-1; x++) {
                    int o=nlovly[x+y*imgw];
                    if(nlovly[x+y*imgw-1] != o
                    || nlovly[x+y*imgw+1] != o
                    || nlovly[x+y*imgw-imgw] != o
                    || nlovly[x+y*imgw+imgw] != o)
                    rgb[x+y*imgw]= a;
                }
//            });
        }
        GraphicsContext ctx = ovlycnv.getGraphicsContext2D();
        ctx.clearRect(0, 0, imgcnv.getWidth(), imgcnv.getHeight());
        PixelWriter pw = ctx.getPixelWriter();
        pw.setPixels(imgx,imgy,imgw,imgh,PixelFormat.getIntArgbInstance(),rgb,0,imgw);
    }

//    private void drawPins() {
//        GraphicsContext ctx = ovlycnv.getGraphicsContext2D();
//        ctx.setStroke(pinColor.getValue());
//        ctx.setLineWidth(3);
//        List<ArrayList<Double>> markers=slice.markers;
//        for (int i = 0; i < markers.size(); i++) {
//            List<Double> m = markers.get(i);
//            double nx = m.get(2) * imgw / slice.width + imgx;
//            double ny = m.get(3) * imgh / slice.height + imgy;
//            ctx.strokeLine(nx, ny - 10, nx, ny + 10);
//            ctx.strokeLine(nx - 10, ny, nx + 10, ny);
//            double ox = m.get(0) * imgw / slice.width + imgx;
//            double oy = m.get(1) * imgh / slice.height + imgy;
//            ctx.strokeLine(ox, oy, nx, ny);
//        }
//    }

//    int gridspacing=0;
//    void setSpacing() {
////        gridspacing=0;
////        if(series.gridmicrons!=0)
////        	gridspacing=(int)(series.gridmicrons*1000/series.pixelnanos);
//    	gridspacing=(int)series.gridspacing;
//    }
    boolean noGrid() {
    	return slice==null;// || gridspacing==0;
    }
//    private void removeThisMakeGrid(Slice slice,int xspacing,int yspacing) {
//    	if(slice.anchoring.size()!=9)System.out.println("non-9 in setGrid");
//        slice.gridx=rnd.nextInt(xspacing);
//        slice.gridy=rnd.nextInt(yspacing);
//        int xp=(int)(slice.width-slice.gridx+xspacing-1)/xspacing;
//        int yp=(int)(slice.height-slice.gridy+yspacing-1)/yspacing;
////        System.out.printf("%dx%d, %f %f\n",xp,yp,slice.gridx,slice.gridy);
////        System.out.println(slice);
//        slice.grid=new ArrayList<>(xp*yp);
////        for(int i=0;i<xp*yp;i++)
////            slice.grid.add((double)rnd.nextInt(3));
////            slice.grid.add((double)0);
////        slice.grid.set(slice.grid.size()-1, 3.);
//        
//    	slice.triangulate(); //!!
//        Double ouv[]=slice.anchoring.toArray(new Double[0]);
//        int[][] raw_overlay=slicer.getInt32Slice(ouv[0], ouv[1], ouv[2], ouv[3], ouv[4], ouv[5], ouv[6], ouv[7], ouv[8], false);
//        for(int line[]:raw_overlay)
//        	for(int x=0;x<line.length;x++)
//        		line[x]=simplify.get(line[x]);
//        
//        int gridx=(int)slice.gridx;
//        int gridy=(int)slice.gridy;
//
//        for(int y=0;y<yp;y++)
//        	for(int x=0;x<xp;x++) {
//        		if(x==0 || y==0 || x==xp-1 || y==yp-1)
//        			slice.grid.add(0.);
//        		else {
//        			int w=sample_raw(gridx+x*xspacing, gridy+y*yspacing, slice, raw_overlay);
//        			if(w!=0 &&
//        					w==sample_raw(gridx+(x-1)*xspacing, gridy+y*yspacing, slice, raw_overlay) &&
//        					w==sample_raw(gridx+(x+1)*xspacing, gridy+y*yspacing, slice, raw_overlay) &&
//        					w==sample_raw(gridx+x*xspacing, gridy+(y-1)*yspacing, slice, raw_overlay) &&
//        					w==sample_raw(gridx+x*xspacing, gridy+(y+1)*yspacing, slice, raw_overlay))
//        				slice.grid.add(1.);
//        			else
//        				slice.grid.add(0.);
//        		}
//        	}
//    }

//    boolean override;
    private void drawGrid() {
        GraphicsContext ctx = gridcnv.getGraphicsContext2D();
        ctx.clearRect(0, 0, gridcnv.getWidth(), gridcnv.getHeight());
    	if(noGrid())
    		return;
//        if(gridspacing>0) {
            if(!overview.isSelected()) {
                int gridx=(int)slice.gridx;
                int gridy=(int)slice.gridy;
                ctx.setStroke(gridColor.getValue());
                ctx.setGlobalAlpha(gridAlpha.getValue()/100);
                ctx.setLineWidth(2);
                switch(gridType.getValue()) {
                    case "Grid":
                        for(int x=gridx;x<slice.width;x+=slice.xspacing) {
                            double dx=x*imgw/slice.width;
                            ctx.strokeLine(imgx+dx, imgy, imgx+dx, imgy+imgh);
                        }
                        for(int y=gridy;y<slice.height;y+=slice.yspacing) {
                            double dy=y*imgh/slice.height;
                            ctx.strokeLine(imgx, imgy+dy, imgx+imgw, imgy+dy);
                        }
                        break;
                    case "Dots":
                        for(int x=gridx;x<slice.width;x+=slice.xspacing)
                            for(int y=gridy;y<slice.height;y+=slice.yspacing)
                                ctx.strokeRect(imgx+x*imgw/slice.width, imgy+y*imgh/slice.height, 1, 1);
                        break;
                    case "Circles":
                        for(int x=gridx;x<slice.width;x+=slice.xspacing)
                            for(int y=gridy;y<slice.height;y+=slice.yspacing)
                                ctx.strokeOval(imgx+x*imgw/slice.width-2, imgy+y*imgh/slice.height-2, 4, 4);
                        break;
                }
            }
            int xp=(int)(slice.width-slice.gridx+slice.xspacing-1)/slice.xspacing;
            int yp=(int)(slice.height-slice.gridy+slice.yspacing-1)/slice.yspacing;
            ctx.setTextAlign(TextAlignment.LEFT);
            ctx.setTextBaseline(VPos.BOTTOM);
            for(int x=0;x<xp;x++)
                for(int y=0;y<yp;y++) {
                    int res=(int)slice.grid.get(x+y*xp).doubleValue();
                    if(!overview.isSelected()) {
                        double xbase=imgx+(slice.gridx+x*slice.xspacing)*imgw/slice.width+3;
                        double ybase=imgy+(slice.gridy+y*slice.yspacing)*imgh/slice.height-2;
                        switch(res) {
                            case 0:
//                                ctx.setStroke(Color.BLACK);
//                                ctx.strokeText("+", xbase,ybase);
                                break;
                            case 1:
                                ctx.setStroke(Color.GREEN);
                                ctx.strokeText("+", xbase,ybase);
                                break;
                            case 2:
                                ctx.setStroke(Color.RED);
                                ctx.strokeText("-", xbase,ybase);
                                break;
                            case 3:
                                ctx.setStroke(Color.BLACK);
                                ctx.strokeText("?", xbase,ybase);
                                break;
                            case 4:
                            	ctx.setStroke(Color.BLUE);
                            	ctx.strokeText("x", xbase,ybase);
                            	break;
                        }
                    } else {
                        double xbase=imgx+(slice.gridx+(x-0.5)*slice.xspacing)*imgw/slice.width;
                        double ybase=imgy+(slice.gridy+(y-0.5)*slice.yspacing)*imgh/slice.height;
                        switch(res) {
                            case 1:
                                ctx.setFill(Color.GREEN);
                                break;
                            case 2:
                                ctx.setFill(Color.RED);
                                break;
                            case 3:
                                ctx.setFill(Color.BLACK);
                                break;
                            case 4:
                            	ctx.setFill(Color.BLUE);
                            	break;
                        }
                        if(res!=0)
                            ctx.fillRect(xbase, ybase, slice.xspacing*imgw/slice.width, slice.yspacing*imgh/slice.height);
                    }
                }
//        }
    }

//    private void drawDebug() {
//        GraphicsContext ctx = ovlycnv.getGraphicsContext2D();
//        ctx.setStroke(debColor.getValue());
//        ctx.setLineWidth(1);
//        List<Triangle> triangles=slice.triangles;
//        for (int i = 0; i < triangles.size(); i++) {
//            Triangle t = triangles.get(i);
//            double ax = imgx + imgw * t.A.get(2) / slice.width;
//            double ay = imgy + imgh * t.A.get(3) / slice.height;
//            double bx = imgx + imgw * t.B.get(2) / slice.width;
//            double by = imgy + imgh * t.B.get(3) / slice.height;
//            double cx = imgx + imgw * t.C.get(2) / slice.width;
//            double cy = imgy + imgh * t.C.get(3) / slice.height;
//            ctx.strokeLine(ax, ay, bx, by);
//            ctx.strokeLine(bx, by, cx, cy);
//            ctx.strokeLine(cx, cy, ax, ay);
//        }
//    }

//    @FXML
//    void debug(ActionEvent event) {
//        reDraw();
//    }
    
    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }
    
    String current;
    Palette palette;
    Int32Slices slicer;
    Path baseFolder;
    String filename;
    List<TreeLabel> jsontree;
    TreeItem<String> root;
    Map<Integer,TreeItem<String>> idmap;
    Map<Integer,TreeLabel> flatmap;

    @FXML
    void open(ActionEvent event) throws Exception {
        Preferences prefs=Preferences.userRoot().node("/no/uio/nesys/qcontrol");
        File f=new File(prefs.get("lastDir", "/"));
        FileChooser fc = new FileChooser();
        if(f.exists())
            fc.setInitialDirectory(f);
        fc.setTitle("Pick JSON file");
        ExtensionFilter ef=new ExtensionFilter("QuickNII JSON files", "*.json");
        fc.getExtensionFilters().add(ef);
        //fc.setSelectedExtensionFilter(ef);
        /*File*/ f = fc.showOpenDialog(stage);
        if (f != null) {
            // !! unload
            baseFolder=f.getParentFile().toPath();
            prefs.put("lastDir", baseFolder.toString());
            filename=f.getName();
            filename=filename.substring(0, filename.length()-5);
            series = new Series();
            try (FileReader fr = new FileReader(f)) {
                Map<String, String> resolver = new HashMap<>();
                resolver.put("resolution", "target-resolution");
                JSON.mapObject(JSON.parse(fr), series, resolver);
            }
            if(series.slices.size()<1) {
                series=null;
                Alert a=new Alert(AlertType.ERROR, "This JSON file was not generated by QuickNII.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            if(series.version10>version10) {
            	Alert a=new Alert(AlertType.WARNING, String.format(Locale.ENGLISH,"This JSON file was created with QCAlign v%.1f.\nPlease update your copy (which is %s)",series.version10/10,title), ButtonType.OK);
            	a.showAndWait();
            }
            int cnt=0;
            for(Slice s:series.slices)
            	if(!s.clear)
            		cnt++;
            lockHier.setSelected(cnt>0);
            lockHier(null);
            series.propagate();
            if(series.target==null) {
                DirectoryChooser dc=new DirectoryChooser();
                dc.setTitle("Old series data, please select an atlas folder from QControl");
                dc.setInitialDirectory(new File(System.getProperty("java.home")));
                File d=dc.showDialog(stage);
                if(d==null)return;
                series.target=d.getName();
            }
            if(current==null || !current.equals(series.target)) {
                current=series.target;
                palette=ITKLabel.parseLabels(current+File.separator+"labels.txt");
                slicer=new Int32Slices(current+File.separator+"labels.nii.gz",true);
            }
            try(FileReader fr=new FileReader(current+File.separator+"tree.json")){
            	simplify=new HashMap<>();
            	simplify.put(0,0);
            	idmap=new HashMap<>();
            	flatmap=new LinkedHashMap<>();
                jsontree=new ArrayList<>();
                JSON.mapList(JSON.parse(fr), jsontree, TreeLabel.class, null);
                root = new TreeItem<String>("Root Node");
                for(TreeLabel tl:jsontree)
                	root.getChildren().add(buildTree(tl));
                for(double d:series.collapsed)
                	idmap.get(Integer.valueOf((int)d)).setExpanded(false);
            	root.addEventHandler(TreeItem.branchCollapsedEvent(),this);
            	root.addEventHandler(TreeItem.branchExpandedEvent(),this);
            	supress=true;
                tree.setRoot(root);
                supress=false;
                domappings();
            }
            if(series.resolution.size()==0) {
                series.resolution.add((double)slicer.XDIM);
                series.resolution.add((double)slicer.YDIM);
                series.resolution.add((double)slicer.ZDIM);
            }
//            System.out.println(series.pixelnanos);
//            System.out.println(series.gridmicrons);
            skiphack=true;
//            nmSpnVal.setValue((int)series.pixelnanos);
//            umSpnVal.setValue((int)series.gridmicrons);
            voxSpnVal.setValue((int)series.gridspacing);
            skiphack=false;
            spnVal.setMin(1);
            spnVal.setMax(series.slices.size());
            spnVal.setValue(spnVal.getMax()/2);
            loadView();
        }
    }
    
    boolean supress;
	@Override
	public void handle(TreeModificationEvent<String> tme) {
		if(!supress) {
//			System.out.println(tme.getTreeItem());
			supress=true;
			if(tme.getEventType()==TreeItem.branchCollapsedEvent())
				collapse(tme.getTreeItem());
			supress=false;
			domappings();
			loadView();
		}
	}
	
	private void collapse(TreeItem<String> ti) {
		ti.setExpanded(false);
		for(TreeItem<String> n: ti.getChildren())
			collapse(n);
	}
	
    private TreeItem<String> buildTree(TreeLabel l){
    	TreeItem<String> node=new TreeItem<String>(l.name);
//    	Integer i=(int)l.id;
//    	simplify.put(i,i);
    	idmap.put((int)l.id,node);
    	flatmap.put((int)l.id, l);
    	node.setExpanded(true);
//    	node.addEventHandler(TreeItem.branchCollapsedEvent(),this);
//    	node.addEventHandler(TreeItem.branchExpandedEvent(),this);
    	for(TreeLabel tl: l.children)
    		node.getChildren().add(buildTree(tl));
    	return node;
    }
    
    void domappings() {
    	for(TreeLabel tl:jsontree)
    		refreshmapping(tl, 0);
    }
    void refreshmapping(TreeLabel tl,int override) {
    	Integer i=(int)tl.id;
    	if(override==0) {
    		simplify.put(i,i);
    		TreeItem<String> ti=idmap.get(i);
    		if(!ti.isExpanded())
    			override=i;
    	} else simplify.put(i,override);
    	for(TreeLabel child:tl.children)
    		refreshmapping(child,override);
    }
    
    @FXML
    void lockHier(ActionEvent event) {
    	tree.setDisable(lockHier.isSelected());
    }
    
    @FXML
    void expandHier(ActionEvent event) {
    	supress=true;
    	for(var item:idmap.values())
    		item.setExpanded(true);
    	supress=false;
    	lockHier.setSelected(false);
    	lockHier(null);
    	domappings();
    	loadView();
    }
    
    Map<Integer, Integer> simplify;
    void loadView() {
        if(series==null)
            return; //!!
        setSlice(series.slices.get(spnVal.getValue()-1));
        image=new Image("file:"+baseFolder.resolve(slice.filename));
        setTitle(slice.filename);
        slice.triangulate();
        drawImage();
        Double ouv[]=slice.anchoring.toArray(new Double[0]);
        raw_overlay=slicer.getInt32Slice(ouv[0], ouv[1], ouv[2], ouv[3], ouv[4], ouv[5], ouv[6], ouv[7], ouv[8], false);
        overlay_width=raw_overlay[0].length;
        overlay_height=raw_overlay.length;
        for(int line[]:raw_overlay)
        	for(int x=0;x<line.length;x++)
        		line[x]=simplify.get(line[x]);
        fastoverlay=new int[overlay_width*overlay_height];
        for(int y=0;y<overlay_height;y++) {
            int l[]=raw_overlay[y];
            for(int x=0;x<overlay_width;x++)
                try {
                fastoverlay[x+y*l.length]=palette.fullmap.get(l[x]).remap;
                } catch(Exception ex) {
                    System.out.println(l[x]);
                }
        }
//        setSpacing();
        if(slice.grid.isEmpty())
            resetSection(slice);
        reDraw();
    }

//    @FXML
//    void save(ActionEvent event) throws IOException {
//        File f=baseFolder.resolve(filename+".json").toFile();
//        File b=baseFolder.resolve(filename+".bak.json").toFile();
//        if(b.exists())
//            System.out.println("delete bak:"+b.delete());
//        System.out.println("create bak:"+f.renameTo(b));
//        try(FileWriter fw=new FileWriter(f)){
//            series.toJSON(fw);
//        }
//    }

//    @FXML
//    void exprt(ActionEvent event) throws IOException {
//        DirectoryChooser dc=new DirectoryChooser();
//        dc.setInitialDirectory(baseFolder.toFile());
//        dc.setTitle("Pick folder for exporting slices");
//        File f=dc.showDialog(stage);
//        if(f!=null) {
//            List<Slice> slices=series.slices;
//            int count=0;
//            try(PrintWriter pw=new PrintWriter(f+File.separator+"report.tsv")){
////                pw.println("snr\tname\twidth\theight\ttotal\tsegmented\tcoverage%\tchanged\tchanged%");
//                pw.println("snr\tname\tsegmented\tchanged\tstable%");
//            for(int i=0;i<slices.size();i++) {
//                Slice slice=slices.get(i);
//                if(slice.markers.size()>0) {
//                    count++;
//                    String name=slice.filename.substring(0, slice.filename.lastIndexOf('.'));
//                    Double ouv[]=slice.anchoring.toArray(new Double[0]);
//                    int overlay[][]=slicer.getInt32Slice(ouv[0], ouv[1], ouv[2], ouv[3], ouv[4], ouv[5], ouv[6], ouv[7], ouv[8], false);
//                    slice.triangulate();
//                    List<Triangle> triangles=slice.triangles;
//                    int h=overlay.length;
//                    int w=overlay[0].length;
//                    byte rgb[]=new byte[w*h*3];
//                    int segmented=0;
//                    int changed=0;
//                    try(DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f+File.separator+name+"_nl.flat")))){
//                        boolean byt=palette.fastcolors.length<=256;
//                        dos.writeByte(byt?1:2);
//                        dos.writeInt(w);
//                        dos.writeInt(h);
//                        for(int y=0;y<h;y++)
//                            for(int x=0;x<w;x++) {
////                                SegLabel c=palette.fullmap.get(overlay[y][x]);
//                                SegLabel c=palette.fullmap.get(0);
//                                double fx = x * slice.width / w;
//                                double fy = y * slice.height / h;
//                                for (int j = 0; j < triangles.size(); j++) {
//                                    double t[] = triangles.get(j).transform(fx, fy);
//                                    if (t != null) {
//                                        int xx = (int) (t[0] * w / slice.width);
//                                        int yy = (int) (t[1] * h / slice.height);
//                                        if(xx>=0 && yy>=0 && xx<overlay[0].length && yy<overlay.length)
//                                            c=palette.fullmap.get(overlay[yy][xx]);//!!
//                                        break;
//                                    }
//                                }
//                                rgb[(x+y*w)*3]=(byte)c.red;
//                                rgb[(x+y*w)*3+1]=(byte)c.green;
//                                rgb[(x+y*w)*3+2]=(byte)c.blue;
//                                if(byt)
//                                    dos.writeByte(c.remap);
//                                else
//                                    dos.writeShort(c.remap);
//                                if(overlay[y][x]!=0 || c.index!=0)
//                                    segmented++;
//                                if(overlay[y][x]!=c.index)
//                                    changed++;
//                            }
////                        pw.println((int)slice.nr+"\t"+slice.filename+"\t"+overlay[0].length+"\t"+overlay.length+"\t"+
////                            overlay[0].length*overlay.length+"\t"+segmented+"\t"+segmented*100/overlay[0].length/overlay.length+"%\t"+
////                            changed+"\t"+changed*100/segmented+"%");
//                        pw.println((int)slice.nr+"\t"+slice.filename+"\t"+segmented+"\t"+changed+"\t"+(segmented-changed)*100/segmented+"%");
//                    }
//                    BufferedImage bi=new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
//                    bi.getRaster().setDataElements(0, 0, w, h, rgb);
//                    ImageIO.write(bi, "png", new File(f+File.separator+name+"_nl.png"));
//                }
//            }
//            }
//            Alert a=new Alert(AlertType.INFORMATION, "Done. "+(count==0?"No":count)+" non-linear segmentation"+(count!=1?"s":"")+" exported.");
//            a.showAndWait();
//        }
//    }
    
    @FXML
    void importhier(ActionEvent event) throws Exception {
        Preferences prefs=Preferences.userRoot().node("/no/uio/nesys/qcontrol");
        File f=new File(prefs.get("lastDir", "/"));
        FileChooser fc = new FileChooser();
        if(f.exists())
            fc.setInitialDirectory(f);
        fc.setTitle("Pick JSON file");
        ExtensionFilter ef=new ExtensionFilter("QControl JSON files", "*.json");
        fc.getExtensionFilters().add(ef);
        //fc.setSelectedExtensionFilter(ef);
        /*File*/ f = fc.showOpenDialog(stage);
        if (f != null) {
            try (FileReader fr = new FileReader(f)) {
            	Series temp=new Series();
                Map<String, String> resolver = new HashMap<>();
                resolver.put("resolution", "target-resolution");
                JSON.mapObject(JSON.parse(fr), temp, resolver);
                supress=true;
                for(TreeItem<String> entry:idmap.values())
                	entry.setExpanded(true);
                for(double d:temp.collapsed)
                	idmap.get(Integer.valueOf((int)d)).setExpanded(false);
                supress=false;
                domappings();
            }
//            gridSpacing();
            loadView();
        }
    }
    
//    @FXML
//    void exportstats(ActionEvent event) throws Exception {
//        if(series==null)return;
//        FileChooser fc = new FileChooser();
//        fc.setInitialDirectory(baseFolder.toFile());
//        fc.setTitle("Export statistics");
//        ExtensionFilter ef=new ExtensionFilter("Text files", "*.txt");
//        fc.getExtensionFilters().add(ef);
//        File f=fc.showSaveDialog(stage);
//        if(f!=null) {
//        	try(PrintWriter pw=new PrintWriter(f)){
//        		pw.println("name\tall\tEmpty(0)\tOk(1)\tWrong(2)\tUndecidable(3)");
//        		List<String> unprocessed=new ArrayList<String>();
//        		series.slices.forEach(slice->{
//        			if(!slice.grid.isEmpty()) {
//	        			int[] bins=new int[4];
//	        			slice.grid.forEach(raw->bins[raw.intValue()]++);
//	        			pw.print(slice.filename);
//	        			pw.print("\t");
//	        			pw.print(slice.grid.size());
//	        			for(int bin:bins)
//	        				pw.print("\t"+bin);
//	        			pw.println();
//        			}else
//        				unprocessed.add(slice.filename);
//        		});
//        		unprocessed.forEach(filename->pw.println(filename));
//        	}
////        	List<Double> collapsed=series.collapsed;
////        	collapsed.clear();
////        	for(Map.Entry<Integer, TreeItem<String>> e:idmap.entrySet())
////        		if(!e.getValue().isExpanded())
////        			collapsed.add(e.getKey().doubleValue());
////            try(FileWriter fw=new FileWriter(f)){
////                series.toJSON(fw);
////            }
//        }
//    }

    private int sample_raw(int x, int y, Slice slice, int[][] raw_overlay) {
    	int overlay_height=raw_overlay.length;
    	int overlay_width=raw_overlay[0].length;
        List<Triangle> triangles=slice.triangles;
        for (int i = 0; i < triangles.size(); i++) {
            double t[] = triangles.get(i).transform(x, y);
            if (t != null) {
                int xx = (int) (t[0] * overlay_width / slice.width);
                int yy = (int) (t[1] * overlay_height / slice.height);
                if(xx<0 || yy<0 || xx>=overlay_width || yy>=overlay_height)
                    return 0;
                //return overlay[yy][xx];
                return raw_overlay[yy][xx];//fastoverlay[xx+yy*overlay_width];
            }
        }
        return 0;// 0x80000000;
    }

//    final String[] statuses=new String[] {"Empty","Okay","Wrong","Undecidable"};
    final String[] statuses=new String[] {"N/A","Accurate","Inaccurate","Uncertain","Damaged"};
    @FXML
//    void sectionstats(ActionEvent event) throws Exception {
    void exportstats(ActionEvent event) throws Exception {
        if(series==null)return;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(baseFolder.toFile());
        fc.setInitialFileName(filename+"_stats.txt");
        fc.setTitle("Pick text file");
        ExtensionFilter ef=new ExtensionFilter("Text files", "*.txt");
        fc.getExtensionFilters().add(ef);
        File f=fc.showSaveDialog(stage);
        if(f==null)
        	return;
//        if(f!=null) {
//            filename=f.getName();
//            filename=filename.substring(0, filename.length()-5);
        
        List<Map<Integer,int[]>> seriesStats=new ArrayList<>();
//        Map<Integer,int[]> totalStats=new TreeMap<>((x,y)->simplify.get(x)-simplify.get(y));
        Map<Integer,int[]> totalStats=new LinkedHashMap<>();
        totalStats.put(0, new int[statuses.length]);
		flatmap.forEach((id,tl)->{
			Integer remap=simplify.get(id);
			if(!totalStats.containsKey(remap)) {
				totalStats.put(remap, new int[statuses.length]);
			}
		});
        
        
        List<int[]> sliceTotals=new ArrayList<>();
        int[] seriesTotals=new int[statuses.length];
        boolean bogus=false;
        for(Slice slice:series.slices) {
        	slice.triangulate(); //!!
        	slice.updateSpacing(series.gridspacing); //!!
            Double ouv[]=slice.anchoring.toArray(new Double[0]);
            int[][] raw_overlay=slicer.getInt32Slice(ouv[0], ouv[1], ouv[2], ouv[3], ouv[4], ouv[5], ouv[6], ouv[7], ouv[8], false);
            for(int line[]:raw_overlay)
            	for(int x=0;x<line.length;x++)
            		line[x]=simplify.get(line[x]);
	        int gridx=(int)slice.gridx;
	        int gridy=(int)slice.gridy;
	        int[] totals=new int[statuses.length];
	        Map<Integer, int[]> stats=new TreeMap<>((x,y)->simplify.get(x)-simplify.get(y));
	        Iterator<Double> it=slice.grid.iterator();
	        if(it.hasNext()) {
		        for(int y=gridy;y<slice.height;y+=slice.yspacing)
		            for(int x=gridx;x<slice.width;x+=slice.xspacing) {
		            	int l=sample_raw(x, y,slice,raw_overlay);
		            	if(!stats.containsKey(l))
		            		stats.put(l, new int[statuses.length]);
//		            	if(!totalStats.containsKey(l))
//		            		totalStats.put(l, new int[4]);
		            	int v=it.next().intValue();
		            	stats.get(l)[v]++;
		            	totalStats.get(l)[v]++;
		            	totals[v]++;
		            	seriesTotals[v]++;
		            }
	        }
	        sliceTotals.add(totals);
	        seriesStats.add(stats);
	        if(it.hasNext()) {
	        	if(!bogus) {
		        	Alert a=new Alert(AlertType.ERROR,"Possible bug found, please report:\n"+slice.filename,ButtonType.OK);
		        	a.showAndWait();
		        	bogus=true;
	        	}
	        	int cnt=0;
	        	while(it.hasNext()) {
	        		it.next();
	        		cnt++;
	        	}
	        	System.out.println(slice.filename);
	        	System.out.println(cnt);
	        }
//	        try(PrintWriter pw=new PrintWriter(baseFolder.resolve(slice.filename+"_stats.txt").toFile())){
//	        	for(int i=0;i<statuses.length;i++) {
//	        		pw.print('\t');
//	        		if(i==0)
//	        			pw.print("Total");
//        			pw.print('\t');
//        			pw.print(statuses[i]);
//        			pw.print('\t');
//        			pw.println(totals[i]);
//	        	}
//	        	stats.forEach((l,s)->{
//		        	for(int i=0;i<statuses.length;i++) {
//		        		if(i==0)
//		        			pw.print(l);
//	        			pw.print('\t');
//		        		if(i==0)
//			        		pw.print(palette.fullmap.get(l).name);
//	        			pw.print('\t');
//	        			pw.print(statuses[i]);
//	        			pw.print('\t');
//	        			pw.println(s[i]);
//		        	}
//	        	});
//	        }
        }
//        try(PrintWriter pw=new PrintWriter(baseFolder.resolve(filename+"_stats.txt").toFile())){
        try(PrintWriter pw=new PrintWriter(f)){
        	pw.print("ID\tStructure\tStatus\tTotal");
        	for(Slice slice:series.slices)
        		pw.print("\t"+slice.filename);
        	pw.println();
        	for(int i=0;i<statuses.length;i++) {
        		pw.print("\tTotal grid\t"+statuses[i]+"\t"+seriesTotals[i]);
        		for(int[] total:sliceTotals)
        			pw.print("\t"+total[i]);
        		pw.println();
        	}
        	for(int i=0;i<statuses.length;i++) {
        		pw.print("\tTotal atlas\t"+statuses[i]+"\t"+(seriesTotals[i]-totalStats.get(Integer.valueOf(0))[i]));
//        		for(int[] total:sliceTotals)
//        			pw.print("\t"+total[i]);
        		for(int j=0;j<sliceTotals.size();j++) {
        			pw.print("\t"+(sliceTotals.get(j)[i]-seriesStats.get(j).get(Integer.valueOf(0))[i]));
        		}
        		pw.println();
        	}
        	totalStats.forEach((id,seriesTotal)->{
        		for(int i=0;i<statuses.length;i++) {
        			pw.print(id+"\t"+palette.fullmap.get(id).name+"\t"+statuses[i]+"\t"+seriesTotal[i]);
        			for(Map<Integer,int[]> stats:seriesStats) {
        				pw.print('\t');
        				if(stats.containsKey(id))
        					pw.print(stats.get(id)[i]);
        			}
        			pw.println();
        		}
        	});
        }
    }
    
    @FXML
    void exportsheet(ActionEvent event) throws Exception {
        if(series==null)return;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(baseFolder.toFile());
        fc.setInitialFileName(filename+"_regions.txt");
        fc.setTitle("Pick text file");
        ExtensionFilter ef=new ExtensionFilter("Text files", "*.txt");
        fc.getExtensionFilters().add(ef);
        File f=fc.showSaveDialog(stage);
        if(f==null)
        	return;
        var inuse=simplify.entrySet().stream().filter(e->!e.getKey().equals(e.getValue())).map(e->e.getValue()).collect(Collectors.toSet());
//        var inuse=simplify.entrySet().stream().filter(e->{
//        	if(e.getKey()==375||e.getKey()==382||e.getValue()==375||e.getValue()==382) {
//        		System.out.println(e);
//        		System.out.println(!e.getKey().equals(e.getValue()));
//        	}
//        	return !e.getKey().equals(e.getValue());}).map(e->e.getValue()).collect(Collectors.toSet());
//    	try(PrintWriter pw=new PrintWriter(baseFolder.resolve(filename+"_regions.txt").toFile())){
    	try(PrintWriter pw=new PrintWriter(f)){
//    		pw.println(slicer.stats);
//    		pw.println(inuse);
    		pw.print("Custom brain region");
    		var columns=new LinkedHashMap<Integer,List<Integer>>();
    		flatmap.forEach((id,tl)->{
//    			if(id==375)
//    				System.out.println("Ammon");
//    			if(id==382)
//    				System.out.println("CA1");
////    			if(id!=0) {
	    			var remap=simplify.get(id);
//	    			if(id==375||id==382) {
//	    				System.out.printf("%d->%d, columns %b, inuse %b, haslabel %b\n",id,remap,columns.containsKey(remap),inuse.contains(remap),slicer.hasLabel(remap));
//	    			}
	    			if(!columns.containsKey(remap) && (inuse.contains(remap) || slicer.hasLabel(remap))) {
	    				columns.put(remap, new ArrayList<>());
	    				pw.print('\t');
	    				pw.print(tl.name);
	    			}
	    			if(columns.containsKey(remap))
	    				columns.get(remap).add(id);
//    			}
    		});
    		pw.println();
    		pw.print("RGB colour");
    		columns.forEach((id,list)->{
    			pw.print('\t');
    			var l=palette.fullmap.get(id);
    			pw.print(255-l.red);
    			pw.print(';');
    			pw.print(255-l.green);
    			pw.print(';');
    			pw.print(255-l.blue);
    		});
    		pw.println();
    		var max=columns.values().stream().mapToInt(List::size).max().getAsInt();
    		for(int i=0;i<max;i++) {
    			var j=i;
    			if(i==0)
    				pw.print("Atlas Ids");
        		columns.forEach((id,list)->{
        			pw.print('\t');
        			if(list.size()>j)
        				pw.print(list.get(j));
        		});
        		pw.println();
    		}
    	}
    }
    
    @FXML
    void saveas(ActionEvent event) throws IOException {
        if(series==null)return;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(baseFolder.toFile());
        fc.setTitle("Pick JSON file");
        ExtensionFilter ef=new ExtensionFilter("JSON files", "*.json");
        fc.getExtensionFilters().add(ef);
        File f=fc.showSaveDialog(stage);
        if(f!=null) {
            filename=f.getName();
            filename=filename.substring(0, filename.length()-5);
        	List<Double> collapsed=series.collapsed;
        	collapsed.clear();
        	for(Map.Entry<Integer, TreeItem<String>> e:idmap.entrySet())
        		if(!e.getValue().isExpanded())
        			collapsed.add(e.getKey().doubleValue());
            try(FileWriter fw=new FileWriter(f)){
                series.toJSON(fw);
            }
        }
    }

    @FXML
    void close(ActionEvent event) {
        if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Do you want to close series?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
                series=null;
                setSlice(null);
                baseFolder=null;
                filename=null;
                setTitle(null);
                drawImage();
                GraphicsContext ctx = ovlycnv.getGraphicsContext2D();
                ctx.clearRect(0, 0, imgcnv.getWidth(), imgcnv.getHeight());
                spnVal.setMax(1);
                imgw=0;
                imgh=0;
            }
        });
    }

    @FXML
    void reGrid(ActionEvent event) {
        drawGrid();
    }

    @FXML
    void reOvly(ActionEvent event) {
        reDraw();
    }

//    @FXML
//    void reDebug(ActionEvent event) {
//        drawDebug();
//        drawPins();
//    }
    
    @FXML
    void goFirst(ActionEvent event) {
//        if(series==null)return;
        spnVal.setValue(1);
    }
    
    @FXML
    void goLast(ActionEvent event) {
        if(series==null)return;
        spnVal.setValue(series.slices.size());
    }
    
    @FXML
    void less(ActionEvent event) {
//        if(series==null)return;
        spnVal.decrement(1);
    }
    
    @FXML
    void more(ActionEvent event) {
//        if(series==null)return;
        spnVal.increment(1);
    }
    
    @FXML
    void less10(ActionEvent event) {
//        if(series==null)return;
        spnVal.decrement(10);
    }
    
    @FXML
    void more10(ActionEvent event) {
//        if(series==null)return;
        spnVal.increment(10);
    }

//    @FXML
//    void clear(ActionEvent event) {
//        if(series==null)return;
//        Alert a=new Alert(AlertType.WARNING,"Proceed with dropping all markers from current section?",ButtonType.YES,ButtonType.NO);
//        a.showAndWait().ifPresent(b->{
//            if(b==ButtonType.YES) {
//                slice.markers.clear();
//                slice.triangulate();
//                reDraw();
//            }
//        });
//    }
    
    static class XY{
    	final int xp;
    	final int yp;
    	final int n;
    	XY(Slice slice){
    		xp=(int)(slice.width-slice.gridx+slice.xspacing-1)/slice.xspacing;
    		yp=(int)(slice.height-slice.gridy+slice.yspacing-1)/slice.yspacing;
    		n=xp*yp;
    	}
    };    
    XY makeGrid(Slice slice, boolean keep) {
    	slice.updateSpacing(series.gridspacing);
    	var xy=new XY(slice);
        if(!keep || slice.grid.size()!=xy.n)
        	slice.grid=new ArrayList<>(Collections.nCopies(xy.n, Double.valueOf(0)));
        return xy;
    }
    
    Random rnd=new Random();
    void resetSection(Slice slice) {
    	if(slice.anchoring.size()!=9)System.out.println("non-9 in resetSection");
    	slice.clear=true;
    	slice.updateSpacing(series.gridspacing);
//        int xspacing=xSpacing(slice);
//        int yspacing=ySpacing(slice);
        slice.gridx=rnd.nextInt(slice.xspacing);
        slice.gridy=rnd.nextInt(slice.yspacing);
        makeGrid(slice,false);
    }
    
    void clearSectionKeepDamage(Slice slice) {
    	slice.clear=true;
//        int xspacing=xSpacing(slice);
//        int yspacing=ySpacing(slice);
    	makeGrid(slice, true);
    	for(var i=0;i<slice.grid.size();i++)
    		if(slice.grid.get(i)!=4)
    			slice.grid.set(i, (double)0);
    }
    
    void prefillSection(Slice slice) {
    	slice.clear=true;
    	slice.triangulate(); //!!
        Double ouv[]=slice.anchoring.toArray(new Double[0]);
        int[][] raw_overlay=slicer.getInt32Slice(ouv[0], ouv[1], ouv[2], ouv[3], ouv[4], ouv[5], ouv[6], ouv[7], ouv[8], false);
        for(int line[]:raw_overlay)
        	for(int x=0;x<line.length;x++)
        		line[x]=simplify.get(line[x]);
        
        int gridx=(int)slice.gridx;
        int gridy=(int)slice.gridy;

//        int xspacing=xSpacing(slice);
//        int yspacing=ySpacing(slice);
        var xy=makeGrid(slice, true);
        for(int y=0;y<xy.yp;y++)
        	for(int x=0;x<xy.xp;x++) {
                int i=(int)(x+y*xy.xp);
                if(slice.grid.get(i)!=4) {
        		if(x==0 || y==0 || x==xy.xp-1 || y==xy.yp-1)
        			slice.grid.set(i,0.);
        		else {
        			int w=sample_raw(gridx+x*slice.xspacing, gridy+y*slice.yspacing, slice, raw_overlay);
        			if(w!=0 &&
        					w==sample_raw(gridx+(x-1)*slice.xspacing, gridy+y*slice.yspacing, slice, raw_overlay) &&
        					w==sample_raw(gridx+(x+1)*slice.xspacing, gridy+y*slice.yspacing, slice, raw_overlay) &&
        					w==sample_raw(gridx+x*slice.xspacing, gridy+(y-1)*slice.yspacing, slice, raw_overlay) &&
        					w==sample_raw(gridx+x*slice.xspacing, gridy+(y+1)*slice.yspacing, slice, raw_overlay))
        				slice.grid.set(i,1.);
        			else
        				slice.grid.set(i,0.);
        		}
        	}}
    }

    @FXML void resetSection(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with complete reset of the grid on this section?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
            	resetSection(slice);
            	updateProgress();
                reDraw();
            }
        });
    }
    
    @FXML void resetSeries(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with complete reset of the grid on all sections?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES)
            	resetSeries();
        });
    }
        
    @FXML void clearSectionKeepDamage(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with clearing non-damage markers on this section?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
            	clearSectionKeepDamage(slice);
            	updateProgress();
                reDraw();
            }
        });
    }
    
    @FXML void clearSeriesKeepDamage(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with clearing non-damage markers on all sections?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
            	for(var slice:series.slices)
            		clearSectionKeepDamage(slice);
            	updateProgress();
                reDraw();
            }
        });
    }
    
    @FXML void prefillSectionKeepDamage(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with prefilling non-damage markers on this section?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
            	prefillSection(slice);
            	updateProgress();
                reDraw();
            }
        });
    }
    
    @FXML void prefillSeriesKeepDamage(ActionEvent event) {
    	if(series==null)return;
        Alert a=new Alert(AlertType.WARNING,"Proceed with prefilling non-damage markers on all sections?",ButtonType.YES,ButtonType.NO);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.YES) {
            	for(var slice:series.slices)
            		prefillSection(slice);
            	updateProgress();
                reDraw();
            }
        });
    }
    
    Hyperlink hyperlink(String text,String URL) {
    	var h=new Hyperlink(text);
        h.setStyle("-fx-translate-y: 0.5px");
        h.setOnAction(e->{
            try {
                Desktop.getDesktop().browse(new URI(URL));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return h;
    }
    Text text(String text) {
    	return new Text(text);
    }
    Text bold(String text) {
    	var t=new Text(text);
        t.setStyle("-fx-font-weight: bold");
        return t;
    }

    @FXML
    void about(ActionEvent event) {
    	var tf=new TextFlow(
    			text("QCAlign is developed at the Neural Systems Laboratory, Institute of Basic Medical Sciences, University of Oslo (Norway), with funding from the European Union’s Horizon 2020 Framework Programme for Research and Innovation under the Framework Partnership Agreement No. 650003 (HBP FPA).\n\n"),
				bold("License:"),text(" MIT license. Source code is available on"),
				hyperlink("GitHub","https://github.com/HumanBrainProject/QCAlign"),
				text("\n\n"),
				bold("Citation\n"),
				text("Manuscript in preparation."),
//				text("- RRID on"),
//				hyperlink("SciCrunch","https://scicrunch.org/resolver/RRID:SCR_017978"),
//				text("(VisuAlign, RRID:SCR_017978)\n"),
//				text("- Puchades MA, Csucs G, Ledergerber D, Leergaard TB, Bjaalie JG (2019) Spatial registration of serial microscopic brain images to three-dimensional reference atlases with the QuickNII tool. PLoS ONE 14(5): e0216796."),
//				hyperlink("https://doi.org/10.1371/journal.pone.0216796", "https://doi.org/10.1371/journal.pone.0216796"),
				text(" See possible updates on"),
				hyperlink("https://qcalign.readthedocs.io/","https://qcalign.readthedocs.io/"),
				//text("?? (it's NITRC for other tools)"),
//				hyperlink("NITRC","https://www.nitrc.org/plugins/mwiki/index.php/visualign:References"),
				text("\n\n"),
				bold("User documentation:"),
				hyperlink("https://qcalign.readthedocs.io/","https://qcalign.readthedocs.io/"),
				text("\n"),
				bold("User support:"),
				hyperlink("https://ebrains.eu/support","https://ebrains.eu/support"),
				text("\n"),
				bold("Contact:"),
				hyperlink("j.g.bjaalie@medisin.uio.no","mailto:j.g.bjaalie@medisin.uio.no"),
				text("\n\n"),
				bold("Waxholm Space atlas of the Sprague Dawley Rat brain"),
				text("\n\n"),
				text("Waxholm Space Atlas of the Sprague Dawley Rat brain, v2, v3, v4 (RRID: SCR_017124; Papp et al., NeuroImage 97, 374-386, 2014; Papp et al., NeuroImage 105, 561–562, 2015; Kjonigsen et al., NeuroImage 108, 441-449, 2015; Osen et al., NeuroImage 199, 38-56, 2019; Kleven et al., in preparation)\n"),
				text("See more on"),
				hyperlink("NITRC","https://www.nitrc.org/projects/whs-sd-atlas"),
				text("\n\n"),
				bold("Allen Mouse Brain Atlas reference atlas version 3 (2015, 2017)"),
				text("\n\n"),
				text("Allen Institute Mouse Brain Atlas, v3.0 (Lein et al., Nature 445:168-76, 2007; Oh et al., Nature 508:207-14, 2015; Technical white paper: Allen mouse common coordinate framework, May 2015 v.1)\n"),
				text("See more on"),
				hyperlink("Allen Mouse Brain Atlas","http://mouse.brain-map.org/"),
				text("\n\n\nCreated by Gergely Csucs and Sharon C Yates, NeSys\n© 2020-2022, University of Oslo")
    	);
        
        Dialog<Void> dlg=new Dialog<>();
        dlg.setResizable(true);
        dlg.setTitle(title);
        DialogPane pane=dlg.getDialogPane();
        pane.setPrefWidth(700);
        pane.setContent(tf);
        pane.getButtonTypes().add(ButtonType.OK);
        dlg.showAndWait();
    }
    
    @FXML
    void minidoc(ActionEvent event) {
    	var tf=new TextFlow(
//    			text("VisuAlign refines an existing alignment. For creating one, please refer to"),
//    			hyperlink("QuickNII", "https://www.nitrc.org/projects/quicknii/"),
//    			text("and articles/documentation related to it. Example datasets with \"before-after\" image pairs are provided at"),
//    			hyperlink("NITRC","https://www.nitrc.org/frs/?group_id=1426"),
//    			text(", in both examples section #1 shows image as registered with QuickNII and section #2 shows the result after nonlinear refinements.\n\n"),
    			bold("Controls:\n"),
    			text("\n- Click on a position to rotate marking between empty, accurate, inaccurate, uncertain, and damaged states\n"
    					+"- Space bar: toggles an overview mode where it's easy to spot holes in the assessment\n"
    					+"- 1: shortcut to clear marking at a position\n"
    					+"- 2: shortcut to mark a position as accurate\n"
    					+"- 3: shortcut to mark a position as inaccurate\n"
    					+"- 4: shortcut to mark a position as uncertain\n"
    					+"- 5: shortcut to mark a position as damaged\n"
    					),
//    			text("\n- Space bar: place marker\n" + 
//    	                "- Backspace, Delete: remove marker under mouse cursor\n" + 
//    	                "- Left arrow: navigate to previous section\n" + 
//    	                "- Right arrow: navigate to next section\n" +
//    	                "Drag the crosses with the mouse in order to apply nonlinear adjustments.\n"),
    			bold("\nMiscellaneous:\n"),
    			text("\n" +
    	                "- Pull Overlay Opacity slider to the maximum (far right) in order to toggle outline mode. Only in outline mode the control for changing outline color becomes active\n" + 
    	                "- Clear/Prefill/Reset menu items will always ask for confirmation, regardless of having or not having unsaved modifications.")
    			);
        
        Dialog<Void> dlg=new Dialog<>();
        dlg.setResizable(true);
        dlg.setTitle(title);
        DialogPane pane=dlg.getDialogPane();
        pane.setPrefWidth(700);
        pane.setContent(tf);
        pane.getButtonTypes().add(ButtonType.OK);
        dlg.showAndWait();
    }
    
    Stage stage;
    public void setTitle(String filename) {
        stage.setTitle(filename==null?title:(title+": "+filename+" (registered to "+(series.target.replaceAll("_", " ").replace(".cutlas", ""))+")"));
    }
    public static final double version10=8;
    public static final String title=String.format(Locale.ENGLISH,"QCAlign v%.1f",version10/10);
}
