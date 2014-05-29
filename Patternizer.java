/*
 * CS 349 Java Code Examples
 *
 * ShapeDemo    Demo of MyShape class: draw shapes using mouse.
 *
 */
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.vecmath.*;
import javax.swing.event.MouseInputListener;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

// create the window and run the demo
public class Patternizer extends JPanel implements MouseInputListener {
	static JFrame f;
	MyShape shape;
	int selected = -1;
	
	static JLabel text = new JLabel("Choose one line thickness:");
	static JRadioButton chk = new JRadioButton("3.0f");
	static JRadioButton chk2 = new JRadioButton("6.0f");
	static JRadioButton chk3 = new JRadioButton("9.0f");

	// original mouse press point
	double startx;
	double starty;
	double startD; // start distance between mouse and center
	double preX;
	double preY;
	double totalAngle = 0;

	static ArrayList<MyShape> shapeList = new ArrayList<MyShape>();
	static centerCircle circle;
	static int Wwidth = 600;
	static int Wheight = 800;

	boolean draw = false; // an indicator tells the listener to record mouse or
	boolean drawn = false;
	// not
	boolean trans = false; // an indicator tells the listener to perform scale
							// or rotate.

	Patternizer() {
		setBackground(Color.BLACK);
		// add listeners
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public static void main(String[] args) {
		// create the window
		Patternizer canvas = new Patternizer();
		JPanel tool = new JPanel();
		tool.setSize(500, 200);


		ButtonGroup group = new ButtonGroup();
		group.add(chk);
		group.add(chk2);
		group.add(chk3);
		
		chk.setSelected(true);
		tool.add(text);
		tool.add(chk);
		tool.add(chk2);
		tool.add(chk3);
		
		
		f = new JFrame("Patternizer"); // jframe is the app window
		f.setLayout(new GridLayout());
		f.setBackground(Color.BLACK);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Wwidth, Wheight); // window size
		f.setContentPane(canvas); // add canvas to jframe
		f.add(tool);
		f.setVisible(true); // show the window

		f.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				updateContent();
			}
		});

	}

	static void updateContent() {
		AffineTransform af = new AffineTransform();
		double xc = (f.getWidth() - 600) / 2;
		double yc = (f.getHeight() - 600) / 2;
		af.translate(-(Wwidth - 600) / 2, -(Wheight - 600) / 2); // translate
																	// back to
																	// beginning
																	// position
		af.translate(xc, yc);
		Wwidth = f.getWidth();
		Wheight = f.getHeight();
		if (circle != null) {
			circle.setCenter(new Point2d(Wwidth / 2, Wheight / 2));
		}
		if (!shapeList.isEmpty()) {
			for (MyShape s : shapeList) {
				s.move(af);

			}
		}
	}

	// custom graphics drawing
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // antialiasing
																// look nicer
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setBackground(Color.BLACK);
		if (circle == null) {
			circle = new centerCircle();
			circle.setColour();
			circle.setCenter(new Point2d(Wwidth / 2, Wheight / 2));
		}

		if (shape != null)
			shape.paint(g2);
		if (!shapeList.isEmpty()) {
			for (MyShape s : shapeList) {
				s.paint(g2);
			}
		}
		if (circle != null)
			circle.paint(g2);
	}

	public int checkSelect(double x, double y) {
		double minDis = 10; // minimum distance from mouse click
		int curChoice = -1;
		for (int i = 0; i < shapeList.size(); i++) {
			double d = shapeList.get(i).hittest(x, y);
			if (d > 0 && d < minDis) {
				curChoice = i;
			}
		}
		return curChoice;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 1) {
			// hittest
			boolean in = circle.hittest(arg0.getX(), arg0.getY());
			if (in) {
				// de-select shape
				if (selected >= 0) {
					shapeList.get(selected).setSelected(false);
					selected = -1;
				}

				draw = false;
				circle.setColour();
				repaint();
			} else { // maybe select
				int i = checkSelect(arg0.getX(), arg0.getY());
				// wherever the mouse click, de-select the previous shape
				if (selected >= 0 && i != selected) {

					shapeList.get(selected).setSelected(false);
					selected = -1;
				}
				if (i >= 0) {
					if (selected != i) {
						selected = i;
						shapeList.get(selected).setSelected(true);
					} else {
						shapeList.get(selected).setSelected(false);
						selected = -1;
					}
				}
				repaint();
			}
		} else if (arg0.getClickCount() == 2) {
			// hittest&clear
			boolean in = circle.hittest(arg0.getX(), arg0.getY());
			if (in) {
				shapeList.clear();
				repaint();
			} else {
				int i = checkSelect(arg0.getX(), arg0.getY());
				// wherever the mouse click, de-select the previous shape

				if (i >= 0) {
					Random rand = new Random();
					float r = rand.nextFloat();
					float g = rand.nextFloat();
					float b = rand.nextFloat();
					Color randomColor = new Color(r, g, b);
					shapeList.get(i).setColour(randomColor);
				}
				repaint();
			}
		} else {

		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		draw = false;
		// can only draw from the circle
		if (circle.hittest(arg0.getX(), arg0.getY()) && draw == false) {
			draw = true;
			totalAngle = 0;
			shape = new MyShape();
			shape.setIsClosed(false);
			shape.setIsFilled(false);
			shape.setSelected(true);
			if(chk.isSelected()){
				shape.setStrokeThickness(3.0f);
			}
			else if(chk2.isSelected()){
				shape.setStrokeThickness(6.0f);
			}else {
				shape.setStrokeThickness(9.0f);
			}
			shape.setColour(circle.getColour());
			repaint();
		} else { // maybe select
			draw = false;
			// check select shape here.
			// Things might
			int i = checkSelect(arg0.getX(), arg0.getY());
			if (i == selected) {
				trans = true;
				startx = arg0.getX();
				starty = arg0.getY();
				preX = arg0.getX();
				preY = arg0.getY();
				startD = Math.sqrt((startx - circle.center.x)
						* (startx - circle.center.x)
						+ (starty - circle.center.y)
						* (starty - circle.center.y));
				// Record the original position

			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (draw) {
			if (drawn) {
				shape.setSelected(false);
				shapeList.add(shape);
				draw = false;
				drawn = false;
			}
			repaint();
		} else if (trans) {
			trans = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// de-select the shape if any selected.
		if (selected >= 0 && trans == false) {
			shapeList.get(selected).setSelected(false);
			selected = -1;
		}

		if (draw) {
			shape.addPoint(arg0.getX(), arg0.getY());
			drawn = true;
			repaint();
		} else if (trans && selected >= 0) {
			double cx = circle.center.x;
			double cy = circle.center.y;
			double mx = arg0.getX();
			double my = arg0.getY();
			Vector2d v1 = new Vector2d(preX, preY);
			Vector2d v2 = new Vector2d(arg0.getX(), arg0.getY());
			Vector2d vc = new Vector2d(cx, cy);
			Vector2d v3 = new Vector2d(cx + 10, cy); // used as start vector
			v1.sub(vc);
			v2.sub(vc);
			v3.sub(vc);
			
			double angle1 = 0;
			double angle2 = 0;
			double angle3 = 0;
			angle1 = v1.angle(v3);
			angle2 = v2.angle(v3);
			// rotate back first
			if (my >= cy && preY >= cy) {
				if (angle2 > angle1) {
					angle3 = (v2.angle(v1));
				} else {
					angle3 = -v2.angle(v1);
				}
			} else if (my <= cy && preY <= cy) {
				if (angle2 > angle1) {
					angle3 = -v2.angle(v1);
				} else {
					angle3 = (v2.angle(v1));
				}
			} else if (my >= cy && preY <= cy) {
				angle3 = -v2.angle(v1);
			} else {
				angle3 = (v2.angle(v1));
			}
			totalAngle = totalAngle + angle3;
			
			
			double scale1;

			scale1 = startD
					/ Math.sqrt((preX - cx) * (preX - cx) + (preY - cy)
							* (preY - cy));
			double scale2 = (Math.sqrt((mx - cx) * (mx - cx) + (my - cy)
					* (my - cy)))
					/ startD;

			shapeList.get(selected).rotate(angle3,totalAngle, scale1, scale2,
					circle.center);

			preX = arg0.getX();
			preY = arg0.getY();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
}
